package custom_exception;

public class InternalException extends WebApartmaniException {

	private static final long serialVersionUID = 8634235820615930596L;

	public InternalException() {
	}

	public InternalException(String message) {
		super(message);
	}

	public InternalException(Throwable cause) {
		super(cause);
	}

	public InternalException(String message, Throwable cause) {
		super(message, cause);
	}

	public InternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
