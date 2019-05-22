package claro.com.pe.desa.titularidad.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oracle.jdbc.internal.OracleTypes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import claro.com.pe.desa.titularidad.bean.Bono;
import claro.com.pe.desa.titularidad.bean.ListaBono;
import claro.com.pe.desa.titularidad.bean.ResponseAuditoria;
import claro.com.pe.desa.titularidad.exception.DBException;
import claro.com.pe.desa.titularidad.util.Constantes;
import claro.com.pe.desa.titularidad.util.PropertiesExternos;


@Repository
public class MotorPromDAOImpl implements MotorPromDAO{
	
	public static final Logger LOGGER = Logger
			.getLogger(MotorPromDAOImpl.class);
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Autowired
	private DataSource bscs;

	@SuppressWarnings("unchecked")
	@Override
	public ListaBono obtenerBonosAdesactivarTitularidad(String mensajeTransaccion,int tipoDesactivacion)
			throws DBException {
		
		
		String metodo = "obtenerCambioPlan Titularidad";
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo);
		long tiempoInicial = System.currentTimeMillis();

		String OWNER = null;
		String PROCEDURE = null;
		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		List<Bono> listabono;
		ListaBono listass=new ListaBono();
	
		try {
		     PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANDNIARUC;
		     
		     switch(tipoDesactivacion){
				case 12:PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANDNIARUC;break;
				case 13:PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANTITULARIDAD;break;
				}

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));		
						objParametrosIN = new MapSqlParameterSource();
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlOutParameter("po_programcur",OracleTypes.CURSOR, new RowMapper<Bono>() {
											@Override
											public Bono mapRow(ResultSet rs, int arg1) throws SQLException {
												Bono bono = new Bono();												
												bono.setProgramid(rs.getString("program_id"));
												bono.setNumerodeDocumento(rs.getString("documentonum"));
												bono.setLinea(rs.getString("dn_num"));
												bono.setTipodocu(rs.getString("doc_type"));
												return bono;
											}
										}),
	        							new SqlOutParameter("po_cod_err", OracleTypes.INTEGER),
	        							new SqlOutParameter("po_des_err", OracleTypes.VARCHAR)
	        							
	        							
										).execute(objParametrosIN);
						
						LOGGER.info(mensajeTransaccion + " Se ejecuto exitosamente el SP");
						LOGGER.info(mensajeTransaccion + " [OUTPUT]: " );
						LOGGER.info(mensajeTransaccion + " [po_cod_err] = " + objetJdbcCall.get("po_cod_err") );
						LOGGER.info(mensajeTransaccion + " [po_des_err] = " + objetJdbcCall.get("po_des_err") );
						
				
						listass.setCodigoRespuesta(String.valueOf(objetJdbcCall.get("po_cod_err")));
						listass.setMensajeRespuesta(String.valueOf(objetJdbcCall.get("po_des_err")));
						listabono = (List<Bono>) objetJdbcCall.get("po_programcur");
						
						LOGGER.info(mensajeTransaccion + " [SIZE LIST] = " + listabono.size());
		
						listass.setListabono(listabono);
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTEVACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente, OWNER + "." + PROCEDURE, MessageFormat.format(
						propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), OWNER + "." + PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, OWNER + "." + PROCEDURE, MessageFormat.format(
						propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del m�todo registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
		
		return listass;
	}

	
	@Override
	public ResponseAuditoria actualizarBono(String mensajeTransaccion, String programid,String observacion)
			throws  DBException {
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio del Actualizar Bono");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		ResponseAuditoria responseAuditoria = new ResponseAuditoria();
	
		try {

			PROCEDURE =	propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+
					propertiesExternos.BSCS_UPDATE_CAMBIO_PLAN;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [pi_contract_id]= ["+ programid + "]");
			LOGGER.info(mensajeTransaccion + " [pi_bono_id]= ["+ observacion + "]");

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
						.addValue("pi_program_id",programid)
						.addValue("pi_observacion",observacion);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_program_id", OracleTypes.INTEGER),
										new SqlParameter("pi_observacion", OracleTypes.VARCHAR),
	                                	new SqlOutParameter("po_cod_err", OracleTypes.INTEGER),
	        							new SqlOutParameter("po_des_err", OracleTypes.VARCHAR)
										).execute(objParametrosIN);
						
						LOGGER.info(mensajeTransaccion + " Se ejecuto exitosamente el SP");
						LOGGER.info(mensajeTransaccion + " [OUTPUT]: " );
						LOGGER.info(mensajeTransaccion + " [po_cod_err] = " + objetJdbcCall.get("po_cod_err") );
						LOGGER.info(mensajeTransaccion + " [po_des_err] = " + objetJdbcCall.get("po_des_err") );
						
						
						
						responseAuditoria.setCodigoRespuesta(String.valueOf(objetJdbcCall.get("po_cod_err")));
						responseAuditoria.setMensajeRespuesta(String.valueOf(objetJdbcCall.get("po_des_err")));

						return responseAuditoria;
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTEVACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente,  PROCEDURE, 
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS),  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.CARGA_ALTA_COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del m�todo registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
	}

	

		
		
	

}
