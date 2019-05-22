package claro.com.pe.incontrato.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExternos {

	//BSCS70
    @Value("${log4j.dir}") public String LOG4J_DIR; 
	@Value("${oracle.jdbc.owner.bscs}")public String ORACLE_JDBC_OWNER_BSCS;
	@Value("${bscs.pkg.motprom}")public String BSCS_PKG_CARGA_ALTA;
	@Value("${bscs.promss.cambio.pago.puntual}")public String BSCS_PAGOPUNTUAL;
	@Value("${bscs.su.cambio.plan}")public String BSCS_UPDATE_CAMBIO_PLAN;
	@Value("${db.bscs.nombre}")public String DB_BSCS_NOMBRE;
	@Value("${db.bscs.timeout}")public String DB_BSCS_TIMEOUT;
	
	//OAC
	@Value("${oac.oracle.jdbc.owner}")public String ORACLEOWNER_OAC;
	@Value("${oac.pkg.motprom}")public String PCK_OACCONS_PAG;
	@Value("${oac.promss.cambio.pago.puntual}")public String OAC_PAGOPUNTUAL;
	@Value("${oac.db.nombre}")public String DB_OAC_NOMBRE;
	@Value("${oac.db.timeout}")public String OACTIMEOUT;
	@Value("${oac.promss.cambio.pago.indicador}")public String INDICADOR;
	
	
	@Value("${cargadatos.motprom.codigo.idt1}")public String CARGA_ALTA_COD_IDT1;
	@Value("${cargadatos.motprom.mensaje.idt1}")public String CARGA_ALTA_MSJ_IDT1;
	@Value("${cargadatos.motprom.codigo.idt2}")public String CARGA_ALTA_COD_IDT2;
	@Value("${cargadatos.motprom.mensaje.idt2}")public String CARGA_ALTA_MSJ_IDT2;
	
	@Value("${codigo.idf1}")public String codigoIdf1;
	@Value("${mensaje.idf1}")public String mensajeIdf1;	
	@Value("${codigo.idf2}")public String codigoIdf2;
	@Value("${mensaje.idf2}")public String mensajeIdf2;
	@Value( "${desactiva.bono.cambio.plan.reintento}" )  public String reintento;
	@Value( "${desactiva.bono.cambio.incumplimiento.pago}" )  public String cantidadMeses;

}