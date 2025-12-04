import { useEffect } from "react";
import { useLocation } from "react-router-dom";

/**
 * Headless component that scrolls to top of page on every navigation change.
 *
 * TODO - Maintain positioning of previous page in case of back/forward button click
 */
export default function ScrollToTop() {
    const pathName  = useLocation();
    useEffect(() => {
        window.scrollTo(0, 0);
    }, [pathName]);

    return null;
}