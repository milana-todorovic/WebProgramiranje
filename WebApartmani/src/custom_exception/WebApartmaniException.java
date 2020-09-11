package custom_exception;

public class WebApartmaniException extends RuntimeException {

	private static final long serialVersionUID = 1336139911478622579L;

	public WebApartmaniException() {
	}

	public WebApartmaniException(String message) {
		super(message);
	}

	public WebApartmaniException(Throwable cause) {
		super(cause);
	}

	public WebApartmaniException(String message, Throwable cause) {
		super(message, cause);
	}

	public WebApartmaniException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
