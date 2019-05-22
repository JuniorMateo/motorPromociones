package claro.com.pe.carga.datos.replica.cambio.plan.dao;

import java.sql.SQLException;

import claro.com.pe.carga.datos.replica.cambio.plan.bean.LisBonoProgram;

public interface BSCSIXDAO {
	
	 
	 LisBonoProgram obtenerConsCambioplanBSCSIX(String mensajeTransaccion,String fechaConsulta)
				throws  SQLException;

}
