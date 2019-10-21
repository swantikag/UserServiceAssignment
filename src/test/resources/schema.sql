CREATE TABLE IF NOT EXISTS MASTER_USER(mob_number varchar(11) PRIMARY KEY,
email varchar(30) not null,
name varchar(30),
age int,
isVerified Boolean,
constraint fk_mobNumber FOREIGN KEY (mob_number) references MASTER_USER(mob_number));

CREATE TABLE IF NOT EXISTS LOGIN_USER(mob_number varchar(11),
uname varchar(20),
password varchar(20)
);

--alter table LOGIN_USER add constraint fk_mobNumber FOREIGN KEY IF NOT EXISTS (mob_number) references MASTER_USER(mob_number);