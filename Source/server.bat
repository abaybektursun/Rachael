javac -cp .:~/opencv/build/java/opencv-2*.jar server/Server.java
java  -cp .:~/opencv/build/java/opencv-2*.jar -Djava.library.path=~/opencv/build/java/x64 server.Server
