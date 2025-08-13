# TypeScript + React Frontend Implementation Plan

## Overview
This document outlines the plan to add a React frontend with TypeScript to the existing Kotlin Multiplatform project alongside the current Compose Web frontend, with easy build target switching.

## What is TypeScript?
TypeScript is a programming language developed by Microsoft that builds on JavaScript by adding static type definitions. It provides:
- **Static typing** - Catch errors at compile time, not runtime
- **Better IDE support** - Autocomplete, refactoring, navigation
- **Compiles to JavaScript** - Runs anywhere JavaScript runs
- **Gradual adoption** - Can mix with existing JavaScript code

### TypeScript vs JavaScript React Example

**JavaScript React:**
```jsx
function LoginForm({ onSubmit }) {
    const [email, setEmail] = useState('');
    return <input value={email} onChange={e => setEmail(e.target.value)} />;
}
```

**TypeScript React:**
```tsx
interface LoginFormProps {
    onSubmit: (email: string) => void;
}

function LoginForm({ onSubmit }: LoginFormProps) {
    const [email, setEmail] = useState<string>('');
    return <input value={email} onChange={e => setEmail(e.target.value)} />;
}
```

## Current Project Structure Analysis
- `web/` - Compose Web frontend (Kotlin/JS)
- `app/` - Ktor server
- `shared/` - Shared code between platforms
- `db/` - Database layer

## Implementation Plan

### 1. Create React Frontend Package Structure
- Create `react-web/` directory with standard React + TypeScript structure
- Set up `package.json` with React, TypeScript, Vite, and build dependencies
- Create basic React components (LoginForm, App) matching existing Compose Web functionality
- Add Vite configuration for development and production builds

**Directory Structure:**
```
react-web/
├── package.json
├── tsconfig.json
├── vite.config.ts
├── build.gradle.kts
├── src/
│   ├── components/
│   │   ├── LoginForm.tsx
│   │   └── App.tsx
│   ├── services/
│   │   └── ApiClient.ts
│   ├── types/
│   │   └── index.ts
│   ├── main.tsx
│   └── index.html
└── public/
```

### 2. Technology Stack
- **React 18** - UI library
- **TypeScript** - Type safety and better developer experience
- **Vite** - Fast build tool and dev server
- **Axios/Fetch** - HTTP client for API calls

### 3. Integrate with Gradle Build System
- Create `react-web/build.gradle.kts` that uses Node.js plugin
- Configure Gradle tasks to run `npm install` and `npm run build`
- Set up proper dependency relationships between modules
- Ensure React build outputs to correct location for Ktor static serving

### 4. Implement Build Target Switching
- Add Gradle property `frontend.type` to control which frontend to build
- Modify root build configuration to conditionally include frontend packages
- Create convenience Gradle tasks:
  - `./gradlew buildWithReact` - builds with React frontend
  - `./gradlew buildWithCompose` - builds with Compose Web frontend  
  - `./gradlew run -Pfrontend=react` - runs server with React frontend
  - `./gradlew run -Pfrontend=compose` - runs server with Compose frontend

### 5. Update Server Static File Serving
- Modify Ktor configuration to serve static files from appropriate build output
- Ensure both frontends output to locations the server can find
- Update `settings.gradle.kts` to include new `react-web` module
- Configure routing to serve appropriate index.html based on frontend choice

### 6. Create Shared API Client
- Port existing `ApiClient.kt` functionality to TypeScript
- Ensure both frontends can communicate with the same Ktor backend APIs
- Implement authentication flow and session management
- Create TypeScript types matching Kotlin data classes from shared module

### 7. Component Parity
Create React equivalents of existing Compose Web components:
- **LoginForm** - User authentication interface
- **App** - Main application container
- **Navigation** - Route handling
- **Error handling** - User feedback for API errors

### 8. Development Workflow
- **Compose Web Development:** `./gradlew :web:browserDevelopmentRun`
- **React Development:** `./gradlew :react-web:npmStart` (Vite dev server)
- **Production Build:** `./gradlew build -Pfrontend=react|compose`

### 9. Update Documentation
- Add new build commands and frontend switching instructions to CLAUDE.md
- Document development workflow for both frontends
- Include TypeScript setup and configuration details

## Why TypeScript + React for This Project?

**Advantages:**
- **Type Safety** - Similar to Kotlin's type system you're already using
- **IDE Support** - Excellent tooling and refactoring capabilities
- **Professional Standard** - Widely adopted in industry
- **Ecosystem** - Massive library ecosystem and community
- **Team Familiarity** - Coming from Kotlin, TypeScript will feel natural

**Integration Benefits:**
- Both frontends can share the same API contracts
- Type safety across the full stack (Kotlin backend, TypeScript frontend)
- Easy switching between frontends for comparison and testing
- Maintain single Ktor backend regardless of frontend choice

## Future Considerations
- **Shared Types** - Consider generating TypeScript types from Kotlin data classes
- **Testing** - Add Jest/Vitest for React component testing
- **Styling** - Choose between CSS modules, styled-components, or Tailwind
- **State Management** - Add Redux/Zustand if application grows complex
- **Bundle Analysis** - Monitor bundle size and optimize as needed

## Next Steps
When ready to implement:
1. Create the react-web module structure
2. Set up package.json and dependencies
3. Create basic React components
4. Configure Gradle integration
5. Test build target switching
6. Update server configuration
7. Test full stack integration