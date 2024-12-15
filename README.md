    Requisitos instalaciones:

- Java 17
- Maven
- Docker
- Postman
- Intellij (Opcional, recomendable si quieres tener control del back)
- Dbeaber u otro (Opcional, recomendable si quieres tener control de la bbdd)


    Onboarding

- git clone <URL_DEL_REPOSITORIO> (clonar el repo de la API)
- docker-compose up -d (ejecutar el docker-compose.yml que nos crea una imagen docker
y ejecuta el contenedor para tener una bbdd sin necesidad de tener postgre instalado)
- Ejecutar el back de la forma que quieras (recomendable con intellij, de momento
no hay variables de entorno porque no tenemos entorno de desarrollo aún)
- Importar en postman la colection que tenemos en la raíz del proyecto.

Notas importantes:
(El proyecto tiene una configuración inicial en el segundo script de flyway)

    Login

- email: user_test@mycar.com
- password: mySecretPassword321


    Create car:

{
"companyName": "Seat",
"modelName": "Leon",
"km": 130000
}

    Create Issue
(hay dos tipos de issues en la app, uno que notifica por km y otro por tiempo, es decir,
por tiempo, por ejemplo, un mes. Por km, por ejemplo, 10.000 km)

- Create issue by km

body: {
"name": "Revisar pastillas de freno",
"currentDistance": 20000,
"notificationDistance": 50000,
"typeId": 1
}

- Create issue by days

body: {
"name": "Revisar nivel de carburante",
"notificationDateDays": 30,
"typeId": 2
}

    Working in progress...