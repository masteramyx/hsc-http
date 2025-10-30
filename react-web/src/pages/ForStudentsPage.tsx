import { SEOHead } from '../components/SEOHead';

export function ForStudentsPage() {
  return (
    <>
      <SEOHead
        title="For Students - Clinical Shadowing"
        description="Pre-med and pre-health students: Find clinical shadowing opportunities to gain hands-on healthcare experience, explore medical specialties, and connect with mentors."
        path="/for-students"
      />
      <div className="py-20">
      <div className="container mx-auto px-6">
        <div className="max-w-5xl mx-auto">
          {/* Hero Section */}
          <div className="text-center mb-16">
            <h1 className="text-5xl font-bold text-gray-900 mb-6">
              For Students
            </h1>
            <p className="text-2xl text-gray-600 max-w-3xl mx-auto">
              Gain the clinical experience you need to excel in your healthcare career journey
            </p>
          </div>

          {/* Benefits Grid */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">Why Shadow with Us?</h2>
            <div className="grid md:grid-cols-2 gap-8">
              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">🏥</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Diverse Specialties
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Explore opportunities across 50+ medical specialties - from primary care to
                  specialized surgery. Find the perfect match for your career interests and goals.
                </p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">✅</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Verified Opportunities
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Every shadowing opportunity is verified for authenticity and compliance.
                  Shadow with confidence knowing you're in professional, safe environments.
                </p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">📍</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Flexible Locations
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Find opportunities near your home, school, or anywhere you're planning to be.
                  Filter by city, state, or specific healthcare facilities.
                </p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">🎓</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Career Guidance
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Connect with mentors who can provide career advice, recommendation letters,
                  and insights into various healthcare career paths.
                </p>
              </div>
            </div>
          </section>

          {/* What You'll Learn Section */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">What You'll Experience</h2>
            <div className="bg-gradient-to-r from-primary-50 to-primary-100 rounded-xl p-8">
              <ul className="space-y-4">
                <li className="flex items-start">
                  <span className="text-primary-600 font-bold text-xl mr-3">•</span>
                  <div>
                    <strong className="text-gray-900">Real Patient Interactions:</strong>
                    <span className="text-gray-700"> Observe how healthcare professionals communicate with patients,
                    build rapport, and deliver compassionate care</span>
                  </div>
                </li>
                <li className="flex items-start">
                  <span className="text-primary-600 font-bold text-xl mr-3">•</span>
                  <div>
                    <strong className="text-gray-900">Clinical Procedures:</strong>
                    <span className="text-gray-700"> Witness medical procedures, diagnostic techniques, and treatment
                    planning in real-world settings</span>
                  </div>
                </li>
                <li className="flex items-start">
                  <span className="text-primary-600 font-bold text-xl mr-3">•</span>
                  <div>
                    <strong className="text-gray-900">Healthcare Workflows:</strong>
                    <span className="text-gray-700"> Understand the daily routines, challenges, and rewards of
                    various healthcare careers</span>
                  </div>
                </li>
                <li className="flex items-start">
                  <span className="text-primary-600 font-bold text-xl mr-3">•</span>
                  <div>
                    <strong className="text-gray-900">Medical Decision Making:</strong>
                    <span className="text-gray-700"> Learn how professionals diagnose conditions, weigh treatment
                    options, and make critical decisions</span>
                  </div>
                </li>
                <li className="flex items-start">
                  <span className="text-primary-600 font-bold text-xl mr-3">•</span>
                  <div>
                    <strong className="text-gray-900">Professional Development:</strong>
                    <span className="text-gray-700"> Ask questions, seek advice, and build relationships that can
                    support your healthcare career journey</span>
                  </div>
                </li>
              </ul>
            </div>
          </section>

          {/* Requirements Section */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">Student Requirements</h2>
            <div className="bg-white rounded-xl shadow-md p-8">
              <h3 className="text-xl font-semibold text-gray-900 mb-4">General Requirements</h3>
              <ul className="space-y-3 text-gray-700 mb-6">
                <li className="flex items-center">
                  <span className="text-primary-600 mr-3">✓</span>
                  Currently enrolled in an accredited college or university
                </li>
                <li className="flex items-center">
                  <span className="text-primary-600 mr-3">✓</span>
                  Pursuing a pre-health or health sciences major
                </li>
                <li className="flex items-center">
                  <span className="text-primary-600 mr-3">✓</span>
                  Completed HIPAA training (we provide resources)
                </li>
                <li className="flex items-center">
                  <span className="text-primary-600 mr-3">✓</span>
                  Professional attitude and commitment to learning
                </li>
              </ul>

              <h3 className="text-xl font-semibold text-gray-900 mb-4">Additional Requirements (Varies by Opportunity)</h3>
              <ul className="space-y-3 text-gray-700">
                <li className="flex items-center">
                  <span className="text-gray-400 mr-3">○</span>
                  Immunization records (flu shot, hepatitis B, etc.)
                </li>
                <li className="flex items-center">
                  <span className="text-gray-400 mr-3">○</span>
                  Background check
                </li>
                <li className="flex items-center">
                  <span className="text-gray-400 mr-3">○</span>
                  Specific coursework completed (anatomy, biology, etc.)
                </li>
                <li className="flex items-center">
                  <span className="text-gray-400 mr-3">○</span>
                  Professional liability insurance
                </li>
              </ul>
            </div>
          </section>

          {/* Success Stories Preview */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">Student Success Stories</h2>
            <div className="space-y-6">
              <div className="bg-white rounded-xl shadow-md p-8 border-l-4 border-primary-600">
                <p className="text-gray-700 italic mb-4">
                  "Shadow Connects helped me discover my passion for emergency medicine. The
                  experiences I gained were invaluable for my medical school applications and
                  gave me confidence in my career choice."
                </p>
                <p className="text-gray-900 font-semibold">— Sarah M., Pre-Med Student</p>
                <p className="text-gray-600 text-sm">Now attending Johns Hopkins School of Medicine</p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8 border-l-4 border-primary-600">
                <p className="text-gray-700 italic mb-4">
                  "I shadowed physicians in three different specialties before finding my calling
                  in pediatrics. The mentorship I received through this platform was life-changing."
                </p>
                <p className="text-gray-900 font-semibold">— James T., Undergraduate Student</p>
                <p className="text-gray-600 text-sm">Accepted to multiple medical schools</p>
              </div>
            </div>
          </section>

          {/* Call to Action */}
          <section className="bg-gradient-to-r from-primary-600 to-primary-800 rounded-xl p-8 text-center text-white">
            <h2 className="text-3xl font-bold mb-4">Ready to Start Your Journey?</h2>
            <p className="text-xl text-primary-100 mb-6">
              Create your free student profile and start exploring clinical shadowing opportunities today.
            </p>
            <button className="px-8 py-4 bg-white text-primary-700 rounded-lg font-semibold text-lg hover:bg-primary-50 transition-colors shadow-lg">
              Create Student Account
            </button>
            <p className="mt-4 text-primary-200 text-sm">
              Free to join • No credit card required • Start shadowing within days
            </p>
          </section>
        </div>
      </div>
    </div>
    </>
  );
}
