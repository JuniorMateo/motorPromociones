-- Create table
create table  MOTPROM.PROMT_CAMPANIA_DESACTIVA
(
  idcampaniadesa          INTEGER not null,
  campania                VARCHAR2(200) ,
  IDOPERACION             INTEGER,
  iddesactivacion         INTEGER not null,
  prioridad               char(3),
  tiempo                  INTEGER
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
alter table MOTPROM.PROMT_CAMPANIA_DESACTIVA
  add constraint PK_Campaniadesa primary key (idcampaniadesa)
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
comment on column MOTPROM.PROMT_CAMPANIA_DESACTIVA.idcampaniadesa
  is 'id  de idcampaniadesa';
    -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMPANIA_DESACTIVA.campania
  is 'idescripcion de campania';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMPANIA_DESACTIVA.IDOPERACION
  is 'id de operacion';
    -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMPANIA_DESACTIVA.iddesactivacion
  is 'id de idDesactivacion';
    -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMPANIA_DESACTIVA.prioridad
  is 'descripcion de prioridad';
    -- Add comments to the columns 
comment on column MOTPROM.PROMT_CAMPANIA_DESACTIVA.tiempo
  is 'valor de tiempo  < o > ' ;

alter table MOTPROM.PROMT_CAMPANIA_DESACTIVA
  add constraint FK_PROMT_CAMPANIA_DESACTIVA foreign key (iddesactivacion)
  references PROMT_DESACTIVACION (idDesactivacion);
