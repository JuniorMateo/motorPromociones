package claro.com.pe.desa.titularidad.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExternos {

    @Value("${log4j.dir}")
    public String LOG4J_DIR;
    
	@Value("${oracle.jdbc.owner.bscs}")
	public String ORACLE_JDBC_OWNER_BSCS;
	
	@Value("${bscs.pkg.motprom}") public String BSCS_PKG_CARGA_ALTA;
	@Value("${bscs.promss.desa.cambio.dniruc}") public String OBTENERCAMBIOPLANDNIARUC;
	@Value("${bscs.promss.desa.cambio.titularidad}") public String OBTENERCAMBIOPLANTITULARIDAD;
	@Value("${bscs.su.cambio.plan}")public String BSCS_UPDATE_CAMBIO_PLAN;
	
	@Value("${db.bscs.nombre}")
	public String DB_BSCS_NOMBRE;
	
	@Value("${db.bscs.timeout}")
	public String DB_BSCS_TIMEOUT;
	
	
	@Value("${codigo.idf1}")
	public String codigoIdf1;
	
	@Value("${mensaje.idf1}")
	public String mensajeIdf1;
	
	
	
	// WS INT-CAA-0101	
		@Value( "${intcaap.0101.GetCustomerInfo.url}" )  public String getProductsOfferingPerContractURL0101;
		@Value( "${intcaap.0101.GetCustomerInfo.url.timeout}" )  public String getProductsOfferingPerContracTimeout0101;
		@Value( "${intcaap.0101.GetCustomerInfo.url.conexion}" )  public String getProductsOfferingPerContracConnectionTimeout0101;
		@Value( "${intcaap.0101.GetCustomerInfo.auth.username}" )  public String usernameGetProductsOfferingPerContrac0101;
		@Value( "${intcaap.0101.GetCustomerInfo.auth.password}" )  public String passwordGetProductsOfferingPerContract0101;
		@Value( "${intcaap.0101.GetCustomerInfo.canal}" )  public String canalDataPower0101;
		@Value( "${intcaap.0101.GetCustomerInfocountry}" )  public String countryPower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.language}" )  public String lenguajePower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.consumer}" )  public String consumerPower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.system}" )  public String systemPower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.modulo}" )  public String moduloPower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.userid}" )  public String useridPower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.dispositivo}" )  public String dispositivoPower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.wslp}" )  public String wslpPower0101;
		@Value( "${intcaap.0101.GetCustomerInfo.msgtype}" )  public String msgtypePower0101;
		@Value( "${intcaap.0101.GetCustomerInfooperation.1}" )  public String operacion1Power0101;
		@Value( "${intcaap.0101.GetCustomerInfo.nombre.aplicacion}" )  public String nombreAplicacion0101;
		@Value( "${intcaap.0101.GetCustomerInfo.usuario.aplicacion}" )  public String usuarioAplicacion0101;
		@Value( "${intcaap.0101.GetCustomerInfo.idApplication}" )  public String idApplication0101;
		@Value( "${productSpecification.productSpecificationType.name}" )  public String productSpecificationType;

		@Value("${cargadatos.motprom.codigo.idt1}")public String CARGA_ALTA_COD_IDT1;
		@Value("${cargadatos.motprom.mensaje.idt1}")public String CARGA_ALTA_MSJ_IDT1;
		@Value("${cargadatos.motprom.codigo.idt2}")public String CARGA_ALTA_COD_IDT2;
		@Value("${cargadatos.motprom.mensaje.idt2}")public String CARGA_ALTA_MSJ_IDT2;
		
		@Value( "${cod.idt3.getProductsOfferingPerContract}" )  public String consultarClienteIdtcod3;
		@Value( "${msj.idt3.getProductsOfferingPerContract}" )  public String consultarClienteIdtmsj3;
		@Value( "${cod.idt4.getProductsOfferingPerContract}" )  public String consultarClienteIdtcod4;
		@Value( "${msj.idt4.getProductsOfferingPerContract}" )  public String consultarClienteIdtmsj4;
		@Value( "${desactiva.bono.cambio.plan.reintento}" )  public String reintento;
		
		@Value( "${valida.titularidad.dni}" )  public String DNI;
		@Value( "${valida.titularidad.ce}" )  public String CARNETEXTRANJERIA;
		@Value( "${valida.titularidad.ruc}" )  public String RUC;
		@Value( "${valida.titularidad.passaporte}" )  public String PASAPORTE;
		
}