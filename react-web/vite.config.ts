import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: 'build',
    assetsDir: 'assets',
    sourcemap: false,
  },
  resolve: {
    alias: {
      // Resolve .js imports to .mjs files for Kotlin/JS shared module
      //
      // Why this is needed:
      // - Kotlin/JS generates .mjs files (ES modules) and .d.ts files (TypeScript types)
      // - TypeScript expects .js imports to pair with .d.ts files (e.g., foo.js → foo.d.ts)
      // - We import as .js so TypeScript finds the types, but the actual file is .mjs
      // - Vite (runtime) needs to resolve the .js import to the actual .mjs file on disk
      // - This alias tells Vite: "when you see this .js import, load the .mjs file instead"
      '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js':
        '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.mjs'
    }
  },
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      // /login is both a React Router page AND a backend API endpoint (POST)
      // bypass: lets browser navigation (GET for HTML) be handled by React Router
      // while POST requests for authentication are proxied to the backend
      '/login': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req) => {
          if (req.method === 'GET' && req.headers.accept?.includes('text/html')) {
            return '/index.html';
          }
        },
      },
      '/logout': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/me': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
    },
  },
})
