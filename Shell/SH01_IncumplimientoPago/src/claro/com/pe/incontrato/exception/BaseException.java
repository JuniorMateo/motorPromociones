package claro.com.pe.incontrato.exception;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	private String codError;

	public BaseException(String codError, String message, Exception exception) {
		super(message, exception);
		this.codError = codError;
	}

	public BaseException(String codError, String message, Throwable exception) {
		super(message, exception);
		this.codError = codError;
	}
	
	public BaseException(String codError, String message) {
		super(message);
		this.codError = codError;
	}

	public BaseException(String message, Exception exception) {
		super(message, exception);
	}

	public BaseException(String message, Throwable exception) {
		super(message, exception);
	}

	public BaseException(Exception exception) {
		super(exception);
	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String servidorFTP, String rutaServidor,
			String nombreArchivo, Exception objException) {
		super(objException);
	}

	public String getCodError() {
		return codError;
	}

	public void setCodError(String codError) {
		this.codError = codError;
	}

}
