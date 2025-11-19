import { Link, useNavigate } from 'react-router-dom';
import { ContactForm } from './ContactForm';
import { useSession } from '../contexts/SessionContext';

export function Header() {
  const { isLoggedIn, user, isLoading, logout } = useSession();
  const navigate = useNavigate();

  const handleLogout = async () => {
    const result = await logout();
    if (result.success) {
      navigate('/');
    } else {
      alert(`Logout failed: ${result.message}`);
    }
  };

  return (
    <header className="fixed top-0 left-0 right-0 z-50 bg-white/90 backdrop-blur-md shadow-sm">
      <nav className="container mx-auto px-6 py-4">
        <div className="flex items-center justify-between">
          <Link to="/" className="flex items-center space-x-2">
            <div className="w-10 h-10 bg-gradient-to-br from-primary-500 to-primary-700 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-xl">SC</span>
            </div>
            <span className="text-2xl font-bold text-gray-900">Shadow Connects</span>
          </Link>
          <div className="hidden md:flex items-center space-x-8">
            <Link
              to="/how-it-works"
              className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
            >
              How It Works
            </Link>
            <Link
              to="/opportunities"
              className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
            >
              Opportunities
            </Link>
            <Link
              to="/about"
              className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
            >
              About
            </Link>

            {/* Show loading state */}
            {isLoading && (
              <span className="text-gray-500">Loading...</span>
            )}

            {/* Show logged-in state */}
            {!isLoading && isLoggedIn && user && (
              <>
                <Link
                     to='/dashboard'
                    className="text-gray-700 font-medium">
                  {user.email}
                </Link>
                <button
                  onClick={handleLogout}
                  className="px-6 py-2 bg-gray-200 text-gray-700 rounded-lg font-medium hover:bg-gray-300 transition-colors"
                >
                  Logout
                </button>
              </>
            )}

            {/* Show logged-out state */}
            {!isLoading && !isLoggedIn && (
              <>
                <Link
                  to="/login"
                  className="text-gray-700 hover:text-primary-600 font-medium transition-colors"
                >
                  Login
                </Link>
                <ContactForm
                  buttonText="Sign Up"
                  buttonClassName="px-6 py-2 bg-primary-600 text-white rounded-lg font-medium hover:bg-primary-700 transition-colors"
                  wrapperClassName=""
                />
              </>
            )}
          </div>
          <button className="md:hidden text-gray-700">
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
          </button>
        </div>
      </nav>
    </header>
  );
}
