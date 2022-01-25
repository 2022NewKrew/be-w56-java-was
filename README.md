# be-w56-java-was

56주차 간단 웹 서버 구현

## 알게된 것

1. CSS를 적용하기 위해서는 content-type을 하드코딩 해선 안된다.

before

```java
dos.writeBytes("HTTP/1.1 "+status.getValue()+" "+status.name()+" \r\n");
dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
dos.writeBytes("Content-Length: "+lengthOfBodyContent+"\r\n");
dos.writeBytes("\r\n");
```

after

```java
dos.writeBytes("HTTP/1.1 "+status.getValue()+" "+status.name()+" \r\n");
dos.writeBytes("Content-Type: "+contentType+"; charset = utf - 8\r\n ");
dos.writeBytes("Content-Length: "+lengthOfBodyContent+"\r\n");
dos.writeBytes("\r\n");
```

2. try-with-resources 사용시 자동으로 자원이 해제되기 때문에 범위를 잘 확인해야 함

error (소켓 닫힘)

```java
try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))){
        String line=br.readLine();
        if(line==null){
            return;
        }
}

requestHeader = HttpRequestUtils.parseRequestHeader(line);
contentType = HttpRequestUtils.readHeaderAccept(br);

// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
try (DataOutputStream dos = new DataOutputStream(out)) {
    RequestPathController.urlMapping(requestHeader, contentType, dos);
}
```

correct

```java
try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
        String line = br.readLine();
        if (line == null) {
            return;
        }

        requestHeader = HttpRequestUtils.parseRequestHeader(line);
        contentType = HttpRequestUtils.readHeaderAccept(br);

        // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        try (DataOutputStream dos = new DataOutputStream(out)) {
            RequestPathController.urlMapping(requestHeader, contentType, dos);
        }
}
```