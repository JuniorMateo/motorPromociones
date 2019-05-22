CREATE OR REPLACE PACKAGE MOTPROM.pkg_motprom_proces_trans Is

  -- Author  : 1399706
  -- Created : 30/11/2018 02:55:49 p.m.
  -- Purpose :

  C_MSJ_EXITO_COD               Constant Integer     := 0;
  C_MSJ_EXITO_DES               Constant Varchar2(2) := 'OK';
  C_MSJ_EXITO_DESMATRIZ         Constant Varchar2(6) := 'EXISTE';

  C_ESTADO_PEND             Constant Char(1) := 'P';
  C_ESTADO_PEND_DESACT         Constant Char(2) := 'PD';
  C_ESTADO_ACT                 Constant Char(1) := 'A';
  C_ESTADO_COMP_ACT            Constant Char(2) := 'CA';


  C_CAMBIO_PLANPAGOPUNTUAL  Constant Integer       := 1;
  C_CAMBIO_PLAN_LINEA_ID  Constant Integer       := 4;
  C_CAMBIO_PLAN_DOWNGRADE Constant Integer       := 5;
  C_CAMBIO_PLAN_UPGRADE Constant Integer       := 2;
  C_CAMBIO_PLAN_PREPAGO Constant Integer       := 3;
  C_CAMBIO_SUSPENSIONPORPAGO Constant Integer       := 8;
  C_CAMBIO_SUSPENSIONPORROBO Constant Integer       := 9;
  C_CAMBIO_SUSPENSIONPORFRAUDE Constant Integer       := 10;
  C_CAMBIO_SUSPENSIONSOLICITUD Constant Integer       := 11;
  C_CAMBIO_PLANBNOMATRIZ Constant Integer       := 7;
  C_CAMBIO_PLANBUPGRADENOMATRIZ Constant Integer       := 6;
  C_CAMBIO_PLANDNIARUC Constant Integer       := 12;
  C_CAMBIO_PLANTITULARIDAD Constant Integer       := 13;
  C_CAMBIO_PLANFACTURACION Constant Integer       := 14;

  C_VACIO               Constant char(2) := ' ';
  C_PREPAGO Constant Varchar2(50) := 'PREPAGO';
  C_CAMBIO_PLAN_PREPAGOMSJ Constant Varchar2(50) := 'Desactivacion por Cambio de plan a prepago';
  
   TYPE reg_bonos IS RECORD(
    PROGRAM_ID                       TIM.PROMT_BONO_PROGRAM.PROGRAM_ID%TYPE
    );
  
   Procedure promss_desa_cambio_planlinea(pi_fec_proc In  Date,
                                         pi_dias     In  Integer,
                                         po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) ;								 
   Procedure promss_desa_cambio_downgrade(pi_fec_proc In  Date,
											  pi_dias     In  Integer,
											  po_programcur      OUT Sys_Refcursor,
											  po_cod_err  Out Integer,
											  po_des_err  Out Varchar2);
   Procedure promss_desa_cambio_upgrade(pi_fec_proc In  Date,
											  pi_dias     In  Integer,
											  po_programcur      OUT Sys_Refcursor,
											  po_cod_err  Out Integer,
											  po_des_err  Out Varchar2);										  
   								 
   Procedure promsu_desa_cambio_planlinea(pi_program_id In  Integer,
										  pi_observacion In Varchar2,
                                          po_cod_err  Out Integer,
                                          po_des_err  Out Varchar2) ;
								 
  Procedure promsu_desa_cambio_planprepago(pi_fec_proc In  Date,
                                           pi_dias     In  Integer,
                                           po_cod_err  Out Integer,
                                           po_des_err  Out Varchar2);

  Procedure promss_desa_cambio_bloqueopago(	pi_criteriobloqueo IN integer,
                                            po_programcur      OUT Sys_Refcursor,
											po_cod_err  Out Integer,
											po_des_err  Out Varchar2);
 				
  Procedure promss_desa_cambionomatriz(pi_fec_proc In  Date,
									  pi_dias     In  Integer,
									  po_programcur      OUT Sys_Refcursor,
									  po_cod_err  Out Integer,
									  po_des_err  Out Varchar2);
  Procedure promsu_desa_cambionomatriz(pi_programid In  Integer,
							pi_campania In Varchar2,
							pi_basic In Varchar2,
							pi_observacion In Varchar2,
                                          po_cod_err  Out Integer,
                                          po_des_err  Out Varchar2);									  
 Procedure promss_desa_cambioupnomatriz(pi_fec_proc In  Date,    
										 pi_dias     In  Integer,
										 po_programcur      OUT Sys_Refcursor,
										 po_cod_err  Out Integer,
										 po_des_err  Out Varchar2);
										 
 Procedure promss_desa_cambio_dniruc(    po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2);
									 
 Procedure promss_desa_cambio_titularidad(po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) ;
									 
  Procedure promss_desa_cambio_facturacion(po_programcur      OUT Sys_Refcursor,
                                          po_cod_err  Out Integer,
                                          po_des_err  Out Varchar2);
									 
  Procedure promss_desa_cambio_pagopuntual(po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2);
 
  Procedure promss_desa_baja_contrato(   po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2);
										 
 Procedure promsu_desa_vigen_bono(pi_fec_proc In  Date,
                                     pi_dias     In  Integer,
									 pi_obervacion  in Varchar2,
                                     po_cod_err  Out Integer,
                                     po_des_err  Out Varchar2);									 
									 
 PROCEDURE promsi_cambioplan_hist(PI_EXT_INFO      IN PROMT_EXTENSIONS_TYPE,
                                    PO_CODRPTA       OUT VARCHAR2,
                                    PO_MSJRPTA       OUT VARCHAR2);
									
 PROCEDURE promss_cambioplan_hist(PI_LINEA      IN VARCHAR2,
                                    PI_TIPODOCUMENTO       IN VARCHAR2,
                                    PI_NRODOCUMENTO       IN VARCHAR2,
                                    PO_datocur      OUT Sys_Refcursor,
                                    PO_CODRPTA       OUT VARCHAR2,
                                    PO_MSJRPTA       OUT VARCHAR2);
								
Procedure promss_venta_bonos_mf2(pi_plan             In  Varchar2,
                                  pi_motivo_act       In  Varchar2,
                                  pi_fec_proc         In  Date,
                                  pi_campania         In  Varchar2,
                                  pi_flag_base        In  int,
                                  po_cur_bonos        Out Sys_Refcursor,
                                  po_cod_err          Out Integer,
                                  po_des_err          Out Varchar2);
								  
Procedure promsi_program_bono_recalculo(
                                        pi_bono_id        In  Integer,
                                        pi_po_bono        In  Varchar2,
                                        pi_po_ontop       In  Varchar2,
                                        pi_po_plan        In  Varchar2,
                                        pi_order_id       In  Varchar2,
                                        pi_fec_act_bono   In  Date,
                                        pi_fec_vigencia   In  Date,
                                        pi_est_bono       In  Char,
                                        pi_est_proc       In  Char,
                                        pi_periodo        In  Integer,
                                        pi_tipo_bono      In  Varchar2,
                                        pi_ciclo_fact     In  Varchar2,
                                        pi_bono_periodico In  Integer,
                                        pi_tipo_opera     In  Varchar2,
                                        pi_fec_crea       In date, --
                                        pi_fec_modi       In date, --
                                        pi_est_om         In number, --
                                        pi_fec_crea_om    In date, --
                                        pi_fec_ini_ejec_om In date, --
                                        pi_fec_fin_ejec_om In date, --
                                        pi_shopping_cart_id In Varchar2,
                                        pi_activacion        Varchar2,
										pi_linea        Varchar2,
                                        po_cod_err        Out Integer,
                                        po_des_err        Out Varchar2) ;

  Procedure promsi_contrato_valida(
                            pi_customer_id_ext In  Varchar2,
                            pi_billing_account In  Varchar2,
                            pi_po_basic        In  Varchar2,
                            pi_cargo_fijo      In  Number,
                            pi_linea           In  Varchar2,
                            pi_campania        In  Varchar2,
                            pi_contract_id_ext In  Varchar2,
							pi_tipo_documento  In  Varchar2,
                            pi_numero_documento  In  Varchar2,
							pi_billcycle  In  Varchar2,
                            po_cod_err         Out Integer,
                            po_des_err         Out Varchar2);
						
 Procedure promss_recal_maxfamilia(pi_num      In  Integer,
                                    pi_base     In  T_PROM_VENTA_LISTA_ONE,
                                    po_lineas   Out Sys_Refcursor,
                                    po_cod_err  Out Integer,
                                    po_des_err  Out Varchar2);
							
 Procedure promss_dnnum_maxfamilia(po_lineas   OUT Sys_Refcursor,
                                    po_cod_err  Out Integer,
                                    po_des_err  Out Varchar2);

End MOTPROM.pkg_motprom_proces_trans;