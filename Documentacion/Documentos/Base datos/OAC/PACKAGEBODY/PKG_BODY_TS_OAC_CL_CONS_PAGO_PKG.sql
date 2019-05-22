create or replace package body TS_OAC_CL_CONS_PAGO_PKG is
	
  /*========================================================================================+
  | DESCRIPTION : Consulta de pagos puntuales                                               |
  | WHEN          WHO                 WHAT                                                  |
  | ----------    ------------------- ------------------------------------------------------|
  | 22/02/2019    Fernan Palomino     Creación                                              |
  |=========================================================================================*/
 procedure pr_pago_puntual(piv_CUSTOMER_ID    varchar2
                           ,pin_CANTIDAD_MESES number
                           ,pov_COD_RESPUESTA  out varchar2
                           ,pov_MSG_RESPUESTA  out varchar2
                           ,pov_INDICADOR_PP   out number) is
		-- Variables
		v_Cust_Trx_Type_Id   APPS.ra_cust_trx_types_all.cust_trx_type_id%type;
		v_Cust_Account_Id    APPS.hz_cust_accounts.cust_account_id%type;
		v_Cant_Doc           number := 0;
		v_Cant_Pag_Inpuntual number := 0;
		d_Fec_Apli_Inpuntual date;
	  
		-- Variables de retorno
		v_COD_RESPUESTA varchar2(2);
		v_MSG_RESPUESTA varchar2(250);
		v_INDICADOR_PP  number;
		
		cursor c_Documentos(pv_Cust_Account_Id  number
		                   ,pv_Cust_Trx_Type_Id number
		                   ,pn_Cantidad_Meses   number) is
		select rct.customer_trx_id
		     , rct.trx_number
				 , aps.due_date
			from APPS.ra_customer_trx_all      rct
			   , APPS.ar_payment_schedules_all aps
		 where 1 = 1
		   and rct.customer_trx_id  = aps.customer_trx_id
			 and aps.customer_id      = pv_Cust_Account_Id
			 and aps.cust_trx_type_id = pv_Cust_Trx_Type_Id
			 and aps.status           = 'CL'
			 and aps.due_date        >= ADD_MONTHS(to_date(to_char(sysdate,'YYYYMM')|| '01', 'YYYYMMDD'), -pn_Cantidad_Meses)
	   ;
		 
	begin
	  
	  -- Validar variables ingresadas
		if nvl(piv_CUSTOMER_ID, 'XX') = 'XX' then
			v_COD_RESPUESTA := '-1';
			v_MSG_RESPUESTA := 'Ingrese el CUSTOMER_ID';
			v_INDICADOR_PP  := 0;
			goto Resultado;			
		end if;
		
		if nvl(pin_CANTIDAD_MESES, -99) = -99 then
			v_COD_RESPUESTA := '-1';
			v_MSG_RESPUESTA := 'Ingrese la cantidad de meses';
			v_INDICADOR_PP  := 0;
			goto Resultado;			
		end if;
	
		-- Obtener el tipo de documento
		select rctt.Cust_Trx_Type_Id
			into v_Cust_Trx_Type_Id
			from APPS.ra_cust_trx_types_all rctt
		 where rctt.name = 'REC';
	  
		-- Obtener customer id OAC
		begin
			select hca.cust_account_id
				into v_Cust_Account_Id
				from APPS.hz_cust_accounts hca
			 where hca.account_number = piv_CUSTOMER_ID;
	     
		exception
			when no_data_found then
				v_COD_RESPUESTA := '-1';
				v_MSG_RESPUESTA := 'Cliente no existe';
				v_INDICADOR_PP  := 0;
				goto Resultado;
			when too_many_rows then
				v_COD_RESPUESTA := '-1';
				v_MSG_RESPUESTA := 'Customer_id duplicado para clientes distintos';
				v_INDICADOR_PP  := 0;
				goto Resultado;
			when others then  
				v_COD_RESPUESTA := '-1';
				v_MSG_RESPUESTA := 'Error al obtener datos del Cliente. ' || sqlerrm;
				v_INDICADOR_PP  := 0;
				goto Resultado;
		end;
	  
		-- Obtner los documentos
		begin

      for c in c_Documentos(v_Cust_Account_Id, v_Cust_Trx_Type_Id, pin_CANTIDAD_MESES) loop
				
				-- ME :: Si tiene al menos un documento con fecha de aplicacion superior 
				--       a la fecha de vencimiento ya es pagador inpuntual
				select case when max(ara.apply_date) > c.due_date then 1 else 0 end
				     , max(ara.apply_date)
				  into v_Cant_Pag_Inpuntual
					   , d_Fec_Apli_Inpuntual
				  from APPS.ar_receivable_applications_all ara
				 where ara.applied_customer_trx_id = c.customer_trx_id
				   and ara.application_type        = 'CASH'
					 and ara.display                 = 'Y';
				
				
			  v_Cant_Doc := v_Cant_Doc + 1;
				
				if (v_Cant_Pag_Inpuntual = 1) then
					
					exit;
					
				end if;
				
			end loop;
		exception
			when others then
				v_COD_RESPUESTA := '-1';
				v_MSG_RESPUESTA := 'No se puede obtener datos de los documentos del Cliente. ' || sqlerrm;
				v_INDICADOR_PP  := 0;
				goto Resultado;
		end;	  
	  
		-- Escenarios
		if v_Cant_Doc = 0 then
			v_COD_RESPUESTA := '0';
			v_MSG_RESPUESTA := 'Cliente no tiene documentos en el rango de meses ingresado';
			v_INDICADOR_PP  := 0; 
		elsif v_Cant_Pag_Inpuntual = 0 then
			v_COD_RESPUESTA := '0';
			v_MSG_RESPUESTA := 'Cliente paga puntual';
			v_INDICADOR_PP  := 1; 
		else
			v_COD_RESPUESTA := '0';
			v_MSG_RESPUESTA := 'Cliente no paga puntual';
			v_INDICADOR_PP  := 2; 
		end if;
	  
		<<Resultado>>
		-- Devolver datos
		pov_COD_RESPUESTA := v_COD_RESPUESTA;
		pov_MSG_RESPUESTA := v_MSG_RESPUESTA;
		pov_INDICADOR_PP  := v_INDICADOR_PP;
	exception
		when others then
			pov_COD_RESPUESTA := '-1';
			pov_MSG_RESPUESTA := 'Error al ejecutar el procedimiento pr_pago_puntual :: ' || sqlerrm;
			pov_INDICADOR_PP  := 0;
	end pr_pago_puntual ;
end TS_OAC_CL_CONS_PAGO_PKG;
