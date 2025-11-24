import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSession } from '../contexts/SessionContext.tsx';
import { UserInfo, UserType, Professional, ProfessionalType, MedicalSpecialty, PracticeType, USStates, DayOfWeek, TimeRange, DayAvailability, DayAvailabilityJS } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';
import { SEOHead} from '../components/SEOHead.tsx';
import {PhotoUpload} from "../components/PhotoUpload.tsx";
import type { FormData } from '../types/FormData.ts';
import {useAvailabilityHandlers} from "../hooks/useAvailabilityHandlers.ts";
import { UpdateProfessionalRequest } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';

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
    const [errors, setErrors] = useState<Partial<Record<keyof FormData, string | null>>>({});
    const [isLoading, setIsLoading] = useState(true);
    const [isSaving, setIsSaving] = useState(false);
    const { handleDayToggle, handleTimeRangeToggle } = useAvailabilityHandlers(setFormData, errors, setErrors)

    /**
     * Fetches a photo from URL and converts it to a File object
     */
    const fetchPhotoAsFile = async (photoUrl: string): Promise<File | null> => {
        try {
            const response = await fetch(photoUrl)
            if(response.ok) {
                const blob = await response.blob();
                const filename = photoUrl.split('/').pop() || 'profile-photo.jpg';
                return new File([blob], filename, { type: blob.type });
            } else {
                return null;
                }
        } catch (error) {
            console.error('Failed to fetch photo:', error);
            return null;
        }
    };

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

                    // Fetch existing photo if URL exists
                    let photoFile: File | null = null;
                    if (data.photoUrl) {
                        photoFile = await fetchPhotoAsFile(data.photoUrl);
                    }

                    // Parse availability JSON string using Kotlin serializer
                    // The Kotlin function converts everything to JS arrays at the boundary
                    let availabilityArray: DayAvailabilityJS[] = [];
                    if (data.availability) {
                        try {
                            availabilityArray = DayAvailability.Companion.fromJsonArray(data.availability);
                        } catch (e) {
                            console.error('Failed to parse availability:', e);
                        }
                    }

                    // Can't use spread operator because types don't match:
                    // Professional has enum objects (professionalType: ProfessionalType), FormData needs strings
                    // Professional has numbers, FormData needs string representations
                    // Need explicit conversion for each field
                    const formState: FormData = {
                        firstName: data.firstName,
                        lastName: data.lastName,
                        email: data.email,
                        phone: data.phone,
                        photo: photoFile,
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
                        availability: availabilityArray,
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

    const handleInputChange = (field: keyof FormData, value: string) => {
        setFormData(prev => ({ ...prev, [field]: value }));
        // Clear error for this field when user types
        if (errors[field]) {
            setErrors(prev => ({ ...prev, [field]: undefined }));
        }
    };

    const handleSave = async () => {
        setIsSaving(true);
        try {
            // Use factory method - handles all string→enum and array→list conversions in Kotlin
            const request: UpdateProfessionalRequest = UpdateProfessionalRequest.Companion.fromJsData(
                formData.firstName,
                formData.lastName,
                formData.phone,
                formData.professionalType,  // String, factory converts to enum
                formData.licenseNumber || null,
                formData.licenseState || null,
                formData.medicalSpecialty || null,  // String, factory converts to enum
                formData.yearsExperience ? parseInt(formData.yearsExperience) : null,
                formData.practiceType || null,  // String, factory converts to enum
                formData.practiceName || null,
                formData.practiceAddress || null,
                formData.practiceCity || null,
                formData.practiceState || null,
                formData.practiceZip || null,
                formData.titlePosition || null,
                formData.bio || null,
                null, // photoUrl - handled separately
                formData.availability,  // DayAvailabilityJS[], factory converts to List<DayAvailability>
                formData.availabilityNotes || null
            );

            console.log('Saving:', request);
            const response = await fetch('/api/v1/professional/profile', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: request.toJsonString()
            });

            if (response.ok) {
                alert('Profile updated successfully!');
                navigate('/dashboard');
            } else {
                const errorText = await response.text();
                console.error('Save failed:', response.status, errorText);
                alert(`Save failed: ${response.status} - ${errorText}`);
            }
        } catch (error) {
            console.error('Save failed:', error);
            alert(`Save failed: ${error}`);
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
                {(() => {
                    switch(activeTab) {
                        case 0: return <PersonalInfoTab formData={formData} errors={errors} handleInputChange={handleInputChange} setFormData={setFormData} />;
                        case 1: return <CredentialsTab formData={formData} errors={errors} handleInputChange={handleInputChange} />;
                        case 2: return <PracticeInfoTab formData={formData} errors={errors} handleInputChange={handleInputChange} />;
                        case 3: return <ShadowingDetailsTab formData={formData} errors={errors} handleInputChange={handleInputChange} handleDayToggle={handleDayToggle} handleTimeRangeToggle={handleTimeRangeToggle} />;
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
function CredentialsTab({ formData, errors, handleInputChange }: any){
    const professionalTypes = ProfessionalType.values();
    const medicalSpecialties = MedicalSpecialty.values();
    const usStates = USStates.getInstance().getAll();

    return (
        <div>
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Professional Credentials</h2>
            <div className="space-y-6">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Professional Type *
                    </label>
                    <select
                        value={formData.professionalType}
                        onChange={(e) => handleInputChange('professionalType', e.target.value)}
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.professionalType ? 'border-red-500' : 'border-gray-300'
                        }`}
                    >
                        <option value="">Select professional type...</option>
                        {professionalTypes.map((type: any) => (
                            <option key={type.name} value={type.name}>
                                {type.displayName}
                            </option>
                        ))}
                    </select>
                    {errors.professionalType && (
                        <p className="text-red-500 text-sm mt-1">{errors.professionalType}</p>
                    )}
                </div>

                <div className="grid md:grid-cols-2 gap-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            License Number *
                        </label>
                        <input
                            type="text"
                            value={formData.licenseNumber}
                            onChange={(e) => handleInputChange('licenseNumber', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.licenseNumber ? 'border-red-500' : 'border-gray-300'
                            }`}
                        />
                        {errors.licenseNumber && (
                            <p className="text-red-500 text-sm mt-1">{errors.licenseNumber}</p>
                        )}
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            License State *
                        </label>
                        <select
                            value={formData.licenseState}
                            onChange={(e) => handleInputChange('licenseState', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.licenseState ? 'border-red-500' : 'border-gray-300'
                            }`}
                        >
                            <option value="">Select state...</option>
                            {usStates.map((state: string) => (
                                <option key={state} value={state}>
                                    {state}
                                </option>
                            ))}
                        </select>
                        {errors.licenseState && (
                            <p className="text-red-500 text-sm mt-1">{errors.licenseState}</p>
                        )}
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Medical Specialty *
                    </label>
                    <select
                        value={formData.medicalSpecialty}
                        onChange={(e) => handleInputChange('medicalSpecialty', e.target.value)}
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.medicalSpecialty ? 'border-red-500' : 'border-gray-300'
                        }`}
                    >
                        <option value="">Select specialty...</option>
                        {medicalSpecialties.map((specialty: any) => (
                            <option key={specialty.name} value={specialty.name}>
                                {specialty.displayName}
                            </option>
                        ))}
                    </select>
                    {errors.medicalSpecialty && (
                        <p className="text-red-500 text-sm mt-1">{errors.medicalSpecialty}</p>
                    )}
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Years of Experience *
                    </label>
                    <input
                        type="number"
                        min="0"
                        max="70"
                        value={formData.yearsExperience}
                        onChange={(e) => handleInputChange('yearsExperience', e.target.value)}
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.yearsExperience ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    {errors.yearsExperience && (
                        <p className="text-red-500 text-sm mt-1">{errors.yearsExperience}</p>
                    )}
                </div>
            </div>
        </div>
    )
}
function PracticeInfoTab({ formData, errors, handleInputChange }: any){
    const practiceTypes = PracticeType.values();
    const usStates = USStates.getInstance().getAll();

    return (
        <div>
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Practice Information</h2>
            <div className="space-y-6">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Practice Type *
                    </label>
                    <select
                        value={formData.practiceType}
                        onChange={(e) => handleInputChange('practiceType', e.target.value)}
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.practiceType ? 'border-red-500' : 'border-gray-300'
                        }`}
                    >
                        <option value="">Select practice type...</option>
                        {practiceTypes.map((type: any) => (
                            <option key={type.name} value={type.name}>
                                {type.displayName}
                            </option>
                        ))}
                    </select>
                    {errors.practiceType && (
                        <p className="text-red-500 text-sm mt-1">{errors.practiceType}</p>
                    )}
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Practice Name *
                    </label>
                    <input
                        type="text"
                        value={formData.practiceName}
                        onChange={(e) => handleInputChange('practiceName', e.target.value)}
                        placeholder="e.g., Bay Area Medical Center"
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.practiceName ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    {errors.practiceName && (
                        <p className="text-red-500 text-sm mt-1">{errors.practiceName}</p>
                    )}
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Street Address *
                    </label>
                    <input
                        type="text"
                        value={formData.practiceAddress}
                        onChange={(e) => handleInputChange('practiceAddress', e.target.value)}
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.practiceAddress ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    {errors.practiceAddress && (
                        <p className="text-red-500 text-sm mt-1">{errors.practiceAddress}</p>
                    )}
                </div>

                <div className="grid md:grid-cols-3 gap-6">
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            City *
                        </label>
                        <input
                            type="text"
                            value={formData.practiceCity}
                            onChange={(e) => handleInputChange('practiceCity', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.practiceCity ? 'border-red-500' : 'border-gray-300'
                            }`}
                        />
                        {errors.practiceCity && (
                            <p className="text-red-500 text-sm mt-1">{errors.practiceCity}</p>
                        )}
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            State *
                        </label>
                        <select
                            value={formData.practiceState}
                            onChange={(e) => handleInputChange('practiceState', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.practiceState ? 'border-red-500' : 'border-gray-300'
                            }`}
                        >
                            <option value="">Select...</option>
                            {usStates.map((state: string) => (
                                <option key={state} value={state}>
                                    {state}
                                </option>
                            ))}
                        </select>
                        {errors.practiceState && (
                            <p className="text-red-500 text-sm mt-1">{errors.practiceState}</p>
                        )}
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-700 mb-2">
                            ZIP Code *
                        </label>
                        <input
                            type="text"
                            value={formData.practiceZip}
                            onChange={(e) => handleInputChange('practiceZip', e.target.value)}
                            className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                                errors.practiceZip ? 'border-red-500' : 'border-gray-300'
                            }`}
                        />
                        {errors.practiceZip && (
                            <p className="text-red-500 text-sm mt-1">{errors.practiceZip}</p>
                        )}
                    </div>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Title/Position *
                    </label>
                    <input
                        type="text"
                        value={formData.titlePosition}
                        onChange={(e) => handleInputChange('titlePosition', e.target.value)}
                        placeholder="e.g., Attending Physician, Chief Resident, etc."
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.titlePosition ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    {errors.titlePosition && (
                        <p className="text-red-500 text-sm mt-1">{errors.titlePosition}</p>
                    )}
                </div>
            </div>
        </div>
    )
}

function ShadowingDetailsTab({ formData, errors, handleInputChange, handleDayToggle, handleTimeRangeToggle }: any){
    const daysOfWeek = DayOfWeek.values();
    const timeRanges = TimeRange.values();

    return (
        <div>
            <h2 className="text-3xl font-bold text-gray-900 mb-6">Shadowing Availability & Profile</h2>
            <div className="space-y-6">
                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-4">
                        Select Available Days *
                    </label>
                    <p className="text-gray-600 text-sm mb-4">
                        Choose the days you're typically available to host shadow students
                    </p>
                    <div className="space-y-3">
                        {daysOfWeek.map((day: DayOfWeek) => {
                            const dayAvailability = formData.availability.find((avail: any) => avail.day === day);
                            const isDaySelected = !!dayAvailability;

                            return (
                                <div key={day.name}>
                                    <label className="flex items-center p-3 border border-gray-300 rounded-lg hover:bg-gray-50 cursor-pointer transition-colors">
                                        <input
                                            type="checkbox"
                                            checked={isDaySelected}
                                            onChange={() => handleDayToggle(day)}
                                            className="w-5 h-5 text-primary-600 border-gray-300 rounded focus:ring-primary-500"
                                        />
                                        <span className="ml-3 text-gray-900 font-medium">{day.displayName}</span>
                                    </label>

                                    {isDaySelected && (
                                        <div className="ml-12 mt-2 space-y-2">
                                            {timeRanges.map((timeRange: TimeRange) => (
                                                <label
                                                    key={timeRange.displayName}
                                                    className="flex items-center p-2 hover:bg-gray-50 cursor-pointer rounded"
                                                >
                                                    <input
                                                        type="checkbox"
                                                        checked={dayAvailability.timeRanges.includes(timeRange)}
                                                        onChange={() => handleTimeRangeToggle(day, timeRange)}
                                                        className="w-4 h-4 text-primary-600 border-gray-300 rounded focus:ring-primary-500"
                                                    />
                                                    <span className="ml-2 text-sm text-gray-700">{timeRange.displayName}</span>
                                                </label>
                                            ))}
                                        </div>
                                    )}
                                </div>
                            );
                        })}
                    </div>
                    {errors.availability && (
                        <p className="text-red-500 text-sm mt-2">{errors.availability}</p>
                    )}
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Professional Bio *
                    </label>
                    <textarea
                        value={formData.bio}
                        onChange={(e) => handleInputChange('bio', e.target.value)}
                        rows={6}
                        placeholder="Tell students about your background, interests, and what they can expect when shadowing you..."
                        className={`w-full px-4 py-2 border rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent ${
                            errors.bio ? 'border-red-500' : 'border-gray-300'
                        }`}
                    />
                    {errors.bio && (
                        <p className="text-red-500 text-sm mt-1">{errors.bio}</p>
                    )}
                    <p className="text-gray-500 text-sm mt-1">
                        This will be visible to students viewing your profile.
                    </p>
                </div>

                <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">
                        Availability Notes
                    </label>
                    <textarea
                        value={formData.availabilityNotes}
                        onChange={(e) => handleInputChange('availabilityNotes', e.target.value)}
                        rows={4}
                        placeholder="Optional: Let students know your general availability, preferred times, or any special requirements..."
                        className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-600 focus:border-transparent"
                    />
                </div>
            </div>
        </div>
    )
}




function StudentEditForm() {
    return <div>Student Edit Form</div>;
}