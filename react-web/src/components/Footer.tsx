import { Link } from 'react-router-dom';

export function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-900 text-gray-300">
      <div className="container mx-auto px-6 py-12">
        <div className="grid md:grid-cols-4 gap-8">
          <div className="col-span-2 md:col-span-1">
            <Link to="/" className="flex items-center space-x-2 mb-4">
              <div className="w-8 h-8 bg-gradient-to-br from-primary-500 to-primary-700 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold">SC</span>
              </div>
              <span className="text-xl font-bold text-white">Shadow Connects</span>
            </Link>
            <p className="text-sm text-gray-400">
              Connecting aspiring healthcare professionals with clinical shadowing opportunities.
            </p>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">For Students</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <Link to="/opportunities" className="hover:text-primary-400 transition-colors">
                  Browse Opportunities
                </Link>
              </li>
              <li>
                <Link to="/how-it-works" className="hover:text-primary-400 transition-colors">
                  How It Works
                </Link>
              </li>
              <li>
                <Link to="/for-students" className="hover:text-primary-400 transition-colors">
                  For Students
                </Link>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">For Professionals</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <Link to="/for-professionals" className="hover:text-primary-400 transition-colors">
                  For Professionals
                </Link>
              </li>
              <li>
                <Link to="/how-it-works" className="hover:text-primary-400 transition-colors">
                  How It Works
                </Link>
              </li>
              <li>
                <Link to="/about" className="hover:text-primary-400 transition-colors">
                  About Us
                </Link>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">Company</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <Link to="/about" className="hover:text-primary-400 transition-colors">
                  About
                </Link>
              </li>
              <li>
                <a href="https://github.com" target="_blank" rel="noopener noreferrer" className="hover:text-primary-400 transition-colors">
                  GitHub
                </a>
              </li>
              <li>
                <Link to="/" className="hover:text-primary-400 transition-colors">
                  Contact
                </Link>
              </li>
            </ul>
          </div>
        </div>
        <div className="border-t border-gray-800 mt-12 pt-8 text-sm text-gray-400 text-center">
          <p>&copy; {currentYear} Shadow Connects. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}
