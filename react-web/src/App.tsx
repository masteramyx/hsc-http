import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { HelmetProvider } from 'react-helmet-async';
import { Header } from './components/Header';
import { Footer } from './components/Footer';
import { ChatWidget } from './components/ChatWidget';
import { HomePage } from './pages/HomePage';
import { HowItWorksPage } from './pages/HowItWorksPage';
import { OpportunitiesPage } from './pages/OpportunitiesPage';
import { AboutPage } from './pages/AboutPage';
import { ForStudentsPage } from './pages/ForStudentsPage';
import { ForProfessionalsPage } from './pages/ForProfessionalsPage';
import { ProfessionalRegistrationPage } from './pages/ProfessionalRegistrationPage';
import {ProfessionalWelcomePage} from "./pages/ProfessionalWelcomePage.tsx";

function App() {
  return (
    <HelmetProvider>
      <BrowserRouter>
        <div className="min-h-screen">
          <Header />
          <main className="pt-16">
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/how-it-works" element={<HowItWorksPage />} />
              <Route path="/opportunities" element={<OpportunitiesPage />} />
              <Route path="/about" element={<AboutPage />} />
              <Route path="/for-students" element={<ForStudentsPage />} />
              <Route path="/for-professionals" element={<ForProfessionalsPage />} />
              <Route path="/register/professional" element={<ProfessionalRegistrationPage />} />
              <Route path="/welcome/professional" element={<ProfessionalWelcomePage />} />
            </Routes>
          </main>
          <Footer />
          <ChatWidget />
        </div>
      </BrowserRouter>
    </HelmetProvider>
  );
}

export default App;
