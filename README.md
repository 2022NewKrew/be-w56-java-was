# 웹 서버 구현하기
## 1단계
### 프로젝트 목표
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.

### 요구사항 1 : 정적인 html 파일 응답
> http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

#### HTTP Request Header 예
```
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

#### 모든 Request Header 출력하기 힌트
- inputStream => inputStreamReader => BufferedReader
```java
Socket connection;

InputStream in = connection.getInputStream();
BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
```

- BufferedReader.readLine() 메소드를 활용하여 라인별로 http header를 읽는다.
- http header 전체를 출력한다.
  - header 마지막은 `while (!.equals(line)) {}`로 확인 가능하다.
  - line이 null 값인 경우에 대한 예외 처리도 해야 한다. 그렇지 않을 경우 무한 루프에 빠진다. `if(line == null) { return; }`
```java
String line = br.readLine();
log.info("request : {}", line);
while (!line.equals("")) {
  if (line == null) {
    return;
  }
  log.info("header : {}", line);
  line = br.readLine();
}
```

#### Request Line에서 path 분리하기 힌트
- Request의 첫 번째 라인에서 요청 URL(위의 예시의 경우 `/index.html`이다.)을 추출한다.
- 구현은 별도의 유틸 클래스를 만들고 단위 테스트를 만들어 진행할 수 있다.

#### path에 해당하는 파일을 읽어 응답하기 힌트
- 요청 URL에 해당하는 파일을 webapp 디렉토리에서 읽어 전달하면 된다.
- 파일 데이터를 byte[]로 읽는다.

```java
byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
```

### Reference
#### Http Request 구조
- [참고링크1](https://velog.io/@mokyoungg/HTTP-HTTP-%EA%B5%AC%EC%A1%B0-%EB%B0%8F-%ED%95%B5%EC%8B%AC-%EC%9A%94%E3%85%85)
