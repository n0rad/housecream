cd ../
call mvn clean
call mvn package
call java -jar target/acse-1.0-SNAPSHOT.jar
pause
