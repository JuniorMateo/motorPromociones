package claro.com.pe.facturacion.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import claro.com.pe.facturacion.bean.BonoProgram;
import claro.com.pe.facturacion.bean.LisBonoProgram;
import claro.com.pe.facturacion.bean.ResponseAuditoria;
import claro.com.pe.facturacion.bean.Subscriptions;
import claro.com.pe.facturacion.client.INTCOP0223Client;
import claro.com.pe.facturacion.dao.MotorPromDAO;
import claro.com.pe.facturacion.exception.DBException;
import claro.com.pe.facturacion.util.Constantes;
import claro.com.pe.facturacion.util.PropertiesExternos;
import pe.com.claro.esb.data.customer.customer.v2.Customer;
import pe.com.claro.esb.data.customer.customer.v2.CustomerAccount;
import pe.com.claro.esb.data.engagedparty.agreement.v2.Agreement;
import pe.com.claro.esb.data.engagedparty.agreement.v2.AgreementItem;
import pe.com.claro.esb.data.engagedparty.party.v2.PartyRole;
import pe.com.claro.esb.data.engagedparty.partyproductspecificationandoffering.v2.PartyRoleProductOffering;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsRequestType;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsResponseType;

import org.springframework.transaction.annotation.Transactional;

@Service
public class MainServiceImpl implements MainService {

    private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);
	@Autowired
	MotorPromDAO motorPromDAO;

	@Autowired
	INTCOP0223Client  iNTCOP0223Client;
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
    public void run(String idTransaction)  
    		throws Exception {
		int numeroIntento=0;
		String codigoResp=Constantes.EXITO;
		LOGGER.info(idTransaction+"[INICIO - SH09_MOTPROM_ValidaDes_Facturacion ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}				

				//descativa Por ciclo de FActuracion   
				codigoResp=descativaPorFActuracion(idTransaction);

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
    
	
	
	public  String  descativaPorFActuracion(String idTransaction)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		LisBonoProgram listaBono=new  LisBonoProgram();
    	List<BonoProgram>  list=new ArrayList<BonoProgram>();
    	String nombreMetodo = "Descativar Bono Por Cambio Plan";
		String cadenaTrazabilidad = "[" + nombreMetodo + " idTx=" + idTransaction + "] ";
		Subscriptions subscriptions=new Subscriptions();
		try {
    		String criterio=Constantes.DESACTIVAFACURACIONDES;
    		
    		LOGGER.info(idTransaction+"INICIO  por : "+criterio);
    		
    		listaBono=motorPromDAO.obtenerCambioFacturacion(idTransaction);
    		if(Constantes.EXITO.equals(listaBono.getCodigoError())){
    			list=listaBono.getListabono();
    			LOGGER.info(idTransaction+"- datos obtenidos a desactivar "+list.size());
                String codigoExterno=Constantes.CADENAVACIA;
    			for(BonoProgram bonoPrograma:list ){
    				
    				if(null!=bonoPrograma.getContractidExterno() && !bonoPrograma.getContractidExterno().isEmpty()){
    					if(!codigoExterno.equals(bonoPrograma.getContractidExterno())){
    						subscriptions=retrieveSubscriptions(cadenaTrazabilidad, bonoPrograma.getContractidExterno(), idTransaction);
    					}
    					
        				if(null!=subscriptions.getCicloFacturacion()){
        					if(!subscriptions.getCicloFacturacion().equals(bonoPrograma.getCiclofacturacion())){				
            					LOGGER.info(idTransaction+"Bono a Desactivar : "+bonoPrograma.getProgramid());
            					ResponseAuditoria auditoria=new ResponseAuditoria();
            					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramid(),Constantes.DESACTIVAFACURACION);
            					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
            						LOGGER.info(idTransaction+"Exito  al desactivar Bono "+bonoPrograma.getProgramid());		        						
            					}else{
            						LOGGER.info(idTransaction+"Error  al desactivar Bono"+bonoPrograma.getProgramid());
            					}		
            				}
        				}else{
        					LOGGER.info(idTransaction+ "No xiste Bono a desactivar"+bonoPrograma.getProgramid());
        				}	
        				
    				}else{
    					LOGGER.info(idTransaction+" No Existe Contractid ID Externo :" +bonoPrograma.getProgramid());
    				}
    				codigoExterno=bonoPrograma.getContractidExterno();
    				if(null==codigoExterno){
    					codigoExterno=Constantes.CADENAVACIA;
    				}
    			}
    		}else{
    			LOGGER.info(idTransaction+"ERROR  DESACTIVACION :"+propertiesExternos.mensajeIdf2);
    		}
    		LOGGER.info(idTransaction+"FIN  POR : "+criterio);
    		
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

	public Subscriptions retrieveSubscriptions(String cadenaTrazabilidad, String contrato,String idTransaction) throws Exception{
		LOGGER.info(cadenaTrazabilidad + " ======= Inicia Invocacion  INT-COP-0223 ======");
		Subscriptions reponse=new Subscriptions();
		RetrieveSubscriptionsResponseType response0223= new RetrieveSubscriptionsResponseType();
		RetrieveSubscriptionsRequestType request=new  RetrieveSubscriptionsRequestType();
		
		try{
			Agreement agreement=new Agreement();
			agreement.setID(contrato);
			AgreementItem  agreementItem=new AgreementItem();
			agreementItem.setAgreement(agreement);
			PartyRoleProductOffering partyRoleProductOffering=new PartyRoleProductOffering();	
			partyRoleProductOffering.getAgreementItem().add(agreementItem);
			PartyRole partyRole=new PartyRole();
			partyRole.getPartyRoleProductOffering().add(partyRoleProductOffering);
			CustomerAccount customerAcount=new CustomerAccount();
			customerAcount.getPartyRole().add(partyRole);
			
			Customer customer=new Customer();
			customer.getCustomerAccount().add(customerAcount);
			
			request.setCustomer(customer);
			response0223=iNTCOP0223Client.retrieveSubscriptionsResponse(cadenaTrazabilidad, request, propertiesExternos.usuarioAppBssAgreementManagement
					, propertiesExternos.nombreAppBssAgreementManagement, idTransaction, propertiesExternos.idApplicationBssAgreementManagement);
			
			if(Integer.parseInt(Constantes.EXITO)==response0223.getResponseStatus().getStatus()){
				for(int i=0;i<response0223.getResponseData().getSubscriptionsType().size();i++){
					
					reponse.setProducttype(response0223.getResponseData().getSubscriptionsType().get(i).getProductCategory().getName());
					//Ciclo de Facturacion
					for(int h=0;h<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().size();h++){
						
						for(int j=0;j<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
							.getCustomerBillSpec().size();j++){
							
							for(int n=0;n<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
							    .getCustomerBillSpec().get(j).getCustomerBillingCycle().size();n++){
								
								reponse.setCicloFacturacion(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
										.getCustomerBillSpec().get(j).getCustomerBillingCycle().get(n).getCustomerBillingCycleExtension().getBillingCycleDay());
							}
						}
					}
					
				}
			}
			
			LOGGER.info(cadenaTrazabilidad + " ======= FIN  Invocacion INT-COP-0223 ======");
		}catch(Exception e){
			LOGGER.info(cadenaTrazabilidad + " ======= Error Invocacion INT-COP-0223 ======"+e.getLocalizedMessage());
			throw new Exception("update exception");
		}
		return reponse;
	}

}