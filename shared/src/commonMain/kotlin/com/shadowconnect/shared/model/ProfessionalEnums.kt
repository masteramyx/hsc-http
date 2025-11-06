@file:JsExport

package com.shadowconnect.shared.model

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * Professional credential types - defines the type of medical professional
 */
@Serializable
enum class ProfessionalType(val displayName: String) {
    MD("Doctor of Medicine (MD)"),
    DO("Doctor of Osteopathic Medicine (DO)"),
    PA("Physician Assistant (PA)"),
    NP("Nurse Practitioner (NP)"),
    APRN("Advanced Practice Registered Nurse (APRN)"),
    CNS("Clinical Nurse Specialist (CNS)"),
    CRNA("Certified Registered Nurse Anesthetist (CRNA)"),
    CNM("Certified Nurse Midwife (CNM)"),
    PT("Physical Therapist (PT)"),
    OT("Occupational Therapist (OT)"),
    SLP("Speech-Language Pathologist (SLP)"),
    LCSW("Licensed Clinical Social Worker (LCSW)"),
    OTHER("Other Healthcare Professional")
}

/**
 * Medical specialties - broad categorization of practice areas
 */
@Serializable
enum class MedicalSpecialty(val displayName: String) {
    FAMILY_MEDICINE("Family Medicine"),
    INTERNAL_MEDICINE("Internal Medicine"),
    PEDIATRICS("Pediatrics"),
    PSYCHIATRY("Psychiatry"),
    NEUROLOGY("Neurology"),
    CARDIOLOGY("Cardiology"),
    SURGERY("Surgery"),
    ORTHOPEDICS("Orthopedic Surgery"),
    OBSTETRICS_GYNECOLOGY("Obstetrics & Gynecology (Women's Health)"),
    GASTROENTEROLOGY("Gastroenterology"),
    GERIATRICS("Geriatric Medicine"),
    NEONATOLOGY("Neonatology"),
    EMERGENCY_MEDICINE("Emergency Medicine"),
    ANESTHESIOLOGY("Anesthesiology"),
    RADIOLOGY("Radiology"),
    PATHOLOGY("Pathology"),
    DERMATOLOGY("Dermatology"),
    ONCOLOGY("Oncology"),
    ENDOCRINOLOGY("Endocrinology"),
    RHEUMATOLOGY("Rheumatology"),
    PULMONOLOGY("Pulmonology"),
    NEPHROLOGY("Nephrology"),
    UROLOGY("Urology"),
    OPHTHALMOLOGY("Ophthalmology"),
    OTOLARYNGOLOGY("Otolaryngology (ENT)"),
    ALLERGY_IMMUNOLOGY("Allergy & Immunology"),
    INFECTIOUS_DISEASE("Infectious Disease"),
    HEMATOLOGY("Hematology"),
    PHYSICAL_MEDICINE("Physical Medicine & Rehabilitation"),
    PAIN_MANAGEMENT("Pain Management"),
    PALLIATIVE_CARE("Palliative Care"),
    SPORTS_MEDICINE("Sports Medicine"),
    OCCUPATIONAL_MEDICINE("Occupational Medicine"),
    PUBLIC_HEALTH("Public Health"),
    OTHER("Other Specialty")
}

/**
 * Practice setting types
 */
@Serializable
enum class PracticeType(val displayName: String) {
    HOSPITAL("Hospital"),
    PRIVATE_PRACTICE("Private Practice"),
    CLINIC("Clinic"),
    URGENT_CARE("Urgent Care"),
    ACADEMIC_MEDICAL_CENTER("Academic Medical Center"),
    COMMUNITY_HEALTH_CENTER("Community Health Center"),
    REHABILITATION_CENTER("Rehabilitation Center"),
    NURSING_HOME("Nursing Home/Long-term Care"),
    RESEARCH_FACILITY("Research Facility"),
    GOVERNMENT_FACILITY("Government/VA Facility"),
    OTHER("Other")
}