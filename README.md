# Proyecto de Microservicios

Este repositorio contiene una arquitectura de microservicios orientada a eventos, diseñada para procesar archivos XML, transformarlos y persistir la información, exponiéndola finalmente a través de una API REST a una aplicación cliente. La comunicación entre servicios se realiza de forma asíncrona utilizando Apache Kafka, y toda la infraestructura está containerizada con Docker y orquestada con Docker Compose.

## 1. Arquitectura General

El sistema se compone de varios servicios desacoplados, cada uno con una responsabilidad única, que se comunican a través de un bus de mensajes.

### Flujo de Datos:

#### 1. ms-05 (XML Ingestor): Vigila un directorio de entrada en busca de archivos XML. Al encontrar uno, lo convierte a formato JSON.

#### 2. Publicación en Kafka: El JSON generado se publica en un tópico de Kafka (xml_a_json_topic).

#### 3. Procesamiento Paralelo:

* ms-01 (Database Writer): Se suscribe al tópico, consume los mensajes JSON y persiste la información en una base de datos PostgreSQL.

* ms-02 (JSON Processor): Se suscribe al mismo tópico, procesa el JSON (p. ej., para enriquecerlo o validarlo) y lo reenvía a un segundo tópico de Kafka (json_procesado_topic).

#### 4. Exposición de Datos:

* ms-04 (API Service): Consume los mensajes del segundo tópico y se conecta a la base de datos para ofrecer endpoints REST con la información procesada y actualizada.

#### 5. Interfaz de Usuario:

* angular-front: Una aplicación de cliente (Single Page Application) desarrollada en Angular que consume las APIs expuestas por ms-04 para visualizar los datos.

## 2. Arquitectura General

* Backend: Java 17, Spring Boot 3.x

* Frontend: Angular 15, TypeScript

* Mensajería: Apache Kafka

* Base de Datos: PostgreSQL

* Containerización: Docker, Docker Compose

* Servidor Web (Frontend): Nginx

## 3. Configuración y Despliegue
Para levantar la arquitectura completa, es necesario seguir dos pasos de configuración previos.

### Pre requisitos

* Docker y Docker Compose: Instalados y en ejecución.

* Java JDK 17+ y Maven: Para compilar los microservicios.

* Node.js y Angular CLI: Para compilar el frontend.

* Git: Para clonar el repositorio.

### Paso 1: Configuración de Directorios Locales (Crucial)
El microservicio ms-05 necesita saber dónde buscar los archivos XML en su máquina local antes de que Docker los mapee a los volúmenes del contenedor.

Abra el archivo application.properties del microservicio ms-05:
ms-05/src/main/resources/application.properties

**Modifique las siguientes propiedades** para que apunten a una ruta válida en su sistema. Se recomienda usar la carpeta customer-data incluida en este repositorio.

![alt text](<Desktop Screenshot 2025.06.14 - 14.11.59.99.png>)

**Nota técnica:** Esta modificación es necesaria para las pruebas locales fuera de Docker. Dentro de Docker, estas variables son sobrescritas por las definidas en docker-compose.yml, asegurando la portabilidad.

### Paso 2: Generación de Artefactos (Archivos .jar)
Docker necesita los archivos .jar compilados de cada microservicio para poder construir las imágenes.

Ejecute el siguiente comando dentro de la carpeta de cada microservicio backend (ms-01, ms-02, ms-04, ms-05):

![alt text](<Desktop Screenshot 2025.06.14 - 14.16.33.52.png>)

**Nota técnica:** Se utiliza la bandera -DskipTests para evitar que Maven ejecute las pruebas de integración durante la construcción. Esto previene errores de conexión a la base de datos o Kafka, ya que en esta fase solo nos interesa empaquetar la aplicación, no probarla en el entorno local.

### Paso 3: Levantar la Arquitectura
Una vez generados los artefactos .jar, vuelva al directorio raíz del proyecto (donde se encuentra el archivo docker-compose.yml) y ejecute un único comando para construir todas las imágenes e iniciar todos los contenedores:

docker-compose up --build

Este comando orquestará la creación de la red, volúmenes, y contenedores para todos los servicios, incluido el frontend de Angular.

### 4. Cómo Probar la Aplicación

1. Acceder al Frontend: Abra su navegador y vaya a http://localhost:4200. Debería ver la interfaz de la aplicación Angular.

2. Iniciar el Flujo:

    * Cree un archivo XML (p. ej., cliente.xml) con la estructura adecuada.

    * Coloque ese archivo en la carpeta customer-data/input en la raíz del proyecto.

3. Verificar los Logs: Observe la salida de docker-compose en su terminal. Verá la actividad secuencial de ms-05, ms-01 y ms-02.

4. Verificar la Base de Datos:

    * Conéctese a la base de datos PostgreSQL usando un cliente como DBeaver o pgAdmin con las credenciales del docker-compose.yml (host: localhost, port: 5432, user: postgres, password: mysql, db: db_customer).

    * Ejecute una consulta SELECT * FROM ... sobre la tabla de destino para confirmar que los datos se han guardado.

5. Verificar la API:

    * Use un cliente de API como Postman para hacer una petición GET a los endpoints de ms-04, que se exponen en http://localhost:8088. Por ejemplo: http://localhost:8088/api/data/list. Debería devolver los datos que acaba de procesar.