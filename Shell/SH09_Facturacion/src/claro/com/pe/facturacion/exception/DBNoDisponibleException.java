package claro.com.pe.facturacion.exception;

public class DBNoDisponibleException extends BaseException{

	private static final long	serialVersionUID	= 1959806790486951584L;
	private String				nombreBD;
	private String				nombreSP;

	public DBNoDisponibleException( String codError, String nombreBD, String nombreSP, String msjError, Exception objException ){
		super( codError, msjError, objException );
		this.nombreBD = nombreBD;
		this.nombreSP = nombreSP;
	}

	public DBNoDisponibleException( String codError, String nombreBD, String nombreSP, String msjError, Throwable objException ){
		super( codError, msjError, objException );
		this.nombreBD = nombreBD;
		this.nombreSP = nombreSP;
	}

	public DBNoDisponibleException( String msjError, Exception objException ){
		super( msjError, objException );
	}

	public DBNoDisponibleException( Exception objException ){
		super( objException );
	}

	public DBNoDisponibleException( String msjError ){
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
