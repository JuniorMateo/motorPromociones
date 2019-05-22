package claro.com.pe.cambioplan.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Util {
	public static final Logger logger = Logger
			.getLogger(Util.class);

	public static Date deStringADate(String mensajeTransaccion,String fechaString) throws ParseException {

		SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd/MM/yyyy");
	    Date convertedCurrentDate = new Date();
	    
		
		try{
			convertedCurrentDate = formatoDelTexto.parse(fechaString);	
		}
		catch(Exception e){
			logger.error(mensajeTransaccion + "Ocurrio un error al transformar la fecha.",e);
		}
		
		return convertedCurrentDate;
	}
	
	

	public static String getStackTraceFromException( Exception exception ){
		StringWriter stringWriter = new StringWriter();
		exception.printStackTrace( new PrintWriter( stringWriter, true ) );
		return stringWriter.toString();
	}

}
