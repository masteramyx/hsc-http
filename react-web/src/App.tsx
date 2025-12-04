import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { HelmetProvider } from 'react-helmet-async';
import { SessionProvider } from './contexts/SessionContext';
import { Header } from './components/Header';
import { EmailVerificationBanner } from './components/EmailVerificationBanner';
import { Footer } from './components/Footer';
import { ChatWidget } from './components/ChatWidget';
import { HomePage } from './pages/HomePage';
import { HowItWorksPage } from './pages/HowItWorksPage';
import { OpportunitiesPage } from './pages/OpportunitiesPage';
import { AboutPage } from './pages/AboutPage';
import { ForStudentsPage } from './pages/ForStudentsPage';
import { ForProfessionalsPage } from './pages/ForProfessionalsPage';
import { ProfessionalRegistrationPage } from './pages/ProfessionalRegistrationPage';
import { ProfessionalWelcomePage } from "./pages/ProfessionalWelcomePage.tsx";
import { EmailVerificationPage } from './pages/EmailVerificationPage';
import { LoginPage } from './pages/LoginPage';
import {DashboardPage} from "./pages/DashboardPage.tsx";
import {EditProfilePage} from "./pages/EditProfilePage.tsx";
import ScrollToTop from "./utils/scrollToTop.tsx";

/**
 * Root application component with three context providers:
 *
 * 1. HelmetProvider - Manages document head (title, meta tags) for SEO across pages.
 *    Allows child components to safely update <head> without conflicts.
 *
 * 2. BrowserRouter - Provides client-side routing using browser's History API.
 *    Enables navigation between pages without full page reloads.
 *
 * 3. SessionProvider - Manages user authentication state (login/logout/session).
 *    Provides isLoggedIn, user info, and auth methods to all child components.
 */
function App() {
  return (
    <HelmetProvider>
      <BrowserRouter>
        <SessionProvider>
          <div className="min-h-screen">
            <Header />
            <EmailVerificationBanner />
            <ScrollToTop />
            <main className="pt-16">
              <Routes>
                <Route path="/" element={<HomePage />} />
                <Route path="/how-it-works" element={<HowItWorksPage />} />
                <Route path="/opportunities" element={<OpportunitiesPage />} />
                <Route path="/about" element={<AboutPage />} />
                <Route path="/for-students" element={<ForStudentsPage />} />
                <Route path="/for-professionals" element={<ForProfessionalsPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register/professional" element={<ProfessionalRegistrationPage />} />
                <Route path="/welcome/professional" element={<ProfessionalWelcomePage />} />
                <Route path="/verify-email" element={<EmailVerificationPage />} />
                <Route path="/dashboard" element={<DashboardPage />} />
                <Route path="/profile/edit" element={<EditProfilePage />} />
              </Routes>
            </main>
            <Footer />
            <ChatWidget />
          </div>
        </SessionProvider>
      </BrowserRouter>
    </HelmetProvider>
  );
}

export default App;
