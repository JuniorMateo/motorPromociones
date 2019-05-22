package claro.com.pe.cambioplan.dao;

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

import claro.com.pe.cambioplan.bean.BonoProgram;
import claro.com.pe.cambioplan.bean.LisBonoProgram;
import claro.com.pe.cambioplan.bean.ResponseAuditoria;
import claro.com.pe.cambioplan.exception.DBException;
import claro.com.pe.cambioplan.exception.DBNoDisponibleException;
import claro.com.pe.cambioplan.exception.DBTimeoutException;
import claro.com.pe.cambioplan.util.Constantes;
import claro.com.pe.cambioplan.util.PropertiesExternos;
import claro.com.pe.cambioplan.util.Util;

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
	public LisBonoProgram obtenerCambioPlan(String mensajeTransaccion, String pi_fec_proc, String pi_dias,int tipoDesactivacion)
			throws DBTimeoutException, DBNoDisponibleException, DBException {
		
		String metodo = "obtenerCambioPlan";
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo);
		long tiempoInicial = System.currentTimeMillis();

		String OWNER = null;
		String PROCEDURE = null;
		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		LisBonoProgram lista=new LisBonoProgram();
		List<BonoProgram> listabono;
	
		try {
			switch(tipoDesactivacion){
			case 2:PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANUPGRADE;break;
			case 4:PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANLINEA;break;
			case 5:PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANDOWNGRADE;break;	
			}

			
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
						.addValue("pi_fec_proc",Util.deStringADate(mensajeTransaccion, pi_fec_proc))
						.addValue("pi_dias",pi_dias);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_fec_proc", OracleTypes.DATE),
										new SqlParameter("pi_dias", OracleTypes.INTEGER),
										new SqlOutParameter("po_programcur",OracleTypes.CURSOR, new RowMapper<BonoProgram>() {
											@Override
											public BonoProgram mapRow(ResultSet rs, int arg1) throws SQLException {
												BonoProgram bono = new BonoProgram();												
												bono.setBonoid(rs.getString("bono_id"));
												bono.setContractid(rs.getString("contract_id"));
												bono.setDnnum(rs.getString("dn_num"));
												bono.setPobasic(rs.getString("po_basic"));
												bono.setContractidExterno(rs.getString("contract_id_ext"));
												bono.setAmount(rs.getBigDecimal("fixed_charge"));
												bono.setProgramid(rs.getString("program_id"));
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
						
				
						lista.setCodigoError(String.valueOf(objetJdbcCall.get("po_cod_err")));
						lista.setDescripcionError(String.valueOf(objetJdbcCall.get("po_des_err")));
						listabono = (List<BonoProgram>) objetJdbcCall.get("po_programcur");
						
						LOGGER.info(mensajeTransaccion + " [SIZE LIST] = " + listabono.size());
						
						lista.setListabono(listabono);
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTEVACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente, OWNER + "." + PROCEDURE,
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), OWNER + "." + PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.CARGA_ALTA_COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, OWNER + "." + PROCEDURE, 
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
		
		return lista;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public LisBonoProgram CambioPlanNoMatriz(String mensajeTransaccion,String pi_fec_proc,String pi_dias,int tipoDesactivacion)
			   throws DBException{
		
		String metodo = "CambioPlanNoMatriz";
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo);
		

		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio del método Obtener Cambio Plan");

		String OWNER = null;
		String PROCEDURE = null;
		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		LisBonoProgram lista=new LisBonoProgram();
		List<BonoProgram> listabono;
	
		try {
			switch(tipoDesactivacion){
			case 7:PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANNOMATRIZ;break;
			case 6:PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.OBTENERCAMBIOPLANUPGRADENOMATRIZ;break;
			}

			
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
						.addValue("pi_fec_proc",Util.deStringADate(mensajeTransaccion, pi_fec_proc))
						.addValue("pi_dias",pi_dias);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_fec_proc", OracleTypes.DATE),
										new SqlParameter("pi_dias", OracleTypes.INTEGER),
										new SqlOutParameter("po_programcur",OracleTypes.CURSOR, new RowMapper<BonoProgram>() {
											@Override
											public BonoProgram mapRow(ResultSet rs, int arg1) throws SQLException {
												BonoProgram bono = new BonoProgram();												
												bono.setBonoid(rs.getString("bono_id"));
												bono.setContractid(rs.getString("contract_id"));
												bono.setDnnum(rs.getString("dn_num"));
												bono.setPobasic(rs.getString("po_basic"));
												bono.setContractidExterno(rs.getString("contract_id_ext"));
												bono.setCampania(rs.getString("campania"));
												bono.setAmount(rs.getBigDecimal("fixed_charge"));
												bono.setProgramid(rs.getString("program_id"));
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
						
				
						lista.setCodigoError(String.valueOf(objetJdbcCall.get("po_cod_err")));
						lista.setDescripcionError(String.valueOf(objetJdbcCall.get("po_des_err")));
						listabono = (List<BonoProgram>) objetJdbcCall.get("po_programcur");
						
						LOGGER.info(mensajeTransaccion + " [SIZE LIST] = " + listabono.size());
						lista.setListabono(listabono);
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTEVACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente, OWNER + "." + PROCEDURE, 
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), OWNER + "." + PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.CARGA_ALTA_COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, OWNER + "." + PROCEDURE, 
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		return lista;
	}

	@Override
	public ResponseAuditoria actualizarBono(String mensajeTransaccion, String programid,String observacion)
			throws DBTimeoutException, DBNoDisponibleException, DBException {
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
			LOGGER.info(mensajeTransaccion + " [pi_program_id]= ["+ programid + "]");
			LOGGER.info(mensajeTransaccion + " [pi_observacion]= ["+ observacion + "]");

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
			LOGGER.info(mensajeTransaccion + " - Fin del método registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
	}

	
	@Override
	 public ResponseAuditoria actualizarCambioPlanNoMatriz(String mensajeTransaccion,String programid,String pi_campania,String piBasic,String obs)
			   throws DBTimeoutException, DBNoDisponibleException, DBException{
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio del Actualizar Bono");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		ResponseAuditoria responseAuditoria = new ResponseAuditoria();
	
		try {

			PROCEDURE =	propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+propertiesExternos.ACTUALIZARCAMBIOPLANNOMATRIZ;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [pi_programid]= ["+ programid + "]");
			LOGGER.info(mensajeTransaccion + " [pi_campania]= ["+ pi_campania + "]");
			LOGGER.info(mensajeTransaccion + " [pi_basic]= ["+ piBasic + "]");

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
						.addValue("pi_programid",programid)
						.addValue("pi_basic",piBasic)
						.addValue("pi_campania",pi_campania)
						.addValue("pi_observacion",obs);;
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_programid", OracleTypes.INTEGER),
										new SqlParameter("pi_campania", OracleTypes.VARCHAR),
										new SqlParameter("pi_basic", OracleTypes.VARCHAR),
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
			LOGGER.info(mensajeTransaccion + " - Fin del método registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
	 }
	
	
	@Override
	public ResponseAuditoria ejecutarCambioPlanPrepago(String mensajeTransaccion, String pi_fec_proc, String pi_dias) throws  DBException {
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio del Actualizar Bono");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		ResponseAuditoria responseAuditoria = new ResponseAuditoria();
	
		try {

			PROCEDURE =	propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+
					propertiesExternos.OBTENERCAMBIOPLANPREPAGO;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
								.addValue("pi_fec_proc",Util.deStringADate(mensajeTransaccion, pi_fec_proc))
								.addValue("pi_dias",pi_dias);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_fec_proc", OracleTypes.DATE),
										new SqlParameter("pi_dias", OracleTypes.INTEGER),
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
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente,  PROCEDURE, MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT1
						.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS),  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.CARGA_ALTA_COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
	}

		
	
		
	

}

