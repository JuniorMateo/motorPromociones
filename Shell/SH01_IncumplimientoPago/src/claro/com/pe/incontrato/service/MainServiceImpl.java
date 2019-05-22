package claro.com.pe.incontrato.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import claro.com.pe.incontrato.bean.BonoProgram;
import claro.com.pe.incontrato.bean.LisBonoProgram;
import claro.com.pe.incontrato.bean.PagoPuntual;
import claro.com.pe.incontrato.bean.ResponseAuditoria;
import claro.com.pe.incontrato.dao.MotorPromDAO;
import claro.com.pe.incontrato.dao.OACDAO;
import claro.com.pe.incontrato.exception.DBException;
import claro.com.pe.incontrato.util.Constantes;
import claro.com.pe.incontrato.util.PropertiesExternos;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MainServiceImpl implements MainService {

    private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);
	@Autowired
	MotorPromDAO motorDao;
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Autowired
	OACDAO oac;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
    public void run(String idTransaction)  
    		throws Exception {
		int numeroIntento=0;
		String codigoResp=Constantes.EXITO;
		LOGGER.info(idTransaction+"[INICIO - SH01_MOTPROM_ValidaDes_IncumplimientoPago ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}				

				//Desactivacion por Incumplimiento  De Pago
				codigoResp=jecutarIncumplimientoPago(idTransaction);

			   if(Constantes.NOEXITO.equals(codigoResp))	{	
				   numeroIntento++;
			   }else{
					break;  
			   }
			}
			
			if(Constantes.NOEXITO.equals(codigoResp)){
				 throw new Exception("update exception");
			}	
			
		}catch(Exception e){
			 LOGGER.info(idTransaction+"Error al ejecutar Transaccion");
			 throw new Exception("update exception");
		}finally{
			LOGGER.info(idTransaction+"[Fin - SH01_MOTPROM_ValidaDes_IncumplimientoPago ]");
		}
    }    
	
	
	
	public  String  jecutarIncumplimientoPago(String idTransaction)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		LisBonoProgram listaBono=new  LisBonoProgram();
    	List<BonoProgram>  list=new ArrayList<BonoProgram>();
    	String nombreMetodo = "Descativar Bono Por Incumplimiento de Pago";
		String cadenaTrazabilidad = "[" + nombreMetodo + " idTx=" + idTransaction + "] ";
		PagoPuntual pagoPuntual=new  PagoPuntual();
		try {
    		String criterio=Constantes.PAGOPUNTUAL;
    		
    		LOGGER.info(idTransaction+"INICIO  : "+criterio);
    		
    		listaBono=motorDao.obtenerCambioFacturacion(idTransaction);
    		if(Constantes.EXITO.equals(listaBono.getCodigoError())){
    			list=listaBono.getListabono();
    			LOGGER.info(idTransaction+"- datos obtenidos a desactivar "+list.size());

    			for(BonoProgram bonoPrograma:list ){	
    				pagoPuntual=oac.consultarPagoPuntual(idTransaction, bonoPrograma.getCustomerId(),Integer.parseInt( propertiesExternos.cantidadMeses));
    				if(Constantes.EXITO.equals(pagoPuntual.getCodigoRespuesta())){
    					if(Integer.parseInt(propertiesExternos.INDICADOR)==pagoPuntual.getIndicador()){				
        					LOGGER.info(idTransaction+" Bono a Desactivar : "+bonoPrograma.getProgram_id());
        					ResponseAuditoria auditoria=new ResponseAuditoria();
        					auditoria=motorDao.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgram_id(),Constantes.PAGOPUNTUAL);
        					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
        						LOGGER.info(idTransaction+" Exito  al desactivar Bono "+bonoPrograma.getProgram_id());		        						
        					}else{
        						LOGGER.info(idTransaction+" Error  al desactivar Bono "+bonoPrograma.getProgram_id());
        					}		
        				}
    				}else{
    					LOGGER.info(idTransaction+" No xiste Bono a desactivar " +bonoPrograma.getProgram_id());
    				}	
    			}
    		}else{
    			LOGGER.info(idTransaction+" ERROR  DESACTIVACION :"+propertiesExternos.mensajeIdf2);
    		}
    		LOGGER.info(idTransaction+" FIN : "+criterio);
    		
		} catch(DBException e){
			LOGGER.error(idTransaction + "- Error en la BD "+ e.getNombreBD() + " - " +e.getNombreSP()+". "+ e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;
		}
		catch (Exception e) {
			LOGGER.error(idTransaction + "Error Interno: " + e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;	
		}
    	finally{
			LOGGER.info(idTransaction+"[Fin de metodo: run]");
		}
		//FIN - Ejecutar Proceso  Shell 
    	
		return codigoResp;
	}
    
}