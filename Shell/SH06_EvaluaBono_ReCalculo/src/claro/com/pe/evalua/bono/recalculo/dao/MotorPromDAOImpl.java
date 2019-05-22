package claro.com.pe.evalua.bono.recalculo.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import oracle.jdbc.internal.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.STRUCT;
import oracle.sql.StructDescriptor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.support.AbstractSqlTypeValue;
import org.springframework.stereotype.Repository;

import claro.com.pe.evalua.bono.recalculo.bean.BonoProgram;
import claro.com.pe.evalua.bono.recalculo.bean.BonoRecalcula;
import claro.com.pe.evalua.bono.recalculo.bean.BonoRecalculo;
import claro.com.pe.evalua.bono.recalculo.bean.LisBonoProgram;
import claro.com.pe.evalua.bono.recalculo.bean.ListMaxFamilia;
import claro.com.pe.evalua.bono.recalculo.bean.ListaBonoRecalculo;
import claro.com.pe.evalua.bono.recalculo.bean.ResponseAuditoria;
import claro.com.pe.evalua.bono.recalculo.bean.Subscriptions;
import claro.com.pe.evalua.bono.recalculo.exception.DBException;
import claro.com.pe.evalua.bono.recalculo.util.Constantes;
import claro.com.pe.evalua.bono.recalculo.util.PropertiesExternos;
import claro.com.pe.evalua.bono.recalculo.util.Util;

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
	public LisBonoProgram obtenerRecalcularEvaluacion(String mensajeTransaccion)
				throws  DBException {
		
		String metodo = "obtener Cambio plan Replica";
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

			PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.BSCS_RECALCULAR;
			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource();
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(		
										new SqlOutParameter("po_lineas",OracleTypes.CURSOR, new RowMapper<BonoProgram>() {
											@Override
											public BonoProgram mapRow(ResultSet rs, int arg1) throws SQLException {
												BonoProgram bono = new BonoProgram();	
												bono.setProgramid(rs.getString("program_id"));
												bono.setContractId(rs.getString("contract_id"));
												bono.setBonoId(rs.getString("bono_id"));
												bono.setEstadoBono(rs.getString("est_bono"));
												bono.setEstadoProceso(rs.getString("est_proc"));
												bono.setPobono(rs.getString("po_bono"));
												bono.setPoontop(rs.getString("po_ontop"));
												bono.setOrderId(rs.getString("order_id"));
												bono.setFecactbono(rs.getString("fec_act_bono"));
												bono.setFecvigencia(rs.getString("fec_vigencia"));
												bono.setPoBasic(rs.getString("po_basic"));
												bono.setPeriodo(rs.getString("periodo"));
												bono.setBonoperiodico(rs.getString("bono_periodico"));
												bono.setTipoopera(rs.getString("tipo_opera"));
												bono.setEstom(rs.getString("est_om"));
												bono.setCampania(rs.getString("campania"));
												bono.setContractidExt(rs.getString("contract_id_ext"));
												bono.setCustomeridExt(rs.getString("customer_id_ext"));
												bono.setBillingAccount(rs.getString("billing_account"));
												bono.setLinea(rs.getString("dn_num"));
												bono.setDocType(rs.getString("doc_type"));
												bono.setDocumentoNum(rs.getString("documentonum"));
												bono.setCargoFijo(rs.getBigDecimal("fixed_charge"));
												bono.setShoppingcartid(rs.getString("shopping_cart_id"));
												bono.setActivacion("activacion");
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
						listabono = (List<BonoProgram>) objetJdbcCall.get("po_lineas");
						
						LOGGER.info(mensajeTransaccion + " [SIZE LIST] = " + listabono.size());
						
						lista.setListabono(listabono);
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTE_VACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(mensajeTransaccion+propertiesExternos.MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT1, nombreComponente, OWNER + "." + PROCEDURE, MessageFormat.format(propertiesExternos.MSJ_IDT1, OWNER + "." + PROCEDURE), e );
			}
			else{
				LOGGER.error(mensajeTransaccion+propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT2, nombreComponente, OWNER + "." + PROCEDURE,
						MessageFormat.format(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método Obtener- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
		
		return lista;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ListaBonoRecalculo obtenerBonoInfoRecalculo(String mensajeTransaccion,String plan,String motivo,String campania)
				throws  DBException {
		
		String metodo = "Obtener Informacion Bono";
		String mensajeLog = mensajeTransaccion + "[" + metodo + "] ";
		
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo);
		long tiempoInicial = System.currentTimeMillis();

		String OWNER = null;
		String PROCEDURE = null;
		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		ListaBonoRecalculo lista=new ListaBonoRecalculo();
		List<BonoRecalculo> listabono;
	    Date fechaAcctual=new Date();
	    Date fechaAcctual2;
	   
		try {
			 String dato=Util.getDateFormato(fechaAcctual, Constantes.FORMATOFECHA4);

			PROCEDURE = propertiesExternos.BSCSPACKAGEEVALPROCESS+"."+ propertiesExternos.BSCSOBTENERDATOSBONO;
			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");
			LOGGER.info(mensajeTransaccion + " [pi_campania]= ["+ campania + "]");
			LOGGER.info(mensajeTransaccion + " [pi_motivo_act]= ["+ motivo + "]");
			LOGGER.info(mensajeTransaccion + " [pi_plan]= ["+ plan + "]"); fechaAcctual2=Util.deStringADate(dato);
			LOGGER.info(mensajeTransaccion + " [pi_fec_proc]= ["+ fechaAcctual2+ "]");

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
								.addValue("pi_plan",plan)
								.addValue("pi_motivo_act",motivo)
								.addValue("pi_fec_proc",fechaAcctual2)
								.addValue("pi_campania",campania)
								.addValue("pi_flag_base",1);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_plan", OracleTypes.VARCHAR),
										new SqlParameter("pi_motivo_act", OracleTypes.VARCHAR),
										new SqlParameter("pi_fec_proc", OracleTypes.DATE),
										new SqlParameter("pi_campania", OracleTypes.VARCHAR),
										new SqlParameter("pi_flag_base", OracleTypes.INTEGER),
										new SqlOutParameter("po_cur_bonos",OracleTypes.CURSOR, new RowMapper<BonoRecalculo>() {
											@Override
											public BonoRecalculo mapRow(ResultSet rs, int arg1) throws SQLException {
												BonoRecalculo bono = new BonoRecalculo();																	
												bono.setBonoId(rs.getString("bono_id"));
												bono.setPobono(rs.getString("po_bono"));
												bono.setPobononame(rs.getString("po_bono_name"));
												bono.setBonodes(rs.getString("bono_des"));
												bono.setGrupo(rs.getString("grupo"));							
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
						listabono = (List<BonoRecalculo>) objetJdbcCall.get("po_cur_bonos");
						
						
						LOGGER.info(mensajeTransaccion + " [SIZE LIST] = " + listabono.size());
						
						lista.setListaRecalculo(listabono);
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTE_VACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(mensajeTransaccion+propertiesExternos.MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT1, nombreComponente, OWNER + "." + PROCEDURE, MessageFormat.format(propertiesExternos.MSJ_IDT1, OWNER + "." + PROCEDURE), e );
			}
			else{
				LOGGER.error(mensajeTransaccion+propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT2, nombreComponente, OWNER + "." + PROCEDURE,
						MessageFormat.format(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + "Fin Obtener Informacion Bono - Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		return lista;
	}
	
	
	@Override
	public ResponseAuditoria insertarBonoInterno(String mensajeTransaccion, BonoRecalcula bono)
			throws DBException {
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio insertar Bono Interno");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		ResponseAuditoria responseAuditoria = new ResponseAuditoria();
	
		try {

			PROCEDURE =	propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+propertiesExternos.BSCS_INSERT_RECALCULO;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [pi_linea]= ["+ bono.getLinea() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_bono_id]= ["+ bono.getBonoId() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_po_bono]= ["+ bono.getPobono() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_po_ontop]= ["+bono.getPontop() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_po_plan]= ["+ bono.getPoplan() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_order_id]= ["+bono.getOrderid() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_fec_act_bono]= ["+ bono.getFecactbono() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_fec_vigencia]= ["+ bono.getFecvigencia() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_est_bono]= ["+ bono.getEstbono() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_est_proc]= ["+ bono.getEstproc() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_periodo]= ["+ bono.getPeriodo() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_tipo_bono]= ["+bono.getTipobono()+ "]");
			LOGGER.info(mensajeTransaccion + " [pi_ciclo_fact]= ["+ bono.getCiclofact() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_bono_periodico]= ["+ bono.getBonoperiodico() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_tipo_opera]= ["+ bono.getTipoopera() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_shopping_cart_id]= ["+ bono.getShoppingcartid()+ "]");
			LOGGER.info(mensajeTransaccion + " [pi_activacion]= ["+ bono.getActivacion() + "]");
			
						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						Date fechaActual=new Date();
						
						objParametrosIN = new MapSqlParameterSource()	
								.addValue("pi_bono_id",bono.getBonoId())
								.addValue("pi_po_bono",bono.getPobono())
								.addValue("pi_po_ontop",bono.getPontop())
								.addValue("pi_po_plan",bono.getPoplan() )
								.addValue("pi_order_id",bono.getOrderid())
								.addValue("pi_fec_act_bono",bono.getFecactbono())
								.addValue("pi_fec_vigencia",bono.getFecvigencia())
								.addValue("pi_est_bono",bono.getEstbono())
								.addValue("pi_est_proc",bono.getEstproc())
								.addValue("pi_periodo",bono.getPeriodo() )
								.addValue("pi_tipo_bono",bono.getTipobono())
								.addValue("pi_ciclo_fact",bono.getCiclofact())
								.addValue("pi_bono_periodico",bono.getBonoperiodico())
								.addValue("pi_tipo_opera",bono.getTipoopera())
								.addValue("pi_fec_crea",fechaActual)
								.addValue("pi_fec_modi", bono.getFecmodi())
								.addValue("pi_est_om",bono.getEstom())
								.addValue("pi_fec_crea_om", fechaActual )
								.addValue("pi_fec_ini_ejec_om",fechaActual)
								.addValue("pi_fec_fin_ejec_om",fechaActual)
								.addValue("pi_shopping_cart_id",bono.getShoppingcartid())
								.addValue("pi_activacion",bono.getActivacion())
								.addValue("pi_linea",bono.getLinea());
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_bono_id", OracleTypes.INTEGER),
										new SqlParameter("pi_po_bono", OracleTypes.VARCHAR),
										new SqlParameter("pi_po_ontop", OracleTypes.VARCHAR),
										new SqlParameter("pi_po_plan", OracleTypes.VARCHAR),
										new SqlParameter("pi_order_id", OracleTypes.VARCHAR),
										new SqlParameter("pi_fec_act_bono", OracleTypes.DATE),
										new SqlParameter("pi_fec_vigencia", OracleTypes.DATE),
										new SqlParameter("pi_est_bono", OracleTypes.CHAR),
										new SqlParameter("pi_est_proc", OracleTypes.CHAR),
										new SqlParameter("pi_periodo", OracleTypes.INTEGER),
										new SqlParameter("pi_tipo_bono", OracleTypes.VARCHAR),
										new SqlParameter("pi_ciclo_fact", OracleTypes.VARCHAR),
										new SqlParameter("pi_bono_periodico", OracleTypes.INTEGER),
										new SqlParameter("pi_tipo_opera", OracleTypes.VARCHAR),
										new SqlParameter("pi_fec_crea", OracleTypes.DATE),
										new SqlParameter("pi_fec_modi", OracleTypes.DATE),
										new SqlParameter("pi_est_om", OracleTypes.NUMBER),
										new SqlParameter("pi_fec_crea_om", OracleTypes.DATE),
										new SqlParameter("pi_fec_ini_ejec_om", OracleTypes.DATE),
										new SqlParameter("pi_fec_fin_ejec_om", OracleTypes.DATE),
										new SqlParameter("pi_shopping_cart_id", OracleTypes.VARCHAR),
										new SqlParameter("pi_activacion", OracleTypes.VARCHAR),
										new SqlParameter("pi_linea", OracleTypes.VARCHAR),
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
			LOGGER.error(mensajeTransaccion+"Error :"+e.getMessage());
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS)+e.getMessage());
				throw new DBException( propertiesExternos.COD_IDT1, nombreComponente,  PROCEDURE, MessageFormat.format(propertiesExternos.MSJ_IDT1,  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS)+e.getMessage());
				throw new DBException( propertiesExternos.COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método Obtener- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
	}
	
	
	@Override
	public ResponseAuditoria insertarContratoInterno(String mensajeTransaccion, BonoProgram bono)
			throws DBException {
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio RecalcularEvaluacion");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		ResponseAuditoria responseAuditoria = new ResponseAuditoria();
	
		try {

			PROCEDURE =	propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+propertiesExternos.BSCSINSERTCONTRATO;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [pi_customer_id_ext]= ["+ bono.getCustomeridExt() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_billing_account]= ["+ bono.getBillingAccount()+ "]");
			LOGGER.info(mensajeTransaccion + " [pi_po_basic]= ["+bono.getPoBasic()+ "]");
			LOGGER.info(mensajeTransaccion + " [pi_cargo_fijo]= ["+ bono.getCargoFijo()+ "]");
			LOGGER.info(mensajeTransaccion + " [pi_linea]= ["+bono.getLinea()+ "]");
			LOGGER.info(mensajeTransaccion + " [pi_campania]= ["+ bono.getCampania()+ "]");
			LOGGER.info(mensajeTransaccion + " [pi_contract_id_ext]= ["+ bono.getContractidExt() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_tipo_documento]= ["+ bono.getDocType() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_numero_documento]= ["+bono.getDocumentoNum() + "]");
			LOGGER.info(mensajeTransaccion + " [pi_billcycle]= ["+ bono.getCiclofactbase() + "]");
			
						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()	
								.addValue("pi_customer_id_ext",bono.getCustomeridExt())
								.addValue("pi_billing_account",bono.getBillingAccount())
								.addValue("pi_po_basic",bono.getPoBasic() )
								.addValue("pi_cargo_fijo",bono.getCargoFijo())
								.addValue("pi_linea",bono.getLinea())
								.addValue("pi_campania",bono.getCampania())
								.addValue("pi_contract_id_ext",bono.getContractidExt())
								.addValue("pi_tipo_documento", bono.getDocType())
								.addValue("pi_numero_documento",bono.getDocumentoNum())
								.addValue("pi_billcycle", bono.getCiclofactbase() );
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_customer_id_ext", OracleTypes.VARCHAR),
										new SqlParameter("pi_billing_account", OracleTypes.VARCHAR),
										new SqlParameter("pi_po_basic", OracleTypes.VARCHAR),
										new SqlParameter("pi_cargo_fijo", OracleTypes.NUMBER),
										new SqlParameter("pi_linea", OracleTypes.VARCHAR),
										new SqlParameter("pi_campania", OracleTypes.VARCHAR),
										new SqlParameter("pi_contract_id_ext", OracleTypes.VARCHAR),
										new SqlParameter("pi_tipo_documento", OracleTypes.VARCHAR),
										new SqlParameter("pi_numero_documento", OracleTypes.VARCHAR),
										new SqlParameter("pi_billcycle", OracleTypes.VARCHAR),
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
			LOGGER.error(mensajeTransaccion+"Error :"+e.getMessage());
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS)+e.getMessage());
				throw new DBException( propertiesExternos.COD_IDT1, nombreComponente,  PROCEDURE, MessageFormat.format(propertiesExternos.MSJ_IDT1,  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS)+e.getMessage());
				throw new DBException( propertiesExternos.COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método Obtener- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ListMaxFamilia getObtenerRecalculo(String mensajeTransaccion,List< Subscriptions> subscriptions,int num)
			throws DBException {
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio  Recalcular Bono");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
        ListMaxFamilia family=new ListMaxFamilia();
		List<BonoProgram> listaMax;
	
		try {

			PROCEDURE =	propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+
					propertiesExternos.BSCS_RECALCULOMAXFAMILIA;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [pi_num]= ["+ num + "]");
			LOGGER.info(mensajeTransaccion + " [size]= ["+ subscriptions.size() + "]");
			final int tmnLista= subscriptions.size();
			final  List<Subscriptions> listaBono=subscriptions;

				SqlTypeValue sqlType = new AbstractSqlTypeValue(){                 
		              protected Object createTypeValue( Connection conn,int sqlType,String typeName ) throws SQLException{           
		                 ArrayDescriptor  objArrayDescriptor  = new ArrayDescriptor( typeName, conn );                                                                                              
		                 StructDescriptor objStructDescriptor = StructDescriptor.createDescriptor(propertiesExternos.TPROMVENTADATOSLINEABASE, conn );  
		                 STRUCT[]         objStructArray      = new STRUCT[tmnLista];
		                 STRUCT           objStruct           = null;
		                 ARRAY            objArray            = null; 
		                 LOGGER.info("SIZE:" + tmnLista);
	
		                 int cont=0;
		                 for(Subscriptions bono:listaBono){
		                	 
				                Object[] attributes = {bono.getContractIdBase(),
				                		               bono.getLineabase(),
				                		               bono.getPoidbase(),
				                		               bono.getPonamebase(),
				                		               bono.getCustomeridextbase(),
				                		               bono.getBillingaccountbase(),
				                		               bono.getCiclofactbase(),
				                		               bono.getCargoFijo(),
				                		               bono.getTipoproductobase(),
				                		               bono.getTiposuscripcionbase(),
				                		               bono.getFecactivacionbase(),
				                		               bono.getEstadolineabase(),
				                		               bono.getBundleid(),
				                		               bono.getStatusbilling(),
				                		               bono.getMail(),
				                		               bono.getTecnologia(),
				                		               bono.getTipotelefono(),
				                		               bono.getFecmodsuscription(),
				                		               bono.getHisbloq(),
				                		               bono.getIndlineaportabase(),
				                		               bono.getHiscambplanbase(),
				                		               bono.getHisrenovacionbase(),
				                		               bono.getBolsalineabase()};        
			     	
				                objStruct = new STRUCT( objStructDescriptor, conn, attributes );  
			         
				                objStructArray[ cont ] = objStruct;  
		                	 
		                	 cont++;
		                 }
		                 
		                 objArray = new ARRAY( objArrayDescriptor, conn, objStructArray );                                                                                     
		                 return objArray;              
		              }
		          }; 
						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
								.addValue("pi_num",num)
								.addValue("pi_base",sqlType);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("pi_num", OracleTypes.INTEGER),
										new SqlParameter("pi_base", OracleTypes.ARRAY,propertiesExternos.TPROMVENTALISTALINEABASE),
										new SqlOutParameter("po_lineas",OracleTypes.CURSOR, new RowMapper<BonoProgram>() {
											@Override
											public BonoProgram mapRow(ResultSet rs, int arg1) throws SQLException {
												BonoProgram bono = new BonoProgram();												
												bono.setBillingAccount(rs.getString("billing_account_base"));
												bono.setContractidExt(rs.getString("contract_id_base"));
												bono.setCargoFijo(rs.getBigDecimal("cargo_fijo_plan_base"));
												bono.setCustomeridExt(rs.getString("customer_id_ext_base"));
												bono.setFecactbono(rs.getString("fec_activacion_base"));
												bono.setLinea(rs.getString("linea_base"));
												bono.setOrden(rs.getString("orden"));
												bono.setPoBasic(rs.getString("po_id_base"));
												bono.setPonamebase(rs.getString("po_name_base"));	
												bono.setCiclofactbase(rs.getString("ciclo_fact_base"));
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
						
						
						
						family.setCodigoError(String.valueOf(objetJdbcCall.get("po_cod_err")));
						family.setDescripcionError(String.valueOf(objetJdbcCall.get("po_des_err")));
						listaMax = (List<BonoProgram>) objetJdbcCall.get("po_lineas");
						LOGGER.info(mensajeTransaccion + " [SIZE LIST] = " + listaMax.size());
						
						int p=0;
						for(BonoProgram max:listaMax){
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getBillingaccountbase]: " +max.getBillingAccount());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getContractIdBbase]: " +max.getContractidExt());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getCustomeridextbase]: " +max.getCustomeridExt());
							LOGGER.info(mensajeTransaccion +"-"+p+" [getFecactivacionbase]: " +max.getFecactbono());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getLineaBase]: " +max.getLinea());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getOrden]: " +max.getOrden());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getPoidbase]: " +max.getPoBasic());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getPonamebase]: " +max.getPonamebase());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [getCargofijoplanbase]: " +max.getCargoFijo());
							LOGGER.info(mensajeTransaccion +"-"+ p+" [ciclo_fact_base]: " +max.getCiclofactbase());
							p++;
						}
						
						family.setListaMax(listaMax);
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTE_VACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.COD_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT1, nombreComponente,  PROCEDURE, MessageFormat.format(propertiesExternos.MSJ_IDT1,  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin  Recalcular Bono- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
		return family;
	}
	
	
	
	@Override
	public ResponseAuditoria actualizarBono(String mensajeTransaccion, String piprogramid,String observacion)
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
			LOGGER.info(mensajeTransaccion + " [pi_program_id]= ["+ piprogramid + "]");
			LOGGER.info(mensajeTransaccion + " [pi_observacion]= ["+ observacion + "]");

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
						.addValue("pi_program_id",piprogramid)
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
			errorMsg = e + Constantes.CONSTANTE_VACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(propertiesExternos.COD_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT1, nombreComponente,  PROCEDURE,
						MessageFormat.format(propertiesExternos.MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS),  PROCEDURE), e );
			}
			else{
				LOGGER.error(propertiesExternos.COD_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.COD_IDT2, nombreComponente, PROCEDURE,
						MessageFormat.format(propertiesExternos.MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método registrar- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
	}

}

