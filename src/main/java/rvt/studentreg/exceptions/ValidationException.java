package rvt.studentreg.exceptions;

/**
 * Tiek izmests, kad lietotāja ievade neatbilst validācijas prasībām.
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
