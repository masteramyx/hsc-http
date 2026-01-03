import { SEOHead } from "../components/SEOHead.tsx";
import { useSession } from "../contexts/SessionContext";
import { useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import { Professional, UserType } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

export function DashboardPage() {
  const { user, isLoading } = useSession();
  const navigate = useNavigate();
  const [profile, setProfile] = useState<Professional | null>(null);
  const [profileLoading, setProfileLoading] = useState(false);
  const [profileError, setProfileError] = useState<string | null>(null);

  // Redirect if not logged in
  useEffect(() => {
    if (!isLoading && !user) {
      navigate('/login');
    }
  }, [user, isLoading, navigate]);

  useEffect(() => {
      if(user?.userType == UserType.PROFESSIONAL) {
          // fetch professional profile
          const fetchProfile = async () => {
              try {
                  setProfileLoading(true)
                  const response = await fetch('/api/v1/professional/profile', {
                      method: 'GET',
                      credentials: 'include', // Important: include cookies
                  });

                  if (response.ok) {
                      const jsonText = await response.text();
                      const data: Professional = Professional.Companion.fromJson(jsonText);
                      setProfile(data)
                  } else {
                      setProfileError('Failed to load profile');
                  }
              } catch (error) {
                  console.error('Fetch failed:', error)
                  setProfileError('Network error occurred')
              } finally {
                  setProfileLoading(false)
              }
          };

          fetchProfile();
      } else {
          // no op yet
      }
  }, [user])


  if (isLoading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="text-gray-600">Loading...</div>
      </div>
    );
  }

  if (!user) {
    return null;
  }

  return (
    <>
      <SEOHead
        title="Dashboard - ShadowConnects"
        description="Your ShadowConnects dashboard"
        path="/dashboard"
      />

      <div className="min-h-screen bg-gray-50 py-12 px-4">
        <div className="max-w-6xl mx-auto">
          {/* Header */}
          <div className="bg-white rounded-lg shadow-md p-8 mb-6">
            <h1 className="text-3xl font-bold text-gray-900 mb-2">
              Welcome back!
            </h1>
            <p className="text-gray-600">{user.email}</p>
            <p className="text-sm text-gray-500 mt-1">
              Account Type: {user.userType.name}
            </p>
          </div>

          {/* Professional Profile Section */}
          {user.userType === UserType.PROFESSIONAL && (
            <div className="bg-white rounded-lg shadow-md p-8 mb-6">
              <h2 className="text-xl font-semibold text-gray-900 mb-4">
                Professional Profile
              </h2>

              {profileLoading && (
                <p className="text-gray-600">Loading profile...</p>
              )}

              {profileError && (
                <div className="bg-red-50 border border-red-200 rounded-lg p-4 mb-4">
                  <p className="text-red-800">{profileError}</p>
                </div>
              )}

              {profile && (
                <div className="space-y-2">
                  <p className="text-gray-700">
                    <span className="font-medium">Name:</span> {profile.firstName} {profile.lastName}
                  </p>
                  <p className="text-gray-700">
                    <span className="font-medium">Type:</span> {profile.professionalType.name}
                  </p>
                  <p className="text-gray-700">
                    <span className="font-medium">Specialization:</span> {profile.specialization?.name || 'Not specified'}
                  </p>
                  <p className="text-gray-700">
                    <span className="font-medium">Organization:</span> {profile.practiceName || 'Not specified'}
                  </p>
                  <p className="text-gray-700">
                    <span className="font-medium">Email Verified:</span>{' '}
                    {user.emailVerified ? (
                      <span className="text-green-600">✓ Verified</span>
                    ) : (
                      <span className="text-yellow-600">Pending Verification</span>
                    )}
                  </p>
                </div>
              )}

              {!profileLoading && !profile && !profileError && (
                <p className="text-gray-600">No profile data available</p>
              )}
            </div>
          )}

          {/* Student Placeholder */}
          {user.userType === UserType.STUDENT && (
            <div className="bg-white rounded-lg shadow-md p-8 mb-6">
              <h2 className="text-xl font-semibold text-gray-900 mb-4">
                Student Dashboard
              </h2>
              <p className="text-gray-600">
                Student dashboard coming soon! You'll be able to browse shadowing opportunities and connect with professionals.
              </p>
            </div>
          )}

          {/* Document Section */}
          <div className="bg-white rounded-lg shadow-md p-8 mb-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-xl font-semibold text-gray-900">
                Documents
              </h2>
              <button
                onClick={() => navigate('/documents/upload')}
                className="text-left p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition"
              >
                <div>
                  <h5 className="font-medium text-gray-900">Upload Document</h5>
                </div>
              </button>
            </div>
            <p className="text-gray-500">No documents uploaded</p>
          </div>

          {/* Activity Placeholder */}
          <div className="bg-white rounded-lg shadow-md p-8 mb-6">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">
              Recent Activity
            </h2>
            <p className="text-gray-500">No recent activity to display</p>
          </div>

          {/* Quick Actions */}
          <div className="bg-white rounded-lg shadow-md p-8">
            <h2 className="text-xl font-semibold text-gray-900 mb-4">
              Quick Actions
            </h2>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <button
                onClick={() => navigate('/profile/edit')}
                className="text-left p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition"
              >
                <h3 className="font-medium text-gray-900">Edit Profile</h3>
                <p className="text-sm text-gray-600">Update your information</p>
              </button>

              <button
                onClick={() => navigate('/opportunities')}
                className="text-left p-4 border border-gray-200 rounded-lg hover:bg-gray-50 transition"
              >
                <h3 className="font-medium text-gray-900">Browse Opportunities</h3>
                <p className="text-sm text-gray-600">
                  {user.userType === UserType.PROFESSIONAL
                    ? 'Find students to mentor'
                    : 'Find shadowing opportunities'}
                </p>
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
