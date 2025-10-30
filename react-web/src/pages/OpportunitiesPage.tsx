import { SEOHead } from '../components/SEOHead';
import { ContactForm } from '../components/ContactForm';

export function OpportunitiesPage() {
  return (
    <>
      <SEOHead
        title="Browse Clinical Shadowing Opportunities"
        description="Explore verified clinical shadowing opportunities across various medical specialties. Filter by location, specialty, and time commitment to find your perfect match."
        path="/opportunities"
      />
      <div className="py-20">
      <div className="container mx-auto px-6">
        <div className="max-w-6xl mx-auto">
          <h1 className="text-5xl font-bold text-gray-900 mb-6">
            Clinical Shadowing Opportunities
          </h1>
          <p className="text-xl text-gray-600 mb-12">
            Explore verified shadowing opportunities across various medical specialties and healthcare settings.
          </p>

          {/* Search and Filter Section */}
          <div className="bg-white rounded-xl shadow-md p-6 mb-12">
            <div className="grid md:grid-cols-3 gap-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Specialty
                </label>
                <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent">
                  <option>All Specialties</option>
                  <option>Cardiology</option>
                  <option>Emergency Medicine</option>
                  <option>Family Medicine</option>
                  <option>Internal Medicine</option>
                  <option>Pediatrics</option>
                  <option>Surgery</option>
                </select>
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Location
                </label>
                <input
                  type="text"
                  placeholder="City, State, or ZIP"
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent"
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Time Commitment
                </label>
                <select className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500 focus:border-transparent">
                  <option>Any Duration</option>
                  <option>Single Day</option>
                  <option>1-2 Weeks</option>
                  <option>1 Month</option>
                  <option>Ongoing</option>
                </select>
              </div>
            </div>
            <button className="mt-4 w-full md:w-auto px-8 py-3 bg-primary-600 text-white rounded-lg font-semibold hover:bg-primary-700 transition-colors">
              Search Opportunities
            </button>
          </div>

          {/* Coming Soon Notice */}
          <div className="bg-primary-50 border border-primary-200 rounded-xl p-8 text-center">
            <h2 className="text-2xl font-bold text-gray-900 mb-4">
              Opportunity Listings Coming Soon!
            </h2>
            <p className="text-gray-700 mb-6">
              We're currently building our network of healthcare professionals and preparing
              verified shadowing opportunities. Sign up now to be notified when opportunities
              become available in your area.
            </p>
            <ContactForm
              buttonText="Join the Waitlist"
              buttonClassName="px-8 py-3 bg-primary-600 text-white rounded-lg font-semibold hover:bg-primary-700 transition-colors"
              wrapperClassName=""
            />
          </div>

          {/* Example Opportunities (Template for future) */}
          <div className="mt-12">
            <h2 className="text-2xl font-bold text-gray-900 mb-6">
              What to Expect
            </h2>
            <div className="grid md:grid-cols-2 gap-6">
              <div className="bg-white rounded-xl shadow-md p-6">
                <div className="flex items-center justify-between mb-4">
                  <h3 className="text-xl font-semibold text-gray-900">
                    Cardiology - General Practice
                  </h3>
                  <span className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
                    Available
                  </span>
                </div>
                <p className="text-gray-600 mb-4">
                  Shadow a cardiologist in an outpatient clinic. Observe patient consultations,
                  ECG interpretations, and treatment planning.
                </p>
                <div className="space-y-2 text-sm text-gray-600">
                  <p><strong>Location:</strong> Example City, CA</p>
                  <p><strong>Duration:</strong> 1-2 days per week</p>
                  <p><strong>Requirements:</strong> Pre-med student, basic medical terminology</p>
                </div>
              </div>

              <div className="bg-white rounded-xl shadow-md p-6">
                <div className="flex items-center justify-between mb-4">
                  <h3 className="text-xl font-semibold text-gray-900">
                    Emergency Medicine
                  </h3>
                  <span className="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
                    Available
                  </span>
                </div>
                <p className="text-gray-600 mb-4">
                  Experience the fast-paced environment of an emergency department.
                  Observe trauma cases, urgent care, and emergency procedures.
                </p>
                <div className="space-y-2 text-sm text-gray-600">
                  <p><strong>Location:</strong> Example City, NY</p>
                  <p><strong>Duration:</strong> 8-12 hour shifts</p>
                  <p><strong>Requirements:</strong> Pre-med/nursing student, HIPAA training</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    </>
  );
}
