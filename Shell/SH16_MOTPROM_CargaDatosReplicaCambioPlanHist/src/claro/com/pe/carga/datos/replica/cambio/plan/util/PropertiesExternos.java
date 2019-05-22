package claro.com.pe.carga.datos.replica.cambio.plan.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExternos {

    @Value("${log4j.dir}") public String LOG4J_DIR; 
	@Value("${oracle.jdbc.owner.bscs}")public String ORACLE_JDBC_OWNER_BSCS;
	@Value("${bscs.pkg.motprom}")public String BSCS_PKG_CARGA_ALTA;
	@Value("${bscs.pkg.motprom.carga}")public String BSCS_PKG_CARGADATOS;
	@Value("${bscs.promss.cambio.plan.replica}")public String BSCS_CAMBIOPLANREPLICA;
	@Value("${bscs.promsi.cambio.plan.replica}")public String BSCS_INSERT_CAMBIO_PLAN;
	@Value("${db.bscs.nombre}")public String DB_BSCS_NOMBRE;
	@Value("${db.bscs.timeout}")public String DB_BSCS_TIMEOUT;
	
	
	@Value("${cargadatos.motprom.codigo.idt1}")public String CARGA_ALTA_COD_IDT1;
	@Value("${cargadatos.motprom.mensaje.idt1}")public String CARGA_ALTA_MSJ_IDT1;
	@Value("${cargadatos.motprom.codigo.idt2}")public String CARGA_ALTA_COD_IDT2;
	@Value("${cargadatos.motprom.mensaje.idt2}")public String CARGA_ALTA_MSJ_IDT2;
	
	@Value( "${desactiva.bono.cambio.plan.reintento}" )  public String reintento;
	
	@Value("${codigo.idf1}")public String codigoIdf1;
	@Value("${mensaje.idf1}")public String mensajeIdf1;	
	@Value("${codigo.idf2}")public String codigoIdf2;
	@Value("${mensaje.idf2}")public String mensajeIdf2;
	
	//BSCSIX
	@Value("${oracle.jdbc.bscsix.driver}")public String DB_DRIVER ;
	@Value("${oracle.jdbc.bscsix.conexion.bscs}")public String DB_CONNECTION ;
	@Value("${oracle.jdbc.bscsix.usuario.bscs}")public String DB_USER ;
	@Value("${oracle.jdbc.bscsix.password.bscs}")public String DB_PASSWORD ;

}