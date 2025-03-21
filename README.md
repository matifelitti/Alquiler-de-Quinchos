
# 🏡 Proyecto de Alquiler de Quinchos  

Una plataforma web para el **alquiler de quinchos**, ideal para la organización de **fiestas y eventos**. La aplicación permite a **propietarios** publicar sus espacios, a **clientes** reservarlos, y a **administradores** gestionar el sistema.  

## 🚀 Tecnologías Utilizadas  

- **Backend**: Java, Spring Framework, Spring Security  
- **Frontend**: HTML, CSS, JavaScript, Bootstrap  
- **Base de Datos**: MySQL / PostgreSQL (según configuración)  

## 🎯 Características  

### 🔑 Roles de Usuario  
- **Administrador** – Gestiona usuarios, propiedades y reservas.  
- **Propietario** – Publica y administra sus propiedades.  
- **Cliente** – Busca, reserva y califica quinchos.  

### 🏠 Funcionalidades  

#### 📌 Páginas Principales  
- **🏡 Inicio (Index)** – Página principal con información general.  
- **🔐 Login** – Autenticación de usuarios con seguridad integrada.  
- **📝 Registro** – Creación de cuentas nuevas.  
- **📖 Nosotros** – Información sobre la misión, visión y equipo.  

#### 🏠 Gestión de Propiedades  
- **📢 Publicar** – Los propietarios pueden listar sus quinchos.  
- **📋 Detalles** – Información completa de cada propiedad (capacidad, ubicación, fotos, etc.).  
- **🔄 Modificar / Eliminar** – Administración de propiedades.  
- **📅 Reservas** – Sistema para agendar y gestionar alquileres.  
- **⭐ Opiniones y Calificaciones** – Los clientes pueden comentar y puntuar después de un evento.  

#### 📊 Panel de Administración  
- **📈 Dashboard** – Vista centralizada con control de usuarios.  

### 🔧 Componentes Reutilizables (Fragments)  
- **🔗 Navbar** – Barra de navegación accesible en toda la plataforma.  
- **📩 Msg** – Sistema de mensajes y alertas.  
- **⚓ Footer** – Pie de página con información de contacto y enlaces.  

## 📌 Instalación y Ejecución  

### 1️⃣ Clonar el Repositorio  
```sh
git clone https://github.com/tu-usuario/alquiler-quinchos.git  
cd alquiler-quinchos  
```  

### 2️⃣ Configurar Base de Datos  
- Ajusta los parámetros en `application.properties` para conectar con tu base de datos MySQL/PostgreSQL.  

### 3️⃣ Ejecutar la Aplicación  
```sh
mvn spring-boot:run  
```  

### 4️⃣ Abrir en el Navegador  
Accede a la aplicación en:  
```
http://localhost:8080  
```
