package claro.com.pe.cambioplan;

import claro.com.pe.cambioplan.service.MainService;
import claro.com.pe.cambioplan.util.Constantes;
import claro.com.pe.cambioplan.util.PropertiesExternos;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class MainClass{
    
    private static Logger logger = Logger.getLogger(MainClass.class);
    private static ApplicationContext objContextoSpring;
    
    @Autowired
    private PropertiesExternos externalProperties;
    
    @Autowired
    private MainService mainService;
    
    public static void main( String[] args ){
        long startTime = System.currentTimeMillis();
        String idTransaction = args[0];
        String pi_fec_proc = args[1];
        String pi_dias = args[2];
        String messageTransaction = "[main idTx=" + idTransaction + "]";
        int result=Constantes.CORRECTO;
        try{
        	logger.info(messageTransaction+"[INICIO de metodo: main]");
            objContextoSpring = new ClassPathXmlApplicationContext(Constantes.CONFIG_PATH);
            MainClass mainClass = objContextoSpring.getBean( MainClass.class );
            mainClass.loadLog4J(idTransaction);

            result= mainClass.launch(idTransaction,pi_fec_proc,pi_dias);
            if(Constantes.NOCORRECTO==result){
            	throw new Exception();
            } 
            
        }catch(Exception e){
        	logger.info(messageTransaction+"[ERROR: "+e.getMessage());
        	logger.info(messageTransaction+"[ERROR: ENVIAR CORREO CON DETALLE]");
            Integer.parseInt(Constantes.EXITOMENSAJE);
        }finally{
            try{
                logger.info(messageTransaction+"[FIN de metodo: main] Tiempo total de proceso(ms): " + (System.currentTimeMillis() - startTime) + " milisegundos.");
                ((ConfigurableApplicationContext) objContextoSpring).close();
            }catch(Exception e){
                logger.error(messageTransaction+"Error al cerrar Contexto: "+ e +".");
            }
        } 
    }
    
    public void loadLog4J(String idTransaction){
        
        long startTime = System.currentTimeMillis();
        String messageTransaction = "[loadLog4J idTx=" + idTransaction + "]";

        PropertyConfigurator.configure(externalProperties.LOG4J_DIR);
        
        logger.info(messageTransaction + "[INICIO de metodo: loadLog4J]");
        logger.info(messageTransaction+"[FIN de metodo: loadLog4J] Tiempo total de proceso(ms): " + (System.currentTimeMillis() - startTime) + " milisegundos.");

    }
    
    public int launch(String idTransaction,String pi_fec_proc,String pi_dias){

        long startTime = System.currentTimeMillis();
        String messageTransaction = "[launch idTx=" + idTransaction + "]";
        int resultado=Constantes.CORRECTO;
        logger.info(messageTransaction + "[INICIO de metodo: launch]");
        
        try{
            mainService.run(idTransaction,pi_fec_proc,pi_dias);
        } catch (Exception e){
            logger.error(messageTransaction+"Error: "+ e + ".");
            resultado=Constantes.NOCORRECTO;
        }
        
        logger.info(messageTransaction+"[FIN de metodo: launch] Tiempo total de proceso(ms): " + (System.currentTimeMillis() - startTime) + " milisegundos.");
        return resultado;
    }

}