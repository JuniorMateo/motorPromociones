package claro.com.pe.evalua.bono.recalculo.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import claro.com.pe.evalua.bono.recalculo.bean.BonoProgram;
import claro.com.pe.evalua.bono.recalculo.bean.BonoRecalcula;
import claro.com.pe.evalua.bono.recalculo.bean.BonoRecalculo;
import claro.com.pe.evalua.bono.recalculo.bean.LisBonoProgram;
import claro.com.pe.evalua.bono.recalculo.bean.ListMaxFamilia;
import claro.com.pe.evalua.bono.recalculo.bean.ListaBonoRecalculo;
import claro.com.pe.evalua.bono.recalculo.bean.ProductOffer;
import claro.com.pe.evalua.bono.recalculo.bean.ResponseAuditoria;
import claro.com.pe.evalua.bono.recalculo.bean.RetrieveSubscriptions;
import claro.com.pe.evalua.bono.recalculo.bean.Subscriptions;
import claro.com.pe.evalua.bono.recalculo.client.INTCAA0002Client;
import claro.com.pe.evalua.bono.recalculo.client.INTCOP0223Client;
import claro.com.pe.evalua.bono.recalculo.dao.MotorPromDAO;
import claro.com.pe.evalua.bono.recalculo.exception.DBException;
import claro.com.pe.evalua.bono.recalculo.util.Constantes;
import claro.com.pe.evalua.bono.recalculo.util.PropertiesExternos;
import claro.com.pe.evalua.bono.recalculo.util.Util;
import pe.com.claro.esb.data.commonbusinessentities.usersandroles.v2.PartyUser;
import pe.com.claro.esb.data.customer.customer.v2.Customer;
import pe.com.claro.esb.data.customer.customer.v2.CustomerAccount;
import pe.com.claro.esb.data.engagedparty.agreement.v2.Agreement;
import pe.com.claro.esb.data.engagedparty.agreement.v2.AgreementItem;
import pe.com.claro.esb.data.engagedparty.engagedpartyextension.v2.PartyUserExtension;
import pe.com.claro.esb.data.engagedparty.party.v2.Party;
import pe.com.claro.esb.data.engagedparty.party.v2.PartyRole;
import pe.com.claro.esb.data.engagedparty.partyproductspecificationandoffering.v2.PartyRoleProductOffering;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsRequestType;
import pe.com.claro.esb.message.bssagreementmanagement.retrievesubscriptions.v2.RetrieveSubscriptionsResponseType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractRequestType;
import pe.com.claro.esb.message.bssproductordering.getproductsofferingpercontract.v2.GetProductsOfferingPerContractResponseType;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MainServiceImpl implements MainService {

    private final Logger LOGGER = Logger.getLogger(MainServiceImpl.class);

    @Autowired
	PropertiesExternos propertiesExternos;
    
    @Autowired
	INTCAA0002Client iNTCAA0002Client;
    
    @Autowired
	INTCOP0223Client iNTCOP0223Client;
    
    @Autowired
    MotorPromDAO motorDao;
	
    @Transactional(rollbackFor = Exception.class)
	@Override
    public void runRecalcular(String idTransaction)  
    		throws Exception {
		int numeroIntento=0;
		String codigoResp=Constantes.EXITO;
		LOGGER.info(idTransaction+"[INICIO - SH06_MOTPROM_EvaluaBono_ReCalculo ]");
		try{
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintento)){

				if(numeroIntento>0){
				  LOGGER.info(idTransaction+"[se procede  al Reintento :]"+numeroIntento);
				}				

				//Carga Dtaos Replica Cambio Plan 
				codigoResp=reCalcularEvaluacionSP( idTransaction);
			
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
			LOGGER.info(idTransaction+"[Fin - SH06_MOTPROM_EvaluaBono_ReCalculo ]");
		}
    } 
  
    public  String  reCalcularEvaluacionSP(String transaccion)
			throws DBException,Exception{
		
    	//INICIO - Ejecutar Proceso  Shell 
		String codigoResp=Constantes.EXITO;
		LisBonoProgram listaBono=new  LisBonoProgram();
		RetrieveSubscriptions retrieveSubscriptions=new RetrieveSubscriptions();
		RetrieveSubscriptions retrieveSubscript=new RetrieveSubscriptions();
		try {
    		String criterio=Constantes.CARGACAMBIOPLAN;
    		LOGGER.info(transaccion+"INICIO  : "+criterio);

    		LOGGER.info(transaccion+Constantes.BSCSCARGA);
			listaBono=motorDao.obtenerRecalcularEvaluacion(transaccion);
			String cadenaTrazabilidad=" datos BSCS obtenidos a Recalcular ";
    		if(Constantes.EXITO.equalsIgnoreCase(listaBono.getCodigoError()) &&listaBono.getListabono().size()>0){			
    			//Inicia Iteraccion    			
    			
    			LOGGER.info(transaccion+" SIZE INICIO LIST :"+listaBono.getListabono().size());
    			for(int i=0;i<listaBono.getListabono().size();i++){
    				boolean exiteUno=true;
    				BonoProgram bonosFor=new BonoProgram();
    				bonosFor=listaBono.getListabono().get(i);
    				
    				LOGGER.info(transaccion+ " ************ ITERACCION ******** :"+i);
    				for(int j=0;j<listaBono.getListabono().size();j++){
    					BonoProgram bonosFor2=new BonoProgram();
    					bonosFor2=listaBono.getListabono().get(j);
    					
    					LOGGER.info(transaccion+" ---------------------------------------------:"+j);
	    				  if(null!=bonosFor.getDocumentoNum() && !bonosFor.getDocumentoNum().isEmpty()
	    					 && null!=bonosFor2.getDocumentoNum() && !bonosFor2.getDocumentoNum().isEmpty()
	    					 &&!bonosFor2.getLinea().isEmpty() && null!=bonosFor2.getLinea()
	    					 &&!bonosFor.getLinea().isEmpty() && null!=bonosFor.getLinea()
	    					 && null!= bonosFor.getCargoFijo()
	    					 && null!= bonosFor2.getCargoFijo()){
	    					  
	    					  if(bonosFor.getDocumentoNum().equalsIgnoreCase(bonosFor2.getDocumentoNum())
	    	    					  && !bonosFor.getLinea().equals(bonosFor2.getLinea())){
	    						  
	    						//Inicio - validar  cambio Plan
    	    					String poBasic=Constantes.CONSTANTE_VACIA;
    	    					String poBasic2=Constantes.CONSTANTE_VACIA;
	    		    			retrieveSubscript=(RetrieveSubscriptions)reintento(Constantes.CUATRO,cadenaTrazabilidad,transaccion,null,null,null,bonosFor.getContractidExt());
	    		    			if(Constantes.EXITO.equalsIgnoreCase(retrieveSubscript.getStatus())){
	    		    				for(Subscriptions sub:retrieveSubscript.getListaSubscriptions()){
		    		    				poBasic=sub.getPoidbase();
		    		    				LOGGER.info(transaccion+" PLan 1:"+poBasic+ " Contrato 1:"+sub.getContractIdBase());
		    		    			} 
	    		    			}
	    		    			retrieveSubscript=(RetrieveSubscriptions)reintento(Constantes.CUATRO,cadenaTrazabilidad,transaccion,null,null,null,bonosFor2.getContractidExt());
	    		    			if(Constantes.EXITO.equalsIgnoreCase(retrieveSubscript.getStatus())){
	    		    				for(Subscriptions sub:retrieveSubscript.getListaSubscriptions()){
		    		    				poBasic2=sub.getPoidbase();
		    		    				LOGGER.info(transaccion+" PLan 2:"+poBasic2+ " Contrato 2:"+sub.getContractIdBase());
		    		    			}
	    		    			}
	    		    			if(bonosFor.getPoBasic().equalsIgnoreCase(poBasic) 
	    		    			  && bonosFor2.getPoBasic().equalsIgnoreCase(poBasic2)){
	    		    				exiteUno=false;
	    		    				break;
	    		    			}
	    		    			//Fin - validar  cambio Plan
	    		    			
	    						LOGGER.info(transaccion+" Procesar  Recalculo Con Numero de Documento :"+bonosFor2.getDocumentoNum());
	    						LOGGER.info(transaccion+" Linea Encontra con mismo Documento 1:"+bonosFor.getLinea());
	    						LOGGER.info(transaccion+" Linea Encontra con mismo Documento 2:"+bonosFor2.getLinea());
	    					   
	    						retrieveSubscriptions=(RetrieveSubscriptions)reintento(Constantes.UNO,cadenaTrazabilidad,transaccion,bonosFor.getDocumentoNum(),
	    								bonosFor.getDocType(),null,null);
	    						List<Subscriptions> listFin=new ArrayList<Subscriptions>(); 
	    						ListMaxFamilia listaMaxfam=new ListMaxFamilia();
	    						if(Constantes.CERO.equalsIgnoreCase(retrieveSubscriptions.getCodeResponse())){
	    							listFin= obtenerListNumero( retrieveSubscriptions.getListaSubscriptions(), cadenaTrazabilidad,  transaccion); 
	    							if(listFin.size()>0){
		    							listaMaxfam=motorDao.getObtenerRecalculo(transaccion, listFin, 2);
		    							if(Constantes.CERO.equalsIgnoreCase(listaMaxfam.getCodigoError())){		    								
		    								//Cargar Lista
		    								List<BonoProgram> listNueva=new ArrayList<BonoProgram>();
		    								listNueva.add(bonosFor);
		    								listNueva.add(bonosFor2);
		    								for( BonoProgram max:listaMaxfam.getListaMax()){
		    									listNueva.add(reCargarBono(bonosFor, max,Constantes.DOSINT));	
		    	    						}		    						
		    								
		    								//Encontrar Menor 1
		    								BonoProgram	menor1=reCargarBono(bonosFor, null, Constantes.UNOINT);
		    								int cont=0;
		    								for( int g=0;g<listNueva.size();g++){
		    									BonoProgram men1=listNueva.get(g);
		    									if(menor1.getCargoFijo().compareTo(men1.getCargoFijo())>=0){
		    									    menor1=reCargarBono( men1,null,Constantes.UNOINT);
		    									    cont=g;
		    										LOGGER.info(transaccion+" Menor I:"+cont +" "+men1.getContractidExt()+" Cargo Fijo  "+men1.getCargoFijo());
		    									}
		    	    						}
		    								LOGGER.info(transaccion+" Eliminar de lista :"+cont+"  : "+menor1.getProgramid());
		    								listNueva.remove(cont);    								
		    								
		    								//Encontrar Menor 2
		    								BonoProgram menor2=reCargarBono(bonosFor2, null, Constantes.UNOINT);
		    								for( BonoProgram men2:listNueva){
		    									if(menor2.getCargoFijo().compareTo(men2.getCargoFijo())>=0){
		    										menor2=reCargarBono( men2,null,Constantes.UNOINT);
		    										LOGGER.info(transaccion+" Menor II:"+cont +" "+men2.getContractidExt()+" Cargo Fijo  "+men2.getCargoFijo());
		    									}
		    	    						}
		    								LOGGER.info(transaccion+" Menor II :"+menor2.getContractidExt());	    								
		    								//Evaluar Recalculo 1-2
		    								if(bonosFor.getCargoFijo().compareTo(menor1.getCargoFijo())!=0){
		    									if(bonosFor2.getCargoFijo().compareTo(menor2.getCargoFijo())!=0){
		    	
	    										    if(bonosFor.getCargoFijo().compareTo(menor1.getCargoFijo())>0
	    										        && !bonosFor.getBillingAccount().equals(menor1.getBillingAccount())){		          	    								
			    										LOGGER.info(transaccion+" Linea  A Activar:"+menor1.getLinea());
			    										ResponseAuditoria auditoria2=new ResponseAuditoria();
		          	    								auditoria2=registrarNuevoBono(cadenaTrazabilidad, menor1);
		          	    								if(Constantes.CERO.equals(auditoria2.getCodigoRespuesta())){
		          	    									LOGGER.info(transaccion+" Exito al Insertar Bono:"+menor1.getProgramid());
		          	    									
		    												//Desactivar Bono
				    										LOGGER.info(transaccion+" Linea  A Desactivar:"+bonosFor.getLinea());
				    										ResponseAuditoria auditoria=new ResponseAuditoria();
			          	    							    auditoria=motorDao.actualizarBono(cadenaTrazabilidad, bonosFor.getProgramid(), Constantes.OBSERVACION);
			          	    								if(Constantes.CERO.equals(auditoria.getCodigoRespuesta())){
			          	    									LOGGER.info(transaccion+" Exito al Desactivar Bono:"+bonosFor.getProgramid());
			          	    								}else{
			          	    									LOGGER.info(transaccion+" ERROR al Desactivar Bono:"+bonosFor.getProgramid());
			          	    								}
		          	    								}
				    								}else{
				    									LOGGER.info(transaccion+" Sin Accion:"+bonosFor.getLinea());
				    									LOGGER.info(transaccion+" Sin Accion:"+menor1.getLinea());
				    								}
	    											
	    											if(bonosFor2.getCargoFijo().compareTo(menor2.getCargoFijo())>0
	    												&& !bonosFor2.getBillingAccount().equals(menor2.getBillingAccount())){		          	    								
			    										LOGGER.info(transaccion+" Linea  A Activar:"+menor2.getLinea());
			    										ResponseAuditoria auditoria2=new ResponseAuditoria();
		          	    								auditoria2=registrarNuevoBono(cadenaTrazabilidad, menor2);
		          	    								if(Constantes.CERO.equals(auditoria2.getCodigoRespuesta())){
		          	    									LOGGER.info(transaccion+" Exito al Insertar Bono:"+menor2.getProgramid());
		          	    									
		    												//Desactivar Bono
				    										LOGGER.info(transaccion+" Linea  A Desactivar:"+bonosFor2.getLinea());
				    										ResponseAuditoria auditoria=new ResponseAuditoria();
			          	    							    auditoria=motorDao.actualizarBono(cadenaTrazabilidad, bonosFor.getProgramid(), Constantes.OBSERVACION);
			          	    								if(Constantes.CERO.equals(auditoria.getCodigoRespuesta())){
			          	    									LOGGER.info(transaccion+" Exito al Desactivar Bono:"+bonosFor2.getProgramid());
			          	    								}else{
			          	    									LOGGER.info(transaccion+" ERROR al Desactivar Bono:"+bonosFor2.getProgramid());
			          	    								}
		          	    								}
				    								}else{
				    									LOGGER.info(transaccion+" Sin Accion:"+bonosFor2.getLinea());
				    									LOGGER.info(transaccion+" Sin Accion:"+menor2.getLinea());
				    								}
	
		    										
			    								}else{
			    									LOGGER.info(transaccion+" Sin Accion:"+bonosFor2.getLinea());
			    									LOGGER.info(transaccion+" Sin Accion:"+menor2.getLinea());
			    									
			    									if(bonosFor.getCargoFijo().compareTo(menor1.getCargoFijo())>0){			    										
			    										LOGGER.info(transaccion+" Linea  A Activar:"+menor1.getLinea());
			    										ResponseAuditoria auditoria2=new ResponseAuditoria();
		          	    								auditoria2=registrarNuevoBono(cadenaTrazabilidad, menor1);
		          	    								if(Constantes.CERO.equals(auditoria2.getCodigoRespuesta())){
		          	    									LOGGER.info(transaccion+" Exito al Insertar Bono:"+menor1.getProgramid());
				    										//Desactivar Bono
				    										LOGGER.info(transaccion+" Linea  A Desactivar:"+bonosFor.getLinea());
				      	    							    ResponseAuditoria auditoria=new ResponseAuditoria();
			          	    							    auditoria=motorDao.actualizarBono(cadenaTrazabilidad, bonosFor.getProgramid(), Constantes.OBSERVACION);
			          	    								if(Constantes.CERO.equals(auditoria.getCodigoRespuesta())){
			          	    									LOGGER.info(transaccion+" Exito al Desactivar Bono:"+bonosFor.getProgramid());
			          	    								}else{
			          	    									LOGGER.info(transaccion+" ERROR al Desactivar Bono:"+bonosFor.getProgramid());
			          	    								}
		          	    								}
				    								}else{
				    									LOGGER.info(transaccion+" Sin Accion:"+bonosFor.getLinea());
				    									LOGGER.info(transaccion+" Sin Accion:"+menor1.getLinea());
				    								}
			    								}
		    								}else{
		    									LOGGER.info(transaccion+" Sin Accion:"+bonosFor.getLinea());
		    									LOGGER.info(transaccion+" Sin Accion:"+menor1.getLinea());
		    									
		    									if(bonosFor2.getCargoFijo().compareTo(menor2.getCargoFijo())>0){
	          	    								//Insertar Bono
		    										LOGGER.info(transaccion+" Linea  A Activar:"+menor2.getLinea());
		    										ResponseAuditoria auditoria2=new ResponseAuditoria();
	          	    								auditoria2=registrarNuevoBono(cadenaTrazabilidad, menor2);
	          	    								if(Constantes.CERO.equals(auditoria2.getCodigoRespuesta())){
	          	    									LOGGER.info(transaccion+" Exito al Insertar Bono:"+menor2.getProgramid());
	          	    									
	          	    								    //Desactivar Bono
			    										LOGGER.info(transaccion+" Linea  A Desactivar:"+bonosFor2.getLinea());
			      	    							    ResponseAuditoria auditoria=new ResponseAuditoria();
		          	    							    auditoria=motorDao.actualizarBono(cadenaTrazabilidad, bonosFor.getProgramid(), Constantes.OBSERVACION);
		          	    								if(Constantes.CERO.equals(auditoria.getCodigoRespuesta())){
		          	    									LOGGER.info(transaccion+" Exito al Desactivar Bono:"+bonosFor2.getProgramid());
		          	    								}else{
		          	    									LOGGER.info(transaccion+" ERROR al Desactivar Bono:"+bonosFor2.getProgramid());
		          	    								}
	          	    								}
			    								}else{
			    									LOGGER.info(transaccion+" Sin Accion:"+bonosFor2.getLinea());
			    									LOGGER.info(transaccion+" Sin Accion:"+menor2.getLinea());
			    								}
		    								}
		    								
		    								listaBono.getListabono().remove(j);
				    						LOGGER.info(transaccion+" LINEA  A ELIMINAR DE LISTA :"+bonosFor2.getLinea());
				    						exiteUno=false;
		    							}else{
		    								 LOGGER.info(transaccion+" No existe Recalculo para el Contrad ID:"+bonosFor2.getDocumentoNum());  
		    							}
	    							}else{
	    								 listaBono.getListabono().remove(j);
			    						 LOGGER.info(transaccion+" LINEA  A ELIMINAR DE LISTA :"+bonosFor2.getLinea());
			    						 exiteUno=false;
	    								 LOGGER.info(transaccion+" No existe Informacion en INT-CAA-0002 :"+bonosFor2.getDocumentoNum());  
	    							}
	    	    				}else{
	    	    					 LOGGER.info(transaccion+" No existe Lineas para el documento INT-COP-0223:"+bonosFor2.getDocumentoNum());  
	    	    				}
	    						
	    	    			}
	    		       }
    					
    				}
    				
    				if(exiteUno){
						//Inicio - validar  cambio Plan
    					String poBasic=Constantes.CONSTANTE_VACIA;
		    			retrieveSubscript=(RetrieveSubscriptions)reintento(Constantes.CUATRO,cadenaTrazabilidad,transaccion,null,null,null,bonosFor.getContractidExt());
		    			 
		    			if(Constantes.EXITO.equalsIgnoreCase(retrieveSubscript.getStatus())){
		    				for(Subscriptions sub:retrieveSubscript.getListaSubscriptions()){
			    				poBasic=sub.getPoidbase();
			    			}
		    			}
		    			
		    			if(bonosFor.getPoBasic().equalsIgnoreCase(poBasic)){
		    				exiteUno=false;
		    			}
		    			//Fin - validar  cambio Plan
    					if(null!=bonosFor.getDocumentoNum() && !bonosFor.getDocumentoNum().isEmpty() && exiteUno){    						
    						LOGGER.info("1 linea  para  el Documento: "+bonosFor.getDocumentoNum());
    						retrieveSubscriptions=(RetrieveSubscriptions)reintento(Constantes.UNO,cadenaTrazabilidad,transaccion,bonosFor.getDocumentoNum(),
    								bonosFor.getDocType(),null,null);
    						List<Subscriptions> listFin=new ArrayList<Subscriptions>(); 
    						ListMaxFamilia listaMaxfam=new ListMaxFamilia();
    						if(Constantes.CERO.equalsIgnoreCase(retrieveSubscriptions.getCodeResponse())){
    							
    							listFin= obtenerListNumero( retrieveSubscriptions.getListaSubscriptions(), cadenaTrazabilidad,  transaccion); 
    							if(listFin.size()>0){
    								listaMaxfam=motorDao.getObtenerRecalculo(transaccion, listFin, 1);
        							if(Constantes.CERO.equalsIgnoreCase(listaMaxfam.getCodigoError())){
        								for( BonoProgram max:listaMaxfam.getListaMax()){
                                          if(null!=bonosFor.getCargoFijo()){
	                                           if(bonosFor.getCargoFijo().compareTo(max.getCargoFijo())>0){
	                                        	   
	                                        	   LOGGER.info(transaccion+" LINEA  A ACTIVAR 1:"+max.getLinea());
	          	    								ResponseAuditoria auditoria2=new ResponseAuditoria();
	          	    								auditoria2=registrarNuevoBono(cadenaTrazabilidad,reCargarBono(bonosFor, max,Constantes.DOSINT));
	          	    								if(Constantes.CERO.equals(auditoria2.getCodigoRespuesta())){
	          	    									LOGGER.info(transaccion+" Exito al Insertar Bono:"+max.getContractidExt());
	          	    									
	          	    									LOGGER.info(transaccion+" LINEA  A DESACTIVAR 1:"+bonosFor.getLinea());      	    								
		          	    							    ResponseAuditoria auditoria=new ResponseAuditoria();
		          	    							    auditoria=motorDao.actualizarBono(cadenaTrazabilidad,bonosFor.getProgramid(), Constantes.OBSERVACION);
		          	    								if(Constantes.CERO.equals(auditoria.getCodigoRespuesta())){
		          	    									LOGGER.info(transaccion+" Exito al Desactivar Bono:"+bonosFor.getContractId());
		          	    								}else{
		          	    									LOGGER.info(transaccion+" ERROR al Desactivar Bono:"+bonosFor.getContractId());
		          	    								}
	          	    									
	          	    								}				
	          	    							}else{
          	    									LOGGER.info(transaccion+"No existe Recalculo ContractId:"+bonosFor.getContractId());
          	    								}  
                                          }else{
             								 LOGGER.info(transaccion+" No existe Recalculo para la Linea con Contrad ID:"+bonosFor.getContractId());  
              							  }    									
    	    						    }
        							}else{
        								 LOGGER.info(transaccion+" No existe Recalculo para la Linea con Contrad ID:"+bonosFor.getContractId());  
        							}
    							}else{
   								 LOGGER.info(transaccion+" No existe Informacion en INT-CAA-0002 :"+bonosFor.getContractidExt());
   							   }	
    	    				}else{
    	    					 LOGGER.info(transaccion+" No existe Lineas para el documento INT-COP-0223:"+bonosFor.getDocumentoNum());  
    	    				}
    					}else{
	    					  LOGGER.info(transaccion+" No existe Recalculo para el Contrad ID: "+bonosFor.getContractId());  
	    				}   					
    				}
    			}	
     	     				LOGGER.info(transaccion+" FIN - INSERT RECALCULAR");
     	     				//FIN  INSERT RECALCULAR				  
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
    
    private BonoProgram reCargarBono( BonoProgram bonos, BonoProgram max,int p){
    	BonoProgram bonosNue=new BonoProgram();
    	try{
			bonosNue.setActivacion(bonos.getActivacion());
    		bonosNue.setBonoId(bonos.getBonoId());
    		bonosNue.setBonoperiodico(bonos.getBonoperiodico());
    		bonosNue.setCampania(bonos.getCampania());   		
    		bonosNue.setDocType(bonos.getDocType());
    		bonosNue.setDocumentoNum(bonos.getDocumentoNum());
    		bonosNue.setEstadoBono(bonos.getEstadoBono());
    		bonosNue.setEstadoProceso(bonos.getEstadoProceso());
    		bonosNue.setEstom(bonos.getEstom());
    		bonosNue.setFecvigencia(bonos.getFecvigencia());
    		bonosNue.setPeriodo(bonos.getPeriodo());
    		bonosNue.setPobono(bonos.getPobono());
    		bonosNue.setPoontop(bonos.getPoontop());
    		bonosNue.setShoppingcartid(bonos.getShoppingcartid());
    		bonosNue.setTipoBono(bonos.getTipoBono());
    		bonosNue.setTipoopera(bonos.getTipoopera());
    		bonosNue.setOrderId(bonos.getOrderId());
    		bonosNue.setProgramid(bonos.getProgramid());
    		
    		if(Constantes.UNOINT==p){
        		bonosNue.setBillingAccount(bonos.getBillingAccount());
        		bonosNue.setCustomeridExt(bonos.getCustomeridExt());
        		bonosNue.setFecactbono(bonos.getFecactbono());
        		bonosNue.setLinea(bonos.getLinea());
        		bonosNue.setOrden(bonos.getOrden());
        		bonosNue.setPoBasic(bonos.getPoBasic());
        		bonosNue.setPonamebase(bonos.getPonamebase());
        		bonosNue.setCargoFijo(bonos.getCargoFijo());
        		bonosNue.setContractId(bonos.getContractId());
        		bonosNue.setCiclofactbase(bonos.getCiclofactbase());
        		bonosNue.setContractidExt(bonos.getContractidExt());
    		}else{
    			bonosNue.setBillingAccount(max.getBillingAccount());
        		bonosNue.setCustomeridExt(max.getCustomeridExt());
        		bonosNue.setFecactbono(max.getFecactbono());
        		bonosNue.setLinea(max.getLinea());
        		bonosNue.setOrden(max.getOrden());
        		bonosNue.setPoBasic(max.getPoBasic());
        		bonosNue.setPonamebase(max.getPonamebase());
        		bonosNue.setCargoFijo(max.getCargoFijo());
        		bonosNue.setContractId(max.getContractId());
        		bonosNue.setCiclofactbase(max.getCiclofactbase());
        		bonosNue.setContractidExt(max.getContractidExt());
    		}
    		
    	}catch(Exception e){
    	} return bonosNue;
    }

    private    ResponseAuditoria registrarNuevoBono(String  cadenaTrazabilidad, BonoProgram max){
    	LOGGER.info(cadenaTrazabilidad+" Inicio Activacion Bono");
    	ResponseAuditoria auditoria=new ResponseAuditoria();
    	ListaBonoRecalculo listaBono=new  ListaBonoRecalculo();
    	try{
    		   //Obtener Info de bono: id_bono- bono des
    		   listaBono=motorDao.obtenerBonoInfoRecalculo(cadenaTrazabilidad, max.getPoBasic(), max.getTipoopera(), max.getCampania());	
    		   BonoRecalcula bonoIns=new BonoRecalcula();
    		   String bonoid=Constantes.CONSTANTE_VACIA;
    		   String poBono=Constantes.CONSTANTE_VACIA;
                for(BonoRecalculo bono:listaBono.getListaRecalculo()){
                	bonoid=bono.getBonoId();
                	poBono=bono.getPobono();
                }
                
                if(null!=bonoid && !bonoid.isEmpty()){
                	bonoid=max.getBonoId();
                }
				Date fechaActual=new Date();
				bonoIns.setPeriodo(Util.obtenerPeriodo(fechaActual, Util.stringToJavaDate( max.getFecvigencia(),Constantes.FORMATOFECHA3)));
				
				bonoIns.setLinea(max.getLinea());
				bonoIns.setBonoId(bonoid);
				bonoIns.setPobono(poBono);
				bonoIns.setPoplan(max.getPoBasic());
				LOGGER.info(cadenaTrazabilidad+"Fecha Activacion de Bono: "+max.getFecactbono());
				bonoIns.setFecactbono(Util.stringToJavaDate(max.getFecactbono(),Constantes.FORMATOFECHA3));
				bonoIns.setFecvigencia(Util.stringToJavaDate(max.getFecvigencia(),Constantes.FORMATOFECHA3));
				bonoIns.setEstbono(Constantes.ESTADOBONOA);
				bonoIns.setEstproc(Constantes.ESTADOPROCESOCA);
				bonoIns.setCiclofact(max.getCiclofactbase());
				bonoIns.setTipoopera(max.getTipoopera());
				bonoIns.setEstom(max.getEstom()); 
				bonoIns.setTipobono(max.getTipoBono());
				bonoIns.setActivacion(max.getActivacion());
				//Insertar datos  del contrato 
				if(!Constantes.CONSTANTE_VACIA.equalsIgnoreCase(bonoid)){
					auditoria=motorDao.insertarContratoInterno(cadenaTrazabilidad, max);
				}
				
				//Insertar Bono en  promt_bono_program
				if(Constantes.EXITO.equalsIgnoreCase(auditoria.getCodigoRespuesta()) || Constantes.EXISTECONTRA.equalsIgnoreCase(auditoria.getCodigoRespuesta())){
					auditoria=null;
					auditoria=motorDao.insertarBonoInterno(cadenaTrazabilidad, bonoIns);
				}

				LOGGER.info(cadenaTrazabilidad+" FIn Activacion Bono: ");
    	}catch(DBException e){
    		LOGGER.error(cadenaTrazabilidad+" Error  Activacion Bono: "+e.getCause());
    	}catch(Exception e){
    		LOGGER.error(cadenaTrazabilidad+" Error  Activacion Bono: "+e.getMessage());
    		LOGGER.error(cadenaTrazabilidad+" Error  Activacion Bono: "+e.getCause());
    	}
    	return auditoria;
    }
    
    private List<Subscriptions> obtenerListNumero(List<Subscriptions> retrieveSubscriptions,String cadenaTrazabilidad, String transaccion){
		ProductOffer productOffer=new  ProductOffer();
		List<Subscriptions> lista=new ArrayList<Subscriptions>(); 
		try{
			  LOGGER.info(transaccion+"  SIZE LIST : "+retrieveSubscriptions.size());
				if(retrieveSubscriptions.size()>0){
					for(Subscriptions subs:retrieveSubscriptions){
						//INICIO  INT-COP-0002    	    						
						productOffer=(ProductOffer)reintento(Constantes.DOS,cadenaTrazabilidad,transaccion,null,null,subs.getContractIdBase(),null);
						//FIN  INT-COP-0002	
						if(null!=productOffer.getAmount()){
							subs.setCargoFijo(productOffer.getAmount());
							lista.add(subs);
						}
					}
					
				}	
		}catch(Exception e){	
			LOGGER.error(transaccion+" Error : "+e.getMessage());
		}
		
		return lista;
    }
    
    private Object reintento(String dato,String cadenaTrazabilidad,String  transaccion,String numeroDocumento,String tipoDocumento,String contractId,String contrato){
    	  int numeroIntento=0;
    	  RetrieveSubscriptions retrieveSubscriptions=new RetrieveSubscriptions();
    	  String codigoResp=Constantes.EXITO;
    	  ProductOffer productOffer=new  ProductOffer();
			while(numeroIntento<=Integer.parseInt(propertiesExternos.reintentoProceso)){

				if(numeroIntento>0){
				  LOGGER.info(transaccion+"[se procede  al Reintento :]"+numeroIntento);
				}	
				try{
					if(Constantes.UNO.equals(dato)){
						//Invocacion INT-COP-0223   
						retrieveSubscriptions=retrieveSubscriptions(cadenaTrazabilidad, numeroDocumento,tipoDocumento, transaccion,null); 
						return retrieveSubscriptions;
					}if(Constantes.DOS.equals(dato)){
						 //INT-CAA-0002  
						productOffer=buscarPlanBono(cadenaTrazabilidad, contractId, transaccion);  
						return productOffer;
					}if(Constantes.CUATRO.equals(dato)){
						//Invocacion INT-COP-0223  
						retrieveSubscriptions=retrieveSubscriptions(cadenaTrazabilidad, null,null, transaccion,contrato); 
						return retrieveSubscriptions;
					}
							
				}catch(Exception e){
					codigoResp=Constantes.NOEXITO;
					LOGGER.error(transaccion+"[Error: Interface :]"+e.getMessage());
				}
				
			   if(Constantes.NOEXITO.equals(codigoResp)){	
   				   numeroIntento++;
   			   }else{
   					break;  
   			   }
		    } return null;
    }
       
    //INT-CAA-0002
    public  ProductOffer buscarPlanBono(String cadenaTrazabilidad, String contrato,String idTransaction)
    	    throws Exception{
    			LOGGER.info(cadenaTrazabilidad + " ======= Inicia Invocacion  INT-CAA-0002 ======");
    			
    			ProductOffer productOffer=new ProductOffer();
    			GetProductsOfferingPerContractResponseType respponseca002;
    			
    			GetProductsOfferingPerContractRequestType getProductsOfferingPerContractRequestType =new  GetProductsOfferingPerContractRequestType();
    			try{
    				CustomerAccount custo=new  CustomerAccount();
    				PartyRole party=new PartyRole();
    				
    				PartyRoleProductOffering  partyRoleProductOffering=new PartyRoleProductOffering();
    				AgreementItem agreementItem=new AgreementItem();
    				Agreement  agreement=new Agreement();
    				agreement.setID(contrato);
    			
    				
    				agreementItem.setAgreement(agreement);
    				partyRoleProductOffering.getAgreementItem().add(agreementItem);
    				party.getPartyRoleProductOffering().add(partyRoleProductOffering);
    				custo.getPartyRole().add(party);
    				
    				getProductsOfferingPerContractRequestType.setCustomerAccount(custo);
    				respponseca002=iNTCAA0002Client.GetProductsOfferingPerContractResponse(cadenaTrazabilidad, getProductsOfferingPerContractRequestType, 
    						propertiesExternos.usuarioAplicacionProductsOfferingPerContract,propertiesExternos.nombreAplicacionProductsOfferingPerContract
    						, idTransaction);
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
    							
    							if(conta &&conta2){
    								break;
    							}
    						}
    						if(conta &&conta2){
								break;
							}
    					}
    				}
    				
    				LOGGER.info(cadenaTrazabilidad + " ======= FIN  Prueba INT-CAA-0002 ======");
    			}catch(Exception e){
    				LOGGER.error(cadenaTrazabilidad + " ======= Error  Prueba INT-CAA-0002 ======"+e.getMessage());
    				throw new Exception("update exception");
    			}
    			
    			return productOffer;
    	    }
    
    //NT-COP-0223 
    public RetrieveSubscriptions retrieveSubscriptions(String cadenaTrazabilidad, String numDocumento,String tipoDoc,String idTransaction,
    		String contrato) throws Exception{
		LOGGER.info(cadenaTrazabilidad + " ======= Inicia Invocacion  INT-COP-0223 ======");
		RetrieveSubscriptionsResponseType response0223= new RetrieveSubscriptionsResponseType();
		RetrieveSubscriptionsRequestType request=new  RetrieveSubscriptionsRequestType();
		RetrieveSubscriptions retrieveSubscriptions=new RetrieveSubscriptions();
		List<Subscriptions> listaSubscriptio=new ArrayList<Subscriptions>();
		retrieveSubscriptions.setStatus(Constantes.NOEXITO);
		Customer customer=new Customer();
		try{			
			if(null!=contrato && !contrato.isEmpty()){
				
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
				customer.getCustomerAccount().add(customerAcount);		
				request.setCustomer(customer);
				
			}else{
				PartyUserExtension extension=new PartyUserExtension();
				extension.setType(Util.obtenerTipoDoc0223(tipoDoc));
				PartyUser partyUser=new PartyUser();
				partyUser.setPartyUserExtension(extension);
				
				Party party=new  Party();
				party.setPartyId(numDocumento);
				party.getPartyUser().add(partyUser);
				
				customer.setParty(party);
				request.setCustomer(customer);
			}

			response0223=iNTCOP0223Client.retrieveSubscriptionsResponse(cadenaTrazabilidad, request, propertiesExternos.usuarioAppBssAgreementManagement
					, propertiesExternos.nombreAppBssAgreementManagement, idTransaction, propertiesExternos.idApplicationBssAgreementManagement);
			
			if(Integer.parseInt(Constantes.EXITO)==response0223.getResponseStatus().getStatus()){
				for(int i=0;i<response0223.getResponseData().getSubscriptionsType().size();i++){
					Subscriptions reponse=new Subscriptions();
						
					reponse.setCustomeridextbase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getID());
					reponse.setTipoproductobase(response0223.getResponseData().getSubscriptionsType().get(i).getProductLine().getName());
					reponse.setTiposuscripcionbase(response0223.getResponseData().getSubscriptionsType().get(i).getProductCategory().getName());
		
					for(int r=0;r<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getPartyRoleProductSpecification().size();r++){
						for(int s=0;s<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getPartyRoleProductSpecification().
								get(r).getProductSpecification().getProductSpecificationType().size();s++){
							    reponse.setLineabase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getPartyRoleProductSpecification().
								get(r).getProductSpecification().getProductSpecificationType().get(s).getName());
						}
					}
					
					for(int h=0;h<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().size();h++){
						
						for(int j=0;j<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h).getCustomerBillSpec().size();j++){
							
							for(int  k=0;k<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h).getCustomerBillSpec()
									.get(j).getCustomerBillingCycle().size();k++){
								
								if(null!=response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
										.getCustomerBillSpec()
										&&!response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
										.getCustomerBillSpec().isEmpty()){
									//Ciclo de Facturacion
									reponse.setCiclofactbase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
											.getCustomerBillSpec().get(j).getCustomerBillingCycle().get(k).getCustomerBillingCycleExtension().getBillingCycleDay());

								}
							}
						}
						
						for(int m=0;m<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h).getPartyRole().size();m++){
							for(int g=0;g<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h).getPartyRole().get(m)
									.getPartyRoleProductOffering().size();g++){
								reponse.setBillingaccountbase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
										.getPartyRole().get(g).getPartyRoleId());
								
								  for(int t=0;t<response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h).getPartyRole().get(m)
									.getPartyRoleProductOffering().get(g).getAgreementItem().size();t++){

											reponse.setContractIdBase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
													.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getAgreement().getID());
											
											reponse.setPoidbase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
													.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getProductOffering().getID());
											
											reponse.setPonamebase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
													.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getProductOffering().getName());
											
											if(null!= response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
													.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getAgreement()
													.getInteractionDate().getEndDateTime()
												&& !response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
													.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getAgreement()
													.getInteractionDate().getEndDateTime().isEmpty()){
												reponse.setFecactivacionbase(Util.getDateFormato (Util.stringToJavaDate( response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
														.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getAgreement()
														.getInteractionDate().getEndDateTime(),Constantes.FORMATOFECHA2),Constantes.FORMATOFECHA3 ));
											}

											reponse.setEstadolineabase(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
													.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getAgreement().getInteractionStatus());

											reponse.setActivo(response0223.getResponseData().getSubscriptionsType().get(i).getCustomer().getCustomerAccount().get(h)
													.getPartyRole().get(m).getPartyRoleProductOffering().get(g).getAgreementItem().get(t).getAgreement().getInteractionStatus());
											 
								  }
							}
						}
					}
					//cargar a  lista, Lineas  activas. 
					if(Constantes.DOS.equalsIgnoreCase(reponse.getActivo())){listaSubscriptio.add(reponse);}
				}
				retrieveSubscriptions.setListaSubscriptions(listaSubscriptio);
				retrieveSubscriptions.setStatus(String.valueOf(response0223.getResponseStatus().getStatus()));
				retrieveSubscriptions.setCodeResponse(String.valueOf(response0223.getResponseStatus().getCodeResponse()));
			}
			
			LOGGER.info(cadenaTrazabilidad + " ======= FIN  Invocacion INT-COP-0223 ======");
		}catch(Exception e){
			LOGGER.error(cadenaTrazabilidad + " ======= Error Invocacion INT-COP-0223 ======"+e.getCause());
			LOGGER.error(cadenaTrazabilidad + " ======= Error Invocacion INT-COP-0223 ======"+e.getMessage());
			throw new Exception("Insert exception");
		}
		return retrieveSubscriptions;
	}
    
}