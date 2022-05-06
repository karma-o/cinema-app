package cinema.app.exception;

/**
 * This class is mostly used to throw exception if we have some error,
 * while trying to read/write/delete/update/etc some entity in database in DAO layer.
 *
 */
public class DataProcessingException extends RuntimeException {
    public DataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
