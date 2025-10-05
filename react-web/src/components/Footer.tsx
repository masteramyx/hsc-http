export function Footer() {
  const currentYear = new Date().getFullYear();

  return (
    <footer className="bg-gray-900 text-gray-300">
      <div className="container mx-auto px-6 py-12">
        <div className="grid md:grid-cols-4 gap-8">
          <div className="col-span-2 md:col-span-1">
            <div className="flex items-center space-x-2 mb-4">
              <div className="w-8 h-8 bg-gradient-to-br from-primary-500 to-primary-700 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold">H</span>
              </div>
              <span className="text-xl font-bold text-white">HSC HTTP</span>
            </div>
            <p className="text-sm text-gray-400">
              High-performance HTTP server built with Kotlin Multiplatform.
            </p>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">Product</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <a href="#features" className="hover:text-primary-400 transition-colors">
                  Features
                </a>
              </li>
              <li>
                <a href="#docs" className="hover:text-primary-400 transition-colors">
                  Documentation
                </a>
              </li>
              <li>
                <a href="#pricing" className="hover:text-primary-400 transition-colors">
                  Pricing
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-white font-semibold mb-4">Resources</h3>
            <ul className="space-y-2 text-sm">
              <li>
                <a href="#guides" className="hover:text-primary-400 transition-colors">
                  Guides
                </a>
              </li>
              <li>
                <a href="#api" className="hover:text-primary-400 transition-colors">
                  API Reference
                </a>
              </li>
              <li>
                <a href="#examples" className="hover:text-primary-400 transition-colors">
                  Examples
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
          <p>&copy; {currentYear} HSC HTTP. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
}
