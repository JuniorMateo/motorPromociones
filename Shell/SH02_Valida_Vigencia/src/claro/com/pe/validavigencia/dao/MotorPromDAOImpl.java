package claro.com.pe.validavigencia.dao;

import java.sql.SQLTimeoutException;
import java.text.MessageFormat;
import java.util.Map;
import javax.sql.DataSource;
import oracle.jdbc.internal.OracleTypes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import claro.com.pe.validavigencia.bean.ResponseAuditoria;
import claro.com.pe.validavigencia.exception.DBException;
import claro.com.pe.validavigencia.util.Constantes;
import claro.com.pe.validavigencia.util.Util;
import claro.com.pe.validavigencia.util.PropertiesExternos;

@Repository
public class MotorPromDAOImpl implements MotorPromDAO{
	
	public static final Logger LOGGER = Logger
			.getLogger(MotorPromDAOImpl.class);
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Autowired
	private DataSource bscs;

	@Override
	public ResponseAuditoria  ejecutarTransaccionFechaVigencia(String mensajeTransaccion, String pi_fec_proc, String pi_dias,String observacion) throws DBException {

		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio del método ejecutar Desactivacion Fecha Vigencia");

		String PROCEDURE = null;
		Util util = new Util();
		
		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
	
		ResponseAuditoria responseAuditoria = new ResponseAuditoria();
		
		try {

			PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+propertiesExternos.BSCS_SP_REG_ALTA;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [pi_fec_proc]= ["+ pi_fec_proc + "]");	
			LOGGER.info(mensajeTransaccion + " [pi_dias]= ["+ pi_dias + "]");

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
						.addValue("pi_fec_proc",util.deStringADate(mensajeTransaccion, pi_fec_proc))
						.addValue("pi_dias",pi_dias)
						.addValue("pi_obervacion",observacion);

						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_fec_proc", OracleTypes.DATE),
										new SqlParameter("pi_dias", OracleTypes.INTEGER),
										new SqlParameter("pi_obervacion", OracleTypes.VARCHAR),
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
			errorMsg = e + Constantes.CONSTANTE_VACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente,  PROCEDURE, MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT1,  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.CARGA_ALTA_COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - FIN del método ejecutar Desactivacion Fecha Vigencia : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
		
		
	}

		
		
	

}

