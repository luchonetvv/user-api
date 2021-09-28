# UserApi
## _Este proyecto contiene un api de creacion de usuarios escrito en Java usando el framework de Spring Boot._

UserApi es un serivicio que expone un API para la creacion de usuarios y consultas de usuario.

## Caracteristicas

- Signin para poder crear inicio de sesion.
- Consultar todos los usuarios.
- Crear usuarios.

UserApi es un servicio de creacion de usuarios escrito en Java ligera basada en las convenciones de formato que los desarrolladores utilizan de forma natural en la creación de software..

## Tecnologias

UserApi usa varios proyectos open source para trabajar apropiadamente:

- [JJWT](https://github.com/jwtk/jjwt) - JJWT tiene como objetivo ser la biblioteca más fácil de usar y comprender para crear y verificar JSON Web Tokens (JWT) en JVM y Android.
- [Spring Boot](https://spring.io/projects/spring-boot) - Spring Boot facilita la creación de aplicaciones independientes basadas en Spring de grado de producción que puede "simplemente ejecutar".
- [Spring Validation](https://docs.spring.io/spring-framework/docs/4.1.x/spring-framework-reference/html/validation.html#validation-beanvalidation) - Spring Validation facilita la validacion de entradad de datos.
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa) - Spring Data JPA, parte de la familia Spring Data más grande, facilita la implementación de repositorios basados ​​en JPA que en su defecto usa la implementacion de Hibernate.
- [Spring Boot Starter Web](https://spring.io/projects/spring-boot) - Es todo lo necesario para poder arrancar un proyecto web api en minutos.
- [H2 Database](https://www.h2database.com/html/main.html) - H2 es un sistema administrador de bases de datos relacionales programado en Java. Puede ser incorporado en aplicaciones Java o ejecutarse de modo cliente-servidor.

Y, por supuesto, la propia UserApi es de código abierto con un [repositorio público](https://github.com/luchonetvv/user-api) en GitHub.
 
## Requerimientos

- [Git](https://git-scm.com/) - Git es un sistema de control de versiones distribuido de código abierto y gratuito diseñado para manejar todo, desde proyectos pequeños a muy grandes, con velocidad y eficiencia.
- [Java](https://www.java.com/en/) - Java es un lenguaje de programación y una plataforma informática lanzada por primera vez por Sun Microsystems en 1995.
- [Visual Studio Code](https://code.visualstudio.com/) - Visual Studio Code es un editor de código fuente desarrollado por Microsoft para Windows, Linux y macOS. Version: 1.60.2 (Universal). Pero se puede utilizar cualquier otro de su preferencia (IntelliJ, eclipse, etc.).

## Demo

Coleccion de postman para probar API [Collection Postman](https://github.com/luchonetvv/user-api/blob/main/UserApi.postman_collection.json)

![Demo](https://user-images.githubusercontent.com/1069705/135114677-2c7ddd55-c8c7-4022-bc78-2efd5d6d1572.gif)

## Instalacion

Clone el repositorio.

```bash
git clone git@github.com:luchonetvv/user-api.git
git status
```

Tienes que instalar Java 8.

Instalar Visual Studio Code y luego abrir el proyecto...
Con solo ejecutar el proyecto desde la clase LuchonetvvApplication.java, es decir,
click en Run y estara listo para crear usuarios.

O si tu quieres construir e instalar desde la terminal, aqui estan los pasos:

On Mac or Linux
```bash
./gradlew bootRun
```

On Windows
```bash
gradlew.bat bootRun
```

## License

Apache License, Version 2.0

**Free Software, Hell Yeah!**
