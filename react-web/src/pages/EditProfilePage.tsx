import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSession } from '../contexts/SessionContext.tsx';
import { UserInfo, UserType, Professional } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';
import { SEOHead} from '../components/SEOHead.tsx';
import {PhotoUpload} from "../components/PhotoUpload.tsx";
import { FormData } from '../types/FormData.ts';

const INITIAL_FORM_DATA: FormData = {
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    photo: null,
    password: '',
    confirmPassword: '',
    professionalType: '',
    licenseNumber: '',
    licenseState: '',
    medicalSpecialty: '',
    yearsExperience: '',
    practiceType: '',
    practiceName: '',
    practiceAddress: '',
    practiceCity: '',
    practiceState: '',
    practiceZip: '',
    titlePosition: '',
    availability: [],
    bio: '',
    availabilityNotes: '',
};

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
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState(0);

    // State management for dirty tracking
    const [originalData, setOriginalData] = useState<FormData | null>(null);
    const [formData, setFormData] = useState<FormData>(INITIAL_FORM_DATA);
    const [errors, setErrors] = useState<Record<string, string | undefined>>({});
    const [isLoading, setIsLoading] = useState(true);
    const [isSaving, setIsSaving] = useState(false);

    // Fetch profile data on mount
    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const response = await fetch('/api/v1/professional/profile', {
                    credentials: 'include'
                });

                if (response.ok) {
                    const jsonText = await response.text();
                    const data = Professional.Companion.fromJson(jsonText)
                    // Can't use spread operator because types don't match:
                    // Professional has enum objects (professionalType: ProfessionalType), FormData needs strings
                    // Professional has numbers, FormData needs string representations
                    // Need explicit conversion for each field
                    const formState: FormData = {
                        firstName: data.firstName,
                        lastName: data.lastName,
                        email: data.email,
                        phone: data.phone,
                        photo: null,
                        password: '',
                        confirmPassword: '',
                        professionalType: data.professionalType.name,
                        licenseNumber: data.licenseNumber || '',
                        licenseState: data.licenseState || '',
                        medicalSpecialty: data.specialization?.name || '',
                        yearsExperience: data.yearsExperience?.toString() || '',
                        practiceType: data.practiceType?.name || '',
                        practiceName: data.practiceName || '',
                        practiceAddress: data.practiceAddress || '',
                        practiceCity: data.practiceCity || '',
                        practiceState: data.practiceState || '',
                        practiceZip: data.practiceZip || '',
                        titlePosition: data.titlePosition || '',
                        availability: [],
                        bio: data.bio || '',
                        availabilityNotes: data.availabilityNotes || ''
                    };
                    // Set both original and form data
                    setOriginalData(formState);
                    setFormData(formState);
                }
            } catch (error) {
                console.error('Failed to fetch profile:', error);
            } finally {
                setIsLoading(false);
            }
        };

        void fetchProfile();
    }, []);

    // Compare formData with originalData to determine if Save should be enabled
    const hasChanges = (originalData !== null && JSON.stringify(formData) !== JSON.stringify(originalData));

    const handleInputChange = (field: string, value: string) => {
        setFormData(prev => ({ ...prev, [field]: value }));
        // Clear error for this field when user types
        if (errors[field]) {
            setErrors(prev => ({ ...prev, [field]: undefined }));
        }
    };

    const handleSave = async () => {
        setIsSaving(true);
        try {
            // TODO: Implement save logic
            console.log('Saving:', formData);
        } catch (error) {
            console.error('Save failed:', error);
        } finally {
            setIsSaving(false);
        }
    };

    const handleCancel = () => {
        navigate('/dashboard');
    };

    if (isLoading) {
        return <div className="text-center py-8">Loading profile...</div>;
    }

    const tabs = ["Personal Info", "Credentials", "Practice Info", "Shadowing"];

    return (
        <div>
            {/*React has no built-in TabRow*/}
            <div className="flex border-b">
                {tabs.map((title, index) => (
                    <button
                        key={index}
                        onClick={() => setActiveTab(index)}
                        className={`px-4 py-2 font-medium transition-colors ${
                            activeTab === index
                                ? 'text-blue-600 border-b-2 border-blue-600'
                                : 'text-gray-600 hover:text-blue-500'
                        }`}
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
                        case 0: return <PersonalInfoTab formData={formData} errors={errors} handleInputChange={handleInputChange} setFormData={setFormData} />;
                        case 1: return <CredentialsTab />;
                        case 2: return <PracticeInfoTab />;
                        case 3: return <ShadowingDetailsTab />;
                    }
                })()}
            </div>

            {/* Save/Cancel Buttons */}
            <div className="flex justify-end gap-4 mt-8 px-4 py-4 border-t bg-gray-50">
                <button
                    onClick={handleCancel}
                    className="px-6 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-100 transition-colors"
                >
                    Cancel
                </button>
                <button
                    onClick={handleSave}
                    disabled={!hasChanges || isSaving}
                    className={`px-6 py-2 rounded-lg font-medium transition-colors ${
                        hasChanges && !isSaving
                            ? 'bg-blue-600 text-white hover:bg-blue-700'
                            : 'bg-gray-300 text-gray-500 cursor-not-allowed'
                    }`}
                >
                    {isSaving ? 'Saving...' : 'Save Changes'}
                </button>
            </div>
        </div>
    )
}



function PersonalInfoTab({ formData, errors, handleInputChange, setFormData }: any) {
    // return <div>Personal Info Tab</div>;
    return (
        <div>
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Basic Information</h2>
            <div className="space-y-6">
                <div className="grid md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            First Name *
                        </label>
                        <input
                            type="text"
                            value={formData.firstName}
                            onChange={(e) => handleInputChange('firstName', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.firstName ? 'border-red-500' : 'border-gray-300'
                            }`}
                        />
                        {errors.firstName && (
                            <p className="text-red-500 text-sm mt-1">{errors.firstName}</p>
                        )}
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Last Name *
                        </label>
                        <input
                            type="text"
                            value={formData.lastName}
                            onChange={(e) => handleInputChange('lastName', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.lastName ? 'border-red-500' : 'border-gray-300'
                            }`}
                        />
                        {errors.lastName && (
                            <p className="text-red-500 text-sm mt-1">{errors.lastName}</p>
                        )}
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Email Address *
                    </label>
                    <input
                        type="email"
                        value={formData.email}
                        onChange={(e) => handleInputChange('email', e.target.value)}
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.email ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    {errors.email && (
                        <p className="text-red-500 text-sm mt-1">{errors.email}</p>
                    )}
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Phone Number *
                    </label>
                    <input
                        type="tel"
                        value={formData.phone}
                        onChange={(e) => handleInputChange('phone', e.target.value)}
                        placeholder="(555) 123-4567"
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.phone ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    {errors.phone && (
                        <p className="text-red-500 text-sm mt-1">{errors.phone}</p>
                    )}
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Profile Photo (Optional)
                    </label>
                    <PhotoUpload
                        value={formData.photo}
                        onChange={(file) => setFormData((prev: FormData) => ({ ...prev, photo: file }))}
                        error={errors.photo}
                    />
                    <p className="text-gray-500 text-sm mt-2">
                        Upload a professional headshot to help students recognize you
                    </p>
                </div>

                <div className="grid md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Password *
                        </label>
                        <input
                            type="password"
                            value={formData.password}
                            onChange={(e) => handleInputChange('password', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.password ? 'border-red-500' : 'border-gray-300'
                            }`}
                        />
                        {errors.password && (
                            <p className="text-red-500 text-sm mt-1">{errors.password}</p>
                        )}
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            Confirm Password *
                        </label>
                        <input
                            type="password"
                            value={formData.confirmPassword}
                            onChange={(e) => handleInputChange('confirmPassword', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.confirmPassword ? 'border-red-500' : 'border-gray-300'
                            }`}
                        />
                        {errors.confirmPassword && (
                            <p className="text-red-500 text-sm mt-1">{errors.confirmPassword}</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    )
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