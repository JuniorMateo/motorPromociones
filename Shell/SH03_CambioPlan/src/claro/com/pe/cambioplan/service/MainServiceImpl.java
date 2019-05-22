package claro.com.pe.cambioplan.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import claro.com.pe.cambioplan.bean.BonoProgram;
import claro.com.pe.cambioplan.bean.LisBonoProgram;
import claro.com.pe.cambioplan.bean.ProductOffer;
import claro.com.pe.cambioplan.bean.ResponseAuditoria;
import claro.com.pe.cambioplan.dao.MotorPromDAO;
import claro.com.pe.cambioplan.exception.DBException;
import claro.com.pe.cambioplan.exception.DBNoDisponibleException;
import claro.com.pe.cambioplan.exception.DBTimeoutException;
import claro.com.pe.cambioplan.util.Constantes;
import claro.com.pe.cambioplan.util.PropertiesExternos;
import pe.com.claro.esb.data.customer.customer.v2.CustomerAccount;
import pe.com.claro.esb.data.engagedparty.agreement.v2.Agreement;
import pe.com.claro.esb.data.engagedparty.agreement.v2.AgreementItem;
import pe.com.claro.esb.data.engagedparty.party.v2.PartyRole;
import pe.com.claro.esb.data.engagedparty.partyproductspecificationandoffering.v2.PartyRoleProductOffering;
import pe.com.claro.esb.data.engagedparty.partyproductspecificationandoffering.v2.PartyRoleProductSpecification;
import pe.com.claro.esb.data.product.productspecification.v2.ProductSpecification;
import pe.com.claro.esb.data.product.productspecification.v2.ProductSpecificationType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractRequestType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractResponseType;
import claro.com.pe.cambioplan.client.INTCAA0002Client;



@Service
public class MainServiceImpl implements MainService {

    private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);
	@Autowired
	MotorPromDAO  motorPromDAO;
	
	@Autowired
	INTCAA0002Client  iNTCAA0002Client;
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
    public void run(String idTransaction,String pi_fec_proc,String pi_dias)  
    		throws DBException,DBNoDisponibleException,DBTimeoutException, Exception {
		int numeroIntento=0;
		String codigoResp=Constantes.EXITO;
		int hito=Constantes.HITOUNO;
		LOGGER.info(idTransaction+"[INICIO - SH03_MOTPROM_ValidaDes_CambioPlan ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}
				if(Constantes.HITOUNO==hito){
			       LOGGER.info(idTransaction+"  CAMBIOPLANLINEA");
				   //Cambio de plan de la línea.   
				   codigoResp=cambioplanLinea(idTransaction, pi_fec_proc, pi_dias,Constantes.CAMBIOPLANLINEA);
				   if(Constantes.EXITO.equals(codigoResp)){
					   hito=Constantes.HITODOS;
				   }
				}
				
				if(Constantes.HITODOS==hito){
					LOGGER.info(idTransaction+"  CAMBIOPLANLINEADOWNGRADE");
				    //Cambio de plan Downgrade.   
				     codigoResp=cambioplanLinea(idTransaction, pi_fec_proc, pi_dias,Constantes.CAMBIOPLANLINEADOWNGRADE);
				   if(Constantes.EXITO.equals(codigoResp)){
				    hito=Constantes.HITOTRES;
				   }
			    }
				
				if(Constantes.HITOTRES==hito){
				  LOGGER.info(idTransaction+"  CAMBIOPLANLINEAUPGRADE");
				   //Cambio de plan UpGrade.   
				   codigoResp=cambioplanLinea(idTransaction, pi_fec_proc, pi_dias,Constantes.CAMBIOPLANLINEAUPGRADE);
				   if(Constantes.EXITO.equals(codigoResp)){
				    hito=Constantes.HITOCUATRO;
				   }
			    }
				
				if(Constantes.HITOCUATRO==hito){
				   LOGGER.info(idTransaction+"  CAMBIOPLANPREPAGO");
				   //Cambio de plan PREPAGO.   
				   codigoResp=cambioplanPrepago(idTransaction, pi_fec_proc, pi_dias);
				  if(Constantes.EXITO.equals(codigoResp)){
					    hito=Constantes.HITOCINCO;
					}
				 }

				if(Constantes.HITOCINCO==hito){
			      LOGGER.info(idTransaction+"  CAMBIOPLANLINEANOMATRIZ");
				  //Cambio de plan no existe en Matriz.   
				  codigoResp=cambioplanLineaNoMatriz(idTransaction, pi_fec_proc, pi_dias,Constantes.CAMBIOPLANLINEANOMATRIZ);
				   if(Constantes.EXITO.equals(codigoResp)){
				      hito=Constantes.HITOSEIS;
				    }
			     }
				
				if(Constantes.HITOSEIS==hito){
				  //Cambio de plan UPGRADE no existe en Matriz.
				  LOGGER.info(idTransaction+"  CAMBIOPLANLINEAUPGRADENOMATIZ");
				  codigoResp=cambioplanLineaNoMatriz(idTransaction, pi_fec_proc, pi_dias,Constantes.CAMBIOPLANLINEAUPGRADENOMATIZ);
			    }

			   if(Constantes.NOEXITO.equals(codigoResp)){	
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
			LOGGER.info(idTransaction+"[Fin - SH03_MOTPROM_ValidaDes_CambioPlan ]");
		}
    }   
	
	
	public  String  cambioplanLinea(String idTransaction,String pi_fec_proc,String pi_dias,int tipoDesactivacion)
			throws DBException,DBNoDisponibleException,DBTimeoutException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		LisBonoProgram listaBono=new  LisBonoProgram();
    	List<BonoProgram>  list=new ArrayList<BonoProgram>();
    	ProductOffer productOffer=new ProductOffer();
    	String nombreMetodo = "Descativar Bono Por Cambio Plan";
		String cadenaTrazabilidad = "[" + nombreMetodo + " idTx=" + idTransaction + "] ";
		String numeroLinea= Constantes.CADENAVACIA;
		String contractidExter= Constantes.CADENAVACIA;
		
		try {
    		String criterio=propertiesExternos.OBTENERCAMBIOPLANUPGRADE;
    		
    		switch(tipoDesactivacion){
			case 2:criterio = Constantes.CAMBIOPLANLINEAUPGRADEDES;break;
			case 4:criterio = Constantes.CAMBIOPLANLINEADES;break;
			case 5:criterio = Constantes.CAMBIOPLANLINEADOWNGRADEDES;break;	
			}
    		
    		LOGGER.info(idTransaction+"INICIO  por : "+criterio);
    		
    		listaBono=motorPromDAO.obtenerCambioPlan(idTransaction,pi_fec_proc,pi_dias,tipoDesactivacion);
    		if(Constantes.EXITO.equals(listaBono.getCodigoError())){
    			list=listaBono.getListabono();
    			LOGGER.info(idTransaction+"- datos obtenidos a desactivar "+list.size());
    			for(BonoProgram bonoPrograma:list ){
    				
    				if( (null!=bonoPrograma.getContractidExterno() && !bonoPrograma.getContractidExterno().isEmpty())
    					  || (null!=bonoPrograma.getDnnum() && !bonoPrograma.getDnnum().isEmpty()) ){
    					
    					  if(!numeroLinea.equals( bonoPrograma.getDnnum()) && !contractidExter.equals(bonoPrograma.getContractidExterno())){
    						  productOffer=buscarPlanBono(cadenaTrazabilidad, bonoPrograma.getDnnum(), bonoPrograma.getContractidExterno(), propertiesExternos.name1, idTransaction);
    					  }else{
    						  LOGGER.info(idTransaction+" ContractidExterno - Linea  : "+ bonoPrograma.getContractidExterno()+"-"+ bonoPrograma.getDnnum()+"  cargados");
    					  }	
	        				if(null!=productOffer.getId()){
	        					if(!productOffer.getId().equals(bonoPrograma.getPobasic())  
	        					 &&  Constantes.CAMBIOPLANLINEA==tipoDesactivacion){				
	            					LOGGER.info(idTransaction+"Bono a Desactivar : "+bonoPrograma.getBonoid());
	            					ResponseAuditoria auditoria=new ResponseAuditoria();
	            					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramid(),Constantes.CAMBIOPLANLINEADES);
	            					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
	            						LOGGER.info(idTransaction+"Exito  al Procesar Bono "+bonoPrograma.getBonoid()+" Por "+Constantes.CAMBIOPLANLINEADES);		        						
	            					}else{
	            						LOGGER.info(idTransaction+"Error  al Procesar Bono"+bonoPrograma.getBonoid()+" Por "+Constantes.CAMBIOPLANLINEADES);
	            					}		
	            				}else{
	            					if(!productOffer.getId().equals(bonoPrograma.getPobasic())  
	            						&&  Constantes.CAMBIOPLANLINEADOWNGRADE==tipoDesactivacion
	            						&&	productOffer.getAmount().compareTo(bonoPrograma.getAmount())<0){				
	                					LOGGER.info(idTransaction+"Bono a Desactivar : "+bonoPrograma.getBonoid());
	                					ResponseAuditoria auditoria=new ResponseAuditoria();
	                					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramid(),Constantes.CAMBIOPLANLINEADOWNGRADEDES);
	                					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
	                						LOGGER.info(idTransaction+"Exito al Procesar Bono "+bonoPrograma.getBonoid()+" Por "+Constantes.CAMBIOPLANLINEADOWNGRADEDES);		        						
	                					}else{
	                						LOGGER.info(idTransaction+"Error al Procesar Bono "+bonoPrograma.getBonoid()+" Por "+Constantes.CAMBIOPLANLINEADOWNGRADEDES);
	                					}		
	                				}else{
	                					  
	                					if(!productOffer.getId().equals(bonoPrograma.getPobasic())  
	                    						&&  Constantes.CAMBIOPLANLINEAUPGRADE==tipoDesactivacion
	                    						&&	productOffer.getAmount().compareTo(bonoPrograma.getAmount())>0){				
	                        					LOGGER.info("Bono a Desactivar : "+bonoPrograma.getBonoid());
	                        					ResponseAuditoria auditoria=new ResponseAuditoria();
	                        					auditoria=motorPromDAO.actualizarBono(cadenaTrazabilidad, bonoPrograma.getProgramid(),Constantes.CAMBIOPLANLINEAUPGRADEDES);
	                        					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
	                        						LOGGER.info(idTransaction+"Exito  al Procesar Bono "+bonoPrograma.getBonoid()+" Por "+Constantes.CAMBIOPLANLINEAUPGRADEDES);		        						
	                        					}else{
	                        						LOGGER.info(idTransaction+"Error al Procesar Bono "+bonoPrograma.getBonoid()+" Por "+Constantes.CAMBIOPLANLINEAUPGRADEDES);
	                        					}		
	                        			 }else{
	                        				 LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getBonoid() +" con   Contractid :"+bonoPrograma.getContractid());
	                        			}
	                				}
	            				}
	        				}else{
	        					LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getBonoid() +" con   Contractid :"+bonoPrograma.getContractid());
	        				}
	        				numeroLinea=bonoPrograma.getDnnum();
	        				contractidExter=bonoPrograma.getContractidExterno();
	        				
    				}else{
    					LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getBonoid() +" con   Contractid :"+bonoPrograma.getContractid());
    				}
    			}
    		}else{
    			LOGGER.info(idTransaction+"ERROR  DESACTIVACION :"+propertiesExternos.mensajeIdf2);
    		}
    		LOGGER.info(idTransaction+"FIN  POR : "+criterio);
    		
		} catch(DBException e){
			LOGGER.error(idTransaction + "- Error en la BD "+ e.getNombreBD() + " - " +e.getNombreSP()+". "+ e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;
		} catch (Exception e) {
			LOGGER.error(idTransaction + "Error Interno: " + e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;	
		}
    	finally{
			LOGGER.info(idTransaction+"[Fin de metodo: run]");
		}
		//FIN - Ejecutar Proceso  Shell 
    	
		return codigoResp;
	}
	
	
	
	public  String  cambioplanLineaNoMatriz(String idTransaction,String pi_fec_proc,String pi_dias,int tipoDesactivacion)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		LisBonoProgram listaBono=new  LisBonoProgram();
    	List<BonoProgram>  list=new ArrayList<BonoProgram>();
    	ProductOffer productOffer=new ProductOffer();
    	String nombreMetodo = "Desactivar Bono Por Cambio Plan";
		String cadenaTrazabilidad = "[" + nombreMetodo + " idTx=" + idTransaction + "] ";
		String numeroLinea= Constantes.CADENAVACIA;
		String contractidExter= Constantes.CADENAVACIA;
		try {
			if(Constantes.CAMBIOPLANLINEANOMATRIZ==tipoDesactivacion){
				LOGGER.info(idTransaction+"- INICIO 1 :DESACTIVACION  Obtener  cambio Plan Linea No Matriz ");
			}else{
				LOGGER.info(idTransaction+"- INICIO 1 :DESACTIVACION  Obtener  cambio Plan Linea Upgrade No Matriz ");
			}
    		
    		
    		listaBono=motorPromDAO.CambioPlanNoMatriz(idTransaction,pi_fec_proc,pi_dias,tipoDesactivacion);
    		if(Constantes.EXITO.equals(listaBono.getCodigoError())){
    			list=listaBono.getListabono();
    			LOGGER.info("- datos obtenidos a desactivar "+list.size());

    			for(BonoProgram bonoPrograma:list ){
    				if( (null!=bonoPrograma.getContractidExterno() && !bonoPrograma.getContractidExterno().isEmpty())
        					  || (null!=bonoPrograma.getDnnum() && !bonoPrograma.getDnnum().isEmpty()) ){
        					
        					  if(!numeroLinea.equals( bonoPrograma.getDnnum()) && !contractidExter.equals(bonoPrograma.getContractidExterno())){
        						  productOffer=buscarPlanBono(cadenaTrazabilidad, bonoPrograma.getDnnum(), bonoPrograma.getContractidExterno(), propertiesExternos.name1, idTransaction);
        					  }else{
        						  LOGGER.info(idTransaction+" ContractidExterno - Linea  : "+ bonoPrograma.getContractidExterno()+"-"+ bonoPrograma.getDnnum()+"  cargados");
        					  }	
        	    			  if(null!=productOffer.getId()){
        	    					if(!productOffer.getId().equals(bonoPrograma.getPobasic())  &&  Constantes.CAMBIOPLANLINEANOMATRIZ==tipoDesactivacion){				
        	        					ResponseAuditoria auditoria=new ResponseAuditoria();
        	        					auditoria=motorPromDAO.actualizarCambioPlanNoMatriz(cadenaTrazabilidad, bonoPrograma.getProgramid(),
        	        							bonoPrograma.getCampania(),productOffer.getId(),Constantes.CAMBIOPLANLINEANOMATRIZDES);
        	        					if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
        	        						if(Constantes.EXITOMENSAJE.equals(auditoria.getMensajeRespuesta())){
        	        							LOGGER.info(idTransaction+"Exito  al desactivar Bono : "+bonoPrograma.getBonoid());	
        	        						}else{
        	        							LOGGER.info(idTransaction+"Plan del Bono: "+bonoPrograma.getBonoid()+" Se encuentra  en la matriz de planes");
        	        						}
        	        							        						
        	        					}else{
        	        						LOGGER.info(idTransaction+"Error  al desactivar Bono"+bonoPrograma.getBonoid());
        	        					}		
        	        				}else{
        	        					if(!productOffer.getId().equals(bonoPrograma.getPobasic())  
        	        							&&  Constantes.CAMBIOPLANLINEAUPGRADENOMATIZ==tipoDesactivacion
        	        							&&	productOffer.getAmount().compareTo(bonoPrograma.getAmount())>0){				
        	            					    ResponseAuditoria auditoria=new ResponseAuditoria();
        	            					    auditoria=motorPromDAO.actualizarCambioPlanNoMatriz(cadenaTrazabilidad, bonoPrograma.getProgramid(),
        	            							bonoPrograma.getCampania(),productOffer.getId(),Constantes.CAMBIOPLANLINEAUPGRADENOMATIZDES);
        	            					  if(Constantes.EXITO.equals(auditoria.getCodigoRespuesta())){
        	            						if(Constantes.EXITOMENSAJE.equals(auditoria.getMensajeRespuesta())){
        	            							LOGGER.info(idTransaction+"Exito  al desactivar Bono : "+bonoPrograma.getBonoid());	
        	            						}else{
        	            							LOGGER.info(idTransaction+"Plan del Bono: "+bonoPrograma.getBonoid()+" Se encuentra  en la matriz de planes");
        	            						}
        	            							        						
        	            					}else{
        	            						LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getBonoid() +" con   Contractid :"+bonoPrograma.getContractid());
        	            					}		
        	            				}else{
        	            					LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getBonoid() +" con   Contractid :"+bonoPrograma.getContractid());
        	            				}
        	        				}
        	    				}else{
        	    					LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getBonoid() +" con   Contractid :"+bonoPrograma.getContractid());
        	    				}
        					numeroLinea=bonoPrograma.getDnnum();
              				contractidExter=bonoPrograma.getContractidExterno();		
    				}else{
    					LOGGER.info(idTransaction+" No Existe Bono: "+bonoPrograma.getBonoid() +" con   Contractid :"+bonoPrograma.getContractid());
    				}
    					
    			}
    		}else{
    			LOGGER.info(idTransaction+"ERROR  :DESACTIVACION  Obtener  cambio Plan "+propertiesExternos.mensajeIdf2);
    		}
    		if(Constantes.CAMBIOPLANLINEANOMATRIZ==tipoDesactivacion){
				LOGGER.info(idTransaction+"- FIN 1 :DESACTIVACION  Obtener  cambio Plan Linea No Matriz ");
			}else{
				LOGGER.info(idTransaction+"- FIN 1 :DESACTIVACION  Obtener  cambio Plan Linea Upgrade No Matriz ");
			}
    		
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
	
	
    public  ProductOffer buscarPlanBono(String cadenaTrazabilidad,String linea, String contrato,String nombre,String idTransaction)
    throws Exception{
		LOGGER.info(cadenaTrazabilidad + " ======= Inicia Invocacion  INT-CAA-0002 ======");
		
		ProductOffer productOffer=new ProductOffer();
		GetProductsOfferingPerContractResponseType respponseca002;
		
		GetProductsOfferingPerContractRequestType getProductsOfferingPerContractRequestType =new  GetProductsOfferingPerContractRequestType();
		try{
			CustomerAccount custo=new  CustomerAccount();
			PartyRole party=new PartyRole();
			pe.com.claro.esb.data.service.service.v2.Service servic=new pe.com.claro.esb.data.service.service.v2.Service();
			servic.setID(linea);
			
			PartyRoleProductOffering  partyRoleProductOffering=new PartyRoleProductOffering();
			AgreementItem agreementItem=new AgreementItem();
			Agreement  agreement=new Agreement();
			agreement.setID(contrato);
			
			InetAddress address = InetAddress.getLocalHost();
			
			PartyRoleProductSpecification partyRoleProductSpecification=new PartyRoleProductSpecification();
			ProductSpecification productSpecification=new ProductSpecification();
			
			ProductSpecificationType productSpecificationType=new ProductSpecificationType();
			productSpecificationType.setName(nombre);
			
			productSpecification.getProductSpecificationType().add(productSpecificationType);
			partyRoleProductSpecification.setProductSpecification(productSpecification);
			agreementItem.setAgreement(agreement);
			partyRoleProductOffering.getAgreementItem().add(agreementItem);
			party.getPartyRoleProductOffering().add(partyRoleProductOffering);
			party.getService().add(servic);
			party.getPartyRoleProductSpecification().add(partyRoleProductSpecification);
			custo.getPartyRole().add(party);
			getProductsOfferingPerContractRequestType.setCustomerAccount(custo);
			respponseca002=iNTCAA0002Client.GetProductsOfferingPerContractResponse(cadenaTrazabilidad, getProductsOfferingPerContractRequestType, 
					propertiesExternos.usuarioAplicacion,propertiesExternos.nombreAplicacion, idTransaction, address.getHostAddress());
            boolean conta=false;
            boolean conta2=false;

			if(Integer.parseInt(Constantes.EXITO)==respponseca002.getResponseStatus().getStatus()){
				LOGGER.info(cadenaTrazabilidad + " ======= SIZE LIST======" + respponseca002.getResponseData().getProductListType().size());
				for(int i=0;i<respponseca002.getResponseData().getProductListType().size();i++){
					
					for(int h=0;h<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().getPartyRole().size();h++){
						
						for(int j=0;j<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
								getPartyRole().get(h).getPartyRoleProductOffering().size();j++){
							
							for( int m=0;m<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
							    getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().size();m++){
								
								for(int g=0;g<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
							       getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getPartyProfileType().size();g++){
									
									if(propertiesExternos.nombreB.equals(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
											getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getPartyProfileType().get(g).getName())){
										
										LOGGER.info(cadenaTrazabilidad + " =======Ubicado  productOfferingType : ======" + respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getPartyProfileType().get(g).getName());
										

										LOGGER.info(cadenaTrazabilidad + " =======Ubicado Plan : ======" + respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getID());
										
										productOffer.setId(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getID());
									
										productOffer.setDescription(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getDescription());
										
										productOffer.setName(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getName());
										
										productOffer.setStartDateTime(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getValidFor().getStartDateTime());
										
										productOffer.setPartyProfileTypeName(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getPartyProfileType().get(g).getName());
										
										productOffer.setProductProductStatus(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getPartyRoleProductOffering().get(j).getAgreementItem().get(m).getProductOffering().getProduct().get(g).getProductStatus());
										conta=true;
										break;
									}
								} 
								
								if(conta){
									break;
								}		
							}
							if(conta){
								break;
							}
						}

						if(conta){
							for(int q=0;q<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
									getPartyRole().get(h).getProductOffering().size();q++){
								
								for(int a=0;a<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
										getPartyRole().get(h).getProductOffering().get(q).getProduct().size();a++){
									
									for(int b=0;b<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
											getPartyRole().get(h).getProductOffering().get(q).getProduct().get(a).getProductPrice().size();b++){									
										for(int t=0;t<respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
												getPartyRole().get(h).getProductOffering().get(q).getProduct().get(a).getProductPrice().get(b).getProductOfferingPrice()
												.getBusinessInteractionItemPrice().size();t++){		
											
											LOGGER.info(cadenaTrazabilidad + " =======Ubicado Unidad : ======" + respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
													getPartyRole().get(h).getProductOffering().get(q).getProduct().get(a).getProductPrice().get(b).getProductOfferingPrice()
													.getBusinessInteractionItemPrice().get(t).getPrice().getUnits());
											
											productOffer.setUnits(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
													getPartyRole().get(h).getProductOffering().get(q).getProduct().get(a).getProductPrice().get(b).getProductOfferingPrice()
													.getBusinessInteractionItemPrice().get(t).getPrice().getUnits());
											
											LOGGER.info(cadenaTrazabilidad + " =======Ubicado Monto : ======" +respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
													getPartyRole().get(h).getProductOffering().get(q).getProduct().get(a).getProductPrice().get(b).getProductOfferingPrice()
													.getBusinessInteractionItemPrice().get(t).getPrice().getAmount());
											
											productOffer.setAmount(respponseca002.getResponseData().getProductListType().get(i).getCustomerAccount().
													getPartyRole().get(h).getProductOffering().get(q).getProduct().get(a).getProductPrice().get(b).getProductOfferingPrice()
													.getBusinessInteractionItemPrice().get(t).getPrice().getAmount());
											conta2=true;
											
										}
										if(conta2){
											break;
										}	
										
									}
									if(conta2){
										break;
									}	
									
								}
								if(conta2){
									break;
								}	
								
							}	
						}
						
						if(conta && conta2){
							break;
						}
					}
					if(conta && conta2){
						break;
					}
				}
			}
			
			LOGGER.info(cadenaTrazabilidad + " ======= FIN  Prueba INT-CAA-0002 ======");
		}catch(Exception e){
			LOGGER.info(cadenaTrazabilidad + " ======= Error  Prueba INT-CAA-0002 ======"+e.getMessage());
			throw new Exception("update exception");
		}
		
		return productOffer;
    }
    
	public  String  cambioplanPrepago(String idTransaction,String pi_fec_proc,String pi_dias)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell -  Descativar Bono  Cambio Plan prepago
		String codigoResp=Constantes.EXITO;
		ResponseAuditoria response=new ResponseAuditoria();
    	String nombreMetodo = "Descativar Bono  Cambio Plan prepago";
		String cadenaTrazabilidad = "[" + nombreMetodo + " idTx=" + idTransaction + "] ";
		
		try {
    		LOGGER.info(idTransaction+"- INICIO 1 :DESACTIVACION  cambio Plan  Prepago");
    		response=motorPromDAO.ejecutarCambioPlanPrepago(cadenaTrazabilidad, pi_fec_proc, pi_dias);
    		if(Constantes.EXITO.equals(response.getCodigoRespuesta())){
    			LOGGER.info(idTransaction+"- datos obtenidos a desactivar ");

    		}else{
    			LOGGER.info(idTransaction+"ERROR  :DESACTIVACION  Obtener  cambio Plan "+propertiesExternos.mensajeIdf2);
    		}

    		LOGGER.info(idTransaction+"- FIN 1 :DESACTIVACION  cambio Plan  Prepago ");
    		LOGGER.info("------------------------------------------------");
    		
		} catch(DBException e){
			LOGGER.error(idTransaction + "- Error en la BD "+ e.getNombreBD() + " - " +e.getNombreSP()+". "+ e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;
		}
		catch (Exception e) {
			LOGGER.error(idTransaction + "Error Interno: " + e.getMessage(),e);
			codigoResp=Constantes.NOEXITO;	
		}
    	finally{
			LOGGER.info(idTransaction+"[Fin de metodo:]");
		}
		//FIN - Ejecutar Proceso  Shell-  Descativar Bono  Cambio Plan prepago
    	
		return codigoResp;
	}
    
    
}