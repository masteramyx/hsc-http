import { SEOHead } from '../components/SEOHead';
import { Hero } from '../components/Hero';
import { Features } from '../components/Features';
import { CTA } from '../components/CTA';

export function HomePage() {
  return (
    <>
      <SEOHead
        title="Clinical Shadowing Opportunities"
        description="Connect with healthcare professionals and gain valuable clinical experience. Find verified shadowing opportunities across medical specialties nationwide."
        path="/"
      />
      <Hero />
      <Features />
      <CTA />
    </>
  );
}
