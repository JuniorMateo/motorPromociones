CREATE OR REPLACE Package Body pkg_portabilidad_cedente Is

/****************************************************************
  * Nombre SP : porta_obtener_dato_operadorcedente
  * Proposito : obtener datos operador cedente
  *  Parametros de entrada : pi_tipodocumentoIn :tipo de documento 01:DNI  02:CARNETEXTRA 03:RUC
  *                                                                04:PASAPORTE 05:CARNET JUBILACION
  *                          pi_numerodocumento: numero de documento
  *                          pi_numeroaportar  : numero a  portar
  *                          pi_activacedente  :fecha de activacion
  * Creado por : JM
  * Fec Creacion : 13/09/2019
  ****************************************************************/
  Procedure porta_obteneroperadorcedente(pi_tipodocumentoIn   IN  CHAR,
                     pi_numerodocumento   IN  VARCHAR2,
                     pi_numeroaportar     IN  VARCHAR2,
                     po_operadorcur      OUT Sys_Refcursor,
                     po_cod_err  Out Integer,
                     po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_operadorcur FOR
        select t.portan_id_portabilidad,
             t.portac_tipo_documento,
             t.portav_numero_documento,
             t.portav_numero_a_portar,
             t.portat_fecha_registro,
             t.portac_cedente,
             t.portad_fecha_activa_cedente
        from GWP.tcrmt_porta_transaccion t
        inner join GWP.tcrmt_porta_transaccion_deta td on td.portan_id_portabilidad = t.portan_id_portabilidad
        where( t.portav_ultimo_mensaje_enviado = C_CPPR Or 
         (t.portav_ultimo_mensaje_enviado = C_CPRABD AND 
         td.portav_motivo_rechazo = C_REC01PRT09) )
           And t.portac_tipo_documento = pi_tipodocumentoIn
           And t.portav_numero_documento = pi_numerodocumento
           And t.portav_numero_a_portar = pi_numeroaportar
           And trunc(t.portat_fecha_registro) = trunc(SYSDATE)
        Order by t.portat_fecha_registro desc;
  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End porta_obteneroperadorcedente;

End pkg_portabilidad_cedente;
