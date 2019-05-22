package claro.com.pe.cambioplan.exception;

public class DBException extends BaseException{

	private static final long	serialVersionUID	= -7670509730102619157L;
	private String				nombreBD;
	private String				nombreSP;

	public DBException( String codError, String nombreBD, String nombreSP, String msjError, Exception objException ){
		super( codError, msjError, objException );
		this.nombreBD = nombreBD;
		this.nombreSP = nombreSP;
	}

	public DBException( String codError, String nombreBD, String nombreSP, String msjError, Throwable objException ){
		super( codError, msjError, objException );
		this.nombreBD = nombreBD;
		this.nombreSP = nombreSP;
	}

	public DBException( String msjError, Exception objException ){
		super( msjError, objException );
	}

	public DBException( Exception objException ){
		super( objException );
	}

	public DBException( String msjError ){
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
