interface Feature {
  icon: string;
  title: string;
  description: string;
}

const features: Feature[] = [
  {
    icon: '🏥',
    title: 'Connect With Professionals',
    description: 'Network directly with physicians, nurses, and healthcare providers willing to share their expertise.',
  },
  {
    icon: '📚',
    title: 'Gain Clinical Experience',
    description: 'Observe real-world medical procedures and patient care in various healthcare settings.',
  },
  {
    icon: '🎓',
    title: 'Career Development',
    description: 'Explore different medical specialties to find your passion before committing to a career path.',
  },
  {
    icon: '🤝',
    title: 'Mentorship Opportunities',
    description: 'Build lasting relationships with experienced healthcare professionals who guide your journey.',
  },
  {
    icon: '⏰',
    title: 'Flexible Scheduling',
    description: 'Find shadowing opportunities that fit your schedule, from single-day visits to ongoing programs.',
  },
  {
    icon: '✅',
    title: 'Verified Opportunities',
    description: 'All shadowing positions are verified and comply with healthcare privacy and safety standards.',
  },
];

export function Features() {
  return (
    <section className="py-20 bg-gray-50">
      <div className="container mx-auto px-6">
        <div className="text-center mb-16">
          <h2 className="text-4xl md:text-5xl font-bold text-gray-900 mb-4">
            Why Choose Shadow Connects?
          </h2>
          <p className="text-xl text-gray-600 max-w-2xl mx-auto">
            The premier platform connecting aspiring healthcare professionals with real-world clinical experience.
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
