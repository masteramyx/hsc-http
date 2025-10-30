import { SEOHead } from '../components/SEOHead';

export function HowItWorksPage() {
  return (
    <>
      <SEOHead
        title="How Shadow Connects Works"
        description="Learn how Shadow Connects helps students find clinical shadowing opportunities and enables healthcare professionals to mentor the next generation."
        path="/how-it-works"
      />
      <div className="py-20">
      <div className="container mx-auto px-6">
        <div className="max-w-4xl mx-auto">
          <h1 className="text-5xl font-bold text-gray-900 mb-8">
            How Shadow Connects Works
          </h1>

          <div className="space-y-12">
            {/* For Students Section */}
            <section>
              <h2 className="text-3xl font-bold text-gray-900 mb-6">For Students</h2>

              <div className="space-y-6">
                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">1</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Create Your Profile</h3>
                      <p className="text-gray-600">
                        Sign up and build your profile with your educational background, interests,
                        and career goals. Tell us what specialties you're interested in exploring.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">2</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Browse Opportunities</h3>
                      <p className="text-gray-600">
                        Search through verified shadowing opportunities across various medical specialties
                        and healthcare settings. Filter by location, specialty, and time commitment.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">3</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Connect & Schedule</h3>
                      <p className="text-gray-600">
                        Apply to opportunities that match your interests. Connect directly with healthcare
                        professionals and schedule your shadowing experience at a time that works for both of you.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">4</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Gain Experience</h3>
                      <p className="text-gray-600">
                        Observe real medical procedures, interact with patients (when appropriate), and learn
                        from experienced healthcare professionals. Build your network and gain invaluable insights.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </section>

            {/* For Professionals Section */}
            <section className="pt-8 border-t border-gray-200">
              <h2 className="text-3xl font-bold text-gray-900 mb-6">For Healthcare Professionals</h2>

              <div className="space-y-6">
                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">1</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Register Your Practice</h3>
                      <p className="text-gray-600">
                        Create a professional profile with your credentials, specialty, and practice information.
                        Go through our simple verification process to ensure student safety.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">2</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Post Opportunities</h3>
                      <p className="text-gray-600">
                        List shadowing opportunities with your availability, requirements, and what students
                        can expect to observe. Set your own schedule and capacity.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">3</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Review Applications</h3>
                      <p className="text-gray-600">
                        Review student applications and select candidates that align with your preferences.
                        Communicate requirements and coordinate schedules through our platform.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-white rounded-xl p-6 shadow-md">
                  <div className="flex items-start space-x-4">
                    <div className="flex-shrink-0 w-12 h-12 bg-primary-100 rounded-full flex items-center justify-center">
                      <span className="text-2xl font-bold text-primary-600">4</span>
                    </div>
                    <div>
                      <h3 className="text-xl font-semibold text-gray-900 mb-2">Mentor & Inspire</h3>
                      <p className="text-gray-600">
                        Share your knowledge and passion for healthcare with the next generation.
                        Help shape future healthcare professionals while giving back to your community.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </section>

            {/* Safety & Compliance */}
            <section className="pt-8 border-t border-gray-200">
              <h2 className="text-3xl font-bold text-gray-900 mb-6">Safety & Compliance</h2>
              <div className="bg-primary-50 rounded-xl p-8">
                <ul className="space-y-4 text-gray-700">
                  <li className="flex items-start">
                    <span className="text-primary-600 mr-3">✓</span>
                    <span><strong>HIPAA Compliant:</strong> All interactions and data handling meet healthcare privacy standards</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-primary-600 mr-3">✓</span>
                    <span><strong>Verified Professionals:</strong> Healthcare providers undergo credential verification</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-primary-600 mr-3">✓</span>
                    <span><strong>Student Screening:</strong> Educational background verification for all students</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-primary-600 mr-3">✓</span>
                    <span><strong>Clear Guidelines:</strong> Comprehensive shadowing policies and expectations</span>
                  </li>
                </ul>
              </div>
            </section>
          </div>
        </div>
      </div>
    </div>
    </>
  );
}
