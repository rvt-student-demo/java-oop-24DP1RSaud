package rvt.studentreg.exceptions;

/**
 * Tiek izmests, kad mēģina reģistrēt studentu ar
 * jau eksistējošu e-pastu vai personas kodu.
 */
public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}
