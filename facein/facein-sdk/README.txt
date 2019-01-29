必须导入到本地仓库否则打包加不进去

mvn install:install-file -Dfile=C:\Users\chens\Desktop\lib\facein-ocrsdk-0.0.1-SNAPSHOT.jar -DgroupId=com.rocketsoftware -DartifactId=facein-ocrsdk -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=C:\Users\chens\Desktop\lib\opencv-401.jar -DgroupId=org.opencv -DartifactId=opencv -Dversion=401 -Dpackaging=jar
