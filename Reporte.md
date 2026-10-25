# Reporte de Implementación - Aplicación de Gestión de Inventario para Pulpería

## Resumen del Proyecto
Se ha desarrollado exitosamente la aplicación Android en Kotlin para la gestión de inventario de una pulpería, cumpliendo con todos los requerimientos y aspectos adicionales solicitados en [Promp.md](file:///C:/Users/WilliamB/AndroidStudioProjects/PM1_T12_0285/Promp.md).

## Arquitectura y Patrón de Diseño
- **Patrón MVC + DAO**: Separación limpia entre las capas de modelo (`model`), acceso a datos (`database` con DAOs y `DatabaseHelper` usando SQLite), adaptadores (`adapter`) y vistas/controladores (`MainActivity`, `ProductoActivity`, `MovimientoActivity`, `ResumenActivity`, etc.).

## Estructura de Base de Datos (SQLite)
1. **Tabla `productos`**:
   - `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
   - `nombre` (TEXT NOT NULL)
   - `precio` (REAL NOT NULL)
   - `stock` (INTEGER NOT NULL)
   - `codigo_barra` (TEXT - Registro manual)
   - `stock_minimo` (INTEGER NOT NULL - Umbral para alertas)

2. **Tabla `movimientos`**:
   - `id` (INTEGER PRIMARY KEY AUTOINCREMENT)
   - `id_producto` (INTEGER NOT NULL, ForeignKey)
   - `cantidad` (INTEGER NOT NULL)
   - `tipo` (TEXT NOT NULL: "ENTRADA" o "SALIDA")
   - `fecha` (TEXT NOT NULL)

## Funcionalidades Implementadas
1. **CRUD de Productos**:
   - Creación, lectura, actualización y eliminación de productos en el inventario.
   - **Validación robusta por campo y unicidad de código de barra**: Comprobación individual de que todos los campos estén llenos y sean válidos. Adicionalmente, verifica en la base de datos si el código de barra ingresado ya existe (excluyendo el producto actual en modo edición), mostrando una advertencia para que se ingrese un nuevo código si está duplicado.
2. **Movimientos de Inventario**:
   - Registro de entradas (incrementan el stock) y salidas (decrementan el stock).
   - Validación transaccional para evitar salidas cuando el stock es insuficiente.
   - Registro automático de fecha y hora.
3. **Resumen y Estadísticas**:
   - Cálculo y visualización del valor total del inventario (`precio * stock`).
   - Top 5 productos con mayor valor en el inventario, mostrando el valor total individual en Lps. con letras de color verde para diferenciarlos.
   - Panel de alertas visuales para productos con stock menor o igual al mínimo predefinido (sin botones de modificar/eliminar).
4. **Aspectos Adicionales**:
   - Alerta visual en listas (color rojo claro) para productos con stock bajo o agotado.
   - Interfaz intuitiva basada en Material Design con actividades y RecyclerViews optimizados.

## Verificación y Pruebas
- Se declararon todas las actividades (`ProductoActivity`, `ProductoFormActivity`, `MovimientoActivity`, `MovimientoFormActivity`, `ResumenActivity`) en el [AndroidManifest.xml](file:///C:/Users/WilliamB/AndroidStudioProjects/PM1_T12_0285/app/src/main/AndroidManifest.xml) para evitar errores de navegación (`ActivityNotFoundException`).
- Se compiló y verificó el proyecto exitosamente mediante Gradle (`app:assembleDebug`), sin errores de compilación.
