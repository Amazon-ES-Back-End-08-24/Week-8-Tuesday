# Week 8

# Tabla de Contenidos

1. [Introducción a las APIs REST](#introducción-a-las-apis-rest)
    - [Características clave de REST](#características-clave-de-rest)
    - [Componentes de un Servicio Web RESTful](#componentes-de-un-servicio-web-restful)
        - [Códigos de Estado HTTP](#códigos-de-estado-http)
2. [Método GET](#método-get)
    - [Path Variables](#path-variables)
    - [Query Parameters](#query-parameters)
    - [Resumen de los Tipos de Parámetros](#resumen-de-los-tipos-de-parámetros)
3. [Ejercicio práctica](#ejercicio-practicando-el-método-get-con-path-variable-y-request-param-en-spring-boot)
    - [Contexto](#contexto)
    - [Requerimientos](#requerimientos)
    - [Especificaciones](#especificaciones)
    - [Pistas](#pistas)

## Introducción a las APIs REST

Una **API (Application Programming Interface)** es una interfaz que permite que dos sistemas diferentes se comuniquen
entre sí. Las APIs son fundamentales en la arquitectura de software moderna para permitir que aplicaciones y servicios
intercambien datos y se integren.

Una de las arquitecturas más utilizadas para diseñar APIs es **REST (Representational State Transfer)**, la cual
facilita la comunicación utilizando el protocolo HTTP. Con REST, podemos construir APIs escalables, ligeras y fáciles de
usar.

### Características clave de REST

- **Cliente-servidor**: La arquitectura REST sigue un modelo de cliente-servidor, donde el cliente realiza solicitudes y
  el servidor responde con los datos solicitados.
- **Stateless (sin estado)**: Cada solicitud del cliente al servidor debe contener toda la información necesaria para
  que el servidor la procese. El servidor no retiene el estado entre las solicitudes.
- **Caché**: Las respuestas de las APIs pueden ser almacenadas en caché para mejorar el rendimiento y reducir la carga
  en el servidor.
- **Capas**: Los sistemas REST pueden estar compuestos de varias capas intermedias que no afectan el comportamiento de
  la API.

### Componentes de un Servicio Web RESTful

1. **Endpoint/URL**: Es la dirección a la cual el cliente envía las solicitudes. Los recursos son accesibles a través de
   estos endpoints.
2. **Métodos HTTP**: Determinan qué operación se realizará sobre los recursos. Los métodos más comunes son:
    - `GET`: Para obtener información.
    - `POST`: Para crear un nuevo recurso.
    - `PUT`: Para actualizar un recurso existente.
    - `DELETE`: Para eliminar un recurso.

3. **Headers HTTP**: Son campos de metadatos adicionales que se pueden enviar en la solicitud, como información de
   autenticación (por ejemplo, tokens JWT), tipo de contenido, etc.

4. **Cuerpo del mensaje (Body)**: Es donde se envían los datos en las solicitudes POST o PUT, y también donde se recibe
   la respuesta en formato JSON.

5. **Respuesta**: La respuesta de una API RESTful suele estar en formato JSON y viene acompañada de un código de estado
   HTTP.

[ejemplo api](https://pokeapi.co/api/v2/pokemon/ditto)

#### Códigos de Estado HTTP

Al implementar una API, es importante devolver códigos de estado HTTP adecuados para indicar si una operación fue
exitosa o no:

- **1xx**: Respuestas informativas
- **2xx**: Respuestas exitosas
- **3xx**: Redirecciones
- **4xx**: Errores del cliente
- **5xx**: Errores del servidor

[api gatos código de estado](https://http.cat/)

Códigos más comunes:

- **200 OK**: La solicitud fue exitosa.
- **201 Created**: Un nuevo recurso fue creado exitosamente.
- **404 Not Found**: El recurso solicitado no fue encontrado.
- **400 Bad Request**: La solicitud no es válida.
- **500 Internal Server Error**: Ocurrió un error en el servidor.

## Método GET

El método **GET** en una API REST se utiliza para **obtener** recursos del servidor. Es un método **idempotente**, lo
que significa que una misma solicitud GET puede repetirse varias veces sin que haya cambios en el estado del servidor.

En una API REST, podemos obtener datos de varias maneras, dependiendo de cómo estructuramos los parámetros de la
solicitud. Los tres tipos principales de parámetros que se utilizan en las solicitudes GET son:

- **Path Variables**
- **Query Parameters**
- **Request Parameters**

### Path Variables

Los **Path Variables** son parámetros que forman parte de la ruta de la URL. Se utilizan cuando queremos identificar un
recurso de forma específica, como una reserva por su ID.

```java

@GetMapping("/bookings/{id}")
public ResponseEntity<TableBooking> getBookingById(@PathVariable Long id) {
    return tableBookingRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
}
```

En este ejemplo, el parámetro `id` forma parte de la ruta. La solicitud sería algo como:

```
GET /api/bookings/1
```

En este caso, se obtiene la reserva con el `id` 1.

- `ResponseEntity<TableBooking>` permite devolver una respuesta HTTP completa con un objeto TableBooking como cuerpo,
  junto con un código de estado HTTP adecuado (como 200 OK o 404 Not Found).
  Proporciona más flexibilidad que devolver solo el objeto, permitiendo personalizar el código de estado, las cabeceras,
  y controlar la respuesta completa de la API REST.

### Query Parameters

Los **Query Parameters** se utilizan para filtrar o buscar recursos según ciertos criterios. Se añaden al final de la
URL en formato de clave-valor separados por un símbolo de interrogación (`?`).

```java

@GetMapping("/bookings/search")
public List<TableBooking> searchBookings(
        @RequestParam(required = false) String customerName,
        @RequestParam(required = false) String reservationDate) {

    if (customerName != null && reservationDate != null) {
        return tableBookingRepository.findByCustomerNameAndReservationDate(customerName, reservationDate);
    } else if (customerName != null) {
        return tableBookingRepository.findByCustomerName(customerName);
    } else if (reservationDate != null) {
        return tableBookingRepository.findByReservationDate(reservationDate);
    } else {
        return tableBookingRepository.findAll();
    }
}
```

En este ejemplo, podemos buscar reservas utilizando el nombre del cliente y/o la fecha de la reserva. La solicitud sería
algo como:

```
GET /api/bookings/search?customerName=John&reservationDate=2024-10-08
```

Si uno de los parámetros no se proporciona, solo se filtrará por el parámetro que esté presente.

```java

@GetMapping("/bookings/filter")
public List<TableBooking> filterBookings(
        @RequestParam String sortBy,
        @RequestParam(required = false, defaultValue = "asc") String order) {

    if ("desc".equalsIgnoreCase(order)) {
        return tableBookingRepository.findAll(Sort.by(Sort.Direction.DESC, sortBy));
    } else {
        return tableBookingRepository.findAll(Sort.by(Sort.Direction.ASC, sortBy));
    }
}
```

En este caso, podemos filtrar los resultados basándonos en un campo de la entidad (por ejemplo, ordenar por fecha o por
nombre). Además, el parámetro `order` tiene un valor por defecto ("asc") que indica si el orden es ascendente o
descendente.

La solicitud sería algo como:

```
GET /api/bookings/filter?sortBy=reservationDate&order=desc
```

Este ejemplo ordenará los resultados por la fecha de reserva en orden descendente.

### Resumen de los Tipos de Parámetros

1. **Path Variables**: Se utilizan cuando el valor de un parámetro es parte de la ruta de la solicitud. Ejemplo:
   `GET /bookings/{id}`.
2. **Query Parameters**: Se utilizan para enviar parámetros opcionales o para realizar búsquedas. Ejemplo:
   `GET /bookings/search?customerName=John`.
3. **Request Parameters**: Se utilizan para obtener parámetros directamente en el método del controlador, y pueden tener
   valores por defecto. Ejemplo: `GET /bookings/filter?sortBy=reservationDate`.

## Ejercicio: Practicando el método GET con Path Variable y Request Param en Spring Boot

### Contexto:

Vas a crear una API REST para gestionar un sistema de **cursos**. El sistema permitirá a los usuarios buscar cursos por
su ID y también realizar búsquedas avanzadas utilizando filtros opcionales como el **nombre del curso** y el **nombre
del instructor**. Para este ejercicio, debes implementar dos métodos GET que utilizarán **Path Variables** y **Request
Parameters**.

### Requerimientos:

1. **Obtener un curso por su ID (Path Variable)**:
    - Implementa un endpoint que permita buscar un curso específico basado en su ID.
    - La URL para este endpoint debe ser algo como: `/api/courses/{id}`, donde `{id}` es el identificador único del
      curso.
    - Si el curso con el ID proporcionado no existe, debes devolver un código de estado 404.

2. **Buscar cursos utilizando filtros opcionales (Request Parameters)**:
    - Implementa otro endpoint que permita buscar cursos utilizando filtros opcionales como el **nombre del curso** y el
      **nombre del instructor**.
    - La URL para este endpoint debe ser: `/api/courses/search`, y los parámetros deben ser pasados como **request
      parameters**:
        - `courseName` (opcional)
        - `instructorName` (opcional)
    - Si no se pasan parámetros, debe devolver todos los cursos disponibles.
    - Si se pasa un parámetro, debe filtrar en base a ese valor.

### Especificaciones:

- Debes implementar los métodos GET para los dos casos mencionados.
- No olvides manejar correctamente las respuestas HTTP, como devolver el código 404 cuando no se encuentre el curso.
- Usa la entidad `Course` que tiene los siguientes campos:
    - `id` (Long)
    - `courseName` (String)
    - `instructorName` (String)
    - `yearPublished` (int)

### Pistas:

- Utiliza `@PathVariable` para obtener el ID del curso en el primer endpoint.
- Usa `@RequestParam` para los parámetros opcionales de búsqueda en el segundo endpoint.
- Recuerda que puedes hacer que los parámetros en `@RequestParam` sean opcionales utilizando `required = false`.


