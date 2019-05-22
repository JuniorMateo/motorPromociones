package claro.com.pe.evalua.bono.recalculo.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExternos {

    @Value("${log4j.dir}") public String LOG4J_DIR; 
	@Value("${oracle.jdbc.owner.bscs}")public String ORACLE_JDBC_OWNER_BSCS;
	@Value("${bscs.pkg.motprom}")public String BSCS_PKG_CARGA_ALTA;
	@Value("${bscs.pkg.motprom.eval}")public String BSCSPACKAGEEVALPROCESS;
	@Value("${bscs.promss.cambio.plan.replica}")public String BSCS_RECALCULAR;
	@Value("${bscs.promsi.cambio.plan.replica}")public String BSCS_INSERT_RECALCULO;
	@Value("${bscs.promsi.cambio.plan.no.contrato}")public String BSCSINSERTCONTRATO;
	@Value("${bscs.promsu.cambio.plan}")public String BSCS_UPDATE_CAMBIO_PLAN;
	@Value("${bscs.promss.cambio.recalcular.max.familia}")public String BSCS_RECALCULOMAXFAMILIA;
	@Value("${bscs.promss.cambio.dato.bono}")public String BSCSOBTENERDATOSBONO;
	@Value("${bscs.promsi.cambio.plan.type}")public String TPROMVENTADATOSLINEABASE;
	@Value("${bscs.promsi.cambio.plan.types.array}")public String TPROMVENTALISTALINEABASE;
	@Value("${db.bscs.nombre}")public String DB_BSCS_NOMBRE;
	@Value("${db.bscs.timeout}")public String DB_BSCS_TIMEOUT;
	
	//IDT BD
	@Value("${motprom.codigo.idt1}")public String COD_IDT1;
	@Value("${motprom.mensaje.idt1}")public String MSJ_IDT1;
	@Value("${motprom.codigo.idt2}")public String COD_IDT2;
	@Value("${motprom.mensaje.idt2}")public String MSJ_IDT2;	
	//IDT WS
	@Value( "${motprom.codigo.idt3}" )  public String COD_IDT3;
	@Value( "${motprom.mensaje.idt3}" )  public String MSJ_IDT3;
	@Value( "${motprom.codigo.idt4}" )  public String COD_IDT4;
	@Value( "${motprom.mensaje.idt4}" )  public String MSJ_IDT4;
	
	
	@Value( "${bono.re.calcular.reintento}" )  public String reintento;
	@Value( "${bono.re.calcular.proceso.reintento}" )  public String reintentoProceso;
	
	@Value("${codigo.idf1}")public String codigoIdf1;
	@Value("${mensaje.idf1}")public String mensajeIdf1;	
	@Value("${codigo.idf2}")public String codigoIdf2;
	@Value("${mensaje.idf2}")public String mensajeIdf2;
	
	// WS INT-CAA-0002	
	@Value( "${intcaa002.getProductsOfferingPerContract.url}" )  public String getProductsOfferingPerContractURL;
	@Value( "${intcaa002.getProductsOfferingPerContract.url.timeout}" )  public String getProductsOfferingPerContracTimeout;
	@Value( "${intcaa002.getProductsOfferingPerContract.url.conexion}" )  public String getProductsOfferingPerContracConnectionTimeout;
	@Value( "${eai.prs.intcaa002.getProductsOfferingPerContract.auth.username}" )  public String usernameGetProductsOfferingPerContrac;
	@Value( "${eai.prs.intcaa002.getProductsOfferingPerContract.auth.password}" )  public String passwordGetProductsOfferingPerContract;
	@Value( "${intcaa002.header.canal}" )  public String canalDataPowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.country}" )  public String countryPowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.language}" )  public String lenguajePowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.consumer}" )  public String consumerPowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.system}" )  public String systemPowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.modulo}" )  public String moduloPowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.userid}" )  public String useridProductsOfferingPerContract;
	@Value( "${intcaa002.param.dispositivo}" )  public String dispositivoPowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.wslp}" )  public String wslpPowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.msgtype}" )  public String msgtypePowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.operation.1}" )  public String operacion1PowerProductsOfferingPerContract;
	@Value( "${intcaa002.param.nombre.aplicacion}" )  public String nombreAplicacionProductsOfferingPerContract;
	@Value( "${intcaa002.param.usuario.aplicacion}" )  public String usuarioAplicacionProductsOfferingPerContract;	
	@Value( "${intcaa002.param.usuario.idApplication}" )  public String idApplicationProductsOfferingPerContract;	
	@Value( "${intcaa002.basic.product.validos}" )  public String nombreB;
	@Value( "${intcaa002.productSpecificationType.name}" )  public String name1;

	
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
	
}