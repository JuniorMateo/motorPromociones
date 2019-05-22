package claro.com.pe.carga.datos.replica.cambio.plan.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.text.MessageFormat;
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
import claro.com.pe.carga.datos.replica.cambio.plan.bean.BonoProgram;
import claro.com.pe.carga.datos.replica.cambio.plan.bean.LisBonoProgram;
import claro.com.pe.carga.datos.replica.cambio.plan.bean.ResponseAuditoria;
import claro.com.pe.carga.datos.replica.cambio.plan.exception.DBException;
import claro.com.pe.carga.datos.replica.cambio.plan.util.Constantes;
import claro.com.pe.carga.datos.replica.cambio.plan.util.PropertiesExternos;
import claro.com.pe.carga.datos.replica.cambio.plan.util.Util;

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
	public LisBonoProgram obtenerConsCambioplan(String mensajeTransaccion,String fechaConsulta)
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

			PROCEDURE = propertiesExternos.BSCS_PKG_CARGA_ALTA+"."+ propertiesExternos.BSCS_CAMBIOPLANREPLICA;
			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [FECHA DE  CONSULTA: ] "+fechaConsulta);

						objJdbcCall = new SimpleJdbcCall(bscs);
						objJdbcTemplate = objJdbcCall.getJdbcTemplate();
						objJdbcTemplate.setQueryTimeout(Integer.parseInt(propertiesExternos.DB_BSCS_TIMEOUT));
						
						objParametrosIN = new MapSqlParameterSource()
						.addValue("PI_FEC_CONSULTA",Util.deStringADate(mensajeTransaccion, fechaConsulta));
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(		
										new SqlParameter("PI_FEC_CONSULTA", OracleTypes.DATE),
										new SqlOutParameter("PO_CURSOR",OracleTypes.CURSOR, new RowMapper<BonoProgram>() {
											@Override
											public BonoProgram mapRow(ResultSet rs, int arg1) throws SQLException {
												BonoProgram bono = new BonoProgram();																	
												bono.setTipodocumentotemp(rs.getString("Tipodocumento"));
												bono.setNrodocumentotemp(rs.getString("NroDocumento"));
												bono.setCustomeridtemp(rs.getString("Customer_ID"));
												bono.setContractidtemp(rs.getString("Co_ID"));
												bono.setLineatemp(rs.getString("Linea"));
												bono.setPlanorigentemp(rs.getString("planOrigen"));
												bono.setCargofijorigentemp(rs.getString("cargoFijoOrigen"));
												bono.setPlandestinotemp(rs.getString("planDestino"));
												bono.setCargofijodestinotemp(rs.getString("cargoFijoDestino"));
												bono.setFechacambioplantemp(rs.getString("fechaCambioPlan"));
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
						listabono = (List<BonoProgram>) objetJdbcCall.get("PO_CURSOR");
						
						LOGGER.info(mensajeTransaccion + " [SIZE LIST] = " + listabono.size());
						
						lista.setListabono(listabono);
		} catch (Exception e) {
			String errorMsg,nombreComponente;
			errorMsg = e + Constantes.CONSTANTE_VACIA;
			nombreComponente = propertiesExternos.DB_BSCS_NOMBRE;
			
			if( errorMsg.contains( SQLTimeoutException.class.getName() ) ){
				LOGGER.error(mensajeTransaccion+propertiesExternos.CARGA_ALTA_MSJ_IDT1.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT1, nombreComponente, OWNER + "." + PROCEDURE, MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT1, OWNER + "." + PROCEDURE), e );
			}
			else{
				LOGGER.error(mensajeTransaccion+propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS));
				throw new DBException( propertiesExternos.CARGA_ALTA_COD_IDT2, nombreComponente, OWNER + "." + PROCEDURE,
						MessageFormat.format(propertiesExternos.CARGA_ALTA_MSJ_IDT2.replace("$nombre_SP", PROCEDURE).replace("$usuario", propertiesExternos.ORACLE_JDBC_OWNER_BSCS), e.getMessage()), e );
			}
		}finally{
			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método Obtener- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
		
		return lista;
	}
		
	@Override
	public ResponseAuditoria insertarCargaMasiva(String mensajeTransaccion, LisBonoProgram lista)
			throws DBException {
		long tiempoInicial = System.currentTimeMillis();
		LOGGER.info(mensajeTransaccion + " - Inicio del Actualizar Bono");
		String PROCEDURE = null;

		JdbcTemplate objJdbcTemplate = null;
		SimpleJdbcCall objJdbcCall = null;
		SqlParameterSource objParametrosIN=null;
		ResponseAuditoria responseAuditoria = new ResponseAuditoria();
	
		try {

			PROCEDURE =	propertiesExternos.BSCS_PKG_CARGADATOS+"."+
					propertiesExternos.BSCS_INSERT_CAMBIO_PLAN;

			LOGGER.info(mensajeTransaccion + " Se establecio conexion con la BD " +propertiesExternos.DB_BSCS_NOMBRE);
			LOGGER.info(mensajeTransaccion + " Consultando a la BD: " + propertiesExternos.DB_BSCS_NOMBRE + ".Detalle: " + propertiesExternos.ORACLE_JDBC_OWNER_BSCS);
			LOGGER.info(mensajeTransaccion + " SP a ejecutar: ["+ PROCEDURE + "]");
			LOGGER.info(mensajeTransaccion + " Ejecutando SP ");	
			LOGGER.info(mensajeTransaccion + " [INPUT] ");	
			LOGGER.info(mensajeTransaccion + " [size]= ["+ lista.getListabono().size() + "]");
			final int tmnLista= lista.getListabono().size();
			final  List<BonoProgram> listaBono=lista.getListabono();

				SqlTypeValue sqlType = new AbstractSqlTypeValue(){                 
		              protected Object createTypeValue( Connection conn,int sqlType,String typeName ) throws SQLException{           
		                 ArrayDescriptor  objArrayDescriptor  = new ArrayDescriptor( typeName, conn );                                                                                              
		                 StructDescriptor objStructDescriptor = StructDescriptor.createDescriptor( "PROMT_EXTENSION_TYPE", conn );  
		                 STRUCT[]         objStructArray      = new STRUCT[tmnLista];
		                 STRUCT           objStruct           = null;
		                 ARRAY            objArray            = null; 
		                 LOGGER.info("SIZE:" + tmnLista);
	
		                 int cont=0;
		                 for(BonoProgram bono:listaBono){
		                	 
				                Object[] attributes = {bono.getTipodocumentotemp(),
				                		               bono.getNrodocumentotemp(),
				                		               bono.getCustomeridtemp(),
				                		               bono.getContractidtemp(),
				                		               bono.getLineatemp(),
				                		               bono.getPlanorigentemp(),
				                		               bono.getCargofijorigentemp(),
				                		               bono.getPlandestinotemp(),
				                		               bono.getCargofijodestinotemp(),
				                		               bono.getFechacambioplantemp()};        
			     	
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
								.addValue("PI_EXT_INFO",sqlType);
						
						Map<String, Object> objetJdbcCall = objJdbcCall
								.withoutProcedureColumnMetaDataAccess()
								.withSchemaName(propertiesExternos.ORACLE_JDBC_OWNER_BSCS)
								.withProcedureName(PROCEDURE)
								.declareParameters(
										new SqlParameter("PI_EXT_INFO", OracleTypes.ARRAY,"PROMT_EXTENSIONS_TYPE"),
	                                	new SqlOutParameter("PO_CODRPTA", OracleTypes.VARCHAR),
	        							new SqlOutParameter("PO_MSJRPTA", OracleTypes.VARCHAR)
										).execute(objParametrosIN);
						
						LOGGER.info(mensajeTransaccion + " Se ejecuto exitosamente el SP");
						LOGGER.info(mensajeTransaccion + " [OUTPUT]: " );
						LOGGER.info(mensajeTransaccion + " [po_cod_err] = " + objetJdbcCall.get("PO_CODRPTA") );
						LOGGER.info(mensajeTransaccion + " [po_des_err] = " + objetJdbcCall.get("PO_MSJRPTA") );
						
						
						
						responseAuditoria.setCodigoRespuesta(String.valueOf(objetJdbcCall.get("PO_CODRPTA")));
						responseAuditoria.setMensajeRespuesta(String.valueOf(objetJdbcCall.get("PO_MSJRPTA")));

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
			LOGGER.info(mensajeTransaccion + " - Fin del método Obtener- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		}
		
	}

}

