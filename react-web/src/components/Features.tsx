interface Feature {
  icon: string;
  title: string;
  description: string;
}

const features: Feature[] = [
  {
    icon: '⚡',
    title: 'Lightning Fast',
    description: 'Built on Ktor for exceptional performance and low latency response times.',
  },
  {
    icon: '🔒',
    title: 'Secure by Default',
    description: 'Industry-standard security practices with authentication and session management.',
  },
  {
    icon: '🎯',
    title: 'Type-Safe',
    description: 'Leverages Kotlin\'s powerful type system for compile-time safety and reliability.',
  },
  {
    icon: '🔄',
    title: 'Multiplatform',
    description: 'Kotlin Multiplatform architecture enables seamless code sharing across platforms.',
  },
  {
    icon: '📦',
    title: 'Easy Deployment',
    description: 'Docker-ready with simple configuration and deployment options.',
  },
  {
    icon: '🛠️',
    title: 'Developer Friendly',
    description: 'Clear APIs, comprehensive documentation, and excellent IDE support.',
  },
];

export function Features() {
  return (
    <section className="py-20 bg-gray-50">
      <div className="container mx-auto px-6">
        <div className="text-center mb-16">
          <h2 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
            Why Choose HSC HTTP?
          </h2>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            A modern HTTP server designed for developers who demand performance and reliability.
          </p>
        </div>
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-8">
          {features.map((feature) => (
            <div
              key={feature.title}
              className="bg-white rounded-xl p-8 shadow-md hover:shadow-xl transition-shadow"
            >
              <div className="text-5xl mb-4">{feature.icon}</div>
              <h3 className="text-2xl font-bold text-gray-900 mb-3">
                {feature.title}
              </h3>
              <p className="text-gray-600 leading-relaxed">
                {feature.description}
              </p>
            </div>
          ))}
        </div>
      </div>
    </section>
  );
}
