create table access
(
    user_id     int      not null,
    business_id int      null,
    time        datetime null
)
    row_format = DYNAMIC;

create table application
(
    id           int auto_increment
        primary key,
    applicant_id int          not null,
    recipient_id int          not null,
    reason       varchar(255) null,
    time         datetime     null
)
    row_format = DYNAMIC;

create table business
(
    business_id  int auto_increment
        primary key,
    name         varchar(255)                        not null,
    address      text                                not null,
    city         varchar(255)                        not null,
    state        varchar(255)                        not null,
    postal_code  varchar(20)                         null,
    latitude     double                              null,
    longitude    double                              null,
    stars        double                              not null,
    ai_stars     double                              null,
    review_count int                                 not null,
    is_open      int       default 1                 not null,
    categories   text                                null,
    attributes   json                                null,
    hours        json                                null,
    created_at   timestamp default CURRENT_TIMESTAMP not null,
    updated_at   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP,
    bid          varchar(255)                        null
)
    row_format = DYNAMIC;

create index idx_bid
    on business (bid);

create index idx_business_id
    on business (business_id);

create table collection
(
    user_id     int      not null,
    business_id int      null,
    date        datetime null
)
    row_format = DYNAMIC;

create table feedbacks
(
    id          int auto_increment
        primary key,
    image_id    varchar(50)                         not null,
    detection   text                                null,
    mode_type   text                                null,
    results     text                                null,
    description text                                null,
    suggestion  text                                null,
    created_at  timestamp default CURRENT_TIMESTAMP null,
    constraint image_id
        unique (image_id)
);

create table hot_search
(
    content longtext not null,
    time    datetime null on update CURRENT_TIMESTAMP
);

create table menu
(
    id          bigint auto_increment
        primary key,
    menu_name   varchar(64)  default 'NULL' not null comment '菜单名',
    path        varchar(200)                null comment '路由地址',
    component   varchar(255)                null comment '组件路径',
    visible     char         default '0'    null comment '菜单状态（0显示 1隐藏）',
    status      char         default '0'    null comment '菜单状态（0正常 1停用）',
    perms       varchar(100)                null comment '权限标识',
    icon        varchar(100) default '#'    null comment '菜单图标',
    create_by   bigint                      null,
    create_time datetime                    null,
    update_by   bigint                      null,
    update_time datetime                    null,
    del_flag    int          default 0      null comment '是否删除（0未删除 1已删除）',
    remark      varchar(500)                null comment '备注'
)
    comment '菜单表' row_format = DYNAMIC;

create table merchant_dishes
(
    id          int auto_increment comment '菜品主键（自增）'
        primary key,
    business_id int                                      not null comment '关联商户ID（外键）',
    dish_name   varchar(100)                             not null comment '菜品名称（如“麻辣小龙虾”）',
    price       decimal(10, 2) default 0.00              not null comment '菜品价格（精准存储金额）',
    photo_id    varchar(64)    default ''                not null comment '菜品图片ID（关联图片存储表/直接存图片标识）',
    is_on_sale  tinyint(1)     default 1                 not null comment '是否在售（1=是，0=否，非核心但实用）',
    create_time datetime       default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime       default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '商户菜品表（极简版）';

create index idx_merchant_id
    on merchant_dishes (business_id)
    comment '提速查询某商户的所有菜品';

create table message
(
    send_id    int          not null,
    receive_id int          not null,
    time       datetime     not null,
    context    varchar(255) not null
)
    row_format = DYNAMIC;

create table news
(
    id      int auto_increment
        primary key,
    time    datetime null,
    content text     null,
    user_id int      null
)
    row_format = DYNAMIC;

create table photos
(
    photo_id    varchar(50)  not null
        primary key,
    business_id varchar(50)  not null,
    caption     text         null,
    label       varchar(100) null
);

create index idx_pid
    on photos (photo_id);

create table reviews
(
    review_id   int auto_increment
        primary key,
    user_id     varchar(255)  not null,
    business_id varchar(255)  not null,
    stars       double        not null,
    ai_stars    double        null,
    useful      int default 0 null,
    funny       int default 0 null,
    cool        int default 0 null,
    text        text          not null,
    date        datetime      not null,
    rid         varchar(255)  null
)
    row_format = DYNAMIC;

create index idx_ai_stars
    on reviews (ai_stars);

create index idx_bid
    on reviews (business_id);

create index idx_r_id
    on reviews (rid);

create index idx_reviews_bid
    on reviews (business_id);

create index idx_reviews_date
    on reviews (date);

create index idx_reviews_review_id
    on reviews (review_id);

create index idx_reviews_rid
    on reviews (rid);

create index idx_reviews_uid
    on reviews (user_id);

create index idx_rid
    on reviews (review_id);

create index idx_uid
    on reviews (user_id);

create table role
(
    id          bigint auto_increment
        primary key,
    name        varchar(128)     null,
    role_key    varchar(100)     null comment '角色权限字符串',
    status      char default '0' null comment '角色状态（0正常 1停用）',
    del_flag    int  default 0   null comment 'del_flag',
    create_by   bigint           null,
    create_time datetime         null,
    update_by   bigint           null,
    update_time datetime         null,
    remark      varchar(500)     null comment '备注'
)
    comment '角色表' row_format = DYNAMIC;

create table role_menu
(
    role_id bigint auto_increment comment '角色ID',
    menu_id bigint default 0 not null comment '菜单id',
    primary key (role_id, menu_id)
)
    row_format = DYNAMIC;

create table tips
(
    tip_id           int auto_increment
        primary key,
    user_id          varchar(50)   not null,
    business_id      varchar(50)   not null,
    text             text          null,
    date             date          null,
    compliment_count int default 0 null,
    constraint unique_tip
        unique (user_id, business_id, text(200))
);

create table user
(
    uid           varchar(255) null,
    user_id       int auto_increment
        primary key,
    name          varchar(255) null,
    password      varchar(255) null,
    email         varchar(255) null,
    gender        varchar(255) null,
    age           int          null,
    city          varchar(255) null,
    review_count  int          null,
    cool          int          null,
    useful        int          null,
    elite         text         null,
    yelping_since datetime     null,
    funny         int          null,
    friends       longtext     null
)
    row_format = DYNAMIC;

create index idx_email
    on user (email);

create index idx_name
    on user (name);

create index idx_psd
    on user (password);

create index idx_uid
    on user (uid);

create index idx_user_id
    on user (user_id);

create index idx_user_yelping_since
    on user (yelping_since);

create table user_business
(
    user_id     int not null,
    business_id int null
)
    row_format = DYNAMIC;

create table user_group
(
    id                 bigint auto_increment comment 'id'
        primary key,
    user_group_name    varchar(200)  null comment '分群名称',
    condition_json_str varchar(2000) null comment '分群条件(json)',
    condition_comment  varchar(2000) null comment '分群条件(描述)',
    user_group_num     bigint        null comment '分群人数',
    update_type        varchar(20)   null comment '更新类型(手动,自动按天)',
    user_group_comment varchar(2000) null comment '分群说明',
    update_time        datetime      null comment '更新时间',
    create_time        datetime      null comment '创建时间'
)
    charset = utf8mb3;

create table user_role
(
    user_id bigint           not null comment '用户id',
    role_id bigint default 0 not null comment '角色id',
    status  int              null
)
    row_format = DYNAMIC;

create index idx_role_id
    on user_role (role_id);

create index idx_status
    on user_role (status);

create index idx_user_id
    on user_role (user_id);


