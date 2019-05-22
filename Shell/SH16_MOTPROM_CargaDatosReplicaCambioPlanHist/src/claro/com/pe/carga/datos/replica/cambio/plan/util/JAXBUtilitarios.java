package claro.com.pe.carga.datos.replica.cambio.plan.util;

import java.io.StringWriter;
import java.util.HashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

public class JAXBUtilitarios{

	private static final Logger wlLogger = Logger.getLogger(JAXBUtilitarios.class.getName());
	@SuppressWarnings("rawtypes")
	private static HashMap<Class, JAXBContext> mapContexts = new HashMap<Class, JAXBContext>();

	@SuppressWarnings( "rawtypes" )
	private static JAXBContext obtainJaxBContextFromClass( Class clas ){
		JAXBContext context;
		context = mapContexts.get( clas );
		if( context == null ){
			try{
				wlLogger.info( "Inicializando jaxcontext... para la clase " + clas.getName() );
				context = JAXBContext.newInstance( clas );
				mapContexts.put( clas, context );
			}
			catch( Exception e ){
				wlLogger.error( "Error creando JAXBContext:", e );
			}
		}
		return context;
	}

	public String getXmlTextFromJaxB( Object objJaxB ){
		String commandoRequestEnXml = null;
		JAXBContext context;
		try{
			context = obtainJaxBContextFromClass( objJaxB.getClass() );
			Marshaller marshaller = context.createMarshaller();
			StringWriter xmlWriter = new StringWriter();
			marshaller.marshal( objJaxB, xmlWriter );
			XmlObject xmlObj = XmlObject.Factory.parse( xmlWriter.toString() );
			commandoRequestEnXml = xmlObj.toString();
		}
		catch( Exception e ){
			wlLogger.error( "Error parseando object to xml:", e );
		}
		return commandoRequestEnXml;
	}

	@SuppressWarnings( { "unchecked", "rawtypes" } )
	public static String anyObjectToXmlText( Object objJaxB ){
		String commandoRequestEnXml = null;
		if( objJaxB != null ){
			JAXBContext context;
			try{
				context = obtainJaxBContextFromClass( objJaxB.getClass() );
				Marshaller marshaller = context.createMarshaller();
				StringWriter xmlWriter = new StringWriter();
				marshaller.marshal( new JAXBElement( new QName( "", objJaxB.getClass().getName() ), objJaxB.getClass(), objJaxB ), xmlWriter );
				XmlObject xmlObj = XmlObject.Factory.parse( xmlWriter.toString() );
				commandoRequestEnXml = xmlObj.toString();
			}
			catch( Exception e ){
				wlLogger.error( "Error parseando object to xml:", e );
			}
		}
		else{
			commandoRequestEnXml = "El objeto es nulo";
		}
		return commandoRequestEnXml;
	}
}