package claro.com.pe.carga.datos.replica.cambio.plan.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import claro.com.pe.carga.datos.replica.cambio.plan.bean.BonoProgram;
import claro.com.pe.carga.datos.replica.cambio.plan.bean.LisBonoProgram;
import claro.com.pe.carga.datos.replica.cambio.plan.bean.ResponseAuditoria;
import claro.com.pe.carga.datos.replica.cambio.plan.dao.BSCSIXDAO;
import claro.com.pe.carga.datos.replica.cambio.plan.dao.MotorPromDAO;
import claro.com.pe.carga.datos.replica.cambio.plan.exception.DBException;
import claro.com.pe.carga.datos.replica.cambio.plan.util.Constantes;
import claro.com.pe.carga.datos.replica.cambio.plan.util.PropertiesExternos;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MainServiceImpl implements MainService {

    private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);

    @Autowired
	PropertiesExternos propertiesExternos;
    
    @Autowired
    MotorPromDAO motorDao;

    @Autowired
    BSCSIXDAO bscsixDao;
	
    @Transactional(rollbackFor = Exception.class)
	@Override
    public void runReplicambioPlanHist(String idTransaction,String pi_fec_proc)  
    		throws Exception {
		int numeroIntento=0;
		String codigoResp=Constantes.EXITO;
		LOGGER.info(idTransaction+"[INICIO - SH16_MOTPROM_CargaDatos_ReplicaCambPlanHist ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}				

				//Carga Dtaos Replica Cambio Plan 
				codigoResp=replicambioPlanHist(pi_fec_proc, idTransaction);
			
			   if(Constantes.NOEXITO.equals(codigoResp))	{	
				   numeroIntento++;
			   }else{
					break;  
			   }
			}
			
			if(Constantes.NOEXITO.equals(codigoResp)){
				 throw new Exception("exception");
			}	
			
		}catch(Exception e){
			 LOGGER.info(idTransaction+propertiesExternos.mensajeIdf1);
			 throw new Exception("exception");
		}finally{
			LOGGER.info(idTransaction+"[Fin - SH16_MOTPROM_CargaDatos_ReplicaCambPlanHist ]");
		}
    } 
    
    
    
    
    public  String  replicambioPlanHist(String fecha,String transaccion)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		LisBonoProgram listaBono=new  LisBonoProgram();
		LisBonoProgram listaBonoIX=new  LisBonoProgram();
    	List<BonoProgram>  list=new ArrayList<BonoProgram>();
		try {
    		String criterio=Constantes.CARGACAMBIOPLAN;
    		LOGGER.info(transaccion+"INICIO  : "+criterio);
    		
    		
    		LOGGER.info(transaccion+Constantes.BSCSCARGA);
			listaBono=motorDao.obtenerConsCambioplan(transaccion, fecha);
			
			LOGGER.info(transaccion+"- datos BSCS obtenidos a cargar"+list.size());
    		if(Constantes.EXITO.equalsIgnoreCase(listaBono.getCodigoError()) &&listaBono.getListabono().size()>0){
    			list=listaBono.getListabono();
    			LOGGER.info(transaccion+Constantes.BSCSCARGAINSERT);
        					ResponseAuditoria auditoria=new ResponseAuditoria();
        					auditoria=motorDao.insertarCargaMasiva(transaccion, listaBono);
        					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
        						LOGGER.info(transaccion+" Exito Carga datos Replica Cambio Plan BSCS");		        						
        					}else{
        						LOGGER.info(transaccion+"NO Carga datos Replica Cambio Plan BSCS ");
        					}		
        		
    		}else{
    			LOGGER.info(transaccion+propertiesExternos.mensajeIdf2);
    		}
    		

			LOGGER.info(transaccion+Constantes.BSCSCARGAIX);
			//listaBonoIX=bscsixDao.obtenerConsCambioplanBSCSIX(transaccion, "01042016");
			listaBonoIX=bscsixDao.obtenerConsCambioplanBSCSIX(transaccion, fecha.replaceAll(Constantes.CONSTSLACE, Constantes.CONSTANTE_VACIA));
			
			LOGGER.info(transaccion+"- datos BSCSIX obtenidos a cargar"+list.size());
    		if(Constantes.EXITO.equalsIgnoreCase(listaBono.getCodigoError()) &&listaBonoIX.getListabono().size()>0){
    			list=listaBono.getListabono();
    			LOGGER.info(transaccion+Constantes.BSCSCARGAINSERT);
        					ResponseAuditoria auditoria=new ResponseAuditoria();
        					auditoria=motorDao.insertarCargaMasiva(transaccion, listaBonoIX);
        					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
        						LOGGER.info(transaccion+" Exito Carga datos Replica Cambio Plan BSCSIX");		        						
        					}else{
        						LOGGER.info(transaccion+" NO Carga datos Replica Cambio Plan BSCSIX");
        					}		
        		
    		}else{
    			LOGGER.info(transaccion+propertiesExternos.mensajeIdf2);
    		}
			
    		LOGGER.info(transaccion+" FIN : "+criterio);
    		
		} catch(DBException e){
			LOGGER.error(transaccion + "- Error en la BD "+ e.getNombreBD() + " - " +e.getNombreSP()+". "+ e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;
		}
		catch (Exception e) {
			LOGGER.error(transaccion + "Error Interno: " + e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;	
		}
    	finally{
			LOGGER.info(transaccion+"[Fin de metodo: run]");
		}
		//FIN - Ejecutar Proceso  Shell 
    	
		return codigoResp;
	}
}