package claro.com.pe.bloqueosusp.util;

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
	
	@Value("${promss.cambio.bloqueo.pago}")public String BSCSSSBLOQUEOSUSPENSION;
	@Value("${promss.su.bloqueo.suspension}")public String BSCSSUBLOQUEOSUSPENSION;
	
	@Value("${db.bscs.nombre}")
	public String DB_BSCS_NOMBRE;
	
	@Value("${db.bscs.timeout}")
	public String DB_BSCS_TIMEOUT;
	
	@Value("${cod.idt1.motprom}")public String COD_IDT1;
	@Value("${msj.idt1.motprom}")public String MSJ_IDT1;
	@Value("${cod.idt2.motprom}")public String COD_IDT2;
	@Value("${msj.idt2.motprom}")public String MSJ_IDT2;
	@Value("${cod.idt3.motprom}")public String COD_IDT3;
	@Value("${msj.idt3.motprom}")public String MSJ_IDT3;
	@Value("${cod.idt4.motprom}")public String COD_IDT4;
	@Value("${msj.idt4.motprom}")public String MSJ_IDT4;
	
	
	
	@Value("${codigo.idf1}")
	public String codigoIdf1;
	
	@Value("${mensaje.idf1}")
	public String mensajeIdf1;
	
	@Value( "${desactiva.bono.cambio.bloqueo.reintento}" )  public String reintento;
	
	//Codigo de criterios a desactivar por suspension 
	@Value( "${desactiva.bono.suspencion.faltapago}" )  public String SUSPENSIONPORPAGO;
	@Value( "${desactiva.bono.suspencion.robo}" )  public String SUSPENSIONPORROBO;
	@Value( "${desactiva.bono.suspencion.fruade}" )  public String SUSPENSIONPORFRAUDE;
	@Value( "${desactiva.bono.suspencion.solicutd.cliente}" )  public String SUSPENSIONPORSOLICITUD;
	
	
	//Codigo razon de  suspension 
	@Value( "${motivo.bono.suspencion.falta.pago}" )  public String SUSPENSIONPORPAGOREASONCOD;
	@Value( "${motivo.bono.suspencion.robo}" )  public String SUSPENSIONPORROBOREASONCOD;
	@Value( "${motivo.bono.suspencion.fruade}" )  public String SUSPENSIONPORFRAUDREASONECOD;
	@Value( "${motivo.bono.suspencion.solicutd.cliente}" )  public String SUSPENSIONPORSOLICITUDREASONCOD;
	
	//Descripcion razon de  suspension 
	@Value( "${motivo.desc.suspencion.falta.pago}" )  public String SUSPENSIONPORPAGOREASONDES;
	@Value( "${motivo.desc.suspencion.robo}" )  public String SUSPENSIONPORROBOREASONDES;
	@Value( "${motivo.desc.suspencion.fruade}" )  public String SUSPENSIONPORFRAUDREASONDES;
	@Value( "${motivo.desc.suspencion.solicutd.cliente}" )  public String SUSPENSIONPORSOLICITUDREASONDES;
	
	//Estado de suspension 
	@Value( "${tickshdes.estado.bono.full.suspendido}" )  public String FULLSUSPENSION;
	@Value( "${tickshdes.estado.bono.part.suspendido}" )  public String PARTSUSPENSION;
	@Value( "${tickstatus.estado.bono}" )  public String OPENESTADO; 
	@Value( "${tickstatus.estado.bon.request}" )  public String OPENESTADOREQUEST; 
	
	
	// WS INT-CAA-0013
	@Value( "${intcop.0013.getStatusReasons.url}" )  public String getStatusReasons0013URL;
	@Value( "${intcop.0013.getStatusReasons.url.timeout}" )  public String getStatusReasons0013Timeout;
	@Value( "${intcop.0013.getStatusReasons.url.conexion}" )  public String getStatusReasons0013ConnectionTimeout;
	@Value( "${intcop.0013.getStatusReasons.auth.username}" )  public String usernameStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.auth.password}" )  public String passwordStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.canal}" )  public String canalStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.country}" )  public String countryStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.language}" )  public String lenguajeStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.consumer}" )  public String consumerStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.system}" )  public String systemStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.modulo}" )  public String moduloStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.userid}" )  public String useridStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.dispositivo}" )  public String dispositivoStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.wslp}" )  public String wslpStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.msgtype}" )  public String msgtypeStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.1}" )  public String operacion1StatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.usuario.aplicacion}" )  public String nombreAplicacionStatusReasons0013;
	@Value( "${intcop.0013.getStatusReasons.idApplication}" )  public String usuarioAplicacionStatusReasons0013;

}