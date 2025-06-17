# Ecommerce

Aplicación backend que permite gestionar productos, carritos, pedidos y compras, con integración de pagos mediante la API de Mercado Pago. Además, cuenta con un módulo de administración para gestionar el inventario de productos.


<h1>Endpoints de la API</h1>
<h2>Carrito</h2>

| Method   | Url                                   | Descripcion                          | Sample Valid Request Body       |
|:---------|:--------------------------------------|:-------------------------------------|:--------------------------------|
| GET      | /carrito/getCarrito/{id}             | obtiene carrito por id              |                                 |
| POST     | /carrito/{id_carrito}/addProducto/{id_producto} | agrega producto al carrito  |                              |
| POST     | /carrito/comprar/{id}                | compra el carrito y crea un pedido |                                 |
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

<h2>Pedido</h2>

| Method   | Url                                   | Descripcion                          | Sample Valid Request Body       |
|:---------|:--------------------------------------|:-------------------------------------|:--------------------------------|
| GET      | /pedido/{id}                         | obtiene obtiene un pedido          |                                 |
| POST     | /pedido/checkPago/{id_carrito}       | verifica que el pago haya sido aprobado y guarda el pedido|                    |

<h2>Autenticación</h2>

| Method   | Url                                   | Descripcion                          | Sample Valid Request Body       |
|:---------|:--------------------------------------|:-------------------------------------|:--------------------------------|
| POST     | /login                                | log in                               | [JSON](#log-in)       |

    
<h2>Ejemplos JSON</h2>

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
<br>
<h3>Agregar Imagen -> /producto/addImg/{id}</h3>
<pre><code class="json">
{
    "url": "http/url/img"
}
</code></pre>
<br>
<h3>Editar producto -> /producto/update/{id}</h3>
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
<br>
<h3>Agregar usuario -> /user/add</h3>
<pre><code class="json">
{
    "username": "user",
    "password": "password",
    "email": "user@gmail.com",
    "telefono": "123456789",
    "roles": ["ADMIN"]
}
</code></pre>
<br>
<h3>Edita usuario -> /user/update/{id}</h3>
<pre><code class="json">
{
    "email": "user@gmail.com",
    "telefono": "123456789",
    "roles": ["ADMIN"]
}
</code></pre>
<br>
<h3>Log in -> /login</h3>
<pre><code class="json">
{
    "username": "username",
    "password": "password"
}
</code></pre>
