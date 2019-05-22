CREATE OR REPLACE Package Body MOTPROM.pkg_motprom_proces_trans Is


/****************************************************************
  * Nombre SP : promss_desa_cambio_planlinea
  * Proposito : obtener todos los bonos a desactivar 
  *           : por unicamente  haber cambiado de plan
  * Creado por : JM
  * Fec Creacion : 13/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_planlinea(pi_fec_proc In  Date,
                                         pi_dias     In  Integer,
                                         po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR
						Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							cd.fixed_charge,
							bp.program_id							
                       From      MOTPROM.promt_bono_program bp 				   
					   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_campania_desactiva ds
					      on ds.idoperacion=(select t.opertype_id  from promt_operation_type t where t.opertype_desc=bp.tipo_opera)
					    And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                        And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                        And bp.est_bono = C_ESTADO_ACT
                        And bp.est_proc = C_ESTADO_COMP_ACT 
                        And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                        And ds.iddesactivacion=C_CAMBIO_PLAN_LINEA_ID
                        And bf.campania is null
						UNION 
						 Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							cd.fixed_charge,
							bp.program_id
                       From           MOTPROM.promt_bono_program bp 
                       inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
					   And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                       And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                       And bp.est_bono = C_ESTADO_ACT
                       And bp.est_proc = C_ESTADO_COMP_ACT 
                       And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                       And ds.iddesactivacion=C_CAMBIO_PLAN_LINEA_ID;

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_planlinea;
  
  
  
  /****************************************************************
  * Nombre SP : promss_desa_cambio_downgrade
  * Proposito : obtener todos los bonos a desactivar 
  *           : por haber  Cambio de plan Downgrade
  * Creado por : JM
  * Fec Creacion : 13/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_downgrade(pi_fec_proc In  Date,
											pi_dias     In  Integer,
											po_programcur      OUT Sys_Refcursor,
											po_cod_err  Out Integer,
											po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR
						 Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							cd.fixed_charge,
							bp.program_id							
                       From      promt_bono_program bp 				   
					   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_campania_desactiva ds
					      on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
					    And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                        And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                        And bp.est_bono = C_ESTADO_ACT
                        And bp.est_proc = C_ESTADO_COMP_ACT 
                        And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                        And ds.iddesactivacion=C_CAMBIO_PLAN_DOWNGRADE
                        And bf.campania is null
						UNION 
						 Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							cd.fixed_charge,
							bp.program_id
                       From            MOTPROM.promt_bono_program bp 
                       inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
					   And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                       And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                       And bp.est_bono = C_ESTADO_ACT
                       And bp.est_proc = C_ESTADO_COMP_ACT 
                       And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                       And ds.iddesactivacion=C_CAMBIO_PLAN_DOWNGRADE;
						

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_downgrade;
  
 /****************************************************************
  * Nombre SP : promss_desa_cambio_upgrade
  * Proposito : obtener todos los bonos a desactivar 
  *           : por haber  Cambio de plan upgrade
  * Creado por : JM
  * Fec Creacion : 13/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_upgrade(pi_fec_proc In  Date,
											pi_dias     In  Integer,
											po_programcur      OUT Sys_Refcursor,
											po_cod_err  Out Integer,
											po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR
                     Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							cd.fixed_charge,
							bp.program_id							
                       From      promt_bono_program bp 				   
					   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_campania_desactiva ds
					      on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
					    And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                        And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                        And bp.est_bono = C_ESTADO_ACT
                        And bp.est_proc = C_ESTADO_COMP_ACT 
                        And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                        And ds.iddesactivacion=C_CAMBIO_PLAN_UPGRADE
                        And bf.campania is null
						UNION 
						 Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							cd.fixed_charge,
							bp.program_id
                       From            MOTPROM.promt_bono_program bp 
                       inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
					   And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                       And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                       And bp.est_bono = C_ESTADO_ACT
                       And bp.est_proc = C_ESTADO_COMP_ACT 
                       And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                       And ds.iddesactivacion=C_CAMBIO_PLAN_UPGRADE;
  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_upgrade;
  
  
  
  /****************************************************************
  * Nombre SP : promsu_desa_cambio_planlinea
  * Proposito : cambiar  el estado de bono a Deactivo
  * Creado por : JM
  * Fec Creacion : 02/05/2018
  ****************************************************************/
   Procedure promsu_desa_cambio_planlinea(pi_program_id In  Integer,
									  pi_observacion In Varchar2,
                                      po_cod_err  Out Integer,
                                      po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;


          Update MOTPROM.promt_bono_program bp
             Set bp.est_proc = C_ESTADO_PEND_DESACT,
                 bp.est_bono = C_ESTADO_PEND,
				 bp.observacion=pi_observacion,
				 bp.fec_estado=sysdate
           Where bp.program_id=pi_program_id;

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promsu_desa_cambio_planlinea;

/****************************************************************
  * Nombre SP : promsu_desa_cambio_planprepago
  * Proposito : obtener todos los bonos a desactivar 
  *           : por unicamente  haber cambiado de postpago a prepago
  * Creado por : JM
  * Fec Creacion : 13/03/2019
  ****************************************************************/
  PROCEDURE promsu_desa_cambio_planprepago(pi_fec_proc In  Date,
                                           pi_dias     In  Integer,
                                           po_cod_err  Out Integer,
                                           po_des_err  Out Varchar2) Is
  TYPE t_reg_bono IS TABLE OF reg_bonos INDEX BY BINARY_INTEGER;
  v_bono         t_reg_bono;
  v_limit PLS_INTEGER := 1000;

  CURSOR v_programcur  IS select bp.program_id
					  from MOTPROM.promt_bono_program bp   
					  inner join MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					  inner join MOTPROM.promt_rateplan_hist rt on rt.contract_id=bp.contract_id
					  inner join MOTPROM.promt_bono_plan bb on bb.bono_id = bp.bono_id
					  inner join   MOTPROM.promt_campania_desactiva cd on cd.campania= bf.campania
					  And trunc(rt.created_date) >= (trunc(pi_fec_proc) - pi_dias)
					  And trunc(rt.created_date) <= trunc(pi_fec_proc)
					  And bp.est_bono = C_ESTADO_ACT
					  And bp.est_proc = C_ESTADO_COMP_ACT  
					  And rt.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rt.contract_id)
					  And bb.tipo_suscripcion=C_PREPAGO
					  And cd.iddesactivacion=C_CAMBIO_PLAN_PREPAGO			  
					UNION 			  			  
					select bp.program_id
					  from MOTPROM.promt_bono_program bp   
					  inner join MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					  inner join MOTPROM.promt_rateplan_hist rt on rt.contract_id=bp.contract_id
					  inner join MOTPROM.promt_bono_plan bb on bb.bono_id = bp.bono_id
					  inner join MOTPROM.promt_campania_desactiva ds on 
					  ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
					  And trunc(rt.created_date) >= (trunc(pi_fec_proc) - pi_dias)
					  And trunc(rt.created_date) <= trunc(pi_fec_proc)
					  And bp.est_bono = C_ESTADO_ACT
					  And bp.est_proc = C_ESTADO_COMP_ACT  
					  And rt.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rt.contract_id)
					  And bb.tipo_suscripcion=C_PREPAGO
					  And ds.iddesactivacion=C_CAMBIO_PLAN_PREPAGO
					  And bf.campania is null;
  BEGIN
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;
				  
  OPEN  v_programcur;
  LOOP
    FETCH v_programcur BULK COLLECT
      INTO v_bono LIMIT v_limit;
	  EXIT WHEN nvl(v_bono.LAST, 0) = 0;
	   IF nvl(v_bono.LAST, 0) > 0 THEN
	    FOR q IN v_bono.FIRST .. v_bono.LAST
	     LOOP
		    UPDATE MOTPROM.promt_bono_program pro
				Set pro.est_proc = C_ESTADO_PEND_DESACT,
				    pro.est_bono = C_ESTADO_PEND,
					pro.observacion=C_CAMBIO_PLAN_PREPAGOMSJ,
					pro.fec_estado=sysdate
				Where pro.program_id = v_bono(q).program_id;
		 END LOOP;
	   END IF;

  END LOOP;
  EXCEPTION 
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  END promsu_desa_cambio_planprepago;
  
  
  /****************************************************************
  * Nombre SP : promss_desa_cambio_bloqueopago
  * Proposito : obtener todos los bonos a desactivar 
  *           : por Bloqueo/suspension por falta de pago.
  * Creado por : JM
  * Fec Creacion : 13/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_bloqueopago(	pi_criteriobloqueo IN integer,
                                            po_programcur      OUT Sys_Refcursor,
											po_cod_err  Out Integer,
											po_des_err  Out Varchar2) Is
  v_criterio  Integer:=0;  
  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   CASE pi_criteriobloqueo
      when 8 then v_criterio := C_CAMBIO_SUSPENSIONPORPAGO;
      when 9 then  v_criterio := C_CAMBIO_SUSPENSIONPORROBO;
      when 10 then  v_criterio := C_CAMBIO_SUSPENSIONPORFRAUDE;
      when 11 then  v_criterio := C_CAMBIO_SUSPENSIONSOLICITUD;
   END CASE;

   OPEN po_programcur FOR
						 Select bp.contract_id,
                                bp.bono_id,
							    cd.dn_num,
							    cd.contract_id_ext,
                                bp.program_id								
                       From     MOTPROM.promt_bono_program bp 				   
					   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_campania_desactiva ds
					      on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
                        And bp.est_bono = C_ESTADO_ACT
                        And bp.est_proc = C_ESTADO_COMP_ACT 
                        And ds.iddesactivacion=v_criterio
                        And bf.campania is null
						UNION 
						 Select bp.contract_id,
                            bp.bono_id,
							cd.dn_num,
							cd.contract_id_ext,
							bp.program_id
                       From          MOTPROM.promt_bono_program bp 
                       inner join    MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
                       inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
                       And bp.est_bono = C_ESTADO_ACT
                       And bp.est_proc = C_ESTADO_COMP_ACT 
                       And ds.iddesactivacion=v_criterio;
						

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_bloqueopago;
  
  
  /****************************************************************
  * Nombre SP : promss_desa_cambionomatriz
  * Proposito : obtener todos los bonos a desactivar 
  *           : por haber  Cambio de plan y no estan en la Matriz
  * Creado por : JM
  * Fec Creacion : 13/09/2018
  ****************************************************************/
  Procedure promss_desa_cambionomatriz(pi_fec_proc In  Date,
											pi_dias     In  Integer,
											po_programcur      OUT Sys_Refcursor,
											po_cod_err  Out Integer,
											po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD; 
    po_des_err := C_MSJ_EXITO_DES;
	

   OPEN po_programcur FOR
                     Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							bf.campania,
                            cd.fixed_charge,
							bp.program_id							
                       From      MOTPROM.promt_bono_program bp 				   
					   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_campania_desactiva ds
					      on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
					    And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                        And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                        And bp.est_bono = C_ESTADO_ACT
                        And bp.est_proc = C_ESTADO_COMP_ACT 
                        And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                        And ds.iddesactivacion=C_CAMBIO_PLANBNOMATRIZ
                        And bf.campania is null
						UNION 
						 Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							bf.campania,
							cd.fixed_charge,
							bp.program_id
                       From            MOTPROM.promt_bono_program bp 
                       inner join    MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
					   And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                       And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                       And bp.est_bono = C_ESTADO_ACT
                       And bp.est_proc = C_ESTADO_COMP_ACT 
                       And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                       And ds.iddesactivacion=C_CAMBIO_PLANBNOMATRIZ;
  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambionomatriz;
  


  /****************************************************************
  * Nombre SP : promsu_desa_cambionomatriz
  * Proposito : cambio de plan por no estar en la matriz
  * Creado por : JM
  * Fec Creacion : 18/03/2019
  ****************************************************************/
   Procedure promsu_desa_cambionomatriz(pi_programid In  Integer,
									      pi_campania In Varchar2,
										  pi_basic In Varchar2,
										  pi_observacion In Varchar2,
                                          po_cod_err  Out Integer,
                                          po_des_err  Out Varchar2) Is
   v_count  Integer:=0;
  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DESMATRIZ;

       Select count(*) into v_count
  	    from MOTPROM.promt_bono_definicion b
		inner join MOTPROM.promt_bono_plan p on b.bono_id = p.bono_id 
		where campania =pi_campania
		and p.po_basic in (pi_basic);

        IF v_count = 0 THEN
		         Update MOTPROM.promt_bono_program bp
                 Set bp.est_proc = C_ESTADO_PEND_DESACT,
                     bp.est_bono = C_ESTADO_PEND,
					 bp.observacion = pi_observacion,
					 bp.fec_estado=sysdate
                     Where bp.program_id=pi_programid;
					 po_des_err := C_MSJ_EXITO_DES;
		END IF;
  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promsu_desa_cambionomatriz;



 /****************************************************************
  * Nombre SP : promss_desa_cambioupnomatriz
  * Proposito : obtener todos los bonos a desactivar 
  *           : por haber  Cambio de plan upgrade y no estan en la Matriz
  * Creado por : JM
  * Fec Creacion : 13/09/2018
  ****************************************************************/
  Procedure promss_desa_cambioupnomatriz(pi_fec_proc In  Date,
											pi_dias     In  Integer,
											po_programcur      OUT Sys_Refcursor,
											po_cod_err  Out Integer,
											po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD; 
    po_des_err := C_MSJ_EXITO_DES;
	

   OPEN po_programcur FOR
                     Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							bf.campania,
                            cd.fixed_charge,
							bp.program_id							
                       From      MOTPROM.promt_bono_program bp 				   
					   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_campania_desactiva ds
					      on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
					    And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                        And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                        And bp.est_bono = C_ESTADO_ACT
                        And bp.est_proc = C_ESTADO_COMP_ACT 
                        And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                        And ds.iddesactivacion=C_CAMBIO_PLANBUPGRADENOMATRIZ
                        And bf.campania is null
						UNION 
						 Select bp.contract_id,
                            cd.dn_num,
                            bp.po_basic,
                            bp.bono_id,
							cd.contract_id_ext,
							bf.campania,
							cd.fixed_charge,
							bp.program_id
                       From            MOTPROM.promt_bono_program bp 
                       inner join    MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
					   inner join     MOTPROM.promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
					   And trunc(rp1.created_date) >= (trunc(pi_fec_proc) - pi_dias)
                       And trunc(rp1.created_date) <= trunc(pi_fec_proc)
                       And bp.est_bono = C_ESTADO_ACT
                       And bp.est_proc = C_ESTADO_COMP_ACT 
                       And rp1.seqno=(select max(t.seqno) from MOTPROM.promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                       And ds.iddesactivacion=C_CAMBIO_PLANBUPGRADENOMATRIZ;
  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambioupnomatriz;
  
  
 /***************************************************************
  * Nombre SP : promss_desa_cambio_dniruc
  * Proposito : obtener todos los bonos a desactivar 
  *           : por cambio de DNI a RUC
  * Creado por : JM
  * Fec Creacion : 20/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_dniruc(   po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR					   
					   Select   cd.dn_num,
							    pc.documentonum,
								bp.program_id,
								pc.doc_type
                       From           MOTPROM.promt_bono_program bp 
                       inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
                       inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
                       inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
                       And bp.est_bono = C_ESTADO_ACT
                       And bp.est_proc = C_ESTADO_COMP_ACT 
                       And ds.iddesactivacion=C_CAMBIO_PLANDNIARUC
                       UNION
                       Select    cd.dn_num,
                                 pc.documentonum,
                                 bp.program_id,
                                 pc.doc_type
                       From           MOTPROM.promt_bono_program bp 
                       inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
                       inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
                       inner join     MOTPROM.promt_campania_desactiva ds
                        on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
                        And bp.est_bono = C_ESTADO_ACT
                        And bp.est_proc = C_ESTADO_COMP_ACT 
                        And ds.iddesactivacion=C_CAMBIO_PLANDNIARUC
                        And bf.campania is null;

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_dniruc;
  
  
  /****************************************************************
  * Nombre SP : promss_desa_cambio_titularidad
  * Proposito : obtener todos los bonos a desactivar 
  *           : por cambio  de titularidad
  * Creado por : JM
  * Fec Creacion : 20/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_titularidad(po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR					   
					   Select cd.dn_num,
							  pc.documentonum,
							  bp.program_id,
							  pc.doc_type
							   From           MOTPROM.promt_bono_program bp 
							   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
							   inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
							   inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
							   inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
							   And bp.est_bono = C_ESTADO_ACT
							   And bp.est_proc = C_ESTADO_COMP_ACT 
							   And ds.iddesactivacion=C_CAMBIO_PLANTITULARIDAD
							   UNION
							   Select    cd.dn_num,
										 pc.documentonum,
										 bp.program_id,
										 pc.doc_type
							   From           MOTPROM.promt_bono_program bp 
							   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
							   inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
							   inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
							   inner join     MOTPROM.promt_campania_desactiva ds
								on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
								And bp.est_bono = C_ESTADO_ACT
								And bp.est_proc = C_ESTADO_COMP_ACT 
								And ds.iddesactivacion=C_CAMBIO_PLANTITULARIDAD
								And bf.campania is null;

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_titularidad;
  
  
  
   /****************************************************************
  * Nombre SP : promss_desa_cambio_facturacion
  * Proposito : obtener todos los bonos a desactivar 
  *           : por Cambio de ciclo de facturación
  * Creado por : JM
  * Fec Creacion : 20/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_facturacion(po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR					   
					   Select cd.dn_num,
							  bp.program_id,
							  pc.billcycle,
							  cd.contract_id_ext
							   From           MOTPROM.promt_bono_program bp 
							   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
							   inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
							   inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
							   inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
							   And bp.est_bono = C_ESTADO_ACT
							   And bp.est_proc = C_ESTADO_COMP_ACT 
							   And ds.iddesactivacion=C_CAMBIO_PLANFACTURACION
							   UNION
							   Select    cd.dn_num,
										 bp.program_id,
										 pc.billcycle,
										 cd.contract_id_ext
							   From           MOTPROM.promt_bono_program bp 
							   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
							   inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
							   inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
							   inner join     MOTPROM.promt_campania_desactiva ds
								on ds.idoperacion=(select t.opertype_id  from MOTPROM.promt_operation_type t where t.opertype_desc=bp.tipo_opera)
								And bp.est_bono = C_ESTADO_ACT
								And bp.est_proc = C_ESTADO_COMP_ACT 
								And ds.iddesactivacion=C_CAMBIO_PLANFACTURACION
								And bf.campania is null;

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_facturacion;
  
  /****************************************************************
  * Nombre SP : promss_desa_cambio_pagopuntual
  * Proposito : obtener todos los bonos a desactivar 
  *           : por  pago puntual
  * Creado por : JM
  * Fec Creacion : 20/09/2018
  ****************************************************************/
  Procedure promss_desa_cambio_pagopuntual(po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR					   
					   Select cd.dn_num,
							  bp.program_id,
							  pc.customer_id_ext
							   From           MOTPROM.promt_bono_program bp 
							   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
							   inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
							   inner join     MOTPROM.promt_campania_desactiva ds on ds.campania=bf.campania 
							   inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
							   And bp.est_bono = C_ESTADO_ACT
							   And bp.est_proc = C_ESTADO_COMP_ACT 
							   And ds.iddesactivacion=C_CAMBIO_PLANPAGOPUNTUAL
							   UNION
							   Select    cd.dn_num,
										 bp.program_id,
										 pc.customer_id_ext
							   From           MOTPROM.promt_bono_program bp 
							   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
							   inner join     MOTPROM.promt_bono_definicion bf on bf.bono_id=bp.bono_id
							   inner join     MOTPROM.promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
							   inner join     MOTPROM.promt_campania_desactiva ds
								on ds.idoperacion=(select t.opertype_id  from promt_operation_type t where t.opertype_desc=bp.tipo_opera)
								And bp.est_bono = C_ESTADO_ACT
								And bp.est_proc = C_ESTADO_COMP_ACT 
								And ds.iddesactivacion=C_CAMBIO_PLANPAGOPUNTUAL
								And bf.campania is null;

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_cambio_pagopuntual;
  
  
   /****************************************************************
  * Nombre SP : promss_desa_baja_contrato
  * Proposito : obtener todos los bonos a desactivar 
  *           : por Baja Contrato
  * Creado por : JM
  * Fec Creacion : 20/09/2018
  ****************************************************************/
  Procedure promss_desa_baja_contrato(   po_programcur      OUT Sys_Refcursor,
                                         po_cod_err  Out Integer,
                                         po_des_err  Out Varchar2) Is

  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

   OPEN po_programcur FOR					   
					   Select cd.dn_num,
							  bp.program_id,
							  cd.contract_id_ext
							   From           MOTPROM.promt_bono_program bp 
							   inner join     MOTPROM.promt_contract_data cd on bp.contract_id = cd.contract_id
							   And bp.est_bono = C_ESTADO_ACT
							   And bp.est_proc = C_ESTADO_COMP_ACT;

  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_desa_baja_contrato;
  
  
  /****************************************************************
  * Nombre SP : promsu_desa_vigen_bono
  * Proposito : Desactivacion de Bonos por  de Fecha vigencia 
  * Creado por : Americo Sierra
  * Fec Creacion : 18/09/2018
  ****************************************************************/
  Procedure promsu_desa_vigen_bono(pi_fec_proc In  Date,
                                     pi_dias     In  Integer,
									 pi_obervacion  in Varchar2,
                                     po_cod_err  Out Integer,
                                     po_des_err  Out Varchar2) Is
  TYPE t_reg_bono IS TABLE OF reg_bonos INDEX BY BINARY_INTEGER;
  v_bono         t_reg_bono;
  v_limit PLS_INTEGER := 100;
  
   CURSOR v_programcur  IS 
       select bp.program_id from  MOTPROM.promt_bono_program  bp
							   where bp.est_proc = C_ESTADO_COMP_ACT 
							   And bp.est_bono = C_ESTADO_ACT
							   And trunc(bp.fec_vigencia) >= (trunc(pi_fec_proc) - pi_dias)
							   And trunc(bp.fec_vigencia) <= trunc(pi_fec_proc) ;
  
  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;
	
	  OPEN  v_programcur;
	  LOOP
		FETCH v_programcur BULK COLLECT
		  INTO v_bono LIMIT v_limit;
		  EXIT WHEN nvl(v_bono.LAST, 0) = 0;
		   IF nvl(v_bono.LAST, 0) > 0 THEN
			FOR q IN v_bono.FIRST .. v_bono.LAST
			 LOOP
				UPDATE MOTPROM.promt_bono_program pro
					Set pro.est_proc = C_ESTADO_PEND_DESACT,
						pro.est_bono = C_ESTADO_PEND,
						pro.observacion=pi_obervacion,
						pro.fec_estado=sysdate
					Where pro.program_id = v_bono(q).program_id;
			 END LOOP;
		   END IF;

	  END LOOP;
  Exception
    When Others Then
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promsu_desa_vigen_bono;
  
  
  
 /****************************************************************
  * Nombre SP : promsi_cambioplan_hist
  * Proposito : Carga de cambio de  estados  de  contratos 
  * Creado por : JM
  * Fec Creacion : 04/04/2019
  ****************************************************************/
  PROCEDURE promsi_cambioplan_hist(PI_EXT_INFO      IN PROMT_EXTENSIONS_TYPE,
                                    PO_CODRPTA       OUT VARCHAR2,
                                    PO_MSJRPTA       OUT VARCHAR2) IS
    COUNT_ROWS_TABLE NUMBER := 0;
	CORRELATIVO             NUMBER;
  BEGIN
    
    SELECT COUNT(*) INTO COUNT_ROWS_TABLE FROM TABLE(PI_EXT_INFO);
    IF COUNT_ROWS_TABLE > 0 THEN
      FOR ITEM IN PI_EXT_INFO.FIRST .. PI_EXT_INFO.LAST LOOP
	    SELECT MOTPROM.PROMT_SEQ_CAMBIOPLAN.NEXTVAL INTO CORRELATIVO FROM DUAL;
        INSERT INTO MOTPROM.PROMT_CAMBIOPLAN_HIST
          (idcambioplan,tipodocumento,nrodocumento,customer_id,contract_id,linea,
           planorigen,cargofijorigen,plandestino,cargofijodestino,fechacambioplan)
        values
          (CORRELATIVO,
		   PI_EXT_INFO     (ITEM).tipodocumento_temp,
           PI_EXT_INFO     (ITEM).nrodocumento_temp,
		   PI_EXT_INFO     (ITEM).customer_id_temp,
           PI_EXT_INFO     (ITEM).contract_id_temp,
		   PI_EXT_INFO     (ITEM).linea_temp,
           PI_EXT_INFO     (ITEM).planorigen_temp,
		   PI_EXT_INFO     (ITEM).cargofijorigen_temp,
           PI_EXT_INFO     (ITEM).plandestino_temp,
		   PI_EXT_INFO     (ITEM).cargofijodestino_temp,
           PI_EXT_INFO     (ITEM).fechacambioplan_temp
		   );
      END LOOP;
      PO_CODRPTA := '0';
      PO_MSJRPTA := 'Registro de transaccion exitosa.';
	ELSE
	  PO_CODRPTA := '1';
      PO_MSJRPTA := 'No existe transaccion a Insertar';
	
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      PO_CODRPTA := '-1';
      PO_MSJRPTA := 'Error de insercion => ' || sqlerrm;

  END promsi_cambioplan_hist;
  
  
  /****************************************************************
  * Nombre SP : promss_cambioplan_hist
  * Proposito : obtener los datos cargados replicas
  * Creado por : JM
  * Fec Creacion : 04/04/2019
  ****************************************************************/
  PROCEDURE promss_cambioplan_hist(PI_LINEA      IN VARCHAR2,
                                    PI_TIPODOCUMENTO       IN VARCHAR2,
                                    PI_NRODOCUMENTO       IN VARCHAR2,
                                    PO_datocur      OUT Sys_Refcursor,
                                    PO_CODRPTA       OUT VARCHAR2,
                                    PO_MSJRPTA       OUT VARCHAR2) IS
  BEGIN
    
    IF PI_LINEA is not null THEN
            OPEN PO_datocur FOR					   
					  select idcambioplan,tipodocumento,nrodocumento,customer_id,contract_id,linea,
							 planorigen,cargofijorigen,plandestino,cargofijodestino,fechacambioplan
					  from MOTPROM.PROMT_CAMBIOPLAN_HIST t
					  where t.linea=PI_LINEA;
			
      PO_CODRPTA := '0';
      PO_MSJRPTA := 'OK';
	ELSE
	    IF PI_TIPODOCUMENTO is not null AND PI_NRODOCUMENTO is not null THEN
		    OPEN PO_datocur FOR					   
					  select idcambioplan,tipodocumento,nrodocumento,customer_id,contract_id,linea,
							 planorigen,cargofijorigen,plandestino,cargofijodestino,fechacambioplan
					  from MOTPROM.PROMT_CAMBIOPLAN_HIST t
					  where t.tipodocumento=PI_TIPODOCUMENTO
                      and t.nrodocumento=PI_NRODOCUMENTO;
			PO_CODRPTA := '0';
			PO_MSJRPTA := 'OK';
		ELSE
		   PO_CODRPTA := '1';
           PO_MSJRPTA := 'No se  envio Linea o Numero  y tipo de Documento para Busqueda';
		END IF;
    END IF;
  EXCEPTION
    WHEN OTHERS THEN
      PO_CODRPTA := '-1';
      PO_MSJRPTA := 'Error de insercion => ' || sqlerrm;

  END promss_cambioplan_hist;
 
	
 /****************************************************************
  * Nombre SP : promss_venta_bonos_mf2
  * Proposito : obtener Datos  del  Bono a Activar
  *           : para el recalculo
  * Creado por : JM
  * Fec Creacion : 26/04/2019
  ****************************************************************/
  Procedure promss_venta_bonos_mf2(pi_plan             In  Varchar2,
                                  pi_motivo_act       In  Varchar2,
                                  pi_fec_proc         In  Date,
                                  pi_campania         In  Varchar2,
                                  pi_flag_base        In  int,
                                  po_cur_bonos        Out Sys_Refcursor,
                                  po_cod_err          Out Integer,
                                  po_des_err          Out Varchar2) Is
  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

    if pi_flag_base = 1 Then
	    MOTPROM.pkg_motprom_proces_eval.promss_venta_bonos_mf(pi_plan,
							  pi_motivo_act,
							  pi_fec_proc,
							  pi_campania,
							  true,
							  po_cur_bonos,
							  po_cod_err,
							  po_des_err);
    Else
     MOTPROM.pkg_motprom_proces_eval.promss_venta_bonos_mf(pi_plan,
							  pi_motivo_act,
							  pi_fec_proc,
							  pi_campania,
							  false,
							  po_cur_bonos,
							  po_cod_err,
							  po_des_err);
    End If;
  Exception
    When Others Then
      pkg_motprom_log.error( Sqlerrm );
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promss_venta_bonos_mf2;
  
  /****************************************************************
  * Nombre SP : promsi_program_bono_recalculo
  * Proposito : insertar bono
  *           : para el recalculo
  * Creado por : JM
  * Fec Creacion : 26/04/2019
  ****************************************************************/
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
                                        po_des_err        Out Varchar2) Is

     v_fechavigencia Date;
	 v_contract_id      Integer;
    Begin
      po_cod_err := C_MSJ_EXITO_COD;
      po_des_err := C_MSJ_EXITO_DES;

      v_fechavigencia:=promfun_fec_desactivacion_bono(pi_periodo,pi_activacion,pi_ciclo_fact,Sysdate);
      v_contract_id := promfun_obtener_contrato(pi_linea);
	  
	  IF  v_contract_id is null THEN
	    Select MOTPROM.seq_promt_contract_data.nextval
        Into v_contract_id
        From dual;
	  END IF;
	   
      promsi_program_bono_interno(v_contract_id,
                                  pi_bono_id,
                                  pi_po_bono,
                                  pi_po_ontop,
                                  pi_po_plan,
                                  pi_order_id,
                                  pi_fec_act_bono,
                                  v_fechavigencia,
                                  pi_est_bono,
                                  pi_est_proc,
                                  pi_periodo,
                                  pi_tipo_bono,
                                  pi_ciclo_fact,
                                  pi_bono_periodico,
                                  pi_tipo_opera,
                                  pi_fec_crea,
                                  pi_fec_modi,
                                  pi_est_om,
                                  pi_fec_crea_om,
                                  pi_fec_ini_ejec_om,
                                  pi_fec_fin_ejec_om,
                                  pi_shopping_cart_id,
                                  po_cod_err,
                                  po_des_err);
    Exception
      When Others Then
        pkg_motprom_log.error( Sqlerrm );
        po_cod_err := Sqlcode;
        po_des_err := Sqlerrm;
    End promsi_program_bono_recalculo;
  
  
  /****************************************************************
  * Nombre SP : promsi_contrato_valida
  * Proposito : Valida  si existe contrato en promt_contract_data 
  *           : en caso de no existir, crea un contrato. 
  * Creado por : JM
  * Fec Creacion : 30/04/2019
  ****************************************************************/
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
                            po_des_err         Out Varchar2) Is
  v_contract_id      Integer;
  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;
	
	   select count(*)  into v_contract_id from MOTPROM.promt_contract_data d where d.dn_num=pi_linea and d.contract_id_ext=pi_contract_id_ext;
	   IF v_contract_id<1 THEN 
           promsi_opera_porta(
		                      pi_customer_id_ext,
                              null,--base.tipo_clie,
                              null,--base.segmento_cliente,
                              null,--base.canal,
                              null,--base.cod_punto_venta,
                              null,--base.cod_dept_pv,
                              null,--base.cod_prov_pv,
                              null,--base.cod_dist_pv,
                              null,--base.fec_nacimiento,
                              null,--base.genero,
                              pi_tipo_documento,
                              pi_numero_documento,
                              null,--base.suscripcion_correo,
                              pi_billcycle,
                              pi_linea,
                              pi_billing_account,
                              pi_contract_id_ext,--base.contract_id_ext,
                              null,--base.tipo_operacion,
                              null,--base.tipo_producto,--base.tipo_producto,
                              null,--base.iccid,--base.imsi,
                              null,--base.cant_decos,
                              null,--base.segmento_equipo,
                              null,--base.tipo_suscripcion,--base.tipo_suscripcion,
                              null,--base.fec_alta,
                              null,--base.campania,
                              null,--base.plan,
                              pi_cargo_fijo,--base.cargo_fijo,
                              null,--base.tope,
                              null,--base.plazo_contrato,--base.plazo_contrato,
                              null,--base.linea_referente,
                              null,--base.fec_act_oper_cedente,--base.fec_act_cedente,
                              null,--base.oper_cedente,--base.operador_cedente,
                              null,--base.cant_lineas_prepago,
                              null,--base.cant_lineas_postpago,
                              null,--base.cant_lineas_servfijo,
                              null,--base.modalidad_venta,
                              null,--base.cod_dept_fac,
                              null,--base.cod_prov_fac,
                              null,--base.cod_dist_fac,
                              null,--base.cod_dept_ins,
                              null,--base.cod_prov_ins,
                              null,--base.cod_dist_ins,
                              null,--base.tecno_equipo_movil,
                              null,--base.modelo_equipo,
                              null,--base.precio_venta_equipo,
                              null,--pi_order_id,
                              pi_po_basic, --po_basic 
                              null, 
                              null,--base.caso_especial,
                              null,---base.app_origen,
                              null,---base.clasi_charlotte,
                              null,---base.marca_equipo,
                              null,
                              null,
                              null,
                              null,
                              null,
                              sysdate,--base.fec_transaccion,
                              sysdate,
                              po_cod_err,
                              po_des_err);

	   END IF;
  Exception
    When Others Then
      pkg_motprom_log.error( Sqlerrm );
      po_cod_err := Sqlcode;
      po_des_err := Sqlerrm;
  End promsi_contrato_valida;
  
  
 /****************************************************************
  * Nombre SP : promss_recal_maxfamilia
  * Proposito : obtener  las lineas 
  *           : con menor  cargo fijo. 
  * Creado por : JM
  * Fec Creacion : 30/04/2019
  ****************************************************************/
  Procedure promss_recal_maxfamilia(pi_num      In  Integer,
                                    pi_base     In  T_PROM_VENTA_LISTA_ONE,
                                    po_lineas   Out Sys_Refcursor,
                                    po_cod_err  Out Integer,
                                    po_des_err  Out Varchar2) Is
  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

    Open po_lineas For
      select * from (
            select row_number() over(partition by 1 order by cargo_fijo_plan_base) as orden,
                     contract_id_base,
                     linea_base,
                     po_id_base,
                     po_name_base,
                     customer_id_ext_base,
                     cargo_fijo_plan_base,
                     billing_account_base,
                     fec_activacion_base,
                     ciclo_fact_base
                from table(pi_base))x
               where x.orden <= pi_num;
    Exception
        When Others Then
          pkg_motprom_log.error( Sqlerrm );
          po_cod_err := Sqlcode;
          po_des_err := Sqlerrm;
  End promss_recal_maxfamilia;
  
  
  /****************************************************************
  * Nombre SP : promss_dnnum_maxfamilia
  * Proposito : obtener  las lineas 
  *           : para el  recalculo. 
  * Creado por : JM
  * Fec Creacion : 30/04/2019
  ****************************************************************/
  Procedure promss_dnnum_maxfamilia(po_lineas   OUT Sys_Refcursor,
                                    po_cod_err  Out Integer,
                                    po_des_err  Out Varchar2) Is
  Begin
    po_cod_err := C_MSJ_EXITO_COD;
    po_des_err := C_MSJ_EXITO_DES;

    Open po_lineas For
    select bp.program_id,
           bp.contract_id,
           bp.est_bono,
           bp.est_proc,
           bp.bono_id,
           bp.po_bono,
           bp.po_ontop,
           bp.order_id,
           bp.fec_act_bono,
           bp.fec_vigencia,
           bp.po_basic,
           bp.periodo,
           bp.bono_periodico,
           bp.tipo_opera,
           bp.est_om,
           bp.shopping_cart_id,
           --bp.cargo_fijo,
           cd.fixed_charge,
           bd.campania,
           cd.contract_id_ext,
           cd.customer_id_ext,
           cd.billing_account,
           cd.dn_num,
           ca.doc_type,
           ca.documentonum,
           bd.activacion
      from MOTPROM.promt_bono_program bp
      join MOTPROM.promt_bono_definicion bd on bp.bono_id=bd.bono_id
      join MOTPROM.promt_contract_data cd on bp.contract_id=cd.contract_id
      join MOTPROM.promt_customer_all ca on cd.customer_id_ext=ca.customer_id_ext
     where bd.campania in ( select valor2
                               from promt_parametros p
                              where p.campo='CMF')
       and ((bp.est_proc='CA' and bp.est_bono=C_ESTADO_ACT) or
           (bp.est_proc='PA' and bp.est_bono=C_ESTADO_PEND) or
           (bp.est_proc='EA' and bp.est_bono=C_ESTADO_PEND))
       and trunc(bp.fec_vigencia) > trunc(sysdate);
    Exception
      When Others Then
        pkg_motprom_log.error( Sqlerrm );
        po_cod_err := Sqlcode;
        po_des_err := Sqlerrm;
  End;
  
End pkg_motprom_proces_trans;
