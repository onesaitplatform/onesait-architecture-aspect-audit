**Audit library**


**Introduction**
Aiming the objective of security and auditing of all the business processes that occur and are invoked in the product, this library must be used, which records the events and therefore allows reconstructing any situation that has occurred.
In addition, it will also allow you to have monitoring information, detect possible errors, attempted attacks or intrusions,... etc.


**Goal**
Our audit library allows each operation executed by the application or a user to be audited, storing these records in different destinations so that they can be consumed later.


**Requirements**
It is required to be able to use the library to have imported:

*  spring security


**Features**
In this first version of the audit library we have some annotations so that each operation is audited according to the needs of the user and the project.

For an operation, REST API, to be audited, it will be necessary and essential to use the @Audit annotation and then we can configure how we want to display this information (parameters).

Here are the annotations we have available:


* **@Audit** to audit REST API methods or operations.
* **@AuditIgnore** to ignore method parameters.
* **@AuditField** to anonymize method parameters. In this we can configure the number of characters that we want to anonymize.


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

**Project Properties**

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
  
  