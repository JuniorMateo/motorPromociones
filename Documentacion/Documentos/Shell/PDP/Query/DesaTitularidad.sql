--SH TITULARIDAD
select * from promt_bono_program p where p.contract_id=112  and  p.bono_id=100100  for update
select * from promt_bono_program p where p.bono_id=7383
select * from promt_bono_program p where p.po_basic like '%ESPECIAL%'
 --BONO_ID=7376 7377 7378 7379 7380 7381 7382-7383 7384    po_bono: BonoMCH29OptOne
select * from promt_bono_definicion d where d.campania='MAX BENEFICIO DUPLICA TUS MB (GE)' and d.bono_id=8796 
select * from promt_contract_data d where   d.customer_id_ext = 'CU_00100001' for update
select * from promt_contract_data d where   d.contract_id_ext = 'CONTR0000001118' for update

select * from promt_contract_data d where   d.contract_id= 12 for update
select * from promt_contract_data d where   d.contract_id_ext= 'CONTR0000001122' for update
SELECT * from  promt_rateplan_hist t where t.contract_id=13 for update
select * from customer_all;
  select * from promt_customer_all --for update; 22470419
  where customer_id_ext = 'CU_00100002' for update   --Max_Internacional_39
select  * from promt_bono_plan
select * from promt_bono_suscripcion  --DNI
select * from promt_campania_desactiva a where a.iddesactivacion=3
-->DNI=10495455   Cutomer_id_text=CU_00100001


--Actualizar  estado A

declare 
conta integer:=7376;
begin 
  for f in  0..6 loop
    update promt_bono_program p
    set  p.est_bono='A',p.est_proc='CA',p.fec_estado='', p.observacion=''
    where p.contract_id=12 and p.bono_id=conta;
    conta:=conta+1;
  end loop;
  
  EXCEPTION
  WHEN OTHERS
    THEN
      DBMS_OUTPUT.put_line(SQLERRM);
end;
--cambio dni a ruc 100036,100037 92 
--cambio dni a ruc 1007250  70 

          Select   cd.dn_num,
                  pc.documentonum,
                bp.bono_id,
                bp.contract_id,
                pc.doc_type
                       From           promt_bono_program bp 
                       inner join     promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     promt_bono_definicion bf on bf.bono_id=bp.bono_id
                       --inner join     promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     promt_campania_desactiva ds on ds.campania=bf.campania 
                       inner join     promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
                       --And trunc(rp1.created_date) >= (trunc(sysdate) - 0)
                       --And trunc(rp1.created_date) <= trunc(sysdate)
                       And bp.est_bono = 'A'
                       And bp.est_proc = 'CA'
                       --And rp1.seqno=(select max(t.seqno) from promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                       And ds.iddesactivacion=12
                       UNION
                       Select    cd.dn_num,
                                 pc.documentonum,
                                 bp.bono_id,
                                 bp.contract_id,
                                 pc.doc_type
                       From           promt_bono_program bp 
                       inner join     promt_contract_data cd on bp.contract_id = cd.contract_id
                       inner join     promt_bono_definicion bf on bf.bono_id=bp.bono_id
                       --inner join     promt_rateplan_hist rp1 on rp1.contract_id=bp.contract_id
                       inner join     promt_customer_all pc on pc.customer_id_ext=cd.customer_id_ext
                       inner join     promt_campania_desactiva ds
                        on ds.idoperacion=(select t.opertype_id  from promt_operation_type t where t.opertype_desc=bp.tipo_opera)
                       -- And trunc(rp1.created_date) >= (trunc(sysdate) - 0)
                       -- And trunc(rp1.created_date) <= trunc(sysdate)
                        And bp.est_bono = 'A'
                        And bp.est_proc = 'CA'
                        --And rp1.seqno=(select max(t.seqno) from promt_rateplan_hist t where t.contract_id=rp1.contract_id)
                        And ds.iddesactivacion=12
                        And bf.campania is null;
                       
                    
