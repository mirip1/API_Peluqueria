drop schema peluqueria_web;
create schema peluqueria_web;
use peluqueria_web;

-- Tablas

create table usuarios (
    id int auto_increment primary key,
    nombre varchar(100) not null,
    apellidos varchar(100) not null,
    email varchar(100) not null unique,
	telefono varchar(15) not null unique,
    password varchar(255) not null,
    rol enum('CLIENTE', 'ADMIN') not null,
	fecha_creacion datetime default current_timestamp,
    baneado boolean not null default 0
);

create table citas (
    id int auto_increment primary key,
    usuario_id int not null,
    fecha_y_hora datetime not null,

    estado enum('activa', 'cancelada', 'finalizada') not null default 'activa',
    constraint fk_usuario foreign key (usuario_id) references usuarios (id) on delete cascade
);

create table servicios (
    id int auto_increment primary key,
    nombre varchar(50) not null,
    precio decimal(10 , 2 ) not null
);

create table horarios (
    id int auto_increment primary key,
    dia_semana enum('lunes', 'martes', 'miercoles', 'jueves', 'viernes', 'sabado', 'domingo') not null,
    hora_inicio time not null,
    hora_fin time not null,
    estado enum('disponible', 'no_disponible') not null default 'disponible'
) ;

create table resenas (
    id int auto_increment primary key,
    usuario_id int not null,
    comentario text,
    puntuacion int not null,
    fecha datetime not null default current_timestamp,
    constraint fk_usuario_resenas foreign key (usuario_id) references usuarios (id) on delete cascade
);

create table peluqueria (
    id int auto_increment primary key,
    nombre varchar(100) not null,
    descripcion text,
    ubicacion varchar(255)
);

-- Triggers

DELIMITER //
create trigger trigger_limite_cita before insert on citas
for each row
begin
  if NEW.estado = 'activa' then
    if (select count(*) from citas where usuario_id = NEW.usuario_id and estado = 'activa') > 0 then
      signal sqlstate '45000' set message_text = 'El usuario ya tiene una cita activa';
    end if;
  end if;
end;
//
DELIMITER ;

DELIMITER //
create trigger trigger_banear_usuario after update on usuarios
for each row
begin
  if NEW.baneado = 1 and OLD.baneado = 0 then
    delete from citas where usuario_id = NEW.id and estado = 'activa';
  end if;
end;
//
DELIMITER ;

-- Datos de prueba

insert into peluqueria (nombre, descripcion, ubicacion)
values ('PeluqueriaEjemp', 'Peluqueria de prueba ', 'Calle Ejemplo, 123, Ciudad, Pais');

INSERT INTO usuarios (nombre, apellidos, email, password, rol, fecha_creacion, baneado, telefono)
VALUES ('Admin', 'Test', 'admin@example.com', '$2a$12$67UmkMD6wFegStulo66QQe6HE8VynOJ6UKrgh76ViQekruXoGxpai', 'ADMIN', CURRENT_TIMESTAMP, 0, '123456788');

INSERT INTO servicios (nombre, precio) VALUES ('Corte de Pelo', 15.00);
INSERT INTO servicios (nombre, precio) VALUES ('Tinte', 30.00);
INSERT INTO servicios (nombre, precio) VALUES ('Peinado', 20.00);



