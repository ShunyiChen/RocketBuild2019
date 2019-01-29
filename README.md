# RocketBuild2019
RocketBuild2019

#### Prepare development environment
Eclipse/IntellJ IDEA with latest version

JDK 1.8

Python 3.5.4 or 3.5.6,download installer from this link https://www.python.org/downloads/windows/

Database ORACLE/MySQL/SQL Server....



#### Build all packages:
> open cmd

>cd facein

>clean package -DskipTests=true


#### Run
Start up server side:
> open cmd

> cd facein-core

> spring-boot:run

Start up JavaClient side:

> open cmd 

> cd facein-sdk\target

> java -jar facein-sdk-0.0.1-SNAPSHOT.jar
