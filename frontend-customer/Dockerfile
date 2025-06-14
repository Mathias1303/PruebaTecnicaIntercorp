# ---- Etapa 1: Construcción (Build Stage) ----
# Usamos una imagen de Node para construir el proyecto
FROM node:18-alpine AS builder

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos los archivos de dependencias
COPY package.json package-lock.json ./

# Instalamos todas las dependencias del proyecto
RUN npm install

# Copiamos el resto del código fuente de la aplicación
COPY . .

# Construimos la aplicación para producción.
# Los artefactos se generarán en la carpeta /app/dist/your-app-name
# Reemplaza 'angular-front' con el nombre de tu proyecto en angular.json
RUN npm run build -- --configuration production

# ---- Etapa 2: Producción (Production Stage) ----
# Usamos una imagen de Nginx, que es un servidor web muy ligero y eficiente
FROM nginx:1.25-alpine

# Copiamos los archivos estáticos construidos en la etapa anterior
# desde la imagen 'builder' a la carpeta de Nginx para servir contenido web.
# Asegúrate de que el nombre de la carpeta 'angular-front' coincida con el de tu proyecto.
COPY --from=builder /app/dist/frontend-customer /usr/share/nginx/html

# Copiamos el archivo de configuración personalizado de Nginx
# Este archivo es crucial para que el enrutamiento de Angular y el proxy a la API funcionen.
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto 80, que es el puerto por defecto de Nginx
EXPOSE 80