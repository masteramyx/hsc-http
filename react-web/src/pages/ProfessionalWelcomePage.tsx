import {SEOHead} from "../components/SEOHead.tsx";
import {useNavigate} from 'react-router-dom';

export function ProfessionalWelcomePage() {
    const navigate = useNavigate()

    return (
        <>
            {/*SEO component*/}
            <SEOHead
                title="Welcome - ShadowConnects"
                description="Welcome to your professional account"
                path="/welcome/professional"
            />

            {/*Main container w/ tailwind css*/}
            <div className="min-h-screen bg-gray-50 py-12 px-4">
                <div className="max-w-3xl mx-auto">
                    {/* Welcome header */}
                    <div className="bg-white rounded-lg shadow-md p-8 mb-6">
                        <h1 className="text-3xl font-bold text-gray-900 mb-2">
                            Welcome! 🎉
                        </h1>
                        <p className="text-gray-600">
                            Your professional profile has been created successfully.
                        </p>
                    </div>

                    {/* Email verification notice */}
                    <div className="bg-blue-50 border border-blue-200 rounded-lg p-6 mb-6">
                        <div className="flex items-start gap-3">
                            <svg className="w-6 h-6 text-blue-600 flex-shrink-0 mt-0.5" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" />
                                <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" />
                            </svg>
                            <div>
                                <h3 className="font-semibold text-gray-900 mb-1">
                                    Verify Your Email Address
                                </h3>
                                <p className="text-gray-600 text-sm">
                                    We've sent a verification email to your inbox. Please click the link
                                    to verify your email address and unlock all features.
                                </p>
                            </div>
                        </div>
                    </div>

                    {/* Next steps card */}
                    <div className="bg-white rounded-lg shadow-md p-8">
                        <h2 className="text-xl font-semibold text-gray-900 mb-4">
                            Next Steps
                        </h2>

                        <div className="space-y-4">
                            {/* Each button is clickable and navigates somewhere */}
                            <button
                                onClick={() => navigate('/dashboard')}
                                className="w-full text-left p-4 border border-gray-200 rounded-lg hover:bg-gray-50
  transition">
                                <h3 className="font-medium text-gray-900">View Dashboard</h3>
                                <p className="text-sm text-gray-600">See your profile and activity</p>
                            </button>

                            <button
                                onClick={() => navigate('/profile/edit')}
                                className="w-full text-left p-4 border border-gray-200 rounded-lg hover:bg-gray-50
  transition"
                            >
                                <h3 className="font-medium text-gray-900">Edit Your Profile</h3>
                                <p className="text-sm text-gray-600">Add more details to stand out</p>
                            </button>

                            <button
                                onClick={() => navigate('/opportunities')}
                                className="w-full text-left p-4 border border-gray-200 rounded-lg hover:bg-gray-50
  transition"
                            >
                                <h3 className="font-medium text-gray-900">Browse Opportunities</h3>
                                <p className="text-sm text-gray-600">Find students to mentor</p>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </>
    );
}