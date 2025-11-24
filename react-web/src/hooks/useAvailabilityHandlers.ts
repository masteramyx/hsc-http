import { DayOfWeek, TimeRange, DayAvailabilityJS } from '../../../shared/build/dist/js/productionLibrary/hsc-http-shared.js';
import type { FormData } from '../types/FormData';

/**
 * Custom hook for managing availability toggle logic
 * Shared between ProfessionalRegistrationPage and EditProfilePage
 */
export function useAvailabilityHandlers(
    setFormData: React.Dispatch<React.SetStateAction<FormData>>,
    errors: Record<string, string | null | undefined>,
    setErrors: React.Dispatch<React.SetStateAction<Record<string, string | null | undefined>>>
) {

    const handleDayToggle = (day: DayOfWeek) => {
        setFormData((prev) => {
            // If day already selected - then unselect
            const isCurrentlySelected = prev.availability.some(avail => avail.day === day);
            const newAvailability = isCurrentlySelected
                ? prev.availability.filter(selectedDay => selectedDay.day !== day)
                : prev.availability.concat(new DayAvailabilityJS(day, []));
            return {...prev, availability: newAvailability }
        });

        if(errors.availability) {
            setErrors((prev) => ({...prev, availability: undefined}))
        }
    };


    const handleTimeRangeToggle = (day: DayOfWeek, timeRange: TimeRange) => {
        setFormData((prev) => {
            const newAvailability = prev.availability.map((dayAvail) => {
                // is this the same day?
                if (dayAvail.day === day) {
                    // does time exist already?
                    const hasTimeRange = dayAvail.timeRanges.includes(timeRange);

                    // if exists already - remove
                    const newTimeRanges = hasTimeRange
                        ? dayAvail.timeRanges.filter(tr => tr !== timeRange)
                        : [...dayAvail.timeRanges, timeRange];

                    // return updated day object - use constructor
                    return new DayAvailabilityJS(dayAvail.day, newTimeRanges);
                }

                // not the right day....do nothing
                return dayAvail;
            });

            return {...prev, availability: newAvailability};
        });

        // Clear errors
        if(errors.availability) {
            setErrors((prev) => ({...prev, availability: undefined}))
        }
    };

    return { handleDayToggle, handleTimeRangeToggle };
}