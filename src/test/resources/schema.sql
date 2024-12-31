create table twotter.profile
(
    name        varchar(20)  not null
        primary key,
    description varchar(200) null
);

create table twotter.role
(
    role enum ('ADMIN', 'USER') not null
        primary key
);

create table twotter.account
(
    created_at     datetime(6)            not null,
    last_logged_in datetime(6)            null,
    id             binary(16)             not null
        primary key,
    username       varchar(20)            not null,
    password       varchar(100)           not null,
    role           enum ('ADMIN', 'USER') not null,
    constraint UKgex1lmaqpg0ir5g1f5eftyaa1
        unique (username),
    constraint FKfs8rn86mtgrcdmqjggp32nx2p
        foreign key (role) references twotter.role (role)
);

create table twotter.user
(
    account_id   binary(16)  null,
    id           binary(16)  not null
        primary key,
    profile_name varchar(20) null,
    username     varchar(20) not null,
    constraint UK5c856itaihtmi69ni04cmpc4m
        unique (username),
    constraint UKjg5asnwi1mi7gr9xhupk1tqtl
        unique (account_id),
    constraint UKk06c09wjkacs42l5v3mul6x87
        unique (profile_name),
    constraint FKg6e3jb9kwr725fa0st0cuyvxl
        foreign key (account_id) references twotter.account (id),
    constraint FKm2i64wsa3hkuqs6pp5vjud98a
        foreign key (profile_name) references twotter.profile (name)
);

create table twotter.comment
(
    posted_at      datetime(6)  not null,
    author_user_id binary(16)   null,
    id             binary(16)   not null
        primary key,
    content        varchar(300) null,
    constraint FKdy5tc5xht5bdxuc8lkxsbwyor
        foreign key (author_user_id) references twotter.user (id)
);

create table twotter.comment_liked_by_users
(
    comment_jpa_entity_id binary(16) not null,
    liked_by_users_id     binary(16) not null,
    primary key (comment_jpa_entity_id, liked_by_users_id),
    constraint FK52db82k0j5mensbww9vyy0wkp
        foreign key (comment_jpa_entity_id) references twotter.comment (id),
    constraint FKssqqdfn6j0i9ap9m3bfganvq0
        foreign key (liked_by_users_id) references twotter.user (id)
);

create table twotter.twoot
(
    posted_at      datetime(6)  not null,
    author_user_id binary(16)   null,
    id             binary(16)   not null
        primary key,
    content        varchar(300) null,
    constraint FKk7y7wc8yfc151ogf9pgdpsbsm
        foreign key (author_user_id) references twotter.user (id)
);

create table twotter.twoot_liked_by_users
(
    liked_by_users_id   binary(16) not null,
    twoot_jpa_entity_id binary(16) not null,
    primary key (liked_by_users_id, twoot_jpa_entity_id),
    constraint FKb8x24fl7lkdbtwya05o8l43ho
        foreign key (twoot_jpa_entity_id) references twotter.twoot (id),
    constraint FKgnkofn8lx3s38p30i51j91n2m
        foreign key (liked_by_users_id) references twotter.user (id)
);

create table twotter.user_like_comment
(
    liked_at   datetime(6) not null,
    comment_id binary(16)  not null,
    user_id    binary(16)  not null,
    primary key (comment_id, user_id),
    constraint FKgl9ysm9vv4uaks9erxf4j0sq9
        foreign key (user_id) references twotter.user (id),
    constraint FKpl3kg2pj4q89dbp1taytuuc5v
        foreign key (comment_id) references twotter.comment (id)
);

create table twotter.user_like_twoot
(
    liked_at datetime(6) not null,
    twoot_id binary(16)  not null,
    user_id  binary(16)  not null,
    primary key (twoot_id, user_id),
    constraint FK4iivsto8jkqwq2nnwikkuh7sp
        foreign key (user_id) references twotter.user (id),
    constraint FKtq8dqxx3v9y4810lyvk81frhj
        foreign key (twoot_id) references twotter.twoot (id)
);

