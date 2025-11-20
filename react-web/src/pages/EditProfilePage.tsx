import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSession } from '../contexts/SessionContext.tsx';
import { UserInfo, UserType } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';
import { SEOHead} from '../components/SEOHead.tsx';

export function EditProfilePage() {
    const { user, isLoading } = useSession() as { user: UserInfo | null; isLoading: boolean };
    const navigate = useNavigate();

    // Redirect if not logged in
    useEffect(() => {
        if (!isLoading && !user) {
            navigate('/login')
        }
    }, [user, isLoading, navigate]);

    if (isLoading) {
        return <div className="min-h-screen bg-gray-50 flex items-center justify-center">
            <div className="text-gray-600">Loading...</div>
        </div>
    }

    if (!user) {
        return null;
    }

    return (
        <>
            <SEOHead title="Edit Profile - ShadowConnects"
                     description="Update your profile information"
                     path="/profile/edit"
                     />

            <div className="min-h-screen bg-gray-50 py-12 px-4">
                <div className="max-w-4xl mx-auto">
                    {/* Debug: Check userType */}
                    <div className="bg-yellow-100 p-4 mb-4 rounded">
                        <p>Debug Info:</p>
                        <p>user.userType value: {JSON.stringify(user.userType)}</p>
                        <p>user.userType type: {typeof user.userType}</p>
                        <p>UserType.PROFESSIONAL: {JSON.stringify(UserType.PROFESSIONAL)}</p>
                        <p>Comparison result: {String(user.userType === UserType.PROFESSIONAL)}</p>
                        <p>From string: {String(UserType.Companion.fromString(user.userType.name))}</p>
                        <p>Enum: {String(UserType.PROFESSIONAL)}</p>
                    </div>

                    {user.userType === UserType.PROFESSIONAL ? (
                        <ProfessionalEditForm/>
                    ) : (
                        <StudentEditForm/>
                    )}
                </div>
            </div>
        </>
    )
}


function ProfessionalEditForm() {
    const [activeTab, setActiveTab] = useState(0);

    const tabs = ["Personal Info", "Credentials", "Practice Info", "Shadowing"];

    return (
        <div>
            {/*React hasno built-in TabRow*/}
            <div className="flex border-b">
                {tabs.map((title, index) => (
                    <button
                        key={index}
                        onClick={() => setActiveTab(index)}
                        className={activeTab === index ? 'border-b-2 border-blue-600' : ''}
                    >
                        {title}
                    </button>
                ))}
            </div>

            {/*Tab Content - render conditionally*/}
            <div className="p-4">
                {/*{activeTab === 0 && <PersonalInfoTab />}*/}
                {/*{activeTab === 1 && <CredentialsTab />}*/}
                {/*{activeTab === 2 && <PracticeInfoTab />}*/}
                {/*{activeTab === 3 && <ShadowingDetailsTab />}*/}
                {(() => {
                    switch(activeTab) {
                        case 0: return <PersonalInfoTab />;
                        case 1: return <CredentialsTab />;
                        case 2: return <PracticeInfoTab />;
                        case 3: return <ShadowingDetailsTab />;
                    }
                })()}
            </div>
        </div>
    )
}



function PersonalInfoTab(){
    return <div>Personal Info Tab</div>;
}
function CredentialsTab(){
    return <div>Personal Info Tab</div>;
}
function PracticeInfoTab(){
    return <div>Personal Info Tab</div>;
}
function ShadowingDetailsTab(){
    return <div>Personal Info Tab</div>;
}




function StudentEditForm() {
    return <div>Student Edit Form</div>;
}