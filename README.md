<h1>Spring Boot, MySQL, Spring Security, JWT, JPA, Rest API</h1>
<p>Esta aplicación es un sistema de gestión de carritos de compra que permite a los usuarios gestionar productos, realizar compras y administrar sus cuentas.</p>

<h2>Carrito</h2>

| Method   | Url                                   | Descripcion                          | Sample Valid Request Body       |
|:---------|:--------------------------------------|:-------------------------------------|:--------------------------------|
| GET      | /carrito/getAll                      | obtiene todos los carritos          |                                 |
| GET      | /carrito/getCarrito/{id}             | obtiene carrito por id              |                                 |
| POST     | /carrito/{id_carrito}/addProducto/{id_producto} | agrega producto al carrito  |                              |
| POST     | /carrito/comprar/{id}                | compra el carrito y crea un factura |                                 |
| GET      | /carrito/facturas                    | muestra todas las facturas          |                                 |
| DELETE   | /carrito/deleteProducto/{id}         | borra un producto por id del carrito|                                 |

<h2>Producto</h2>

| Method   | Url                                   | Descripcion                          | Sample Valid Request Body       |
|:---------|:--------------------------------------|:-------------------------------------|:--------------------------------|
| GET      | /producto/getAll                      | obtiene todos los productos         |                                 |
| GET      | /producto/get/{id}                   | obtiene un producto                 |                                 |
| POST     | /producto/add                         | agrega un producto                  | [JSON](#agregar-producto)                           |
| POST     | /producto/addImg/{id}                | agrega una imagen a un producto por id | [JSON](#agregar-imagen)                         |
| PUT      | /producto/update/{id}                 | edita un producto                   | [JSON](#editar-producto)                           |
| DELETE   | /producto/delete/{id}                 | borra un producto por id            |                                 |
| DELETE   | /producto/deleteImg/{id}              | borra una imagen del producto por id|                                 |

<h2>Usuario</h2>

| Method   | Url                                   | Descripcion                          | Sample Valid Request Body       |
|:---------|:--------------------------------------|:-------------------------------------|:--------------------------------|
| GET      | /user/getAll                         | obtiene todos los usuarios          |                                 |
| GET      | /user/get/{id}                       | obtiene usuario por id              |                                 |
| POST     | /user/add                            | agrega un usuario                    | [JSON](#agregar-usuario)       |
| PUT      | /user/update/{id}                    | edita un usuario por id             | [JSON](#editar-usuario)        |
| DELETE   | /user/delete/{id}                    | borra un usuario por id              |                                 |

<h2>Autenticación</h2>

| Method   | Url                                   | Descripcion                          | Sample Valid Request Body       |
|:---------|:--------------------------------------|:-------------------------------------|:--------------------------------|
| POST     | /login                                | log in                               | [JSON](#log-in)       |

<h3>Agregar producto -> /producto/add</h3>
<pre><code class="json">
{
    "nombre": "iphone x",
    "descripcion": "descripcion",
    "categoria": "celular",
    "imagenes": [
        "urlfoto 1",
        "urlfoto 2"
    ],
    "stock": 10,
    "precio": 1000000.0
}
</code></pre>
    
<h2>Sample Valid JSON Request Bodys</h2>

<h3 id="agregar-producto">Agregar producto -> /producto/add</h3>
```json
{
    "nombre": "iphone x",
    "descripcion": "descripcion",
    "categoria": "celular",
    "imagenes": [
        "urlfoto 1",
        "urlfoto 2"
    ],
    "stock": 10,
    "precio": 1000000.0
}```
 <h3 id="agregar-imagen">Agregar Imagen -> /producto/addImg/{id}</h3> ```json { "url": "http/url/img" } ``` <h3 id="editar-producto">Editar producto -> /producto/update/{id}</h3> ```json { "nombre": "iphone x", "descripcion": "descripcion", "categoria": "celular", "imagenes": [ "urlfoto 1", "urlfoto 2" ], "stock": 10, "precio": 1000000.0 } ``` <h3 id="agregar-usuario">Agregar usuario -> /user/add</h3> ```json { "username": "user", "password": "password", "email": "user@gmail.com", "telefono": "123456789", "roles": ["ADMIN"] } ``` <h3 id="editar-usuario">Edita usuario -> /user/update/{id}</h3> ```json { "email": "user@gmail.com", "telefono": "123456789", "roles": ["ADMIN"] } ``` <h3 id="log-in">Log in -> /login</h3> ```json { "username": "username", "password": "password" } ```
