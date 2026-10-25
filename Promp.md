APLICACION GESTION DE INVENTARIO PARA PULPERIA

Objetivo: Desarrollar una aplicación Android en Kotlin para gestionar el inventario de una pulpería. 
La aplicación permitirá registrar productos con su precio y stock disponible, así como los movimientos de entrada y salida de inventario.

Requisitos:
- Se aplicara el patron MVC + DAO para la persistencia de datos usando SQlite.

Tablas
- Productos: Almacena la informacion de los productos.(id, nombre, precio, stock).
- Movimientos: Almacena la informacion de los movimientos de entrada y salida de inventario.(id, id_producto, cantidad, tipo, fecha).

Pantallas de la aplicacion
- CRUD de productos: Crear, leer, actualizar y eliminar productos del inventario.
- Movimientos: Registrar entradas y salidas de productos, actualizando el stock correspondiente.
- Resumen: Mostrar valor total del inventario (price * stock) y top 5 productos por valor.

Aspectos Adicional
- Registrar códigos de barra de manera manual para los productos.
- Alertar al usuario cuando un producto alcance un stock mínimo predefinido.
- La aplicación permitirá registrar productos con su precio y stock disponible, así como los movimientos de entrada y salida de inventario.
- La aplicación permitirá registrar códigos de barra manualmente y mostrará alertas cuando el stock de un producto sea mínimo.

Verifica que la aplicacion funcione correctamente, y crea un Reporte.md con los cambios realizados.