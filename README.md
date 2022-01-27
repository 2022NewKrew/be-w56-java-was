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

requestLineHeader = HttpRequestUtils.parseRequestHeader(line);
contentType = HttpRequestUtils.readHeaderAccept(br);

// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
try (DataOutputStream dos = new DataOutputStream(out)) {
    RequestPathController.urlMapping(requestLineHeader, contentType, dos);
}
```

correct

```java
try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
        String line = br.readLine();
        if (line == null) {
            return;
        }

        requestLineHeader = HttpRequestUtils.parseRequestHeader(line);
        contentType = HttpRequestUtils.readHeaderAccept(br);

        // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
        try (DataOutputStream dos = new DataOutputStream(out)) {
            RequestPathController.urlMapping(requestLineHeader, contentType, dos);
        }
}
```

## 의문점

1. url이 /user/create일 땐 redirection이 제대로 작동하지 않았는데 /user로 수정하니 작동한다.
   1. /user/create일 땐 계속 /user가 남아있어서 404오류가 발생했음 ex) /user/user/form.html, /user/index.html
   2. /user일 땐 위와 같은 현상이 일어나지 않음. 원인은 파악 중
2. Set-Cookie로 쿠키를 설정했는데, 세션 쿠키가 아닌지 계속 정보가 남아있다. 검색을 해보니 max-age와 같이 유효기간을 설정하지 않는다면 세션 쿠키라고 하는데 내 쿠키는 왜 계속 남아있는걸까?