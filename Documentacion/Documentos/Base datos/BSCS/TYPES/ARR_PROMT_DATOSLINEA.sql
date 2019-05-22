CREATE OR REPLACE Type MOTPROM.t_prom_venta_datos_linea_one force As Object (
  contract_id_base        Varchar2(40),--[Nuevo]{Obtener lineas que tenga el cliente}
  linea_base              Varchar2(15),--[Nuevo]{Obtener lineas que tenga el cliente}
  po_id_base              Varchar2(50),--[Nuevo]{Obtener lineas que tenga el cliente}
  po_name_base            Varchar2(50),--[Nuevo]{Obtener lineas que tenga el cliente}
  customer_id_ext_base    Varchar2(30),--[Nuevo]{Obtener lineas que tenga el cliente}
  billing_account_base    Varchar2(30),--[Nuevo]{Obtener lineas que tenga el cliente}
  ciclo_fact_base          Varchar2(2),--[Nuevo]{Obtener lineas que tenga el cliente}
  cargo_fijo_plan_base    Number(10,2),--[Nuevo]{Obtener el Cargo Fijo a partir de la PO Basica}
  tipo_producto_base      Varchar2(20),--[Nuevo]{Obtener lineas que tenga el cliente}
  tipo_suscripcion_base    Varchar2(15),--[Nuevo]{Obtener lineas que tenga el cliente}
  fec_activacion_base      Date,--[Nuevo]{Consulta  fecha de activacion de la linea}
  estado_linea_base        Varchar2(20),--[Nuevo]{Consulta el estado de la linea}
  bundle_id                Varchar2(50),--[Nuevo]{Consulta el estado de la linea} 31/03/2019
  status_billing          Varchar2(50),--[Nuevo]{Consulta el estado de la linea} 31/03/2019
  mail                    Varchar2(120),--[Nuevo]{Consulta el estado de la linea} 31/03/2019
  tecnologia              Varchar2(50),--[Nuevo]{Consulta el estado de la linea} 31/03/2019
  tipo_telefono           Varchar2(50),--[Nuevo]{Consulta el estado de la linea} 31/03/2019
  fec_mod_suscription     Date,--[Nuevo]{Consulta el estado de la linea} 31/03/2019
  his_bloq                t_prom_venta_lista_bloq,--[Nuevo]{onsulta bloqueo de linea}
  ind_linea_porta_base    Varchar2(20),--[Nuevo]{Lineas de la base que hayan sido portadas}
  his_camb_plan_base      t_prom_venta_lista_hist_cplan,--[Nuevo]{Historial cambio de planes en los ultimos 30 dias}
  his_renovacion_base      t_prom_venta_lista_hist_reno,--[Nuevo]{Consulta renovaciones de la linea}
  bolsa_linea_base		  	t_prom_venta_lista_base_bolsa--[Nuevo]{Obten
  )
