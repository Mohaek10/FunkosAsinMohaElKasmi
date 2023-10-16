DROP TABLE IF EXISTS FUNKOS;
CREATE TABLE IF NOT EXISTS FUNKOS(
    id int primary key auto_increment,
    cod char(36) not null default RANDOM_UUID(),
    MyID bigint,
    nombre varchar(255) not null,
    modelo enum('MARVEL', 'DISNEY', 'ANIME', 'OTROS') not null,
    precio real not null,
    fecha_lanzamiento date not null,
    created_at timestamp not null default CURRENT_TIMESTAMP,
    updated_at timestamp not null default CURRENT_TIMESTAMP
);