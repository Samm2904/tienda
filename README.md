# API Tienda

API REST para la gestión de productos de una tienda, desarrollada con Spring Boot. Los datos se guardan en una base MySQL.

## Tecnologías

- Java 25.
- Spring Boot 4.1.2 (Spring Web MVC).
- Spring Data JPA / Hibernate.
- MySQL.
- Lombok.
- Maven (incluye Maven Wrapper).

## Requisitos previos

- JDK 25 o superior.
- MySQL en ejecución en `localhost:3306`.
- Un cliente HTTP para probar los endpoints .

## Estructura del proyecto

```
src/main/java/com/techlab/api_techlab/
├── ApiTechlabApplication.java      # Clase principal
├── controller/
│   └── ProductoController.java     # Endpoints REST (/productos)
├── service/
│   └── ProductoService.java        # Lógica de negocio
├── repository/
│   ├── ProductoRepository.java     # Acceso a datos de productos
│   └── CategoriaRepository.java    # Acceso a datos de categorías
├── model/
│   ├── Producto.java               # Entidad Producto
│   └── Categoria.java              # Entidad Categoria
├── util/
│   └── Validador.java              # Validaciones de nombre, precio y stock
└── exception/
    ├── GlobalExceptionHandler.java # Manejo centralizado de errores
    ├── ProductoNoEncontradoException.java
    ├── ProductoDuplicadoException.java
    └── StockInsuficienteException.java
```

La aplicación sigue una arquitectura en capas: **controller → service → repository → base de datos**.

## Configuración de la base de datos

La conexión se define en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tienda
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
```

Como `ddl-auto` está en `validate`, Hibernate **no crea las tablas**: solo verifica que existan y coincidan con las entidades. Antes de iniciar la aplicación hay que crear la base y las tablas:

```sql
CREATE DATABASE tienda;
USE tienda;

CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255)
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    precio DECIMAL(38,2),
    stock INT NOT NULL,
    categoria_id INT,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- La API no tiene endpoints para categorías: se cargan directamente en la base
INSERT INTO categoria (nombre) VALUES ('Ropa'), ('Vinilos'), ('Accesorios');

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME(6),
    total DECIMAL(38,2)
);

CREATE TABLE linea_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT,
    producto_id INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(38,2),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);
```

Si se quiere se pueden agregar productos de antemano:

```sql
INSERT INTO producto (nombre, precio, stock, categoria_id) VALUES
 ('Vinilo 1', 32000.00, 15, 2),
 ('Vinilo 2', 18500.00, 8, 2,
 ('Remera de algodón', 12000.00, 40, 1),
 ('Campera de abrigo', 58000.00, 12, 1),
 ('Anillo', 8000.00, 15, 3),
 ('Collar', 10000.00, 10, 3),
```

Si el usuario o la contraseña de MySQL son otros, se modifican en `application.properties`.

## Cómo ejecutar

Desde la carpeta del proyecto (`api-techlab`):

```bash
# Windows
cd .\tienda
mvnw.cmd spring-boot:run
```

La API queda disponible en `http://localhost:8080`.
---

# Documentación de Endpoints — API Tienda

## 1. Categorías.
Ruta base: `/categorias`

| Método | Endpoint | Parámetros / Cuerpo | Respuesta | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/categorias` | Ninguno | `200 OK` — `List<Categoria>` | Lista todas las categorías disponibles. |

---

## 2. Pedidos.
Ruta base: `/pedidos`

| Método | Endpoint | Parámetros / Cuerpo | Respuesta | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/pedidos` | **Body (JSON):** `PedidoRequest` | `201 Created` — `PedidoResponse` | Crea un nuevo pedido. |
| `GET` | `/pedidos` | Ninguno | `200 OK` — `List<PedidoResponse>` | Lista todos los pedidos registrados. |
| `GET` | `/pedidos/{id}` | **Path:** `id` (`int`) | `200 OK` — `PedidoResponse` | Obtiene el detalle de un pedido por su ID. |

---

## 3. Productos.
Ruta base: `/productos`

| Método | Endpoint | Parámetros / Cuerpo | Respuesta | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/productos` | Ninguno | `200 OK` — `List<Producto>` | Lista todos los productos. |
| `GET` | `/productos/categoria/{categoria}` | **Path:** `categoria` (`String`) | `200 OK` — `List<Producto>` | Filtra los productos pertenecientes a una categoría. |
| `GET` | `/productos/{id}` | **Path:** `id` (`int`) | `200 OK` — `Producto` | Obtiene un producto específico por su ID. |
| `POST` | `/productos` | **Body (JSON):** `Producto` | `201 Created` — `Producto` | Crea un nuevo producto. |
| `PUT` | `/productos/{id}` | **Path:** `id` (`int`)<br>**Body (JSON):** `Producto` | `200 OK` — `Producto` | Actualiza los datos de un producto existente. |
| `DELETE` | `/productos/{id}` | **Path:** `id` (`int`) | `204 No Content` | Elimina un producto por su ID. |
| `GET` | `/productos?nombre={nombre}` | **Query:** `nombre` (`String`) | `200 OK` — `List<Producto>` | Busca productos cuyo nombre coincida con el parámetro. |
| `GET` | `/productos/valor-inventario` | Ninguno | `200 OK` — `BigDecimal` | Calcula y devuelve el valor total del inventario. |
| `GET` | `/productos/con-categoria` | Ninguno | `200 OK` — `List<ProductoConCategoria>` | Lista los productos junto con la información de su categoría. |

---

## Agradecimientos
Gracias Miguel Nefle por el curso, el acompañamiento, el front y el README!!

## Dev
Sofia Martinez.
