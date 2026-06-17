package com.ems.util;

/**
 * UTILITY CLASS: InputValidator
 *
 * PURPOSE:
 *   A collection of static helper methods for validating user input.
 *   "Static" because these are pure functions — they take input and return
 *   true/false without needing any object state.
 *
 * WHY A SEPARATE UTILITY CLASS?
 *   If you put validation directly in the Service, it becomes hard to read.
 *   By extracting it, you can reuse these validators anywhere in the codebase
 *   and test them independently.
 *
 * INTERVIEW POINT: Regex
 *   We use Regular Expressions (regex) for email and phone validation.
 *   Regex is a pattern-matching language — very common in real projects.
 */
public class InputValidator {

    // Private constructor: prevents instantiation of this utility class
    // (you never do 'new InputValidator()', just call InputValidator.isValidEmail(...))
    private InputValidator() {}

    /**
     * Checks that a string is not null and not just whitespace.
     *
     * @param value the string to check
     * @return true if the string has at least one non-space character
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validates email format using a regex pattern.
     *
     * Pattern explained:
     *   [A-Za-z0-9+_.-]+  → one or more valid characters before @
     *   @                  → literal @
     *   [A-Za-z0-9.-]+     → domain name
     *   \.                 → literal dot (escaped)
     *   [A-Za-z]{2,}       → top-level domain (at least 2 chars: .com, .in, .org)
     *
     * Valid:   john.doe@example.com  |  user_123@company.co.in
     * Invalid: john@@domain.com     |  nodomain  |  @missing.com
     *
     * @param email the string to validate
     * @return true if the format looks like a real email
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Validates that phone is exactly 10 digits (Indian mobile format).
     *
     * Pattern: \\d{10}
     *   \\d → any digit (0-9)
     *   {10} → exactly 10 of them
     *
     * Valid:   9876543210
     * Invalid: 123 (too short) | 12345678901 (too long) | 98765abc10 (letters)
     *
     * @param phone the string to validate
     * @return true if it's exactly 10 digits
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("\\d{10}");
    }

    /**
     * Safely parses a string to an integer.
     * Returns -1 if the string is not a valid number.
     * Used when reading menu choices from Scanner to avoid NumberFormatException.
     *
     * @param input the string to parse
     * @return the integer value, or -1 if parsing fails
     */
    public static int parseIntSafe(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Safely parses a string to a double.
     * Returns -1.0 if the string is not a valid number.
     *
     * @param input the string to parse
     * @return the double value, or -1.0 if parsing fails
     */
    public static double parseDoubleSafe(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}
