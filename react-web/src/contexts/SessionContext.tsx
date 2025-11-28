import { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react';
import { useLocation } from 'react-router-dom';
import { UserInfo, UserType, SessionState, LoginResponse, LogoutResponse } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

type SessionContextType = SessionState & {
  login: (email: string, password: string) => Promise<{ success: boolean; message: string }>;
  logout: () => Promise<{ success: boolean; message: string }>;
  checkSession: () => Promise<void>;
};

const SessionContext = createContext<SessionContextType | undefined>(undefined);

function createSessionState({ isLoggedIn, user, isLoading }: {
    isLoggedIn: boolean,
    user: UserInfo | null,
    isLoading: boolean
}) {
    return new SessionState(isLoggedIn, user, isLoading)
}

export function SessionProvider({ children }: { children: ReactNode }) {
  const location = useLocation();
  const [sessionState, setSessionState] = useState<SessionState>(
      createSessionState({
    isLoggedIn: false,
    user: null,
    isLoading: true,
    })
  );

  // Check for existing session on mount
  const checkSession = useCallback(async () => {
    try {
      const response = await fetch('/me', {
        method: 'GET',
        credentials: 'include', // Important: include cookies
      });

      if (response.ok) {
        const data: LoginResponse = await response.json();
        if (data.success && data.user) {
            const userInfo: UserInfo = new UserInfo(
                data.user.id,
                data.user.email,
                UserType.Companion.fromString(data.user.userType),
                data.user.emailVerified
            );
          setSessionState(createSessionState({
            isLoggedIn: true,
            user: userInfo,
            isLoading: false,
          })
        );
        } else {
          setSessionState(createSessionState({
            isLoggedIn: false,
            user: null,
            isLoading: false,
          })
        );
        }
      } else {
        setSessionState(createSessionState({
          isLoggedIn: false,
          user: null,
          isLoading: false,
        })
      );
      }
    } catch (error) {
      console.error('Session check failed:', error);
      setSessionState(createSessionState({
        isLoggedIn: false,
        user: null,
        isLoading: false,
      })
    );
    }
  }, []);

  // Login function
  const login = async (email: string, password: string) => {
    try {
      const response = await fetch('/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include', // Important: include cookies
        body: JSON.stringify({ email, password }),
      });

      const data: LoginResponse = await response.json();

      if (data.success && data.user) {
        setSessionState(createSessionState({
          isLoggedIn: true,
          user: data.user,
          isLoading: false,
        })
      );
        return { success: true, message: data.message };
      } else {
        return { success: false, message: data.message };
      }
    } catch (error) {
      console.error('Login failed:', error);
      return { success: false, message: 'Network error occurred' };
    }
  };

  // Logout function
  const logout = async () => {
    try {
      const response = await fetch('/logout', {
        method: 'POST',
        credentials: 'include',
      });

      const data: LogoutResponse = await response.json();

      if (data.success) {
          console.log('Logout success')
          // Clear session state regardless of API response
          setSessionState(createSessionState({
              isLoggedIn: false,
              user: null,
              isLoading: false,
          })
      );
          return { success: true, message: data.message }
      } else {
         console.error('Logout error:', data.message)
          return { success: false, message: data.message }
      }
    } catch (error) {
        console.error('Logout request failed:', error);
        return { success: false, message: 'Unknown error' }
    }
  };

  // Check session on mount and on every route change
  useEffect(() => {
    checkSession();
  }, [location.pathname, checkSession]);

  return (
    <SessionContext.Provider
      // @ts-expect-error - SessionState class methods not needed in context
      value={{
        ...sessionState,
        login,
        logout,
        checkSession,
      }}
    >
      {children}
    </SessionContext.Provider>
  );
}

// Custom hook to use session context
export function useSession() {
  const context = useContext(SessionContext);
  if (context === undefined) {
    throw new Error('useSession must be used within a SessionProvider');
  }
  return context;
}
