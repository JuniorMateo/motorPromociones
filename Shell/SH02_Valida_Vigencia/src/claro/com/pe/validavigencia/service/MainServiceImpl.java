package claro.com.pe.validavigencia.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import claro.com.pe.validavigencia.bean.ResponseAuditoria;
import claro.com.pe.validavigencia.dao.MotorPromDAO;
import claro.com.pe.validavigencia.exception.DBException;
import claro.com.pe.validavigencia.util.Constantes;
import claro.com.pe.validavigencia.util.PropertiesExternos;


@Service
public class MainServiceImpl implements MainService {

    private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);
	@Autowired
	MotorPromDAO motorPromDAO;
		
	@Autowired
	PropertiesExternos propertiesExternos;

	
	@Transactional(rollbackFor = Exception.class)
	@Override
    public void run(String idTransaction,String pi_fec_proc,String pi_dias)  
    		throws Exception {
		int numeroIntento=0;
		String codigoResp=Constantes.EXITO;
		LOGGER.info(idTransaction+"[INICIO - SH09_MOTPROM_ValidaDes_Facturacion ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}				

				//Desactivacion por Incumplimiento  De Pago
				codigoResp=jecutarIncumplimientoPago(idTransaction, pi_fec_proc, pi_dias);

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
			LOGGER.info(idTransaction+"[Fin - SH09_MOTPROM_ValidaDes_Facturacion ]");
		}
    }    
	

	public  String  jecutarIncumplimientoPago(String idTransaction,String pi_fec_proc,String pi_dias)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		ResponseAuditoria response=new ResponseAuditoria();
		try {
    		String criterio=Constantes.CAMBIOPLANLINEAUPGRADEDES;
    		
    		LOGGER.info(idTransaction+"INICIO  : "+criterio);
    		response=motorPromDAO.ejecutarTransaccionFechaVigencia(idTransaction,pi_fec_proc,pi_dias, Constantes.CAMBIOPLANLINEAUPGRADEDES);
    		if(Constantes.EXITO.equals(response.getCodigoRespuesta())){
    			LOGGER.info(idTransaction+" Exito al desacitvar Bonos");
    		}else{
    			LOGGER.info(idTransaction+" No xiste Bono a desactivar");
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