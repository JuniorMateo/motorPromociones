package claro.com.pe.facturacion.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExternos {

    @Value("${log4j.dir}") public String LOG4J_DIR; 
	@Value("${oracle.jdbc.owner.bscs}")public String ORACLE_JDBC_OWNER_BSCS;
	@Value("${bscs.pkg.motprom}")public String BSCS_PKG_CARGA_ALTA;
	@Value("${bscs.promss.cambio.ciclo.facturacion}")public String BSCS_DESACTIVACIONFACTURACION;
	@Value("${bscs.su.cambio.plan}")public String BSCS_UPDATE_CAMBIO_PLAN;
	@Value("${db.bscs.nombre}")public String DB_BSCS_NOMBRE;
	@Value("${db.bscs.timeout}")public String DB_BSCS_TIMEOUT;
	
	
	@Value("${cargadatos.motprom.codigo.idt1}")public String CARGA_ALTA_COD_IDT1;
	@Value("${cargadatos.motprom.mensaje.idt1}")public String CARGA_ALTA_MSJ_IDT1;
	@Value("${cargadatos.motprom.codigo.idt2}")public String CARGA_ALTA_COD_IDT2;
	@Value("${cargadatos.motprom.mensaje.idt2}")public String CARGA_ALTA_MSJ_IDT2;
    
 // WS INT-COP-0223
 	@Value( "${intcop.0223.bssAgreementManagement.url}" )  public String bssAgreementManagementURL;
 	@Value( "${intcop.0223.bssAgreementManagement.url.timeout}" )  public String bssAgreementManagementTimeout;
 	@Value( "${intcop.0223.bssAgreementManagement.url.conexion}" )  public String bssAgreementManagementConnectionTimeout;
 	@Value( "${intcop.0223.bssAgreementManagement.auth.username}" )  public String usernameBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.auth.password}" )  public String passwordBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.canal}" )  public String canalbssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagementcountry}" )  public String countrybssAgreementManagementr;
 	@Value( "${intcop.0223.bssAgreementManagement.language}" )  public String lenguajebssAgreementManagementr;
 	@Value( "${intcop.0223.bssAgreementManagement.consumer}" )  public String consumerbssAgreementManagementr;
 	@Value( "${intcop.0223.bssAgreementManagement.system}" )  public String systembssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.modulo}" )  public String moduloBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.userid}" )  public String useridBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.dispositivo}" )  public String dispositivoBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.wslp}" )  public String wslpBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.msgtype}" )  public String msgtypeBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagementoperation.1}" )  public String operacion1BssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.nombre.aplicacion}" )  public String nombreAppBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.usuario.aplicacion}" )  public String usuarioAppBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.idApplication}" )  public String idApplicationBssAgreementManagement;
 	@Value( "${intcop.0223.bssAgreementManagement.msgtype}" )  public String msgtypePower;
	@Value( "${productSpecification.productSpecificationType.name}" )  public String name1;
	@Value( "${desactiva.bono.cambio.plan.reintento}" )  public String reintento;
	
	
	//IDT WS
	@Value( "${cod.idt3.getProductsOfferingPerContract}" )  public String consultarClienteIdtcod3;
	@Value( "${msj.idt3.getProductsOfferingPerContract}" )  public String consultarClienteIdtmsj3;
	@Value( "${cod.idt4.getProductsOfferingPerContract}" )  public String consultarClienteIdtcod4;
	@Value( "${msj.idt4.getProductsOfferingPerContract}" )  public String consultarClienteIdtmsj4;
	
	@Value("${codigo.idf1}")public String codigoIdf1;
	@Value("${mensaje.idf1}")public String mensajeIdf1;	
	@Value("${codigo.idf2}")public String codigoIdf2;
	@Value("${mensaje.idf2}")public String mensajeIdf2;
	

}