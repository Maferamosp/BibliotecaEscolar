# BibliotecaEscolar

Sistema básico de gestión de biblioteca escolar, desarrollado en Java con interfaz gráfica Swing y base de datos MySQL. Sigue el patrón MVC y usa DAO para el acceso a datos.

## Funcionalidades

- **Autenticación:** ingreso con RUT y contraseña. Según el rol se habilitan distintas funciones.
- **Libros** (bibliotecario): registrar, modificar, eliminar y listar. El estudiante solo consulta.
- **Estudiantes** (bibliotecario): registrar estudiantes y listarlos.
- **Préstamos y devoluciones:** registrar préstamos, controlar el stock, calcular la fecha de vencimiento (7 días) y detectar devoluciones con atraso.
- **Reportes** (bibliotecario): libros más prestados, historial por estudiante y libros actualmente en préstamo.

### Roles

| Rol | Permisos |
|---|---|
| Bibliotecario | Gestión total |
| Estudiante | Préstamo, devolución y consulta de libros |

## Requisitos

- JDK 26 (según `pom.xml`)
- Maven
- MySQL o MariaDB en `localhost:3306`

## Usuarios de prueba

Todos usan la contraseña `clave123`.

| Rol | RUT |
|---|---|
| Bibliotecario | `12345678-9` |
| Estudiante | `98765432-1` |

## Estructura del proyecto

```
src/main/java/
├── main/        Punto de entrada (Main)
├── model/       Clases de dominio (Libro, Estudiante, Prestamo, Usuario, Categoria)
├── dao/         Acceso a datos con JDBC
├── controlador/ Lógica de negocio y conexión a la base de datos
└── app/         Interfaz gráfica Swing (vistas)
database/
└── biblioteca.sql
```

## Notas técnicas

- Los préstamos se ejecutan en un hilo aparte para que la interfaz no se bloquee.
- El descuento de stock está protegido con `synchronized` y una transacción, para evitar errores por accesos simultáneos.
