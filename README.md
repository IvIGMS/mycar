# Proyecto API de Gestión de Vehículos

Este proyecto es una API que permite gestionar vehículos, registrar incidencias y realizar notificaciones basadas en kilometraje o tiempo. A continuación, se detallan los requisitos, el proceso de configuración y algunas notas importantes para trabajar con el proyecto.

## Requisitos de Instalación
Antes de comenzar, asegúrate de tener instalados los siguientes componentes:
- **Java 17**  
- **Maven**  
- **Docker**  
- **Postman**  
- **IntelliJ IDEA** *(Opcional, recomendado para gestionar el backend)*  
- **DBeaver** u otro cliente de bases de datos *(Opcional, recomendado para visualizar la base de datos)*  


## Onboarding: Configuración del Proyecto

1. **Clonar el repositorio:**  
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   ```
    Utiliza la rama DEVELOP.

2. **Iniciar la base de datos con Docker Compose:**
    Ejecuta el siguiente comando para levantar la base de datos en un contenedor Docker:
    ```bash
    docker-compose up -d
    ```
    Esto creará una imagen Docker y ejecutará el contenedor con una base de datos PostgreSQL preconfigurada. No necesitas tener PostgreSQL instalado localmente.


3. **Ejecutar el backend:**
    Puedes hacerlo desde IntelliJ IDEA u otro IDE. Si prefieres ejecutarlo manualmente, sigue las instrucciones en la sección de Notas Importantes.

4. **Importar la colección de Postman:**
    En la raíz del proyecto encontrarás una colección de Postman que puedes importar para probar las rutas de la API.

## Notas Importantes
- Si no utilizas IntelliJ para ejecutar el backend, necesitarás instalar Maven y las dependencias necesarias. Para hacerlo, ejecuta:

    Esto descargará las dependencias y dejará el proyecto listo para ejecutarse.
    ```bash
    mvnw clean install
    ```

    Para iniciar el backend con Maven, usa el siguiente comando:
    ```bash
    mvnw spring-boot:run
    ```

- El proyecto tiene una configuración inicial proporcionada por Flyway, que se ejecutará automáticamente al iniciar la aplicación.

## Autenticación
- Para realizar operaciones en la API, puedes usar las siguientes credenciales:
    ```bash
    {
        "email": "user_test@mycar.com"
        "contraseña": "mySecretPassword321"
    }
    ```

## Endpoints Destacados
Crear un Vehículo

- Endpoint: /api/v1/cars

- Método: POST

- Body (JSON):
    ```bash
    {
    "companyName": "Seat",
    "modelName": "Leon",
    "km": 130000
    }
    ```


Crear una Incidencia, la API soporta dos tipos de incidencias: por kilometraje o por tiempo.

Crear incidencia por kilometraje

- Endpoint: /api/v1/issues

- Método: POST

- Body (JSON):
    ```bash
    {
        "name": "Revisar pastillas de freno",
        "currentDistance": 20000,
        "notificationDistance": 50000,
        "typeId": 1
    }
    ```

Crear incidencia por tiempo

- Endpoint: /api/v1/issues
- Método: POST
- Body (JSON):

    ```bash
    {
    "name": "Revisar nivel de carburante",
    "notificationDateDays": 30,
    "typeId": 2
    }
    ```

# Estado del Proyecto 
Actualmente, el proyecto está en progreso y pueden surgir cambios en la implementación.

