package claro.com.pe.carga.datos.replica.cambio.plan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import claro.com.pe.carga.datos.replica.cambio.plan.bean.BonoProgram;
import claro.com.pe.carga.datos.replica.cambio.plan.bean.LisBonoProgram;
import claro.com.pe.carga.datos.replica.cambio.plan.util.Constantes;
import claro.com.pe.carga.datos.replica.cambio.plan.util.PropertiesExternos;

@Repository
public class BSCSIXDAOImpl  implements BSCSIXDAO{
	public static final Logger LOGGER = Logger.getLogger(BSCSIXDAOImpl.class);

	@Autowired
	PropertiesExternos propertiesExternos;
	
	@Override
	public LisBonoProgram obtenerConsCambioplanBSCSIX(String mensajeTransaccion, String fechaConsulta) throws SQLException {

		Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		LisBonoProgram lista=new LisBonoProgram();
		List<BonoProgram> listabono=new ArrayList<BonoProgram>();

		String metodo = " Obtener Cambio plan BSCSIX";
		String mensajeLog = mensajeTransaccion;
		
		LOGGER.info(mensajeLog + "[INICIO] - METODO: " + metodo + " FECHA: "+fechaConsulta);
		long tiempoInicial = System.currentTimeMillis();
		
		String insertTableSQL="SELECT "+
						  "CON.CUSTOMER_ID     				CUSTOMER_ID_EXT,"+
						  "CON.CO_ID           				CONTRACT_TD_EXT,"+
						  "DN.DN_NUM LINEA,"+
						  "(SELECT DISTINCT TMCODE FROM RATEPLAN_HIST A WHERE A.CO_ID = CON.CO_ID AND "+
						  " A.SEQNO = (SELECT MAX(B.SEQNO)-1 FROM RATEPLAN_HIST B WHERE B.CO_ID = CON.CO_ID))     PLANORIGEN,"+
						  "(SELECT ACCESSFEE*1.18 FROM MPULKTMB WHERE "+
						  "TMCODE = (SELECT DISTINCT TMCODE FROM RATEPLAN_HIST A "+
						  "WHERE A.CO_ID = CON.CO_ID AND "+
						  " A.SEQNO = (SELECT MAX(B.SEQNO)-1 FROM RATEPLAN_HIST B WHERE B.CO_ID = CON.CO_ID)))    CARGOFIJOORIGEN,"+
						  " HIS.TMCODE                                                                          PLANDESTINO,"+
						  " (SELECT ACCESSFEE*1.18 FROM MPULKTMB B WHERE B.TMCODE IN "+
						  " (HIS.TMCODE) AND B.SNCODE IN (27) AND VSCODE = (SELECT MAX(VSCODE) FROM "+
						  " RATEPLAN_VERSION A WHERE A.TMCODE = B.TMCODE))                                        CARGOFIJODESTINO,"+
						  " HIS.TMCODE_DATE                                                                       FECHACAMBIOPLAN,"+
						  " DECODE(CC.ID_TYPE,1,'PASAPORTE',2,'DNI',6,'RUC')                                      TIPODOCUMENTO,"+
						  " CC.CSCOMPREGNO                                                                   NRODOCUMENTO "+
						  "FROM "+
						  " CONTRACT_ALL           CON "+
						  "LEFT JOIN CUSTOMER_ALL  MER "+
						  "ON CON.CUSTOMER_ID=MER.CUSTOMER_ID "+
						  "LEFT JOIN CCONTACT_ALL                         CC "+
						  "ON CC.CUSTOMER_ID = MER.CUSTOMER_ID "+
						  "LEFT JOIN CONTRACT_HISTORY                     ORY "+
						  "ON CON.CO_ID=ORY.CO_ID "+
						  "LEFT JOIN CONTR_SERVICES_CAP                   CSC "+
						  "ON CON.CO_ID=CSC.CO_ID "+
						  "LEFT JOIN DIRECTORY_NUMBER                     DN "+
						  "ON DN.DN_ID=CSC.CO_ID "+
						  "LEFT JOIN RATEPLAN_HIST                        HIS "+
						  "ON CON.CO_ID=HIS.CO_ID "+
						  "WHERE   HIS.TMCODE_DATE = TO_DATE('"+fechaConsulta+"','DDMMYYYY') "+
						  "AND CH_REASON = 6";
		try {
			dbConnection = getDBConnection();
			preparedStatement = dbConnection.prepareStatement(insertTableSQL);

			// execute insert SQL stetement
			ResultSet rs=preparedStatement.executeQuery();
			while(rs.next()){
				BonoProgram bono=new BonoProgram();
				bono.setCustomeridtemp(rs.getString(1));
				bono.setContractidtemp(rs.getString(2));
				bono.setLineatemp(rs.getString(3));
				bono.setPlanorigentemp(rs.getString(4));
				bono.setCargofijorigentemp(rs.getString(5));
				bono.setPlandestinotemp(rs.getString(6));
				bono.setCargofijodestinotemp(rs.getString(7));
				bono.setFechacambioplantemp(rs.getString(8));
				bono.setTipodocumentotemp(rs.getString(9));
				bono.setNrodocumentotemp(rs.getString(10));
				listabono.add(bono);
			}
			
			LOGGER.info(mensajeTransaccion + " SIZE Lista:  "+listabono.size());
			
			lista.setCodigoError(Constantes.EXITO);
			lista.setDescripcionError(Constantes.EXITOMENSAJE);
			lista.setListabono(listabono);
			LOGGER.info(mensajeTransaccion + " FIN - Carga Objetos SQl : ");
			

		} catch (SQLException e) {
			LOGGER.info(mensajeTransaccion + " Error al Procesar SQl : " + e.getMessage());

		} finally {

			if (preparedStatement != null) {
				preparedStatement.close();
			}

			if (dbConnection != null) {
				dbConnection.close();
			}

			long tiempoFinal = System.currentTimeMillis();
			LOGGER.info(mensajeTransaccion + " - Fin del método Obtener- Tiempo total de proceso : " + (tiempoFinal - tiempoInicial) + " milisegundos.");
		} return lista;

	}
	
	private  Connection getDBConnection() {

		Connection dbConnection = null;

		try {

			Class.forName(propertiesExternos.DB_DRIVER);

		} catch (ClassNotFoundException e) {

			System.out.println(e.getMessage());

		}

		try {

			dbConnection = DriverManager.getConnection(
					propertiesExternos.DB_CONNECTION, propertiesExternos.DB_USER,propertiesExternos.DB_PASSWORD);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		}

		return dbConnection;

	}

}
