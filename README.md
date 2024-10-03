<h1>Spring Boot, MySQL, Spring Security, JWT, JPA, Rest API</h1>
<p></p>

<p>Carrito</p>

| Method   | Url        | Descripcion | Sample Valid Request Body|
|:---------|:-----------|:-----------|:---------|
| GET | /carrito/getAll | obtiene todos los carritos |  |
| GET | /carrito/getCarrito/{id} | obtiene carrito por id |  |
| POST | /carrito/{id_carrito}/addProducto/{id_producto} | agrega producto al carrito | JSON |
| POST | /carrito/comprar/{id} | compra el carrito y crea un factura | JSON |
| GET | /carrito/facturas | muestra todas las facturas |  |
| DELETE | /carrito/deleteProducto/{id} | borra un producto por id del carrito |  |

# Producto
| Method   | Url        | Descripcion | Sample Valid Request Body|
|:---------|:-----------|:-----------|:---------|
| GET | /producto/getAll | obtiene todos los producto |  |
| GET | /producto/get/{id} | obtiene un producto |  |
| POST | /producto/add | agrega un producto | JSON |
| POST | /producto/addImg/{id} | agrega una imagen a un producto por id | JSON |
| PUT | /producto/update/{id} | edita un producto | JSON |
| DELETE | /producto/delete/{id} | borra un producto por id |  |
| DELETE | /producto/deleteImg/{id} | borra una imagen del producto por id |  |

# User
| Method   | Url        | Descripcion | Sample Valid Request Body|
|:---------|:-----------|:-----------|:---------|
| GET | /user/getAll | obtiene todos los usuarios |  |
| GET | /user/get/{id} | obtiene usuario por id |  |
| POST | /user/add | agrega un usuario | JSON  |
| PUT | /user/update/{id} | edita un usuario por id | JSON |
| DELETE | /user/delete/{id} | borra un usuario por id |  |

# Auth
| Method   | Url        | Descripcion | Sample Valid Request Body|
|:---------|:-----------|:-----------|:---------|
| POST | /login | log in | JSON |
