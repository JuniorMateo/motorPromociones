package claro.com.pe.desa.titularidad.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import claro.com.pe.desa.titularidad.bean.Bono;
import claro.com.pe.desa.titularidad.bean.Cliente;
import claro.com.pe.desa.titularidad.bean.ListaBono;
import claro.com.pe.desa.titularidad.bean.ResponseAuditoria;
import claro.com.pe.desa.titularidad.client.INTCAA0101Client;
import claro.com.pe.desa.titularidad.dao.MotorPromDAO;
import claro.com.pe.desa.titularidad.exception.DBException;
import claro.com.pe.desa.titularidad.util.Constantes;
import claro.com.pe.desa.titularidad.util.PropertiesExternos;
import pe.com.claro.esb.data.customer.customer.v2.CustomerAccount;
import pe.com.claro.esb.data.engagedparty.party.v2.PartyRole;
import pe.com.claro.esb.data.engagedparty.partyproductspecificationandoffering.v2.PartyRoleProductSpecification;
import pe.com.claro.esb.data.product.productspecification.v2.ProductSpecification;
import pe.com.claro.esb.message.bsscustomermanagement.getcustomerinfo.v2.GetCustomerInfoRequestType;
import pe.com.claro.esb.message.bsscustomermanagement.getcustomerinfo.v2.GetCustomerInfoResponseType;

@Service
public class MainServiceImpl implements MainService {

    private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);
	@Autowired
	MotorPromDAO motorPromDAO;

	@Autowired
	INTCAA0101Client iNTCAA0101Client;
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
   
	@Transactional(rollbackFor = Exception.class)
	@Override
    public void run(String idTransaction)  
    		throws Exception {
		int numeroIntento=0;
		String codigoRUCINT=Constantes.EXITO;
		String codigoTITULARIDAINT=Constantes.EXITO;
		LOGGER.info(idTransaction+"[INICIO - SH08_MOTPROM_ValidaDes_Titularidad ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}				
				
				if(Constantes.EXITO.equals(codigoTITULARIDAINT)){
					 LOGGER.info(idTransaction+" Inicia- "+Constantes.CAMBIOPLANDNIARUCDES);
				     codigoRUCINT=cambioplanTitularidad(idTransaction,Constantes.CAMBIOPLANDNIARUCDES,Constantes.CAMBIOPLANDNIARUCINT);	
				}
				
	
				if(Constantes.EXITO.equals(codigoRUCINT)){
					LOGGER.info(idTransaction+"Inicia-  "+Constantes.CAMBIOPLANTITULARIDADES);
					codigoTITULARIDAINT=cambioplanTitularidad(idTransaction,Constantes.CAMBIOPLANTITULARIDADES,Constantes.CAMBIOPLANTITULARIDAINT);
					
				}
				
			   if(Constantes.NOEXITO.equals(codigoRUCINT) || Constantes.NOEXITO.equals(codigoTITULARIDAINT)){	
				   numeroIntento++;
			   }else{
					break;  
			   }
			}

			if(Constantes.NOEXITO.equals(codigoRUCINT) ||Constantes.NOEXITO.equals(codigoTITULARIDAINT)){
				 throw new Exception("update exception");
			}	
			
		}catch(Exception e){
			 LOGGER.info(idTransaction+"Error al ejecutar Transaccion");
			 throw new Exception("update exception");
		}finally{
			LOGGER.info(idTransaction+"[Fin - SH08_MOTPROM_ValidaDes_Titularidad ]");
		}
    } 
    
    
	public  String  cambioplanTitularidad(String idTransaction,String Motivo,int tipoDesactivacion)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		List<Bono> listabono;
		ListaBono listass=new ListaBono();
    	Cliente cliente=new Cliente();
    	String nombreMetodo = "Descativar Bono Por :"+Motivo;
		String cadenaTrazabilidad = "[" + nombreMetodo + " idTx=" + idTransaction + "] ";
		String linea=Constantes.CADENAVACIA;
		
		try {
    		listass=motorPromDAO.obtenerBonosAdesactivarTitularidad(idTransaction,tipoDesactivacion);
    		if(Constantes.EXITO.equals(listass.getCodigoRespuesta())){
    			listabono=listass.getListabono();

    			for(Bono bonoPrograma:listabono ){
    				if(!linea.equals(bonoPrograma.getLinea())){
    					cliente=CustomerInfo(cadenaTrazabilidad,idTransaction,bonoPrograma.getLinea());
    				}else{
    					LOGGER.info(idTransaction+" Linea Se encuentra Cargada: "+bonoPrograma.getLinea());
    				}
    				
    				if(null!=cliente.getDni() && !cliente.getDni().isEmpty()){
    					  
    					//Cambio de DNI a RUC
    					if(!documentoDes(Integer.parseInt(cliente.getTipoDocument())).equals(bonoPrograma.getTipodocu())  
    					 &&  Constantes.CAMBIOPLANDNIARUCINT==tipoDesactivacion
    					 && propertiesExternos.DNI.equals(bonoPrograma.getTipodocu())
    					 && propertiesExternos.RUC.equals(documentoDes(Integer.parseInt(cliente.getTipoDocument() ) ) )){				
        					LOGGER.info(idTransaction+" Bono a Desactivar : "+bonoPrograma.getProgramid());
        					ResponseAuditoria auditoria=new ResponseAuditoria(); 
      
        					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramid(),Constantes.CAMBIOPLANDNIARUCDES);
        					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
        						LOGGER.info(idTransaction+" Exito  al desactivar Bono "+bonoPrograma.getProgramid());		        						
        					}else{
        						LOGGER.info(idTransaction+" Error  al desactivar Bono"+bonoPrograma.getProgramid());
        					}		
        				}else{
        					//Cambio de titularidad
        					if(!cliente.getDni().equals(bonoPrograma.getNumerodeDocumento())  
        	    					 &&  Constantes.CAMBIOPLANTITULARIDAINT==tipoDesactivacion){				
            					LOGGER.info(idTransaction+" Bono a Desactivar : "+bonoPrograma.getProgramid());
            					ResponseAuditoria auditoria=new ResponseAuditoria();
            					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramid(),Constantes.CAMBIOPLANTITULARIDADES);
            					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
            						LOGGER.info(idTransaction+" Exito  al desactivar Bono "+bonoPrograma.getProgramid());		        						
            					}else{
            						LOGGER.info(idTransaction+" Error  al desactivar Bono"+bonoPrograma.getProgramid());
            					}		
            				}else{
            					LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getProgramid()+" "+Motivo);
            				}
        				}
    				}else{
    					LOGGER.info(idTransaction+" No xiste Bono: "+Motivo);
    				}	
    				linea=bonoPrograma.getLinea();
    			}
    		}else{
    			LOGGER.info(idTransaction+" No xiste Bono: "+Motivo);
    		}
    		LOGGER.info(idTransaction+"FIN  POR : "+Motivo);
    		
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
	
	
    public  Cliente  CustomerInfo(String cadenaTrazabilidad,String idTransaction,String Linea) throws Exception{
    	
    	GetCustomerInfoRequestType request =new  GetCustomerInfoRequestType();;    	
    	GetCustomerInfoResponseType response=new GetCustomerInfoResponseType();
    	Cliente cliente =new Cliente();
    	
    	LOGGER.info(cadenaTrazabilidad + " ======= INICIO - INT-CAA-0101 ======");
		try{
			PartyRole  partyRole=new PartyRole();
			pe.com.claro.esb.data.service.service.v2.Service servicedata=new pe.com.claro.esb.data.service.service.v2.Service();
			servicedata.setID(Linea);
			PartyRoleProductSpecification partyRoleProductSpecification=new PartyRoleProductSpecification();
			ProductSpecification productSpecification=new ProductSpecification();
			productSpecification.setName(propertiesExternos.productSpecificationType);
			partyRoleProductSpecification.setProductSpecification(productSpecification);
			
			partyRole.getPartyRoleProductSpecification().add(partyRoleProductSpecification);
			partyRole.getService().add(servicedata);
			CustomerAccount  customerAccount=new CustomerAccount();
			
			customerAccount.getPartyRole().add(partyRole);
			request.setCustomerAccount(customerAccount);
				    	
	    	response=iNTCAA0101Client.getCustomerInfo(cadenaTrazabilidad, request, propertiesExternos.usuarioAplicacion0101
					, propertiesExternos.nombreAplicacion0101, idTransaction, propertiesExternos.idApplication0101);

	    	if(Integer.parseInt(Constantes.EXITO)==response.getResponseStatus().getStatus()){
	    			cliente.setDni(response.getResponseData().getCustomerAccount().getCustomer().getParty().getPartyId());
	    	    	cliente.setTipoDocument(response.getResponseData().getCustomerAccount().getCustomer().getParty().getPartyExtension().getIdentificationType());
	    	}
				
	    	
			LOGGER.info(cadenaTrazabilidad + " ======= FIN - INT-CAA-0101 ======");
			return  cliente;
		}catch(Exception e){
			LOGGER.info(cadenaTrazabilidad + " ======= Error  INT-CAA-0101 ======"+e.getLocalizedMessage());
			throw new Exception("update exception");
		}
    }
    
    public String documentoDes(int tipodoc){
    	String tipoDes=Constantes.CADENAVACIA;
    	switch(tipodoc){
    	case 1:tipoDes=propertiesExternos.DNI;break;
    	case 2:tipoDes=propertiesExternos.CARNETEXTRANJERIA;break;
    	case 3:tipoDes=propertiesExternos.RUC;break;
    	case 4:tipoDes=propertiesExternos.PASAPORTE;break;
    	default :tipoDes=Constantes.CADENAVACIA;break;
    	}
    	return tipoDes;
    }
}