package rvt.studentreg.model;

/**
 * Student datu modelis (POJO).
 * Satur visus studenta laukus un getter/setter metodes.
 */
public class Student {

    private String firstName;
    private String lastName;
    private String email;
    private String personalCode;
    private String registeredAt;

    public Student(String firstName, String lastName, String email,
                   String personalCode, String registeredAt) {
        this.firstName    = firstName;
        this.lastName     = lastName;
        this.email        = email;
        this.personalCode = personalCode;
        this.registeredAt = registeredAt;
    }

    // Getteri
    public String getFirstName()    { return firstName; }
    public String getLastName()     { return lastName; }
    public String getEmail()        { return email; }
    public String getPersonalCode() { return personalCode; }
    public String getRegisteredAt() { return registeredAt; }

    // Setteri (vajadzīgi edit funkcijai)
    public void setFirstName(String firstName)       { this.firstName = firstName; }
    public void setLastName(String lastName)         { this.lastName = lastName; }
    public void setEmail(String email)               { this.email = email; }
    public void setPersonalCode(String personalCode) { this.personalCode = personalCode; }
    public void setRegisteredAt(String registeredAt) { this.registeredAt = registeredAt; }

    /**
     * Pārveido studentu uz CSV rindu.
     * Formāts: firstName,lastName,email,personalCode,registeredAt
     */
    public String toCsvLine() {
        return String.join(",", firstName, lastName, email, personalCode, registeredAt);
    }

    /**
     * Izveido Student objektu no CSV rindas.
     */
    public static Student fromCsvLine(String csvLine) {
        String[] parts = csvLine.split(",", -1);
        if (parts.length < 5) {
            throw new IllegalArgumentException("Nepareiza CSV rinda: " + csvLine);
        }
        return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim(),
                           parts[3].trim(), parts[4].trim());
    }

    @Override
    public String toString() {
        return "Student{" + firstName + " " + lastName + ", " + email + ", " + personalCode + "}";
    }
}
