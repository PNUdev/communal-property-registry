create table if not exists category_by_purpose
(
  id   bigint       not null auto_increment,
  name varchar(255) not null,
  primary key (id)
);

create table if not exists property
(
  id                                             bigint       not null auto_increment,
  address                                        varchar(255) not null,
  lat                                            double       not null,
  lon                                            double       not null,
  name                                           varchar(255), -- nullable
  category_by_purpose_id                         bigint       not null,
  property_status                                varchar(255) not null,
  area                                           double       not null,
  area_transferred                               double,       -- nullable
  balance_holder                                 varchar(255) not null,
  owner                                          varchar(255) not null,
  lease_agreement_end_date                       date,         -- nullable
  amount_of_rent                                 double,       -- nullable
  is_area_transferred_publicly_viewable         bit          not null,
  is_balance_holder_publicly_viewable           bit          not null,
  is_owner_publicly_viewable                    bit          not null,
  is_lease_agreement_end_date_publicly_viewable bit          not null,
  is_amount_of_rent_publicly_viewable           bit          not null,
  primary key (id),
  foreign key (category_by_purpose_id) references category_by_purpose (id)
);

create table if not exists attachment_category
(
  id                    bigint       not null auto_increment,
  name                  varchar(255) not null,
  is_publicly_viewable bit          not null,
  primary key (id)
);

create table if not exists attachment
(
  id                     bigint not null auto_increment,
  note                   varchar(255), -- nullable
  link                   varchar(255), -- nullable
  attachment_category_id bigint not null,
  property_id            bigint not null,
  is_publicly_viewable  bit    not null,
  primary key (id),
  foreign key (attachment_category_id) references attachment_category (id),
  foreign key (property_id) references property (id)
);

create table if not exists user_action
(
  id           bigint       not null auto_increment,
  ip_address   varchar(255) not null,
  http_method  varchar(255) not null,
  url          varchar(255) not null,
  referrer_url varchar(255), -- nullable
  time         datetime     not null,
  primary key (id)
);