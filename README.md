# be-w56-java-was
56주차 간단 웹 서버 구현

## 알게된 것
1. CSS를 적용하기 위해서는 content-type을 하드코딩 해선 안된다.

before
```java
dos.writeBytes("HTTP/1.1 " + status.getValue() + " " + status.name() + " \r\n");
dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
dos.writeBytes("\r\n");
```

after
```java
dos.writeBytes("HTTP/1.1 " + status.getValue() + " " + status.name() + " \r\n");
dos.writeBytes("Content-Type: " + contentType + "; charset = utf - 8\r\n ");
dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
dos.writeBytes("\r\n");
```