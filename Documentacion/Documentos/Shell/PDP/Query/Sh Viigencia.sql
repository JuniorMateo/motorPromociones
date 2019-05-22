--SH  Vigencia
select * from promt_bono_program p where   p.bono_id=106001 for update   --123100  100037 100036f
select * from promt_bono_program p where p.contract_id=30  for update  and  p.bono_id=7294  for update --BONO_ID=100036 100037
select * from promt_bono_program p where   p.po_bono like '%Max%'
select * from promt_bono_program p where   p.program_id=38 for update --38 47
select * from promt_bono_definicion d where d.campania='%Maxa%' and d.bono_id=8796 
select * from promt_bono_definicion d where d.bono_id=104886  for update
select * from promt_contract_data t where  t.contract_id=104  for update  -- 994883764
select * from promt_contract_data d where   d.contract_id_ext = 'CONTR0000001118' for update
select * from  promt_rateplan_hist t where t.contract_id=104 for update
select * from promt_operation_type
select * from promt_desactivacion
select * from promt_campania_desactiva a  where  a.idcampaniadesa=5
select * from customer_all;
  select * from promt_customer_all --for update; 22470419
  where customer_id_ext = 'CU03736871992' for update   --CU_00100001
  
select sysdate as fecha from dual;
  
declare 
conta integer:=100036;
begin 
  for f in  0..2 loop
    update promt_bono_program p
    set  p.est_bono='A',p.est_proc='CA',p.fec_estado='', p.observacion=''
    where p.contract_id=92 and p.bono_id=conta;
    conta:=conta+1;
  end loop;
  
  EXCEPTION
  WHEN OTHERS
    THEN
      DBMS_OUTPUT.put_line(SQLERRM);
end;

select * from promt_bono_program p where   p.bono_id=106001

declare 
conta integer:=570;
begin 
  for f in  0..1 loop
    update promt_bono_program p
    set  p.est_bono='A',p.est_proc='CA',p.fec_estado='', p.observacion=''
    where p.contract_id=conta and p.bono_id=106001;
    conta:=conta+1;
  end loop;
  
  EXCEPTION
  WHEN OTHERS
    THEN
      DBMS_OUTPUT.put_line(SQLERRM);
end;
  
 select bp.program_id,bp.fec_vigencia from  promt_bono_program  bp
							   where bp.est_proc = 'CA' 
							   And bp.est_bono = 'A'
							   And trunc(bp.fec_vigencia) >= (trunc(to_date('05/05/2020')) - 0)
							   And trunc(bp.fec_vigencia) <= trunc(to_date('05/05/2020')) ;
-- 38  47
