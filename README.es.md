**Librería de auditoría**


**Introducción**
Con el objetivo de seguridad y auditoria de todos los procesos de negocio que suceden y se invocan en el producto, se debe utilizar esta librería que registra los eventos y por lo tanto permite reconstruir cualquier situación ocurrida.
Además también permitirá tener información de monitorización, detectar los posibles errores, intentos de ataques o intrusiones,...etc


**Objetivo**
Nuestra librería de auditoría permite que cada operación que ejecute la aplicación o un usuario sea auditada, almacenando dichos registros en distintos destinos para que, posteriormente, sean explotados.


**Requisitos**
Se requiere para poder usar la librería tener importado:

*  spring security


**Características**
En esta primera versión de auditoría disponemos de algunas anotaciones para que cada operación sea auditada según las necesidades del usuario y del proyecto. 

Para que una operación, API REST, sea auditada va ser necesario e imprescindible utilizar la anotación @Audit y luego podremos configurar como queremos visualizar está información (parámetros).

A continuación, se detallan las anotaciones que tenemos disponibles: 


* **@Audit** para auditar métodos u operaciones API REST.
* **@AuditIgnore** para ignorar parámetros del método.
* **@AuditField** para anonimizar parámetros del método. En esta podremos configurar el número de caracteres que queremos anonimizar.


```xml
<dependency>
	<groupId>com.minsait.architecture</groupId>
	<artifactId>architecture-audit</artifactId>
	<version>${ultima.version}</version>
</dependency>
´´´

```xml
<repositories>
	<repository>
		<id>architecture-maven-releases</id>
		<url>${NEXUS_URL}/repository/maven-releases/</url>
	</repository>
</repositories>
´´´

```xml
	<properties>
		<NEXUS_URL>https://nexus.onesaitplatform.com</NEXUS_URL>
	</properties>
```

**Propiedades del proyecto**

```yaml
audit:
  app: historian
  console: true
  file:
    enable: true
    path: D://
    name: output.txt
    separator: '#'
  kafka:
    enable: false
    topic: audit
  transactional: true
```
  
  