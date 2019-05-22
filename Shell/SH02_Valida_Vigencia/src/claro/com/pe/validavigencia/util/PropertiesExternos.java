package claro.com.pe.validavigencia.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesExternos {

    @Value("${log4j.dir}")
    public String LOG4J_DIR;
    
	@Value("${oracle.jdbc.owner.bscs}")
	public String ORACLE_JDBC_OWNER_BSCS;
	
	@Value("${bscs.pkg.motprom}")
	public String BSCS_PKG_CARGA_ALTA;
	
	@Value("${bscs.sp.registra}")
	public String BSCS_SP_REG_ALTA;
	
	@Value("${db.bscs.nombre}")
	public String DB_BSCS_NOMBRE;
	
	@Value("${db.bscs.timeout}")
	public String DB_BSCS_TIMEOUT;
	
	@Value("${cargadatos.motprom.codigo.idt1}")public String CARGA_ALTA_COD_IDT1;
	@Value("${cargadatos.motprom.mensaje.idt1}")public String CARGA_ALTA_MSJ_IDT1;
	@Value("${cargadatos.motprom.codigo.idt2}")public String CARGA_ALTA_COD_IDT2;
	@Value("${cargadatos.motprom.mensaje.idt2}")public String CARGA_ALTA_MSJ_IDT2;
	
	
	@Value("${codigo.idf1}")public String codigoIdf1;
	@Value("${mensaje.idf1}")public String mensajeIdf1;	
	@Value("${codigo.idf2}")public String codigoIdf2;
	@Value("${mensaje.idf2}")public String mensajeIdf2;
	@Value( "${desactiva.bono.vigencia.reintento}" )  public String reintento;

}