# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

-- init script create procs
-- Inital script to create stored procedures etc for mysql platform
DROP PROCEDURE IF EXISTS usp_ebean_drop_foreign_keys;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_foreign_keys TABLE, COLUMN
-- deletes all constraints and foreign keys referring to TABLE.COLUMN
--
CREATE PROCEDURE usp_ebean_drop_foreign_keys(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE c_fk_name CHAR(255);
  DECLARE curs CURSOR FOR SELECT CONSTRAINT_NAME from information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE() and TABLE_NAME = p_table_name and COLUMN_NAME = p_column_name
      AND REFERENCED_TABLE_NAME IS NOT NULL;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN curs;

  read_loop: LOOP
    FETCH curs INTO c_fk_name;
    IF done THEN
      LEAVE read_loop;
    END IF;
    SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP FOREIGN KEY ', c_fk_name);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
  END LOOP;

  CLOSE curs;
END
$$

DROP PROCEDURE IF EXISTS usp_ebean_drop_column;

delimiter $$
--
-- PROCEDURE: usp_ebean_drop_column TABLE, COLUMN
-- deletes the column and ensures that all indices and constraints are dropped first
--
CREATE PROCEDURE usp_ebean_drop_column(IN p_table_name VARCHAR(255), IN p_column_name VARCHAR(255))
BEGIN
  CALL usp_ebean_drop_foreign_keys(p_table_name, p_column_name);
  SET @sql = CONCAT('ALTER TABLE ', p_table_name, ' DROP COLUMN ', p_column_name);
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
END
$$
create table history (
  id                            bigint auto_increment not null,
  date                          datetime(6),
  product_id                    bigint,
  int_package                   integer not null,
  piece                         integer not null,
  user_id                       bigint,
  constraint pk_history primary key (id)
);

create table product (
  id                            bigint auto_increment not null,
  date                          datetime(6),
  name                          varchar(255),
  int_package                   integer not null,
  piece                         integer not null,
  category_id                   bigint,
  constraint pk_product primary key (id)
);

create table product_category (
  id                            bigint auto_increment not null,
  date                          datetime(6),
  category_name                 varchar(255),
  constraint pk_product_category primary key (id)
);

create table role (
  id                            bigint auto_increment not null,
  date                          datetime(6),
  name                          varchar(255),
  logic                         varchar(255),
  constraint pk_role primary key (id)
);

create table sale (
  id                            bigint auto_increment not null,
  date                          datetime(6),
  product_id                    bigint,
  int_package                   integer not null,
  piece                         integer not null,
  user_id                       bigint,
  commission                    double not null,
  constraint pk_sale primary key (id)
);

create table user (
  id                            bigint auto_increment not null,
  date                          datetime(6),
  full_name                     varchar(255),
  email                         varchar(255),
  profile                       varchar(255),
  username                      varchar(255),
  password                      varchar(255),
  role                          varchar(255),
  constraint pk_user primary key (id)
);

create table user_role (
  id                            bigint auto_increment not null,
  date                          datetime(6),
  user_id                       bigint,
  role_id                       bigint,
  constraint pk_user_role primary key (id)
);

create index ix_history_product_id on history (product_id);
alter table history add constraint fk_history_product_id foreign key (product_id) references product (id) on delete restrict on update restrict;

create index ix_history_user_id on history (user_id);
alter table history add constraint fk_history_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_product_category_id on product (category_id);
alter table product add constraint fk_product_category_id foreign key (category_id) references product_category (id) on delete restrict on update restrict;

create index ix_sale_product_id on sale (product_id);
alter table sale add constraint fk_sale_product_id foreign key (product_id) references product (id) on delete restrict on update restrict;

create index ix_sale_user_id on sale (user_id);
alter table sale add constraint fk_sale_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_role_user_id on user_role (user_id);
alter table user_role add constraint fk_user_role_user_id foreign key (user_id) references user (id) on delete restrict on update restrict;

create index ix_user_role_role_id on user_role (role_id);
alter table user_role add constraint fk_user_role_role_id foreign key (role_id) references role (id) on delete restrict on update restrict;


# --- !Downs

alter table history drop foreign key fk_history_product_id;
drop index ix_history_product_id on history;

alter table history drop foreign key fk_history_user_id;
drop index ix_history_user_id on history;

alter table product drop foreign key fk_product_category_id;
drop index ix_product_category_id on product;

alter table sale drop foreign key fk_sale_product_id;
drop index ix_sale_product_id on sale;

alter table sale drop foreign key fk_sale_user_id;
drop index ix_sale_user_id on sale;

alter table user_role drop foreign key fk_user_role_user_id;
drop index ix_user_role_user_id on user_role;

alter table user_role drop foreign key fk_user_role_role_id;
drop index ix_user_role_role_id on user_role;

drop table if exists history;

drop table if exists product;

drop table if exists product_category;

drop table if exists role;

drop table if exists sale;

drop table if exists user;

drop table if exists user_role;

