-- Create table
create table  MOTPROM.PROMT_DESACTIVACION
(
  idDesactivacion         INTEGER not null,
  descripcion             VARCHAR2(300) ,
  grupo                   VARCHAR2(200)
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
alter table MOTPROM.PROMT_DESACTIVACION
  add constraint PK_Desactivacion primary key (idDesactivacion)
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
comment on column MOTPROM.PROMT_DESACTIVACION.idDesactivacion
  is 'id  de desactivacion';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_DESACTIVACION.descripcion
  is 'descripcion de desactivacion';
  -- Add comments to the columns 
comment on column MOTPROM.PROMT_DESACTIVACION.grupo
  is 'grupo de desactivacion';

