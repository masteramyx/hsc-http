export function CTA() {
  return (
    <section className="py-20 bg-gradient-to-r from-primary-600 to-primary-800">
      <div className="container mx-auto px-6">
        <div className="max-w-4xl mx-auto text-center text-white">
          <h2 className="text-4xl md:text-5xl font-bold mb-6">
            Ready to Get Started?
          </h2>
          <p className="text-xl text-primary-100 mb-10 leading-relaxed">
            Join developers building high-performance applications with HSC HTTP.
          </p>
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <button className="px-8 py-4 bg-white text-primary-700 rounded-lg font-semibold text-lg hover:bg-primary-50 transition-colors shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 transition-all">
              Start Building Now
            </button>
            <button className="px-8 py-4 bg-primary-700/50 backdrop-blur-sm text-white rounded-lg font-semibold text-lg hover:bg-primary-700/70 transition-colors border border-primary-400/30">
              View on GitHub
            </button>
          </div>
          <p className="mt-8 text-primary-200 text-sm">
            Free and open source • No credit card required
          </p>
        </div>
      </div>
    </section>
  );
}
