CREATE DATABASE IF NOT EXISTS bdcoldigo DEFAULT CHARACTER SET utf8 ;

CREATE TABLE IF NOT EXISTS marcas (
	id int unsigned not null auto_increment,
    nome varchar(45) not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS produtos (
	id INT (5) UNSIGNED zerofill NOT NULL auto_increment,
    categoria TINYINT(1) UNSIGNED NOT NULL,
    modelo varchar(45),
    capacidade INT(4) UNSIGNED NOT NULL,
    valor DECIMAL (7,2) UNSIGNED NOT NULL,
    marcas_id INT UNSIGNED NOT NULL,
    primary key (id),
    INDEX fk_produtos_marcas_idx (marcas_id ASC),
    CONSTRAINT fk_produtos_marcas
    FOREIGN KEY (marcas_id)
    REFERENCES marcas (id)
);

INSERT INTO marcas (nome) VALUE ('Consul');
INSERT INTO marcas (nome) VALUE ('Brastemp');
INSERT INTO marcas (nome) VALUE ('Eletrolux');