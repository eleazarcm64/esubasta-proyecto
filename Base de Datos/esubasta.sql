CREATE DATABASE IF NOT EXISTS esubasta;
USE esubasta;

CREATE TABLE IF NOT EXISTS categorias (
                                          id_categoria INT PRIMARY KEY AUTO_INCREMENT,
                                          nombre VARCHAR(100) NOT NULL,
    descripcion TEXT
    );

CREATE TABLE IF NOT EXISTS usuarios (
                                        id_usuario INT PRIMARY KEY AUTO_INCREMENT,
                                        nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    tipo VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS articulos (
                                         id_articulo INT PRIMARY KEY AUTO_INCREMENT,
                                         id_vendedor INT NOT NULL,
                                         id_categoria INT NOT NULL,
                                         titulo VARCHAR(200) NOT NULL,
    descripcion TEXT,
    precio_inicial DECIMAL(10,2) NOT NULL,
    precio_actual DECIMAL(10,2) NOT NULL,
    precio_reserva DECIMAL(10,2),
    fecha_fin DATETIME NOT NULL,
    estado VARCHAR(20) DEFAULT 'activa',
    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
    );

CREATE TABLE IF NOT EXISTS imagenes_articulo (
                                                 id_imagen INT PRIMARY KEY AUTO_INCREMENT,
                                                 id_articulo INT NOT NULL,
                                                 url_imagen VARCHAR(255),
    FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo)
    );

CREATE TABLE IF NOT EXISTS pujas (
                                     id_puja INT PRIMARY KEY AUTO_INCREMENT,
                                     id_articulo INT NOT NULL,
                                     id_comprador INT NOT NULL,
                                     monto DECIMAL(10,2) NOT NULL,
    es_ganadora BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo),
    FOREIGN KEY (id_comprador) REFERENCES usuarios(id_usuario)
    );

CREATE TABLE IF NOT EXISTS favoritos (
                                         id_favorito INT PRIMARY KEY AUTO_INCREMENT,
                                         id_usuario INT NOT NULL,
                                         id_articulo INT NOT NULL,
                                         FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo)
    );

CREATE TABLE IF NOT EXISTS notificaciones (
                                              id_notificacion INT PRIMARY KEY AUTO_INCREMENT,
                                              id_usuario INT NOT NULL,
                                              mensaje TEXT NOT NULL,
                                              leida BOOLEAN DEFAULT FALSE,
                                              FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
    );

CREATE TABLE IF NOT EXISTS ventas (
                                      id_venta INT PRIMARY KEY AUTO_INCREMENT,
                                      id_articulo INT NOT NULL,
                                      id_ganador INT NOT NULL,
                                      monto_final DECIMAL(10,2) NOT NULL,
    estado_pago VARCHAR(20) DEFAULT 'pendiente',
    FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo),
    FOREIGN KEY (id_ganador) REFERENCES usuarios(id_usuario)
    );

CREATE TABLE IF NOT EXISTS pagos (
                                     id_pago INT PRIMARY KEY AUTO_INCREMENT,
                                     id_venta INT NOT NULL,
                                     metodo VARCHAR(50) NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    estado VARCHAR(20) DEFAULT 'pendiente',
    FOREIGN KEY (id_venta) REFERENCES ventas(id_venta)
    );

CREATE TABLE IF NOT EXISTS preguntas (
                                         id_pregunta INT PRIMARY KEY AUTO_INCREMENT,
                                         id_articulo INT NOT NULL,
                                         id_usuario INT NOT NULL,
                                         pregunta TEXT NOT NULL,
                                         respuesta TEXT,
                                         FOREIGN KEY (id_articulo) REFERENCES articulos(id_articulo),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
    );

CREATE TABLE IF NOT EXISTS valoraciones (
                                            id_valoracion INT PRIMARY KEY AUTO_INCREMENT,
                                            id_venta INT NOT NULL,
                                            id_usuario_califica INT NOT NULL,
                                            id_usuario_calificado INT NOT NULL,
                                            puntaje INT NOT NULL,
                                            comentario TEXT,
                                            FOREIGN KEY (id_venta) REFERENCES ventas(id_venta),
    FOREIGN KEY (id_usuario_califica) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_usuario_calificado) REFERENCES usuarios(id_usuario)
    );

-- DATOS
INSERT INTO categorias VALUES (1,'Electrónica','Celulares, laptops, camaras'),(2,'Hogar','Muebles, sofa, cocina'),(3,'Antigüedades','Relojes, objetos vintage'),(4,'Ropa','Zapatillas, casacas, polos'),(5,'Vehículos','Motos, autos, bicicletas'),(6,'Deportes','Bicicletas, pesas'),(7,'Coleccionables','Monedas, comics'),(8,'Juguetes','Legos, consolas retro');
INSERT INTO usuarios VALUES (1,'Juan Perez','juan@test.com','1234','vendedor'),(2,'Ana Lopez','ana@test.com','1234','comprador'),(3,'Carlos Quispe','carlos@test.com','1234','ambos'),(4,'Maria Torres','maria@test.com','1234','vendedor'),(5,'Diego Ramos','diego@test.com','1234','comprador'),(6,'Lucia Fernandez','lucia@test.com','1234','comprador'),(7,'Pedro Huaman','pedro@test.com','1234','vendedor'),(8,'Sofia Castillo','sofia@test.com','1234','comprador'),(9,'Andres Vega','andres@test.com','1234','ambos'),(10,'Valeria Diaz','valeria@test.com','1234','comprador');
INSERT INTO articulos VALUES (1,1,1,'iPhone 11 usado','Buen estado, bateria 85%, con caja',500,750,600,DATE_ADD(NOW(), INTERVAL 2 DAY),'activa'),(2,4,1,'Laptop HP Pavilion 15','Core i5, 8GB RAM, SSD 256GB',1200,1450,1300,DATE_ADD(NOW(), INTERVAL 5 DAY),'activa'),(3,7,3,'Reloj antiguo Seiko 1970','Reloj vintage coleccionable, funciona',800,1200,1000,DATE_ADD(NOW(), INTERVAL 1 DAY),'activa'),(4,1,4,'Zapatillas Nike Air Jordan 1','Talla 42, casi nuevas',300,420,350,DATE_ADD(NOW(), INTERVAL 3 DAY),'activa'),(5,4,2,'Sofa 3 cuerpos','Sofa gris, poco uso',400,400,NULL,DATE_ADD(NOW(), INTERVAL 4 DAY),'activa'),(6,7,6,'Bicicleta montañera Trek','Aro 29, 21 cambios',900,1350,1100,DATE_SUB(NOW(), INTERVAL 1 DAY),'vendida'),(7,9,7,'Moneda de 1 sol 1890','Moneda de plata',150,480,200,DATE_SUB(NOW(), INTERVAL 2 DAY),'vendida'),(8,1,8,'PlayStation 2 + 10 juegos','Consola retro con 2 mandos',250,310,NULL,DATE_ADD(NOW(), INTERVAL 6 DAY),'activa'),(9,4,1,'Camara Canon EOS M50','Camara semiprofesional',1500,1500,1400,DATE_ADD(NOW(), INTERVAL 7 DAY),'activa'),(10,9,5,'Moto Honda CB125 2019','Moto en buen estado',2500,2900,2700,DATE_ADD(NOW(), INTERVAL 3 DAY),'activa');
INSERT INTO imagenes_articulo VALUES (1,1,'https://ejemplo.com/iphone1.jpg'),(2,1,'https://ejemplo.com/iphone2.jpg'),(3,2,'https://ejemplo.com/laptop1.jpg'),(4,3,'https://ejemplo.com/reloj1.jpg'),(5,4,'https://ejemplo.com/nike1.jpg'),(6,6,'https://ejemplo.com/bici1.jpg'),(7,7,'https://ejemplo.com/moneda1.jpg'),(8,8,'https://ejemplo.com/ps2.jpg'),(9,10,'https://ejemplo.com/moto1.jpg');
INSERT INTO pujas VALUES (1,1,2,550,FALSE),(2,1,5,650,FALSE),(3,1,2,750,FALSE),(4,2,6,1300,FALSE),(5,2,10,1450,FALSE),(6,3,3,900,FALSE),(7,3,8,1200,FALSE),(8,4,6,350,FALSE),(9,4,5,420,FALSE),(10,6,2,1000,FALSE),(11,6,8,1350,TRUE),(12,7,5,250,FALSE),(13,7,2,480,TRUE),(14,10,3,2700,FALSE),(15,10,6,2900,FALSE);
INSERT INTO favoritos VALUES (1,2,1),(2,2,3),(3,2,6),(4,5,2),(5,5,4),(6,6,1),(7,6,10),(8,8,3),(9,10,4),(10,3,10);
INSERT INTO notificaciones VALUES (1,5,'Has sido superado en la puja del iPhone 11',TRUE),(2,2,'Vas ganando la subasta del iPhone 11 con S/750!',FALSE),(3,3,'Has sido superado en la Moto Honda CB125',FALSE),(4,8,'Ganaste la subasta de la Bicicleta Trek!',FALSE),(5,2,'Ganaste la subasta de la Moneda de 1 sol 1890!',TRUE);
INSERT INTO ventas VALUES (1,6,8,1350,'pagado'),(2,7,2,480,'pendiente');
INSERT INTO pagos VALUES (1,1,'yape',1350,'completado'),(2,2,'transferencia',480,'pendiente');
INSERT INTO preguntas VALUES (1,1,2,'¿La bateria dura todo el dia?','Si, dura unas 8 horas'),(2,1,5,'¿Haces envio a Arequipa?','Si, por Olva Courier'),(3,2,6,'¿Tiene garantia?','No, es de segunda'),(4,3,8,'¿Es original de 1970?','Si, tengo su certificado'),(5,10,3,'¿Aceptas intercambio?',NULL);
INSERT INTO valoraciones VALUES (1,1,8,7,5,'Excelente vendedor, la bici llego como en la foto'),(2,2,2,9,4,'Buena moneda, tardo un poco el envio');