--SH FACTURACION
--bono_id :100001,100002,100003,100004,100005, 100005 -- PO_BONO:cambio_de_plan_especial
select * from promt_bono_program p where p.contract_id=14 
and  p.bono_id IN(100001,100002,100003,100004,100005) for update
select * from promt_bono_program p where  p.bono_id=100002  for update 
select * from promt_bono_definicion d where d.bono_id=100002  FOr update
select * from promt_contract_data d where   d.customer_id_ext = 'CU_00100001' for update
select * from promt_contract_data d where   d.contract_id_ext = 'CONTR0000001118' for update
select * from promt_contract_data c where  c.contract_id=14
SELECT * from  promt_rateplan_hist t where t.contract_id=14 for update
select * from customer_all;
select * from promt_customer_all p where  p.customer_id_ext='CUST0000014408' for update; 22470419
where customer_id_ext = 'CUST0000000238' for update
select * from promt_operation_type y where  y.opertype_desc='PORTA'
select * from promt_bono_suscripcion  --DNI
select * from promt_campania_desactiva a where a.iddesactivacion=14


--Actualizar  estado A
declare 
conta integer:=100001;
begin 
  for f in  0..6 loop
    update promt_bono_program p
    set  p.est_bono='A',p.est_proc='CA',p.fec_estado=''
    where p.contract_id=14 and p.bono_id=conta;
    conta:=conta+1;
  end loop;
  
  EXCEPTION
  WHEN OTHERS
    THEN
      DBMS_OUTPUT.put_line(SQLERRM);
end;


--consulta Facturacion 
Select cd.dn_num,
                bp.bono_id,
                bp.contract_id,
                pc.billcycle,
                cd.contract_id_ext
                 From           promt_bono_program bp 
                 inner join     promt_contract_data cd on bp.contract_id = cd.contract_id
                 inner join     promt_bono_definicion bf on bf.bono_id=bp.bono_id
                 inner join     promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                 inner join     promt_campania_desactiva ds on ds.campania=bf.campania 
                 inner join     promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
                 And trunc(rp1.created_date) >= (trunc(sysdate) - 0)
                 And trunc(rp1.created_date) <= trunc(sysdate)
                 And bp.est_bono = 'A'
                  And bp.est_proc = 'CA'
                 And rp1.seqno=(select max(t.seqno) from promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                 And ds.iddesactivacion=14
                 UNION
                 Select    cd.dn_num,  --bonos  que  no tienen  campañas
                     bp.bono_id,
                     bp.contract_id,
                     pc.billcycle,
                     cd.contract_id_ext
                 From           promt_bono_program bp 
                 inner join     promt_contract_data cd on bp.contract_id = cd.contract_id
                 inner join     promt_bono_definicion bf on bf.bono_id=bp.bono_id
                 inner join     promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                 inner join     promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
                 inner join     promt_campania_desactiva ds
                on ds.idoperacion=(select t.opertype_id  from promt_operation_type t where t.opertype_desc=bp.tipo_opera)
                 And trunc(rp1.created_date) >= (trunc(sysdate) - 0)
                 And trunc(rp1.created_date) <= trunc(sysdate)
                 And bp.est_bono = 'A'
                 And bp.est_proc = 'CA'
                And rp1.seqno=(select max(t.seqno) from promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                And ds.iddesactivacion=14
                And bf.campania is null;
                    
