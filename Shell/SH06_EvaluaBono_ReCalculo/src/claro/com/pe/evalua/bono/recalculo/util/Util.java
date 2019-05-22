package claro.com.pe.evalua.bono.recalculo.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class Util {
	public static final Logger logger = Logger
			.getLogger(Util.class);

	public  static Date deStringADate(String fechaString) throws ParseException {

		SimpleDateFormat formatoDelTexto = new SimpleDateFormat(Constantes.FORMATOFECHA4);
	    Date convertedCurrentDate = new Date();
	    
		
		try{
			convertedCurrentDate = formatoDelTexto.parse(fechaString);			
		}
		catch(Exception e){
			logger.error( "Ocurrio un error al transformar la fecha."+e.getMessage(),e);
		}
		
		return convertedCurrentDate;
	}
	
	 public static Date  stringToJavaDate(String sDate,String formato)  throws Exception{
		 
		 SimpleDateFormat sdf = new SimpleDateFormat(formato);
		 Date date = sdf.parse(sDate);
	        return date;  
	 } 
	 
 
	 
	 public static String getDateFormato(Date fecha, String formato) throws Exception {
	    	if (fecha == null) return Constantes.CONSTANTE_VACIA;
	    	return new SimpleDateFormat(formato).format(fecha);
		}
	
	public static String getStackTraceFromException( Exception exception ){
		StringWriter stringWriter = new StringWriter();
		exception.printStackTrace( new PrintWriter( stringWriter, true ) );
		return stringWriter.toString();
	}
	
	public static String obtenerTipoDoc0223(String tipo){
		String doc=Constantes.CERO;
		if(Constantes.DNI.equalsIgnoreCase(tipo)){
			doc=Constantes.UNO;
		}if(Constantes.RUC.equalsIgnoreCase(tipo)){
			doc=Constantes.TRES;
		}if(Constantes.CE.equalsIgnoreCase(tipo)){
			doc=Constantes.DOS;
		}if(Constantes.PASSAPORT.equalsIgnoreCase(tipo)){
			doc=Constantes.CUATRO;
		} return doc;
	}
	
	
	public static int encontrarAnio(Date fecha){
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      return calendar.get(Calendar.YEAR);
	}
	public static int encontrarDia(Date fecha){
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      return calendar.get(Calendar.DAY_OF_MONTH);
	}
	public static int encontrarMes(Date fecha){
	      Calendar calendar = Calendar.getInstance();
	      calendar.setTime(fecha); 
	      return calendar.get(Calendar.MONTH)+1;
	}
	
	public static int obtenerPeriodo(Date fecha1,Date fecha2){
		int periodo=0;
		try{
			int anio1= encontrarAnio(fecha1);
			int mes1=encontrarMes(fecha1);
			int dia1=encontrarDia(fecha1);
			
			int anio2= encontrarAnio(fecha2);
			int mes2=encontrarMes(fecha2);
			int dia2=encontrarDia(fecha2);
		
			
			int diasum=dia2-dia1;
			if(diasum<0){
				diasum=diasum*-1;
			}
			double diadiv=(double)diasum / 30;
			int periodoMes=mes2-mes1;
			if(diadiv>=0.5){
				periodo=(anio2-anio1)*12+periodoMes-1;
			}else{
				periodo=(anio2-anio1)*12+periodoMes;
			}				
			
		}catch(Exception e){
			
		}
		return periodo;
	}
	
	
	

}
