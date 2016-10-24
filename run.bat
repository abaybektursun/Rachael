REM Setup 
IF EXIST JavaCV/*.jar(echo "Yes")
ELSE (echo "No")


REM Compile to bytecode
javac -classpath .;JavaCV/javacpp.jar;JavaCV/javacv.jar chat/client/Client.java
javac -classpath .;mongo-java-driver-3.3.0.jar chat/server/Server.java

REM Run
start java -cp .;mongo-java-driver-3.3.0.jar chat.server.Server
timeout /t 6
start java -cp .;JavaCV/javacpp.jar;JavaCV/javacv.jar chat.client.Client
