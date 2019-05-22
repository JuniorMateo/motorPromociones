package claro.com.pe.cambioplan.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExternos {

    @Value("${log4j.dir}") public String LOG4J_DIR; 
	@Value("${oracle.jdbc.owner.bscs}")public String ORACLE_JDBC_OWNER_BSCS;
	@Value("${bscs.pkg.motprom}")public String BSCS_PKG_CARGA_ALTA;
	@Value("${bscs.su.cambio.plan}")public String BSCS_UPDATE_CAMBIO_PLAN;
	@Value("${db.bscs.nombre}")public String DB_BSCS_NOMBRE;
	@Value("${db.bscs.timeout}")public String DB_BSCS_TIMEOUT;
	
	//tipo desactivacion
	@Value("${bscs.ss.obtener.cambio.plan.linea}")public String OBTENERCAMBIOPLANLINEA;
	@Value("${bscs.ss.obtener.cambio.plan.downgrade}")public String OBTENERCAMBIOPLANDOWNGRADE;
	@Value("${bscs.ss.obtener.cambio.plan.upgrade}")public String OBTENERCAMBIOPLANUPGRADE;
	@Value("${bscs.ss.obtener.cambio.plan.prepago}")public String OBTENERCAMBIOPLANPREPAGO;
	@Value("${bscs.ss.obtener.cambio.plan.no.matiz}")public String OBTENERCAMBIOPLANNOMATRIZ;
	@Value("${bscs.su.obtener.cambio.plan.no.matiz}")public String ACTUALIZARCAMBIOPLANNOMATRIZ;
	@Value("${bscs.ss.obtener.cambio.plan.upgrade.no.matiz}")public String OBTENERCAMBIOPLANUPGRADENOMATRIZ;
	
	@Value("${cargadatos.motprom.codigo.idt1}")public String CARGA_ALTA_COD_IDT1;
	@Value("${cargadatos.motprom.mensaje.idt1}")public String CARGA_ALTA_MSJ_IDT1;
	@Value("${cargadatos.motprom.codigo.idt2}")public String CARGA_ALTA_COD_IDT2;
	@Value("${cargadatos.motprom.mensaje.idt2}")public String CARGA_ALTA_MSJ_IDT2;
	
	
	@Value("${codigo.idf1}")
	public String codigoIdf1;
	@Value("${mensaje.idf1}")
	public String mensajeIdf1;	
	@Value("${codigo.idf2}")
	public String codigoIdf2;
	@Value("${mensaje.idf2}")
	public String mensajeIdf2;
	
	// WS INT-CAA-0002	
	@Value( "${intcaa002.getProductsOfferingPerContract.url}" )  public String getProductsOfferingPerContractURL;
	@Value( "${intcaa002.getProductsOfferingPerContract.url.timeout}" )  public String getProductsOfferingPerContracTimeout;
	@Value( "${intcaa002.getProductsOfferingPerContract.url.conexion}" )  public String getProductsOfferingPerContracConnectionTimeout;
	@Value( "${eai.prs.intcaa002.getProductsOfferingPerContract.auth.username}" )  public String usernameGetProductsOfferingPerContrac;
	@Value( "${eai.prs.intcaa002.getProductsOfferingPerContract.auth.password}" )  public String passwordGetProductsOfferingPerContract;
	@Value( "${header.canal}" )  public String canalDataPower;
	@Value( "${dp.param.country}" )  public String countryPower;
	@Value( "${dp.param.language}" )  public String lenguajePower;
	@Value( "${dp.param.consumer}" )  public String consumerPower;
	@Value( "${dp.param.system}" )  public String systemPower;
	@Value( "${dp.param.modulo}" )  public String moduloPower;
	@Value( "${dp.param.userid}" )  public String useridPower;
	@Value( "${dp.param.dispositivo}" )  public String dispositivoPower;
	@Value( "${dp.param.wslp}" )  public String wslpPower;
	@Value( "${dp.param.msgtype}" )  public String msgtypePower;
	@Value( "${dp.param.operation.1}" )  public String operacion1Power;
	@Value( "${dp.param.nombre.aplicacion}" )  public String nombreAplicacion;
	@Value( "${dp.param.usuario.aplicacion}" )  public String usuarioAplicacion;	
	
	
	@Value( "${basic.product.validos}" )  public String nombreB;
	@Value( "${productSpecification.productSpecificationType.name}" )  public String name1;
	@Value( "${desactiva.bono.cambio.plan.reintento}" )  public String reintento;
	
	//IDT WS
	@Value( "${cod.idt3.getProductsOfferingPerContract}" )  public String consultarClienteIdtcod3;
	@Value( "${msj.idt3.getProductsOfferingPerContract}" )  public String consultarClienteIdtmsj3;
	@Value( "${cod.idt4.getProductsOfferingPerContract}" )  public String consultarClienteIdtcod4;
	@Value( "${msj.idt4.getProductsOfferingPerContract}" )  public String consultarClienteIdtmsj4;


}