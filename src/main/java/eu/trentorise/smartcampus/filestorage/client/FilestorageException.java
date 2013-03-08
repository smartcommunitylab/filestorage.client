package eu.trentorise.smartcampus.filestorage.client;

public class FilestorageException extends Exception {

	private static final long serialVersionUID = -6712610279990729561L;

	public FilestorageException() {
		super();
	}

	public FilestorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public FilestorageException(String message) {
		super(message);
	}

	public FilestorageException(Throwable cause) {
		super(cause);
	}

}
