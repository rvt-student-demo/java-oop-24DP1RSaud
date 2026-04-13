package rvt.studentreg.exceptions;

/**
 * Tiek izmests, kad students ar norādīto personas kodu
 * netiek atrasts sistēmā (remove/edit operācijās).
 */
public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String personalCode) {
        super("Students ar personas kodu '" + personalCode + "' nav atrasts.");
    }
}
