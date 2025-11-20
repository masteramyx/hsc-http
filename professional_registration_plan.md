# Professional Registration System - Model 1 Implementation Plan

## Overview
Build a complete professional registration flow with file uploads, multi-step wizard, and integrated sign-up experience.

---

## Phase 1: Database Schema Updates (15 mins) √

### 1.1 Update `db/init.sql` - Professional Table √ 
Add new fields to professional table:
- `photo_url` (TEXT) - Store uploaded photo path
- `practice_type` (VARCHAR 50) - Enum: hospital, private_practice, clinic, urgent_care, other
- `practice_city` (VARCHAR 100)
- `practice_state` (VARCHAR 2)
- `practice_address` (TEXT) - Optional full address
- `shadowing_specialties` (TEXT) - JSON array of specialties
- `student_requirements` (TEXT) - Requirements description
- `available_days` (TEXT) - JSON array: ["Monday","Tuesday"]
- `available_times` (TEXT) - JSON array: ["morning","afternoon"]

### 1.2 Update SQLDelight schema file √ 
- Update `app/src/main/sqldelight/.../Professional.sq` to match new schema
- Add queries for professional registration and profile updates

### 1.3 Add indexes √ 
```sql
CREATE INDEX idx_professional_location ON professional(practice_city, practice_state);
CREATE INDEX idx_professional_specialty ON professional(specialty);
```

---

## Phase 2: File Upload Infrastructure (30 mins) √

### 2.1 Configure Cloudflare R2 Storage √ 
- Create two R2 buckets: `hsc-dev-photos` and `hsc-prod-photos`
- Enable public access on both buckets
- Get R2 credentials (Account ID, Access Key ID, Secret Access Key)
- Store credentials in environment variables:
  - `R2_ACCOUNT_ID`
  - `R2_ACCESS_KEY_ID`
  - `R2_SECRET_ACCESS_KEY`
  - `R2_BUCKET_NAME` (set to `hsc-dev-photos` for dev, `hsc-prod-photos` for prod)
  - `R2_PUBLIC_URL` (R2 public bucket URL, e.g., `https://pub-abc123.r2.dev`)

### 2.2 Add R2 Client Dependency √
- Add AWS SDK for Kotlin (R2 is S3-compatible)
- Configure S3 client to point to R2 endpoint: `https://<account-id>.r2.cloudflarestorage.com`

### 2.3 Create R2StorageService (Kotlin) √
- `uploadFile(bytes: ByteArray, contentType: String, extension: String): String`
  - Generates unique filename (UUID + extension)
  - Uploads to R2 bucket using S3-compatible API
  - Returns public photo URL: `https://<public-url>/<filename>`
- `deleteFile(filename: String)` - for cleanup/profile updates
- Handles R2 connection, bucket operations, error handling

### 2.4 Add file upload endpoint (Kotlin) √ 
- `POST /api/upload/photo`
- Accept multipart form data
- Validate file type (jpg, png, webp, max 5MB)
- Use R2StorageService to upload
- Return public photo URL in response

### 2.5 Add Ktor content negotiation for multipart √
- Configure multipart in Ktor application

 **Note:** Images are served directly from R2's public URL. No backend GET endpoint needed - frontend uses the returned URL directly in `<img>` tags.

---

## Phase 3: Backend API Endpoints (45 mins) √

### 3.1 Professional Registration Endpoint √ 
- `POST /api/professional/register`
- Request body: Complete professional profile data (JSON)
- Validates required fields
- Creates professional record
- Returns success/error

### 3.2 Professional Profile Update √
- `PUT /api/professional/profile`
- Protected route (requires authentication)
- Updates existing professional profile

### 3.3 Get Professional Profile √
- `GET /api/professional/profile`
- Protected route (requires authentication)
- Returns current user's professional profile

---

## Phase 4: Frontend Multi-Step Wizard (2-3 hours)

### 4.1 Create Registration Wizard Component
`react-web/src/components/ProfessionalRegistrationWizard.tsx`

**Step 1: Account Creation** √ 
- Email, Password, Confirm Password
- Creates user account with type='professional'

**Step 2: Personal Information** √ 
- First Name, Last Name, Phone
- Photo upload (with preview)

**Step 3: Credentials** √
- Medical License Number
- Primary Specialty (dropdown)
- Years of Experience
- Board Certifications (optional)

**Step 4: Practice Information** √
- Practice/Hospital Name
- Practice Type (dropdown)
- City, State
- Address
- Title/Position

**Step 5: Shadowing Details** √
- Bio/About Me (textarea) 
- Specialties Offered (multi-select)
- Student Requirements (checkboxes + custom text)
- Availability (days checkboxes, times checkboxes)
- Max Students per Session (number input)
- Duration Options (checkboxes)

**Step 6: Review & Submit** √ 
- Display all entered information
- Terms & Conditions checkbox
- Submit button

**Wizard Features:** √ 
- Progress indicator (1/6, 2/6, etc.)
- Back/Next navigation
- Client-side validation per step
- Form state management (useState)
- Photo upload with preview

### 4.2 Create Photo Upload Component √ 
`react-web/src/components/PhotoUpload.tsx`
- Drag & drop or click to upload
- Image preview
- Size/type validation
- Upload progress indicator

### 4.3 Create Specialty Multi-Select Component ?
`react-web/src/components/SpecialtySelect.tsx`
- Searchable dropdown
- Multiple selection
- Common specialties list

---

## Phase 5: Integration & Routing (30 mins)

### 5.1 Update Sign-Up Flow
- Modify existing sign-up to detect user_type
- After account creation, if type='professional':
  - Redirect to ProfessionalRegistrationWizard
  - Pass user_id/token for authenticated requests

### 5.2 Add Protected Route
- Professional dashboard route (future)
- Redirect incomplete profiles to wizard

### 5.3 Update Navigation
- "For Professionals" CTA buttons → Sign up flow
- Header "Sign Up" → User type selection → Professional flow

---

## Phase 6: Data Storage Format (15 mins)

**JSON Structures:**

```json
// shadowing_specialties
["Cardiology", "Internal Medicine", "Emergency Medicine"]

// available_days
["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]

// available_times
["morning", "afternoon", "evening"]

// duration_options
["single_day", "weekly", "monthly", "ongoing"]

// student_requirements (stored as structured JSON)
{
  "pre_health_student": true,
  "hipaa_training": true,
  "background_check": false,
  "immunizations": true,
  "custom_requirements": "Must have completed Anatomy course"
}
```

---

## Phase 7: Validation & Error Handling (20 mins)

**Backend Validation:**
- Required fields check
- Email format validation
- Phone number format
- License number uniqueness
- File upload validation (size, type)

**Frontend Validation:**
- Real-time field validation
- Step completion validation (can't proceed without required fields)
- Error messages
- Success confirmation

---

## Phase 8: Testing & Polish (30 mins)

### 8.1 Manual Testing
- Complete registration flow end-to-end
- Test file upload
- Test data persistence
- Test validation errors

### 8.2 UI Polish
- Loading states
- Error states
- Success messages
- Mobile responsiveness

---

## File Structure

**Backend:**
```
app/src/main/kotlin/com/shadowconnect/
├── routes/
│   ├── ProfessionalRoutes.kt (new)
│   └── FileUploadRoutes.kt (new)
├── services/
│   └── FileUploadService.kt (new)
└── models/
    └── ProfessionalRegistrationRequest.kt (new)
```

**Frontend:**
```
react-web/src/
├── pages/
│   └── ProfessionalRegistrationPage.tsx (new)
├── components/
│   ├── ProfessionalRegistrationWizard.tsx (new)
│   ├── PhotoUpload.tsx (new)
│   ├── SpecialtySelect.tsx (new)
│   └── ProgressIndicator.tsx (new)
```

**Database:**
```
db/
├── init.sql (updated)
└── uploads/ (new directory)
    └── professional-photos/ (new)
```

---

## Dependencies to Add

**Backend:**
- Ktor multipart support (if not already included)

**Frontend:**
- No new dependencies needed (using existing React/Tailwind)

---

## Estimated Time: 4-5 hours total

**Priority Breakdown:**
1. Database schema (must do first)
2. File upload infrastructure (foundational)
3. Backend API endpoints (core functionality)
4. Frontend wizard (main user-facing feature)
5. Integration & testing (polish)

---

## Post-Implementation: Next Steps

After this is complete, you'll have:
- ✅ Professionals can register with complete profiles
- ✅ Photo uploads working
- ✅ Data stored in structured JSON format
- ✅ Multi-step wizard with good UX

Future enhancements:
- Professional dashboard to edit profile
- Manual verification workflow (admin panel)
- Email verification for professionals
- Profile preview (how students see them)
