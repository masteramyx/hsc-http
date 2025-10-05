export function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-900 text-gray-300">
      <div className="container mx-auto px-6 py-12">
        <div className="grid md:grid-cols-4 gap-8">
          <div className="col-span-2 md:col-span-1">
            <div className="flex items-center space-x-2 mb-4">
              <div className="w-8 h-8 bg-gradient-to-br from-primary-500 to-primary-700 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold">SC</span>
              </div>
              <span className="text-xl font-bold text-white">Shadow Connects</span>
            </div>
            <p className="text-sm text-gray-400">
              Connecting aspiring healthcare professionals with clinical shadowing opportunities.
            </p>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">For Students</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <a href="#browse" className="hover:text-primary-400 transition-colors">
                  Browse Opportunities
                </a>
              </li>
              <li>
                <a href="#how-it-works" className="hover:text-primary-400 transition-colors">
                  How It Works
                </a>
              </li>
              <li>
                <a href="#success-stories" className="hover:text-primary-400 transition-colors">
                  Success Stories
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">For Professionals</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <a href="#list-opportunity" className="hover:text-primary-400 transition-colors">
                  List Opportunity
                </a>
              </li>
              <li>
                <a href="#benefits" className="hover:text-primary-400 transition-colors">
                  Benefits
                </a>
              </li>
              <li>
                <a href="#guidelines" className="hover:text-primary-400 transition-colors">
                  Guidelines
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">Community</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <a href="#github" className="hover:text-primary-400 transition-colors">
                  GitHub
                </a>
              </li>
              <li>
                <a href="#discord" className="hover:text-primary-400 transition-colors">
                  Discord
                </a>
              </li>
              <li>
                <a href="#twitter" className="hover:text-primary-400 transition-colors">
                  Twitter
                </a>
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
