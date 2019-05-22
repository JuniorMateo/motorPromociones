package claro.com.pe.desa.titularidad.exception;

public class DBTimeoutException extends BaseException{

	private static final long	serialVersionUID	= -7519941877692528998L;
	private String				nombreBD;
	private String				nombreSP;

	public DBTimeoutException( String codError, String nombreBD, String nombreSP, String msjError, Exception objException ){
		super( codError, msjError, objException );
		this.nombreBD = nombreBD;
		this.nombreSP = nombreSP;
	}

	public DBTimeoutException( String codError, String nombreBD, String nombreSP, String msjError, Throwable objException ){
		super( codError, msjError, objException );
		this.nombreBD = nombreBD;
		this.nombreSP = nombreSP;
	}

	public DBTimeoutException( String msjError, Exception objException ){
		super( msjError, objException );
	}

	public DBTimeoutException( Exception objException ){
		super( objException );
	}

	public DBTimeoutException( String msjError ){
		super( msjError );
	}

	public String getNombreBD(){
		return nombreBD;
	}

	public void setNombreBD( String nombreBD ){
		this.nombreBD = nombreBD;
	}

	public String getNombreSP(){
		return nombreSP;
	}

	public void setNombreSP( String nombreSP ){
		this.nombreSP = nombreSP;
	}

}
