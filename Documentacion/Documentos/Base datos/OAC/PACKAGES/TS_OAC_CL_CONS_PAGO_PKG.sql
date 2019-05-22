create or replace package TS_OAC_CL_CONS_PAGO_PKG is
                          
  procedure pr_pago_puntual(piv_CUSTOMER_ID    varchar2
                           ,pin_CANTIDAD_MESES number
                           ,pov_COD_RESPUESTA  out varchar2
                           ,pov_MSG_RESPUESTA  out varchar2
                           ,pov_INDICADOR_PP   out number); 
end TS_OAC_CL_CONS_PAGO_PKG;
