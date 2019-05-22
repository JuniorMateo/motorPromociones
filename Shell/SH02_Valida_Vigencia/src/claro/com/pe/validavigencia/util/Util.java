package claro.com.pe.validavigencia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Util {
	public static final Logger logger = Logger
			.getLogger(Util.class);

	public Date deStringADate(String mensajeTransaccion,String fechaString) throws ParseException {

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
	

}
