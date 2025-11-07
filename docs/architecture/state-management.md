# State Management Architecture

## Overview

This document explains the fundamental differences in state management between backend services, mobile applications, and web frontends in the HealthShadow platform.

## Backend (Ktor/JVM) - Stateless Architecture

### Characteristics

The backend is a **long-running, stateless process** that handles HTTP requests independently.

```kotlin
// Backend is essentially stateless per-request
fun Application.configureRouting() {
    routing {
        post("/register") {
            // Request comes in
            val data = call.receive<RegistrationData>()
            // Process it
            database.insert(data)
            // Respond and forget
            call.respond(HttpStatusCode.OK)
        }
    }
}
```

**Key Properties:**
- **Process Lifecycle:** Never stops (runs until server shutdown)
- **Request Handling:** Each HTTP request is independent and stateless
- **No Lifecycle Concerns:** Application just runs continuously
- **Memory Management:** JVM garbage collection handles cleanup
- **State Persistence:** All state lives in PostgreSQL database, not in application memory
- **Scalability:** Can run multiple instances horizontally (each request can go to any server)

### Why Backend is Stateless

1. **Horizontal Scaling:** Multiple server instances can handle requests without sharing in-memory state
2. **Reliability:** Server crashes don't lose user data (it's all in the database)
3. **Simplicity:** No need to track user sessions, form states, or UI positions
4. **Performance:** Database handles persistence efficiently with connection pooling

### State Storage

All persistent state lives in:
- **PostgreSQL Database** - User data, professional profiles, shadowing opportunities
- **File Storage (R2/S3)** - Uploaded documents, profile images
- **Cache Layer (future)** - Redis for session data and temporary state

## Mobile (Android) - Complex Lifecycle Management

### Characteristics

Mobile applications have **short-lived components** with complex lifecycles managed by the Android OS.

```kotlin
class ProfileActivity : AppCompatActivity() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        // Activity created - initialize UI
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save state before potential destruction
        super.onSaveInstanceState(outState)
        outState.putString("form_data", formData.toJson())
    }

    override fun onPause() {
        // User navigates away - save transient state
        super.onPause()
        viewModel.saveFormState()
    }

    override fun onDestroy() {
        // Activity destroyed - cleanup resources
        super.onDestroy()
        viewModel.clearDisposables()
    }
}
```

**Key Properties:**
- **Component Lifecycle:** onCreate → onStart → onResume → onPause → onStop → onDestroy
- **OS Control:** Android can kill your process at any time to free memory
- **Configuration Changes:** Screen rotation destroys and recreates the entire activity
- **Limited Resources:** Must be aggressive about memory and battery usage
- **Process Death:** Must survive being killed in the background

### Why Mobile is Complex

1. **Resource Constraints:** Limited memory, battery, and CPU compared to servers
2. **OS Management:** System aggressively manages app lifecycles to preserve resources
3. **User Experience:** Must maintain state across configuration changes and background transitions
4. **Offline Support:** Often needs to work without network connectivity

### Common Mobile State Problems

**Problem: Screen Rotation**
```kotlin
// User fills out registration form with 10 fields
// User rotates screen → Activity destroyed → Form data LOST
// Solution: Use ViewModel (survives configuration changes)
```

**Problem: Background Process Kill**
```kotlin
// User fills form, switches to another app
// OS kills your app to free memory → Form data LOST
// Solution: Use onSaveInstanceState() or ViewModel with SavedStateHandle
```

**Problem: Memory Pressure**
```kotlin
// User has 50 apps open, system is low on memory
// OS kills background activities → State LOST
// Solution: Persist critical state to local database or SharedPreferences
```

### Mobile State Management Solutions

1. **ViewModel** - Survives configuration changes, doesn't survive process death
2. **SavedStateHandle** - Survives both configuration changes and process death
3. **Room Database** - Persistent local storage for offline-first architecture
4. **SharedPreferences** - Simple key-value storage for settings
5. **DataStore** - Modern replacement for SharedPreferences
6. **WorkManager** - Background task execution that survives process death

## Web Frontend (React) - Moderate Complexity

### Characteristics

React applications have **component lifecycles** simpler than mobile but more complex than backend.

```typescript
function ProfessionalRegistrationPage() {
    // Component state - lost on page refresh
    const [currentStep, setCurrentStep] = useState(1);
    const [formData, setFormData] = useState<FormData>(INITIAL_FORM_DATA);

    // Effect runs on mount
    useEffect(() => {
        console.log('Component mounted');
        return () => {
            console.log('Component unmounted');
        };
    }, []);

    // State lives in component memory
    // If user refreshes page → all state is LOST
}
```

**Key Properties:**
- **Component Lifecycle:** Mount → Update → Unmount
- **User Control:** User can refresh page (loses everything) or navigate away
- **Browser Storage:** LocalStorage/SessionStorage for persistence
- **No OS Interference:** Browser doesn't arbitrarily kill the app
- **Network Dependent:** Most state comes from backend API calls

### React State Lifecycle

```
User opens page → Component mounts → useState initializes
User interacts → setState updates → Component re-renders
User navigates away → Component unmounts → State destroyed
User refreshes page → Everything reloads → State starts fresh
```

### Why React is Simpler Than Mobile

1. **No Process Death:** Browser doesn't kill your app to free memory (unless user closes tab)
2. **No Configuration Changes:** Screen resize doesn't destroy components
3. **Simpler Lifecycle:** Only mount/update/unmount vs. 10+ lifecycle methods on Android
4. **Less Resource Constraints:** Desktop/laptop browsers have more memory/CPU than phones

### React State Management Solutions

1. **useState** - Local component state (lost on unmount/refresh)
2. **useContext** - Share state across component tree
3. **useReducer** - Complex state logic
4. **LocalStorage** - Persist state across page refreshes
5. **React Query** - Server state caching and synchronization
6. **Redux/Zustand** - Global state management for large apps

### React State Persistence

```typescript
// Save form state to localStorage
useEffect(() => {
    localStorage.setItem('registrationForm', JSON.stringify(formData));
}, [formData]);

// Restore on mount
useEffect(() => {
    const saved = localStorage.getItem('registrationForm');
    if (saved) {
        setFormData(JSON.parse(saved));
    }
}, []);
```

## Comparison Table

| Aspect | Backend | Mobile (Android) | Frontend (React) |
|--------|---------|------------------|------------------|
| **Process Lifetime** | Continuous (days/months) | Intermittent (killed by OS) | Session-based (user controls) |
| **State Complexity** | Simple (stateless) | Very Complex | Moderate |
| **Lifecycle Management** | None | 10+ lifecycle methods | 3 phases (mount/update/unmount) |
| **Resource Constraints** | High (server hardware) | Low (phone hardware) | Medium (browser) |
| **State Persistence** | Database | Multiple strategies required | LocalStorage + Backend API |
| **Memory Management** | JVM GC handles it | Manual + OS intervention | Browser GC handles it |
| **Primary Concern** | Request processing speed | User experience + battery life | User experience + load time |
| **State Loss Risk** | None (DB persisted) | High (OS can kill anytime) | Medium (user refresh) |

## Architecture Decision: Why Kotlin/JS for Shared Types

The HealthShadow platform uses **Kotlin Multiplatform** to share type definitions between backend and frontend:

```kotlin
// Defined once in shared/ module
@Serializable
enum class ProfessionalType(val displayName: String) {
    MD("Doctor of Medicine (MD)"),
    DO("Doctor of Osteopathic Medicine (DO)"),
    PA("Physician Assistant (PA)"),
    NP("Nurse Practitioner (NP)")
}
```

**Used in Backend (JVM):**
```kotlin
val professional = Professional(
    professionalType = ProfessionalType.MD,
    // ...
)
```

**Used in Frontend (TypeScript/React):**
```typescript
import { ProfessionalType } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.mjs';

const types = Object.values(ProfessionalType);
// Renders: Doctor of Medicine (MD), Doctor of Osteopathic Medicine (DO), etc.
```

**Benefits:**
- **Single Source of Truth:** Enums defined once, used everywhere
- **Type Safety:** Backend has compile-time safety, frontend has runtime validation
- **Consistency:** Dropdown options match database constraints exactly
- **No Duplication:** Don't maintain parallel enum definitions

**How it Works:**
1. Kotlin/JS compiler generates ES modules from shared code
2. React imports types from compiled `.mjs` files
3. Backend uses the same Kotlin enums for database operations
4. Both frontend and backend stay in sync automatically

## Best Practices

### Backend State Management
- ✅ Keep request handlers stateless
- ✅ Store all state in database
- ✅ Use connection pooling (HikariCP) for performance
- ❌ Don't store user state in application memory
- ❌ Don't use global mutable state

### Mobile State Management (Future Android App)
- ✅ Use ViewModel for UI state
- ✅ Use SavedStateHandle for critical form data
- ✅ Persist to Room database for offline support
- ✅ Save state in onSaveInstanceState()
- ❌ Don't assume your activity stays in memory
- ❌ Don't store large objects in SavedInstanceState

### Frontend State Management
- ✅ Use useState for component-local state
- ✅ Use localStorage for form persistence
- ✅ Use React Query for server state caching
- ✅ Optimize re-renders with useMemo/useCallback
- ❌ Don't store sensitive data in localStorage
- ❌ Don't assume state survives page refresh

## Future Considerations

### Mobile App Development
When building an Android app for HealthShadow:
- Use Jetpack Compose for modern UI
- Use Koin for dependency injection
- Use Room for local database
- Use WorkManager for background sync
- Consider offline-first architecture with eventual consistency

### State Synchronization
As the platform grows:
- Consider WebSockets for real-time updates
- Implement optimistic UI updates
- Add conflict resolution for offline edits
- Use event sourcing for audit trails

### Performance Optimization
- Backend: Add Redis caching layer
- Mobile: Implement pagination and lazy loading
- Frontend: Code splitting and lazy component loading
- All: Implement proper loading states and skeleton screens