CREATE OR REPLACE Package pkg_portabilidad_cedente Is

  -- Author  : JM
  -- Created : 20/03/2019 02:55:49 p.m.

  C_MSJ_EXITO_COD               Constant Integer     := 0;
  C_MSJ_EXITO_DES               Constant Varchar2(2) := 'OK';
  C_CPPR       Constant Varchar2(10) :='CPPR';
  C_CPRABD       Constant Varchar2(10) := 'CPRABD';
  C_REC01PRT09      Constant Varchar2(10) := 'REC01PRT09';


  Procedure porta_obteneroperadorcedente(pi_tipodocumentoIn   IN  CHAR,
                             pi_numerodocumento   IN  VARCHAR2,
                             pi_numeroaportar     IN  VARCHAR2,
                             po_operadorcur      OUT Sys_Refcursor,
                             po_cod_err  Out Integer,
                             po_des_err  Out Varchar2);

End pkg_portabilidad_cedente;
