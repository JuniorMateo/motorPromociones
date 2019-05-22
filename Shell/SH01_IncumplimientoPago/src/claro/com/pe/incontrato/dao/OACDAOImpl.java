package claro.com.pe.incontrato.dao;

import java.sql.SQLTimeoutException;
import java.text.MessageFormat;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import claro.com.pe.incontrato.bean.PagoPuntual;
import claro.com.pe.incontrato.exception.DBException;
import claro.com.pe.incontrato.util.Constantes;
import claro.com.pe.incontrato.util.PropertiesExternos;
import oracle.jdbc.internal.OracleTypes;

@Repository
public class OACDAOImpl implements OACDAO{

	public static final Logger LOGGER = Logger
			.getLogger(OACDAOImpl.class);
	
	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Autowired
	private DataSource oac;
	
	@Override
	public PagoPuntual consultarPagoPuntual(String mensajeTransaccion, String customeId, int numeroMeses)
			throws DBException {
	
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio Consultar Pago Puntual OAC");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		PagoPuntual  pagoPuntual=new PagoPuntual();
	
		try {

			PROCEDURE =	propertiesExternos.PCK_OACCONS_PAG+"."+propertiesExternos.OAC_PAGOPUNTUAL;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_OAC_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_OAC_NOMBRE + ".Detalle: " + propertiesExternos.ORACLEOWNER_OAC);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [CUSTOMER_ID]= ["+ customeId + "]");
			LOGGER.info(mensajeTransaccion + " [CANTIDAD_MESES]= ["+ numeroMeses + "]");

						objJdbcCall = new SimpleJdbcCall(oac);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
						.addValue("piv_CUSTOMER_ID",customeId)
						.addValue("pin_CANTIDAD_MESES",numeroMeses);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLEOWNER_OAC)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("piv_CUSTOMER_ID", OracleTypes.VARCHAR),
										new SqlParameter("pin_CANTIDAD_MESES", OracleTypes.NUMBER),
										new SqlOutParameter("pov_COD_RESPUESTA", OracleTypes.VARCHAR),
	                                	new SqlOutParameter("pov_MSG_RESPUESTA", OracleTypes.VARCHAR),
	        							new SqlOutParameter("pov_INDICADOR_PP", OracleTypes.NUMBER)
										).execute(objParametrosIN);
						
						LOGGER.info(mensajeTransaccion + " Se ejecuto exitosamente el SP");
						LOGGER.info(mensajeTransaccion + " [OUTPUT]: " );
						LOGGER.info(mensajeTransaccion + " [po_cod_err] = " + objetJdbcCall.get("pov_COD_RESPUESTA") );
						LOGGER.info(mensajeTransaccion + " [po_des_err] = " + objetJdbcCall.get("pov_MSG_RESPUESTA") );
						LOGGER.info(mensajeTransaccion + " [pov_INDICADOR_PP] = " + objetJdbcCall.get("pov_INDICADOR_PP") );


						pagoPuntual.setCodigoRespuesta(String.valueOf(objetJdbcCall.get("pov_COD_RESPUESTA")));
						pagoPuntual.setMensajeRespuesta(String.valueOf(objetJdbcCall.get("pov_MSG_RESPUESTA")));
						pagoPuntual.setIndicador(Integer.parseInt(String.valueOf(objetJdbcCall.get("pov_INDICADOR_PP"))));
						return pagoPuntual;
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CADENAVACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente,  PROCEDURE, MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT1,  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.CARGA_ALTA_COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLEOWNER_OAC));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLEOWNER_OAC), e.getMessage()), e );
			}
		}finally{
			
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
	}

}
