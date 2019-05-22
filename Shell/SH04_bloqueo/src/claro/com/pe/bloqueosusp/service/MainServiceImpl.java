package claro.com.pe.bloqueosusp.service;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import claro.com.pe.bloqueosusp.bean.BonoProgram;
import claro.com.pe.bloqueosusp.bean.LisBonoProgram;
import claro.com.pe.bloqueosusp.bean.ReasonsList;
import claro.com.pe.bloqueosusp.bean.ResponseAuditoria;
import claro.com.pe.bloqueosusp.client.INTCAA0013Client;
import claro.com.pe.bloqueosusp.dao.MotorPromDAO;
import claro.com.pe.bloqueosusp.exception.DBException;
import claro.com.pe.bloqueosusp.util.Constantes;
import claro.com.pe.bloqueosusp.util.PropertiesExternos;
import pe.com.claro.esb.data.customer.customer.v2.CustomerAccount;
import pe.com.claro.esb.data.customer.customerorder.v2.CustomerOrder;
import pe.com.claro.esb.data.engagedparty.agreement.v2.Agreement;
import pe.com.claro.esb.data.engagedparty.agreement.v2.AgreementItem;
import pe.com.claro.esb.data.engagedparty.party.v2.PartyRole;
import pe.com.claro.esb.data.engagedparty.partyorder.v2.PartyOrder;
import pe.com.claro.esb.data.engagedparty.partyproductspecificationandoffering.v2.PartyRoleProductOffering;
import pe.com.claro.esb.message.bsscustomermanagement.getstatusreasons.v2.GetStatusReasonsRequestType;
import pe.com.claro.esb.message.bsscustomermanagement.getstatusreasons.v2.GetStatusReasonsResponseType;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MainServiceImpl implements MainService {

	private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);
	@Autowired
	MotorPromDAO motorPromDAO;

	@Autowired
	INTCAA0013Client intCAA0013Client;
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
    public void run(String idTransaction)  
    		throws Exception {
		int numeroIntento=0;
		String codigoResp=Constantes.EXITO;
		LOGGER.info(idTransaction+"[INICIO - "+Constantes.SHELLBLOQUEOSUSPENSION+" ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}				

				//8: Desactiva Bloqueo/suspensión por falta de pago.
				codigoResp=descativaPoBloqueoSuspension(idTransaction,propertiesExternos.SUSPENSIONPORPAGO,Constantes.SUSPENSIONPORPAGO
						                               ,propertiesExternos.SUSPENSIONPORPAGOREASONCOD,propertiesExternos.SUSPENSIONPORPAGOREASONDES);
				
				//09: Bloqueo/suspensión por robo.
				codigoResp=descativaPoBloqueoSuspension(idTransaction,propertiesExternos.SUSPENSIONPORROBO,Constantes.SUSPENSIONPORROBO
														,propertiesExternos.SUSPENSIONPORROBOREASONCOD,propertiesExternos.SUSPENSIONPORROBOREASONDES);
				
				//10: Bloqueo/suspensión por fraude. 
				codigoResp=descativaPoBloqueoSuspension(idTransaction,propertiesExternos.SUSPENSIONPORFRAUDE,Constantes.SUSPENSIONPORFRAUDE
														,propertiesExternos.SUSPENSIONPORFRAUDREASONECOD,propertiesExternos.SUSPENSIONPORFRAUDREASONDES);
				
				//11: Suspensión a solicitud del cliente.
				codigoResp=descativaPoBloqueoSuspension(idTransaction,propertiesExternos.SUSPENSIONPORSOLICITUD,Constantes.SUSPENSIONPORSOLICITUD
														,propertiesExternos.SUSPENSIONPORSOLICITUDREASONCOD,propertiesExternos.SUSPENSIONPORSOLICITUDREASONDES);
				

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
			LOGGER.info(idTransaction+"[FIN - "+Constantes.SHELLBLOQUEOSUSPENSION+" ]");
		}
    }    
    
	
	public  String  descativaPoBloqueoSuspension(String idTransaction,String criterioSuspension,String motivo,String CodReason,String Reason)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		LisBonoProgram listaBono=new  LisBonoProgram();
    	List<BonoProgram>  list=new ArrayList<BonoProgram>();
    	String nombreMetodo = Constantes.SHELLBLOQUEOSUSPENSIONMOTIVOs;
		String cadenaTrazabilidad = "[" + nombreMetodo + " idTx=" + idTransaction + "] ";
		ReasonsList reasons=new ReasonsList();
		try {
    		String criterio=Constantes.SHELLBLOQUEOSUSPENSIONMOTIVOs;
    		
    		LOGGER.info(idTransaction+" INICIO : "+criterio+ motivo);
    		
    		listaBono=motorPromDAO.obtenerBonosPorSuspencion(idTransaction,criterioSuspension);
    		if(Constantes.EXITO.equals(listaBono.getCodigoError())){
    			list=listaBono.getListabono();
    			LOGGER.info(idTransaction+"- datos obtenidos a desactivar "+list.size());

    			String contractExterno=Constantes.CONSTANTEVACIA;
    			
    		
    			for(BonoProgram bonoPrograma:list ){
    				if(null!=bonoPrograma.getContractidExterno() && !bonoPrograma.getContractidExterno().isEmpty()){
        				if(!contractExterno.equals(bonoPrograma.getContractidExterno())){
        					reasons=getReasonsList(cadenaTrazabilidad, bonoPrograma.getContractidExterno(), idTransaction);
        				}else{
        					LOGGER.info(idTransaction+" ContractidExterno cargado: "+bonoPrograma.getContractidExterno());
        				}
        				
        				if(null!=reasons.getTickStatus()&& !reasons.getTickStatus().isEmpty() && propertiesExternos.OPENESTADO.equalsIgnoreCase(reasons.getTickStatus())){
        			       if(CodReason.isEmpty()){
           					if(propertiesExternos.FULLSUSPENSION.equalsIgnoreCase(reasons.getTickShdes())
             					   ||propertiesExternos.PARTSUSPENSION.equalsIgnoreCase(reasons.getTickShdes())){				
                 					LOGGER.info(idTransaction+" Bono a Desactivar : "+bonoPrograma.getBonoid());
                 					ResponseAuditoria auditoria=new ResponseAuditoria();
                 					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramId(),Constantes.SHELLBLOQUEOSUSPENSIONMOTIVOs);
                 					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
                 						LOGGER.info(idTransaction+" Exito al desactivar Bono con rogramId :"+bonoPrograma.getProgramId()+" Por "+Reason);	        						
                 					}else{
                 						LOGGER.info(idTransaction+" Error al desctivar Bono con ProgramId : "+bonoPrograma.getProgramId()+" Por "+Reason);
                 					}		
                 				}else{
                 					LOGGER.info(idTransaction+" Sin desactivacion  ProgramId :"+bonoPrograma.getProgramId()+" Por: "+Reason);
                 				}
        			       }else{
	           					if((propertiesExternos.FULLSUSPENSION.equalsIgnoreCase(reasons.getTickShdes())
	             					   ||propertiesExternos.PARTSUSPENSION.equalsIgnoreCase(reasons.getTickShdes()))
	             					   && CodReason.equalsIgnoreCase(reasons.getIdReason())){				
	                 					LOGGER.info(idTransaction+" Bono a Desactivar : "+bonoPrograma.getBonoid());
	                 					ResponseAuditoria auditoria=new ResponseAuditoria();
	                 					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramId(),Constantes.SHELLBLOQUEOSUSPENSIONMOTIVOs);
	                 					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
	                 						LOGGER.info(idTransaction+" Exito al desactivar Bono con rogramId :"+bonoPrograma.getProgramId()+" Por "+Reason);	        						
	                 					}else{
	                 						LOGGER.info(idTransaction+" Error al desctivar Bono con ProgramId : "+bonoPrograma.getProgramId()+" Por "+Reason);
	                 					}		
	                 				}else{
	                 					LOGGER.info(idTransaction+" Sin desactivacion  ProgramId :"+bonoPrograma.getProgramId()+" Por: "+Reason);
	                 				}
        			       }

        				}else{
        					LOGGER.info(idTransaction+" Sin desactivacion  ProgramId :"+bonoPrograma.getProgramId()+" Por: "+Reason);
        				}	
        				contractExterno=bonoPrograma.getContractidExterno();
    				}else{
    					LOGGER.info(idTransaction+" ProgramId :"+bonoPrograma.getProgramId()+" No Tiene Contract_ID_Text");
    				}
    			}
    		}else{
    			LOGGER.info(idTransaction+" ERROR  DESACTIVACION :"+propertiesExternos.mensajeIdf1);
    		}
    		LOGGER.info(idTransaction+" FIN : "+criterio+motivo);
    		
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
	
    public ReasonsList getReasonsList(String cadenaTrazabilidad, String contrato,String idTransaction) throws Exception{
		LOGGER.info(cadenaTrazabilidad + " ======= Inicia Invocacion  INT-COP-0013 ======");
		ReasonsList rasonsList=new ReasonsList();
		GetStatusReasonsResponseType response= new GetStatusReasonsResponseType();
		GetStatusReasonsRequestType request=new  GetStatusReasonsRequestType();
		
		
		try{		
			Agreement agreement=new Agreement();
			agreement.setID(contrato);
			
			AgreementItem  agreementItem=new AgreementItem();
			agreementItem.setAgreement(agreement);
			
			PartyRoleProductOffering partyRoleProductOffering=new PartyRoleProductOffering();	
			partyRoleProductOffering.getAgreementItem().add(agreementItem);
            CustomerOrder customerOrder=new CustomerOrder();
			
			PartyOrder partyOrder=new PartyOrder();
			partyOrder.setInteractionStatus(propertiesExternos.OPENESTADOREQUEST);
			customerOrder.setPartyOrder(partyOrder);
			PartyRole partyRole=new PartyRole();
			partyRole.getPartyRoleProductOffering().add(partyRoleProductOffering);
			partyRole.getCustomerOrder().add(customerOrder);
			
			
			CustomerAccount customerAcount=new CustomerAccount();
			customerAcount.getPartyRole().add(partyRole);
			
			request.setCustomerAccount(customerAcount);
			response=intCAA0013Client.getStatusReasons(request, cadenaTrazabilidad, propertiesExternos.usuarioAplicacionStatusReasons0013,
					propertiesExternos.nombreAplicacionStatusReasons0013, idTransaction, propertiesExternos.usuarioAplicacionStatusReasons0013);
			
			if(Integer.parseInt(Constantes.EXITO)==response.getResponseStatus().getStatus()){
				for(int i=0;i<response.getResponseData().getReasonsList().size();i++){
					for(int k=0;k<response.getResponseData().getReasonsList().get(i).getCustomerAccount().getCustomerOrder().size();k++){
						rasonsList.setTickShdes(response.getResponseData().getReasonsList().get(i).getCustomerAccount().getCustomerOrder()
								.get(k).getPartyOrder().getBusinessInteractionType().getName());
						rasonsList.setTickStatus(response.getResponseData().getReasonsList().get(i).getCustomerAccount().getCustomerOrder()
								.get(k).getPartyOrder().getInteractionStatus());
						rasonsList.setIdReason(response.getResponseData().getReasonsList().get(i).getCustomerAccount().getCustomerOrder()
								.get(k).getPartyOrder().getBusinessInteractionExtension().getReasonCode());
						rasonsList.setReason(response.getResponseData().getReasonsList().get(i).getCustomerAccount().getCustomerOrder()
								.get(k).getPartyOrder().getBusinessInteractionExtension().getReason());
					}				
				}
			}
			
			LOGGER.info(cadenaTrazabilidad + " ======= FIN  Invocacion INT-COP-0013 ======");
		}catch(Exception e){
			LOGGER.info(cadenaTrazabilidad + " ======= Error Invocacion INT-COP-0013 ======"+e.getLocalizedMessage());
			throw new Exception("update exception");
		}
		return rasonsList;
	}
}