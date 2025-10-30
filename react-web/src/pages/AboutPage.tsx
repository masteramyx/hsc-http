import { SEOHead } from '../components/SEOHead';

export function AboutPage() {
  return (
    <>
      <SEOHead
        title="About Shadow Connects"
        description="Learn about our mission to connect aspiring healthcare professionals with clinical shadowing opportunities. Discover how we're making healthcare education more accessible."
        path="/about"
      />
      <div className="py-20">
      <div className="container mx-auto px-6">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-5xl font-bold text-gray-900 mb-8">
            About Shadow Connects
          </h1>

          {/* Mission Section */}
          <section className="mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Our Mission</h2>
            <div className="bg-gradient-to-r from-primary-50 to-primary-100 rounded-xl p-8">
              <p className="text-lg text-gray-800 leading-relaxed">
                Shadow Connects was created to bridge the gap between aspiring healthcare
                professionals and clinical experience opportunities. We believe that hands-on
                exposure to real medical practice is essential for students to make informed
                career decisions and develop into compassionate, skilled healthcare providers.
              </p>
            </div>
          </section>

          {/* The Problem Section */}
          <section className="mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">The Challenge We're Solving</h2>
            <div className="space-y-4 text-gray-700">
              <p>
                Finding quality clinical shadowing opportunities has traditionally been a major
                challenge for pre-medical and pre-health students. Many students face:
              </p>
              <ul className="list-disc list-inside space-y-2 ml-4">
                <li>Limited access to healthcare professionals willing to host shadowers</li>
                <li>Lack of centralized platforms to discover opportunities</li>
                <li>Uncertainty about requirements and expectations</li>
                <li>Geographic barriers to finding nearby opportunities</li>
                <li>Difficulty connecting with professionals in specific specialties</li>
              </ul>
              <p>
                Meanwhile, healthcare professionals who want to mentor the next generation
                often don't know how to connect with motivated students.
              </p>
            </div>
          </section>

          {/* Solution Section */}
          <section className="mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Our Solution</h2>
            <div className="grid md:grid-cols-2 gap-6">
              <div className="bg-white rounded-xl shadow-md p-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-3">For Students</h3>
                <p className="text-gray-600">
                  A centralized platform to discover, apply for, and manage clinical shadowing
                  opportunities across various specialties and locations. Transparent requirements,
                  verified professionals, and streamlined communication.
                </p>
              </div>
              <div className="bg-white rounded-xl shadow-md p-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-3">For Professionals</h3>
                <p className="text-gray-600">
                  An easy way to offer shadowing opportunities, connect with motivated students,
                  and give back to the healthcare community. Complete control over scheduling,
                  requirements, and capacity.
                </p>
              </div>
            </div>
          </section>

          {/* Values Section */}
          <section className="mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Our Values</h2>
            <div className="space-y-6">
              <div className="border-l-4 border-primary-600 pl-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-2">Accessibility</h3>
                <p className="text-gray-600">
                  We believe every motivated student deserves access to clinical shadowing
                  opportunities, regardless of their network or background.
                </p>
              </div>
              <div className="border-l-4 border-primary-600 pl-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-2">Quality & Safety</h3>
                <p className="text-gray-600">
                  All opportunities are verified, HIPAA compliant, and meet professional
                  healthcare standards to ensure safe, valuable experiences.
                </p>
              </div>
              <div className="border-l-4 border-primary-600 pl-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-2">Mentorship</h3>
                <p className="text-gray-600">
                  We facilitate meaningful connections between students and professionals
                  that can develop into long-term mentorship relationships.
                </p>
              </div>
              <div className="border-l-4 border-primary-600 pl-6">
                <h3 className="text-xl font-semibold text-gray-900 mb-2">Community</h3>
                <p className="text-gray-600">
                  Building a supportive community of students and professionals united by
                  their passion for healthcare and education.
                </p>
              </div>
            </div>
          </section>

          {/* Impact Section */}
          <section className="mb-12">
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Our Impact</h2>
            <div className="grid md:grid-cols-3 gap-6">
              <div className="text-center bg-white rounded-xl shadow-md p-6">
                <div className="text-4xl font-bold text-primary-600 mb-2">1000+</div>
                <div className="text-gray-600">Students Connected</div>
              </div>
              <div className="text-center bg-white rounded-xl shadow-md p-6">
                <div className="text-4xl font-bold text-primary-600 mb-2">500+</div>
                <div className="text-gray-600">Healthcare Professionals</div>
              </div>
              <div className="text-center bg-white rounded-xl shadow-md p-6">
                <div className="text-4xl font-bold text-primary-600 mb-2">50+</div>
                <div className="text-gray-600">Medical Specialties</div>
              </div>
            </div>
          </section>

          {/* Call to Action */}
          <section className="bg-gradient-to-r from-primary-600 to-primary-800 rounded-xl p-8 text-center text-white">
            <h2 className="text-3xl font-bold mb-4">Join Our Community</h2>
            <p className="text-xl text-primary-100 mb-6">
              Whether you're a student seeking clinical experience or a healthcare professional
              looking to mentor the next generation, we'd love to have you.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center">
              <button className="px-8 py-3 bg-white text-primary-700 rounded-lg font-semibold hover:bg-primary-50 transition-colors">
                Sign Up as Student
              </button>
              <button className="px-8 py-3 bg-primary-700/50 backdrop-blur-sm text-white rounded-lg font-semibold hover:bg-primary-700/70 transition-colors border border-primary-400/30">
                Sign Up as Professional
              </button>
            </div>
          </section>
        </div>
      </div>
    </div>
    </>
  );
}
