package rvt.studentreg.validation;

import rvt.studentreg.exceptions.ValidationException;
import java.util.regex.Pattern;

/**
 * Validācijas klase — satur RegEx pārbaudes visiem ievaddatiem.
 * Katra metode vai nu atgriež true/false, vai izmet ValidationException.
 */
public class Validator {

    // -----------------------------------------------------------------------
    // RegEx konstantes
    // -----------------------------------------------------------------------

    /** Vārds / Uzvārds: tikai burti (ieskaitot latviešu), min 3, max 50 simboli.
     *  Atļauj arī defisi salikteņos, piem. "Anna-Marija". */
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-zĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽž][A-Za-zĀāČčĒēĢģĪīĶķĻļŅņŠšŪūŽž\\-]{2,49}$");

    /** E-pasts: standarta formāts ar @ un domēnu. */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$");

    /** Personas kods: DDMMGG-NNNNN (6 cipari, defise, 5 cipari).
     *  Piem. 010190-12345 */
    private static final Pattern PERSONAL_CODE_PATTERN =
            Pattern.compile("^\\d{6}-\\d{5}$");

    /** Datums un laiks: GGGG-MM-DD HH:MM
     *  Piem. 2024-01-15 09:30 */
    private static final Pattern DATETIME_PATTERN =
            Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) " +
                            "([01]\\d|2[0-3]):[0-5]\\d$");

    // -----------------------------------------------------------------------
    // Publiskās validācijas metodes (izmet ValidationException)
    // -----------------------------------------------------------------------

    /**
     * Pārbauda vārdu vai uzvārdu.
     * @throws ValidationException ja ievade neatbilst prasībām
     */
    public static void validateName(String name, String fieldName) {
        if (name == null || name.isBlank()) {
            throw new ValidationException(fieldName + " nedrīkst būt tukšs!");
        }
        if (name.trim().length() < 3) {
            throw new ValidationException(fieldName + " jābūt vismaz 3 simboliem!");
        }
        if (!NAME_PATTERN.matcher(name.trim()).matches()) {
            throw new ValidationException(fieldName + " drīkst saturēt tikai burtus un defisi! " +
                    "Ievadītā vērtība: '" + name + "'");
        }
    }

    /**
     * Pārbauda e-pasta formātu.
     * @throws ValidationException ja formāts ir nepareizs
     */
    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("E-pasts nedrīkst būt tukšs!");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new ValidationException("E-pasta formāts ir nepareizs! " +
                    "Piemērs: janis@example.lv\nIevadītā vērtība: '" + email + "'");
        }
    }

    /**
     * Pārbauda personas koda formātu.
     * @throws ValidationException ja formāts neatbilst DDMMGG-NNNNN
     */
    public static void validatePersonalCode(String code) {
        if (code == null || code.isBlank()) {
            throw new ValidationException("Personas kods nedrīkst būt tukšs!");
        }
        if (!PERSONAL_CODE_PATTERN.matcher(code.trim()).matches()) {
            throw new ValidationException("Personas koda formāts ir nepareizs! " +
                    "Vajadzīgais formāts: DDMMGG-NNNNN (piem. 010190-12345)\n" +
                    "Ievadītā vērtība: '" + code + "'");
        }
    }

    /**
     * Pārbauda reģistrācijas datuma un laika formātu.
     * @throws ValidationException ja formāts neatbilst GGGG-MM-DD HH:MM
     */
    public static void validateDateTime(String dateTime) {
        if (dateTime == null || dateTime.isBlank()) {
            throw new ValidationException("Datums un laiks nedrīkst būt tukšs!");
        }
        if (!DATETIME_PATTERN.matcher(dateTime.trim()).matches()) {
            throw new ValidationException("Datuma/laika formāts ir nepareizs! " +
                    "Vajadzīgais formāts: GGGG-MM-DD HH:MM (piem. 2024-09-01 08:30)\n" +
                    "Ievadītā vērtība: '" + dateTime + "'");
        }
    }

    // -----------------------------------------------------------------------
    // Palīgmetodes (boolean versijas — bez izņēmumiem)
    // -----------------------------------------------------------------------

    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPersonalCode(String code) {
        return code != null && PERSONAL_CODE_PATTERN.matcher(code.trim()).matches();
    }

    public static boolean isValidDateTime(String dt) {
        return dt != null && DATETIME_PATTERN.matcher(dt.trim()).matches();
    }
}
