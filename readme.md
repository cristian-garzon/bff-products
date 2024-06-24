## Prueba Técnica

el servicio es un bff donde estamos simulando la api externa con
esta api [leer documentación](https://dummyjson.com/docs/product).

una vez iniciada la aplicación, puedes ir a [swagger-ui](http://localhost:8080/swagger-ui/index.html#/),
allí encontrará todos los puntos finales que puede utilizar.

Decidí utilizar la caché en memoria, porque no tenía el tiempo suficiente para hacerlo con redis.
En este caso es útil usar la caché en memoria, porque es solo una aplicación, pero, en el caso de que necesites una aplicación escalable, lo mejor es usar redes.
necesita una aplicación escalable, la mejor manera es el redis, porque, todas las instancias utilizan el mismo redis.
Además, añadiría una bandera de configuración, donde antes de iniciar la aplicación, podemos decidir si
Queremos iniciar la aplicación con la memoria caché, o redis caché.

Por el momento, la autenticación es una autenticación simple, donde solo tenemos un usuario con las siguientes credenciales:
```json
{
  "email": "test@test.com",
  "password": "secret"
}
```

Hice la prueba para el controlador, el repositorio y el servicio.

## preguntas extra:

### ¿Qué medidas tomarías para prevenir ataques de inyección SQL en tu código?

para la autenticación es necesario usar prepared statements, porque si no lo haces, y usas una simple query con params,
Es probable que se unan comandos sql de la contraseña o nombre de usuario.

### Explica la diferencia entre autenticación y autorización sobre el contexto en una aplicación web

La autenticación se trata de que te autentiques en una aplicación, y obtengas un token para acceder a la aplicación.
Y la autorización se refiere a los permisos que se tienen en la aplicación, en este caso hablamos de los roles de los usuarios.

### Describir cómo construir un arquitecto escalable para una aplicación web que espera un aumento significativo del tráfico.
* Primero, es importante empezar con el código, si quieres hacer una aplicación escalable, necesitas escribir código con DDD y arquitectura hexagonal, para facilitar la lectura y escritura de código.
* En segundo lugar, sobre los recursos es importante tener en cuenta las siguientes sugerencias:
  * **Base de datos**: si usted tiene por ejemplo un ecommerce grande, usted necesita tener una base de datos y servicio a las operaciones, y otra base de datos y servicio a la ayuda.
    De esta forma tenemos dos dominios, soporte y operación.
  * **Cache**: cuando hablamos de una gran aplicación, necesitamos usar caché para acceder a la información.
  * **Prueba de estrés**: He aprendido con la experiencia, que es importante hacer pruebas de estrés si la aplicación va a tener un gran tráfico. Porque incluso si hacemos un código limpio y de las pruebas unitarias todo es perfecto,
    no sabemos cómo va a responder la aplicación con un tráfico significativo.

### Háblame de un reto que hayas tenido en tu último trabajo.

En primer lugar, tenemos un servicio donde guardar las tareas a ejecutar, en un redis, y vimos un problema, donde si por cualquier razón, el redis hizo un flush, toda la operación sería
abajo, porque este servicio es de nivel 1 y prioridad 0. Por lo tanto, tuve que crear un trabajador, donde podemos ejecutar una migración de base de datos a redis, el problema
es que necesitábamos que esta migración fuera lo más rápida posible. Utilicé Coroutines, para migrar los datos por lotes, de esta manera, podríamos migrar más de 60000 tareas
en menos de 15 segundos.

Segundo, estábamos haciendo una migración de jenkins a awx para hacer deployments, y tenemos un servicio que crea deployments a jenkins o awx. En ese momento, tuve que crear un código adaptable,
donde pudiéramos cambiar el flujo de despliegue desde rest api, de esta manera, devops podría hacer la migración de jenkins a awx, pais por pais, tier por tier.

### ¿Puedes mencionar y explicar brevemente tres patrones de diseño que hayas utilizado en proyectos anteriores?

* Patrón observador: se utiliza si necesitamos enviar una notificación con los cambios de una entidad.
* Patrón Adaptador: se utiliza cuando se tiene un objeto que proporciona una determinada funcionalidad o conjunto de responsabilidades, pero su interfaz no coincide con lo que un cliente espera o requiere. En estos casos, un Adaptador actúa como puente, permitiendo utilizar el Adaptador como si fuera otra interfaz compatible.
* Inyección de dependencias: este patrón permite inyectar objetos en una clase, con lo que se reduce el acoplamiento de clases y se mejora la capacidad de prueba y la flexibilidad al hacer explícitas las dependencias.

## Caso de Uso

La aplicación de validación de horas trabajadas en la compañía NTTDATA, presenta
una falla recurrente:
La aplicación presenta caídas constantes, el equipo de soporte realiza la revisión
de los tiempos de repuesta de los servicios BFF y observa un tiempo de respuesta
de 16 a 20 segundos. Al revisar los logs evidencian un timeout presentado por los
servicios login y consultar horas, igualmente los servicios llevan corriendo más de
60 días contantes. Dato adicional los servidores contienen un administrador de
servicios Kubernetes y cada servicio maneja tres imágenes desplegadas.

### ¿Qué practica implementaría para solucionar el problema?
* Seria bueno sacar un TH y un HD, de esta maner podemos ver en que se esta consumiendo la cpu el servicio.
* Como lo dije anterior mente, en estos casos es bueno hacer un stress test, de esta manera podemos analizar de mejor manera el inconveniente.
* Para poder solucionar se deberia analizar si se podria hacer un escalamiento horizontal, para que el servicio tenga mas recursos, que esa es una de las razones por las cuales los servicios a veces comienzan a tener latencia.
### ¿Qué Script de Kubernetes utilizaría, basado en el nombre de los servicios?
Para ser sincero, no tengo mucho conocimiento de kubernetes, aparte de los comando para poder desplegar una aplicacion en awx.