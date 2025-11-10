import { useState, Fragment } from 'react';
import { SEOHead } from '../components/SEOHead';
import { PhotoUpload } from '../components/PhotoUpload';
// @ts-ignore - Kotlin/JS types
import { ProfessionalType, MedicalSpecialty, PracticeType, USStates } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.mjs';

type FormData = {
  // Step 1: Basic Information
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  photo: File | null;
  password: string;
  confirmPassword: string;

  // Step 2: Professional Details
  professionalType: string;
  licenseNumber: string;
  licenseState: string;
  medicalSpecialty: string;
  yearsExperience: string;

  // Step 3: Practice Information
  practiceType: string;
  practiceName: string;
  practiceAddress: string;
  practiceCity: string;
  practiceState: string;
  practiceZip: string;
  titlePosition: string;

  // Step 4: Profile
  bio: string;
  availabilityNotes: string;
};

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
  bio: '',
  availabilityNotes: '',
};

export function ProfessionalRegistrationPage() {
  const [currentStep, setCurrentStep] = useState(1);
  const [formData, setFormData] = useState<FormData>(INITIAL_FORM_DATA);
  const [errors, setErrors] = useState<Partial<Record<keyof FormData, string>>>({});

  const totalSteps = 4;

  // Get all professional types from Kotlin enum using the standard Kotlin enum API
  const professionalTypes = ProfessionalType.values();

  // Get all medical specialties from Kotlin enum
  const medicalSpecialties = MedicalSpecialty.values();

  // Get all practice types from Kotlin enum
  const practiceTypes = PracticeType.values();

  // Get all US states from Kotlin constants using stable API
  const usStates = USStates.getInstance().getAll();

  const handleInputChange = (field: keyof FormData, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors((prev) => ({ ...prev, [field]: undefined }));
    }
  };

  const validateStep = (step: number): boolean => {
    const newErrors: Partial<Record<keyof FormData, string>> = {};

    if (step === 1) {
      if (!formData.firstName.trim()) newErrors.firstName = 'First name is required';
      if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required';
      if (!formData.email.trim()) newErrors.email = 'Email is required';
      else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email))
        newErrors.email = 'Invalid email format';
      if (!formData.phone.trim()) newErrors.phone = 'Phone number is required';
      if (!formData.password) newErrors.password = 'Password is required';
      else if (formData.password.length < 8) newErrors.password = 'Password must be at least 8 characters';
      if (!formData.confirmPassword) newErrors.confirmPassword = 'Please confirm your password';
      else if (formData.password !== formData.confirmPassword)
        newErrors.confirmPassword = 'Passwords do not match';
    } else if (step === 2) {
      if (!formData.professionalType) newErrors.professionalType = 'Professional type is required';
      if (!formData.licenseNumber.trim()) newErrors.licenseNumber = 'License number is required';
      if (!formData.licenseState) newErrors.licenseState = 'License state is required';
      if (!formData.medicalSpecialty) newErrors.medicalSpecialty = 'Medical specialty is required';
      if (!formData.yearsExperience) newErrors.yearsExperience = 'Years of experience is required';
    } else if (step === 3) {
      if (!formData.practiceType) newErrors.practiceType = 'Practice type is required';
      if (!formData.practiceName.trim()) newErrors.practiceName = 'Practice name is required';
      if (!formData.practiceAddress.trim()) newErrors.practiceAddress = 'Address is required';
      if (!formData.practiceCity.trim()) newErrors.practiceCity = 'City is required';
      if (!formData.practiceState) newErrors.practiceState = 'State is required';
      if (!formData.practiceZip.trim()) newErrors.practiceZip = 'ZIP code is required';
      if (!formData.titlePosition.trim()) newErrors.titlePosition = 'Title/Position is required';
    } else if (step === 4) {
      if (!formData.bio.trim()) newErrors.bio = 'Professional bio is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleNext = () => {
    if (validateStep(currentStep)) {
      setCurrentStep((prev) => Math.min(prev + 1, totalSteps));
    }
  };

  const handlePrevious = () => {
    setCurrentStep((prev) => Math.max(prev - 1, 1));
  };

  const handleSubmit = async () => {
    if (!validateStep(currentStep)) return;

    // TODO: Submit to backend API
    console.log('Submitting registration:', formData);

    // Comprehensive data display for validation
    const professionalTypeDisplay = professionalTypes.find((t: any) => t.a1_1 === formData.professionalType)?.displayName || formData.professionalType;
    const specialtyDisplay = medicalSpecialties.find((s: any) => s.a1_1 === formData.medicalSpecialty)?.displayName || formData.medicalSpecialty;
    const practiceTypeDisplay = practiceTypes.find((t: any) => t.a1_1 === formData.practiceType)?.displayName || formData.practiceType;

    const message = `
Registration Data Collected:

=== BASIC INFORMATION ===
Name: ${formData.firstName} ${formData.lastName}
Email: ${formData.email}
Phone: ${formData.phone}
Photo: ${formData.photo ? formData.photo.name + ' (' + Math.round(formData.photo.size / 1024) + 'KB)' : '(not uploaded)'}
Password: ${formData.password ? '(set - ' + formData.password.length + ' chars)' : '(not set)'}

=== PROFESSIONAL DETAILS ===
Professional Type: ${professionalTypeDisplay}
License Number: ${formData.licenseNumber}
License State: ${formData.licenseState}
Medical Specialty: ${specialtyDisplay}
Years of Experience: ${formData.yearsExperience}

=== PRACTICE INFORMATION ===
Practice Type: ${practiceTypeDisplay}
Practice Name: ${formData.practiceName}
Address: ${formData.practiceAddress}
City: ${formData.practiceCity}
State: ${formData.practiceState}
ZIP: ${formData.practiceZip}
Title/Position: ${formData.titlePosition}

=== PROFILE ===
Bio: ${formData.bio.substring(0, 100)}${formData.bio.length > 100 ? '...' : ''}
Availability Notes: ${formData.availabilityNotes || '(none)'}

Backend integration pending...
    `.trim();

    alert(message);
  };

  return (
    <>
      <SEOHead
        title="Professional Registration - Shadow Connects"
        description="Register as a healthcare professional to host shadow students and mentor the next generation."
        path="/register/professional"
      />
      <div className="py-12 bg-gray-50 min-h-screen">
        <div className="container mx-auto px-6">
          <div className="max-w-3xl mx-auto">
            {/* Progress Steps */}
            <div className="mb-8">
                <div className="flex items-center justify-between">
                    {[1, 2, 3, 4].map((step, index) => (
                        <Fragment key={step}>
                            <div className="flex flex-col items-center">
                                <div
                                className={`w-12 h-12 rounded-full flex items-center justify-center font-bold text-lg ${
                                                 step < currentStep
                                             ? 'bg-primary-600 text-white' 
                                             : step === currentStep
                                             ? 'bg-primary-600 text-white ring-4 ring-primary-200'
                                             : 'bg-gray-300 text-gray-600'
                                         }`}
                                >
                                    {step < currentStep ? '√' : step}
                                </div>
                                <span
                                    className={`mt-4 text-sm font-medium ${
                                        currentStep === step ? 'text-primary-600' : 'text-gray-600'
                                    }`}
                                >
              {['Basic Info', 'Professional', 'Practice', 'Profile'][index]}
            </span>
                            </div>
                            {/* Connecting line between steps */}
                            {index < 3 && (
                                <div
                                    className={`flex-1 h-1 mx-4 -mt-6 ${
                                        step < currentStep ? 'bg-primary-600' : 'bg-gray-300'
                                    }`}
                                />
                            )}
                        </Fragment>
                    ))}
                </div>
            </div>

            {/* Form Card */}
            <div className="bg-white rounded-xl shadow-lg p-8">
              {/* Step 1: Basic Information */}
              {currentStep === 1 && (
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
                        onChange={(file) => setFormData((prev) => ({ ...prev, photo: file }))}
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
              )}

              {/* Step 2: Professional Details */}
              {currentStep === 2 && (
                <div>
                  <h2 className="text-3xl font-bold text-gray-900 mb-6">Professional Details</h2>
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
                          <option key={type.a1_1} value={type.a1_1}>
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
                          <option key={specialty.a1_1} value={specialty.a1_1}>
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
              )}

              {/* Step 3: Practice Information */}
              {currentStep === 3 && (
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
                          <option key={type.a1_1} value={type.a1_1}>
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
              )}

              {/* Step 4: Profile */}
              {currentStep === 4 && (
                <div>
                  <h2 className="text-3xl font-bold text-gray-900 mb-6">Complete Your Profile</h2>
                  <div className="space-y-6">
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

                    {/* Summary Section */}
                    <div className="bg-primary-50 rounded-lg p-6 mt-8">
                      <h3 className="text-xl font-semibold text-gray-900 mb-4">
                        Review Your Information
                      </h3>
                      <div className="space-y-2 text-sm">
                        <p>
                          <span className="font-semibold">Name:</span> {formData.firstName}{' '}
                          {formData.lastName}
                        </p>
                        <p>
                          <span className="font-semibold">Email:</span> {formData.email}
                        </p>
                        <p>
                          <span className="font-semibold">Professional Type:</span>{' '}
                          {professionalTypes.find((t: any) => t.a1_1 === formData.professionalType)
                            ?.displayName || formData.professionalType}
                        </p>
                        <p>
                          <span className="font-semibold">Specialty:</span>{' '}
                          {medicalSpecialties.find(
                            (s: any) => s.a1_1 === formData.medicalSpecialty
                          )?.displayName || formData.medicalSpecialty}
                        </p>
                        <p>
                          <span className="font-semibold">Practice:</span> {formData.practiceName}
                        </p>
                        <p>
                          <span className="font-semibold">Location:</span> {formData.practiceCity},{' '}
                          {formData.practiceState}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              )}

              {/* Navigation Buttons */}
              <div className="flex justify-between mt-8 pt-6 border-t">
                <button
                  onClick={handlePrevious}
                  disabled={currentStep === 1}
                  className={`px-6 py-2 rounded-lg font-semibold ${
                    currentStep === 1
                      ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                      : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
                  }`}
                >
                  Previous
                </button>

                {currentStep < totalSteps ? (
                  <button
                    onClick={handleNext}
                    className="px-6 py-2 bg-primary-600 text-white rounded-lg font-semibold hover:bg-primary-700"
                  >
                    Next
                  </button>
                ) : (
                  <button
                    onClick={handleSubmit}
                    className="px-8 py-2 bg-primary-600 text-white rounded-lg font-semibold hover:bg-primary-700"
                  >
                    Submit Registration
                  </button>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}