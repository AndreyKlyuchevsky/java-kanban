package manager.client;

public class KVTaskClientException extends RuntimeException {
    public KVTaskClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public KVTaskClientException(String message) {

        super(message);

    }
}
