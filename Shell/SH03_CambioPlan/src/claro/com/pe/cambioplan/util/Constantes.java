package claro.com.pe.cambioplan.util;

import org.springframework.stereotype.Component;

@Component
public class Constantes {

    public static final String LOG4J_PROPERTIES = "log4j.properties";
    public static final String CONFIG_PATH = "spring/applicationContext.xml";
	public static final String CONSTANTEVACIA = "";
	public static final String EXITO = "0";
	public static final String EXITOMENSAJE = "OK";
	public static final String NOEXITO = "1";
	public static String CADENAVACIA="";
	
	public static String INTCAA0002="INT-CAA-0002";
	public static String GETPRODUCTSOFFERINGPERCONTRACT="GetProductsOfferingPerContract";
	
	public static String INTCOP0223="INT-COP-0223";
	public static String RETRIEVESUBSCRIPTIONS="retrieveSubscriptions";
	
	//tipo de desactivacion
	public static final int CAMBIOPLANLINEA= 4;
	public static final int CAMBIOPLANLINEADOWNGRADE=5;
	public static final int CAMBIOPLANLINEAUPGRADE=2;
	public static final int CAMBIOPLANLINEAUPGRADENOMATIZ=6;
	public static final int CAMBIOPLANLINEANOMATRIZ=7;
	
	public static final String CAMBIOPLANLINEADES = "Desactivacion por Cambio de plan de la línea";
	public static final String CAMBIOPLANLINEADOWNGRADEDES = "Desactivacion por Cambio de plan Downgrade";
	public static final String CAMBIOPLANLINEAUPGRADEDES = "Desactivacion por Cambio de plan Upgrade";
	
	public static final String CAMBIOPLANLINEAUPGRADENOMATIZDES = "Desactivacion por Cambio de plan Upgrade que no esta en matriz";
	public static final String CAMBIOPLANLINEANOMATRIZDES = "Desactivacion por  Cambio de plan que no esta en matriz";
	
	public static final int CORRECTO = 0;
	public static final int NOCORRECTO = 1;
	
	public static final int HITOUNO= 1;
	public static final int HITODOS= 2;
	public static final int HITOTRES= 3;
	public static final int HITOCUATRO= 4;
	public static final int HITOCINCO= 5;
	public static final int HITOSEIS= 6;
}