package com.ems.model;

/**
 * BASE CLASS: Person
 *
 * PURPOSE:
 *   A parent class that holds attributes shared by ANY person in the system.
 *   Today we only have Employee, but tomorrow you might add Manager, Intern,
 *   Contractor — all of them are also "people" with a name and email.
 *
 * OOP CONCEPTS USED:
 *   - Inheritance foundation: This class is designed to be extended.
 *   - Encapsulation: name and email are private, accessed via getters/setters.
 *   - Abstract thinking: We don't instantiate Person directly; it's a template.
 *
 * WHY THIS IS GOOD FOR AN INTERVIEW:
 *   Shows you understand the "is-a" relationship. An Employee IS-A Person.
 *   It avoids code duplication — you don't repeat name/email in every class.
 */
public class Person {

    private String name;   // Full name of the person
    private String email;  // Email address (used as a unique contact)

    // ── Constructor ───────────────────────────────────────────────────────────
    public Person(String name, String email) {
        this.name  = name;
        this.email = email;
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getName()  { return name;  }
    public String getEmail() { return email; }

    // ── Setters ───────────────────────────────────────────────────────────────
    public void setName(String name)   { this.name  = name;  }
    public void setEmail(String email) { this.email = email; }
}
