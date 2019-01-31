# RocketBuild2019
RocketBuild2019

#### Prepare development environment
1.Eclipse/IntellJ IDEA with latest version

2.JDK 1.8

3.Put opencv_java401.dll into current jre/bin, download opencv_java401.dll from this link https://myshare.rocketsoftware.com/myshare/d/0LNJ7JDEp 

4.Python 3.5.4 or 3.5.6,download installer from this link https://www.python.org/downloads/windows/

5.We don't need database ORACLE/MySQL/SQL Server....any more, we are using H2 memory database,

It will automatically create a table 'passports' as shown below:

<img width="100%" height="100%" src="https://github.com/ShunyiChen/RocketBuild2019/blob/master/DBTable.jpg"/> 



#### Build all packages:
> open cmd

> cd facein

> clean install -DskipTests=true


#### Run
Start up server side:
> open cmd

> cd facein-core

> spring-boot:run

Start up JavaClient side:

> open cmd 

> cd facein-sdk\target

> java -jar facein-sdk-0.0.1-SNAPSHOT.jar
