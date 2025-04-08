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

    estado enum('ACTIVA', 'CANCELADA', 'FINALIZADA') not null default 'ACTIVA',
    constraint fk_usuario foreign key (usuario_id) references usuarios (id) on delete cascade
);

create table servicios (
    id int auto_increment primary key,
    nombre varchar(50) not null,
    precio decimal(10 , 2 ) not null
);

create table horario_base (
    id int auto_increment primary key,
    dia_semana enum('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO') not null,
    hora_inicio time not null,
    hora_fin time not null,
    estado enum('DISPONIBLE', 'NO_DISPONIBLE') not null default 'DISPONIBLE'
);

create table horario_excepcion (
    id int auto_increment primary key,
    fecha date not null,
    hora_inicio time not null,
    hora_fin time not null,
    estado enum('DISPONIBLE', 'NO_DISPONIBLE') not null,
    dia_semana enum('LUNES', 'MARTES', 'MIERCOLES', 'JUEVES', 'VIERNES', 'SABADO', 'DOMINGO') not null
);

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

DELIMITER //

create trigger trigger_cita_cancelada after update on citas
for each row
begin
    declare dia varchar(10);
    if NEW.estado = 'CANCELADA' and OLD.estado <> 'CANCELADA' then
        set dia = case dayofweek(NEW.fecha_y_hora)
                     when 1 then 'DOMINGO'
                     when 2 then 'LUNES'
                     when 3 then 'MAaRTES'
                     when 4 then 'MIERCOLES'
                     when 5 then 'JUEVES'
                     when 6 then 'VIERNES'
                     when 7 then 'SABADO'
                  end;
update horarios 
set 
    estado = 'DISPONIBLE'
where
    dia_semana = dia
        and hora_inicio = time(NEW.fecha_y_hora);
    end if;
end;
//
DELIMITER ;



-- Datos de prueba

insert into peluqueria (nombre, descripcion, ubicacion)
values ('PeluqueriaEjemp', 'Peluqueria de prueba ', 'Calle Ejemplo, 123, Ciudad, Pais');

insert into usuarios (nombre, apellidos, email, password, rol, fecha_creacion, baneado, telefono)
values ('Admin', 'Test', 'admin@example.com', '$2a$12$67UmkMD6wFegStulo66QQe6HE8VynOJ6UKrgh76ViQekruXoGxpai', 'ADMIN', current_timestamp, 0, '123456788');

insert into servicios (nombre, precio) values ('Corte de Pelo', 15.00);
insert into servicios (nombre, precio) values ('Tinte', 30.00);
insert into servicios (nombre, precio) values ('Peinado', 20.00);

-- Inserta horarios base iniciales

INSERT INTO horario_base (dia_semana, hora_inicio, hora_fin, estado) VALUES 
('LUNES', '08:00:00', '14:00:00', 'DISPONIBLE'),
('LUNES', '16:00:00', '20:00:00', 'DISPONIBLE'),
('MARTES', '08:00:00', '14:00:00', 'DISPONIBLE'),
('MARTES', '16:00:00', '20:00:00', 'DISPONIBLE'),
('MIERCOLES', '08:00:00', '14:00:00', 'DISPONIBLE'),
('MIERCOLES', '16:00:00', '20:00:00', 'DISPONIBLE'),
('JUEVES', '08:00:00', '14:00:00', 'DISPONIBLE'),
('JUEVES', '16:00:00', '20:00:00', 'DISPONIBLE'),
('VIERNES', '08:00:00', '14:00:00', 'DISPONIBLE'),
('VIERNES', '16:00:00', '20:00:00', 'DISPONIBLE'),
('SABADO', '09:00:00', '13:00:00', 'DISPONIBLE'),
('DOMINGO', '10:00:00', '14:00:00', 'DISPONIBLE');




