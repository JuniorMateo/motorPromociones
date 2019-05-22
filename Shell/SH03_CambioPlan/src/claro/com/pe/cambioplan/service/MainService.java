package claro.com.pe.cambioplan.service;

import claro.com.pe.cambioplan.exception.DBException;
import claro.com.pe.cambioplan.exception.DBNoDisponibleException;
import claro.com.pe.cambioplan.exception.DBTimeoutException;

public interface MainService {
    
    public void run(String idTransaccion,String pi_fec_proc,String pi_dias) 
    		throws DBException,DBNoDisponibleException,DBTimeoutException, Exception;
    
}