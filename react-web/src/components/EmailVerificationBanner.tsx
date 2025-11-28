import { useState } from 'react';
import { useSession } from '../contexts/SessionContext';

export function EmailVerificationBanner() {
  const { isLoggedIn, user, isLoading } = useSession();
  const [isResending, setIsResending] = useState(false);
  const [message, setMessage] = useState('');
  const [isDismissed, setIsDismissed] = useState(false);

  console.log('EmailVerificationBanner:', { isLoggedIn, user, isLoading, emailVerified: user?.emailVerified, isDismissed });

  // Don't show if still loading, user is verified, or not logged in
  if (isLoading ||
      !isLoggedIn ||
      user?.emailVerified ||
      isDismissed) {
    console.log('Banner hidden - returning null');
    return null;
  }

  console.log('Banner should be visible!');

  const handleResend = async () => {
    setIsResending(true);
    setMessage('');

    try {
      const response = await fetch('/api/v1/email/resend-verification', {
        method: 'POST',
        credentials: 'include'
      });

      const data = await response.json();

      if (data.success) {
        setMessage('Verification email sent! Check your inbox.');
      } else {
        setMessage('Failed to send email. Please try again.');
      }
    } catch (error) {
      console.error('Resend error:', error);
      setMessage('An error occurred. Please try again.');
    } finally {
      setIsResending(false);
    }
  };

  return (
    <div className="fixed top-16 left-0 right-0 py-2 z-40 bg-yellow-50 border-b border-yellow-200">
      <div className="container mx-auto px-4 py-3">
        <div className="flex items-center justify-between flex-wrap gap-2">
          <div className="flex items-center gap-2">
            <svg className="w-5 h-5 text-yellow-600" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clipRule="evenodd" />
            </svg>
            <p className="text-sm text-yellow-800">
              Please verify your email address to access all features.
            </p>
          </div>

          <div className="flex items-center gap-2">
            {message && (
              <span className="text-sm text-yellow-800 mr-2">{message}</span>
            )}
            <button
              onClick={handleResend}
              disabled={isResending}
              className="text-sm font-semibold text-yellow-900 hover:text-yellow-700 disabled:opacity-50"
            >
              {isResending ? 'Sending...' : 'Resend Email'}
            </button>
            <button
              onClick={() => setIsDismissed(true)}
              className="text-yellow-600 hover:text-yellow-800"
              aria-label="Dismiss"
            >
              <svg className="w-5 h-5" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z" clipRule="evenodd" />
              </svg>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}