DROP TABLE IF EXISTS users;

create table users
(
    id           varchar(100) not null,
    name         varchar(20) not null,
    password     varchar(10) not null,
    level        tinyint not null,
    login        int not null,
    recommend    int not null,

    PRIMARY KEY (id)
)