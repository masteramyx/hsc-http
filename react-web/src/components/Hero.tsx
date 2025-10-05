export function Hero() {
  return (
    <section className="relative overflow-hidden bg-gradient-to-br from-primary-600 via-primary-700 to-primary-900 text-white">
      <div className="absolute inset-0 bg-black opacity-20"></div>
      <div className="container mx-auto px-6 py-24 md:py-32 relative z-10">
        <div className="max-w-4xl mx-auto text-center">
          <h1 className="text-5xl md:text-7xl font-bold mb-6 leading-tight">
            Connect With
            <span className="block bg-gradient-to-r from-primary-200 to-white bg-clip-text text-transparent">
              Healthcare Professionals
            </span>
          </h1>
          <p className="text-xl md:text-2xl mb-10 text-primary-100 leading-relaxed">
            Gain invaluable clinical experience through professional health shadowing opportunities.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <button className="px-8 py-4 bg-white text-primary-700 rounded-lg font-semibold text-lg hover:bg-primary-50 transition-colors shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all">
              Find Opportunities
            </button>
            <button className="px-8 py-4 bg-primary-800/50 backdrop-blur-sm text-white rounded-lg font-semibold text-lg hover:bg-primary-800/70 transition-colors border border-primary-400/30">
              For Professionals
            </button>
          </div>
        </div>
      </div>
      <div className="absolute bottom-0 left-0 right-0">
        <svg
          className="w-full h-16 md:h-24 fill-white"
          viewBox="0 0 1440 120"
          preserveAspectRatio="none"
        >
          <path d="M0,64L80,69.3C160,75,320,85,480,80C640,75,800,53,960,48C1120,43,1280,53,1360,58.7L1440,64L1440,120L1360,120C1280,120,1120,120,960,120C800,120,640,120,480,120C320,120,160,120,80,120L0,120Z"></path>
        </svg>
      </div>
    </section>
  );
}
