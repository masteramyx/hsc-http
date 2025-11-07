import { useNavigate } from 'react-router-dom';
import { SEOHead } from '../components/SEOHead';

export function ForProfessionalsPage() {
  const navigate = useNavigate();
  return (
    <>
      <SEOHead
        title="For Healthcare Professionals - Host Shadow Students"
        description="Healthcare professionals: Share your expertise by hosting shadow students. Make an impact, inspire the next generation, and give back to the medical community."
        path="/for-professionals"
      />
      <div className="py-20">
      <div className="container mx-auto px-6">
        <div className="max-w-5xl mx-auto">
          {/* Hero Section */}
          <div className="text-center mb-16">
            <h1 className="text-5xl font-bold text-gray-900 mb-6">
              For Healthcare Professionals
            </h1>
            <p className="text-2xl text-gray-600 max-w-3xl mx-auto">
              Share your expertise and inspire the next generation of healthcare professionals
            </p>
          </div>

          {/* Benefits Grid */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">Why Host Shadowing Students?</h2>
            <div className="grid md:grid-cols-2 gap-8">
              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">🎓</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Give Back & Mentor
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Share your knowledge and passion for medicine with motivated students.
                  Help shape the future of healthcare by mentoring the next generation of providers.
                </p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">⚡</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Stay Energized
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Teaching and mentoring can reinvigorate your own practice. Students bring fresh
                  perspectives and enthusiasm that can remind you why you chose healthcare.
                </p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">🏆</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Professional Recognition
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Build your reputation as an educator and mentor in the medical community.
                  Verified badge and reviews showcase your commitment to education.
                </p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8">
                <div className="text-4xl mb-4">⚙️</div>
                <h3 className="text-2xl font-bold text-gray-900 mb-3">
                  Complete Control
                </h3>
                <p className="text-gray-600 leading-relaxed">
                  Set your own schedule, requirements, and capacity. Only accept students that
                  meet your criteria. Pause or modify opportunities at any time.
                </p>
              </div>
            </div>
          </section>

          {/* How It Works for Professionals */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">Simple Process for Busy Professionals</h2>
            <div className="space-y-6">
              <div className="bg-gradient-to-r from-primary-50 to-white rounded-xl p-6 flex items-start space-x-6">
                <div className="flex-shrink-0 w-12 h-12 bg-primary-600 text-white rounded-full flex items-center justify-center font-bold text-xl">
                  1
                </div>
                <div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">Quick Registration</h3>
                  <p className="text-gray-600">
                    Create your professional profile in minutes. Provide your credentials, specialty,
                    and practice information. We verify your license and credentials.
                  </p>
                </div>
              </div>

              <div className="bg-gradient-to-r from-primary-50 to-white rounded-xl p-6 flex items-start space-x-6">
                <div className="flex-shrink-0 w-12 h-12 bg-primary-600 text-white rounded-full flex items-center justify-center font-bold text-xl">
                  2
                </div>
                <div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">Post on Your Schedule</h3>
                  <p className="text-gray-600">
                    List shadowing opportunities when it works for you. Specify dates, times,
                    requirements, and what students will observe. Update anytime.
                  </p>
                </div>
              </div>

              <div className="bg-gradient-to-r from-primary-50 to-white rounded-xl p-6 flex items-start space-x-6">
                <div className="flex-shrink-0 w-12 h-12 bg-primary-600 text-white rounded-full flex items-center justify-center font-bold text-xl">
                  3
                </div>
                <div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">Review & Select</h3>
                  <p className="text-gray-600">
                    Students apply with their profiles and qualifications. Review at your convenience
                    and select students that match your preferences.
                  </p>
                </div>
              </div>

              <div className="bg-gradient-to-r from-primary-50 to-white rounded-xl p-6 flex items-start space-x-6">
                <div className="flex-shrink-0 w-12 h-12 bg-primary-600 text-white rounded-full flex items-center justify-center font-bold text-xl">
                  4
                </div>
                <div>
                  <h3 className="text-xl font-semibold text-gray-900 mb-2">Host & Mentor</h3>
                  <p className="text-gray-600">
                    Welcome students to your practice. Share your expertise, answer questions,
                    and provide the clinical exposure they need to succeed.
                  </p>
                </div>
              </div>
            </div>
          </section>

          {/* What We Handle */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">We Handle the Details</h2>
            <div className="bg-white rounded-xl shadow-md p-8">
              <div className="grid md:grid-cols-2 gap-6">
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-3">Platform Features</h3>
                  <ul className="space-y-2 text-gray-700">
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      Student verification and screening
                    </li>
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      HIPAA compliance documentation
                    </li>
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      Automated scheduling and reminders
                    </li>
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      Secure messaging system
                    </li>
                  </ul>
                </div>
                <div>
                  <h3 className="text-lg font-semibold text-gray-900 mb-3">Your Responsibilities</h3>
                  <ul className="space-y-2 text-gray-700">
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      Provide clinical observation opportunities
                    </li>
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      Answer student questions (time permitting)
                    </li>
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      Ensure proper patient consent when needed
                    </li>
                    <li className="flex items-start">
                      <span className="text-primary-600 mr-2">✓</span>
                      Provide feedback to students (optional)
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </section>

          {/* Specialties Section */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">We Welcome All Specialties</h2>
            <div className="bg-primary-50 rounded-xl p-8">
              <div className="grid md:grid-cols-3 gap-4 text-gray-700">
                <div>
                  <h3 className="font-semibold text-gray-900 mb-2">Primary Care</h3>
                  <ul className="space-y-1 text-sm">
                    <li>• Family Medicine</li>
                    <li>• Internal Medicine</li>
                    <li>• Pediatrics</li>
                  </ul>
                </div>
                <div>
                  <h3 className="font-semibold text-gray-900 mb-2">Surgical</h3>
                  <ul className="space-y-1 text-sm">
                    <li>• General Surgery</li>
                    <li>• Orthopedic Surgery</li>
                    <li>• Cardiothoracic Surgery</li>
                  </ul>
                </div>
                <div>
                  <h3 className="font-semibold text-gray-900 mb-2">Specialties</h3>
                  <ul className="space-y-1 text-sm">
                    <li>• Cardiology</li>
                    <li>• Neurology</li>
                    <li>• Oncology</li>
                  </ul>
                </div>
                <div>
                  <h3 className="font-semibold text-gray-900 mb-2">Emergency & Critical Care</h3>
                  <ul className="space-y-1 text-sm">
                    <li>• Emergency Medicine</li>
                    <li>• Critical Care</li>
                    <li>• Anesthesiology</li>
                  </ul>
                </div>
                <div>
                  <h3 className="font-semibold text-gray-900 mb-2">Diagnostics</h3>
                  <ul className="space-y-1 text-sm">
                    <li>• Radiology</li>
                    <li>• Pathology</li>
                    <li>• Laboratory Medicine</li>
                  </ul>
                </div>
                <div>
                  <h3 className="font-semibold text-gray-900 mb-2">Allied Health</h3>
                  <ul className="space-y-1 text-sm">
                    <li>• Nursing</li>
                    <li>• Physician Assistant</li>
                    <li>• Physical Therapy</li>
                  </ul>
                </div>
              </div>
              <p className="text-gray-600 text-sm mt-6 text-center">
                Don't see your specialty? We accept all healthcare fields!
              </p>
            </div>
          </section>

          {/* Testimonials */}
          <section className="mb-16">
            <h2 className="text-3xl font-bold text-gray-900 mb-8">What Professionals Say</h2>
            <div className="space-y-6">
              <div className="bg-white rounded-xl shadow-md p-8 border-l-4 border-primary-600">
                <p className="text-gray-700 italic mb-4">
                  "Hosting shadow students has been incredibly rewarding. Their enthusiasm reminds
                  me why I became a physician. The platform makes it easy to manage everything
                  around my busy schedule."
                </p>
                <p className="text-gray-900 font-semibold">— Dr. Michael Chen, Cardiologist</p>
                <p className="text-gray-600 text-sm">15 years experience • Hosted 30+ students</p>
              </div>

              <div className="bg-white rounded-xl shadow-md p-8 border-l-4 border-primary-600">
                <p className="text-gray-700 italic mb-4">
                  "I love giving back to future healthcare providers. Shadow Connects handles all
                  the logistics, so I can focus on teaching. Several of my shadow students have
                  stayed in touch and become colleagues."
                </p>
                <p className="text-gray-900 font-semibold">— Dr. Jennifer Williams, Emergency Medicine</p>
                <p className="text-gray-600 text-sm">8 years experience • Hosted 45+ students</p>
              </div>
            </div>
          </section>

          {/* Call to Action */}
          <section className="bg-gradient-to-r from-primary-600 to-primary-800 rounded-xl p-8 text-center text-white">
            <h2 className="text-3xl font-bold mb-4">Start Mentoring Today</h2>
            <p className="text-xl text-primary-100 mb-6">
              Join hundreds of healthcare professionals making a difference in students' lives.
            </p>
            <button
              onClick={() => navigate('/register/professional')}
              className="px-8 py-4 bg-white text-primary-700 rounded-lg font-semibold text-lg hover:bg-primary-50 transition-colors shadow-lg"
            >
              Create Professional Account
            </button>
            <p className="mt-4 text-primary-200 text-sm">
              Free to join • Complete control over your schedule • Make an impact
            </p>
          </section>
        </div>
      </div>
    </div>
    </>
  );
}
