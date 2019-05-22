package claro.com.pe.facturacion.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class EAIUtil {

	private static HashMap<Class<Object>, JAXBContext> mapContexts = new HashMap<Class<Object>, JAXBContext>();
	
	private static final Logger LOGGER = Logger.getLogger(EAIUtil.class);

	public static String obtenerMensajeProperties(Integer cantidad,
												   String[] valores,
												   String mensaje){

		String valor = Constantes.CADENAVACIA;
		Object[] object = null;
		MessageFormat form =  new MessageFormat(mensaje);;

		object = new Object[cantidad];

		for(int i=0;i<=cantidad-1;i++){
			object[i] = valores[i];
		}

		valor = form.format(object);

		return valor;
	}
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}


	public static String getXmlTextFromJaxB(Object anyObject) {
		String commandoRequestEnXml = null;
		JAXBContext context;
		try {
			context = obtainJaxBContextFromClass(anyObject.getClass());
			Marshaller marshaller = context.createMarshaller();

			StringWriter xmlWriter = new StringWriter();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(new JAXBElement(
								  new QName(Constantes.CADENAVACIA, 
										  	anyObject.getClass().getSimpleName()), 
								  anyObject.getClass(),
								  anyObject), 
								  xmlWriter);

			commandoRequestEnXml = xmlWriter.toString();
		} catch (Exception e) {
			LOGGER.error("ERROR[XMLException]: " + e.getMessage());
		}
		return commandoRequestEnXml;
	}

	private static JAXBContext obtainJaxBContextFromClass(Class clas) {
		JAXBContext context;
		context = mapContexts.get(clas);
		if (context == null) {
			try {
				context = JAXBContext.newInstance(clas);
				mapContexts.put(clas, context);
			} catch (Exception e) {
				LOGGER.error("ERROR[XMLException]: " + e.getMessage(), e);
			}
		}
		return context;
	}

	public static  String obtenerDatoLineaHLR(String resultado) {
	    int posicion = 0;    
	    posicion = resultado.lastIndexOf('.');
	    if (resultado.length() > posicion ) {
	      return resultado.substring(posicion + 1).trim() ;
	    }
	    return Constantes.CADENAVACIA;
	}

    public static double round(double val, int places) {
		 long factor = (long)Math.pow(10,places);
		 double valor = val * factor;
		 long tmp = Math.round(valor);
		 return (double)tmp / factor;
    }

    public static java.sql.Date getSQLDate() {
    	return new java.sql.Date(Calendar.getInstance().getTime().getTime());
	}

    public static Date getUtilDate(String fecha, String formato) throws Exception {
    	return new SimpleDateFormat(formato).parse(fecha);
	}

    public static Date getUtilDateDeMiliseg(long milisegundos) throws Exception {
    	Date fecha = new Date();
    	fecha.setTime(milisegundos);
    	return fecha;
	}

    public static Date getUtilDateDeMiliseg(String milisegundos) throws Exception {
		if (milisegundos == null || milisegundos.trim().equals(Constantes.CADENAVACIA)) {
			return null;
		}
		Date fecha = new Date();
		fecha.setTime(Long.parseLong(milisegundos));
		return fecha;
	}

    public static String getDateFormato(Date fecha, String formato) throws Exception {
    	if (fecha == null) return Constantes.CADENAVACIA;
    	return new SimpleDateFormat(formato).format(fecha);
	}

    public static String getDateFormato(String fecha, String formato) throws Exception {
    	if (fecha == null || fecha.trim().equals(Constantes.CADENAVACIA)) return Constantes.CADENAVACIA;
    	return new SimpleDateFormat(formato).format(fecha);
	}

    public static String getDateFormato(Calendar fecha, String formato) throws Exception {
    	if (fecha == null) return Constantes.CADENAVACIA;
    	return new SimpleDateFormat(formato).format(fecha.getTime());
	}

    public static String getDateFormato(long fecha, String formato) throws Exception {
    	return new SimpleDateFormat(formato).format(fecha);
	}

	public static int calcularEdad(String fecha, String formato) {
		try {
			return calcularEdad(new SimpleDateFormat(formato).parse(fecha));
		} catch (ParseException e) {
			return -1;
		}
	}

	public static int calcularEdad(Date birthDate) {
		try {
			Calendar birth = new GregorianCalendar();
			Calendar today = new GregorianCalendar();
			int age = 0;
			int factor = 0;
			Date currentDate = new Date(); // current date
			birth.setTime(birthDate);
			today.setTime(currentDate);
			if (today.get(Calendar.MONTH) <= birth.get(Calendar.MONTH)) {
				if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)) {
					if (today.get(Calendar.DATE) < birth.get(Calendar.DATE)) {
						factor = -1; // Aun no celebra su cumpleaños
					}
				} else {
					factor = -1; // Aun no celebra su cumpleaños
				}
			}
			age = (today.get(Calendar.YEAR) - birth.get(Calendar.YEAR))  + factor;
			return age;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static Calendar fechaStringToCalendar(String fecha, String formato) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		try {
			cal.setTime(sdf.parse(fecha));
		} catch (ParseException e) {
			LOGGER.error("ERROR[ParseException]: " + e.getMessage(), e);
		}
		return cal;
	}
	
	
	public static String getStackTraceFromException( Exception exception ){
		StringWriter stringWriter = new StringWriter();
		exception.printStackTrace( new PrintWriter( stringWriter, true ) );
		return stringWriter.toString();
	}
}
