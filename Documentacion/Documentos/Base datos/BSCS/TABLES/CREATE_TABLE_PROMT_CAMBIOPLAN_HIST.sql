-- Create table
create table  MOTPROM.PROMT_CAMBIOPLAN_HIST
( 
idcambioplan  	 INTEGER not null,
tipodocumento 	 VARCHAR2(30),
nrodocumento   	 VARCHAR2(20),
customer_id   	 INTEGER,
contract_id   	 INTEGER,
linea            VARCHAR2(100),
planorigen    	 INTEGER,
cargofijorigen 	 FLOAT,
plandestino      INTEGER,
cargofijodestino FLOAT,
fechacambioplan  DATE
 )
tablespace TABLESPACE_MOTPROM
  pctfree 10
  pctused 40
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table MOTPROM.PROMT_CAMBIOPLAN_HIST
  add constraint PK_PROMT_CAMBIOPLAN_HIST primary key (idcambioplan)
  using index 
  tablespace MOTPROM_INDX
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.idcambioplan
  is 'id  de cambio de plan';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.tipodocumento
  is 'tipo de documento';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.nrodocumento
  is 'numero de documento';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.customer_id
  is 'customer id';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.contract_id
  is 'contract_id';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.linea
  is 'linea';
    -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.planorigen
  is 'plan origen';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.cargofijorigen
  is 'cargo fijo origen';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.plandestino
  is 'plande stino';
    -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.cargofijodestino
  is 'cargo fij odestino';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMBIOPLAN_HIST.fechacambioplan
  is 'fecha cambio plan';
  
  
  
