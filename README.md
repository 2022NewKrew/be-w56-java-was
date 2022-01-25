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
- [Http Request 구조 - 참고링크](https://velog.io/@mokyoungg/HTTP-HTTP-%EA%B5%AC%EC%A1%B0-%EB%B0%8F-%ED%95%B5%EC%8B%AC-%EC%9A%94%E3%85%85)

***

## 2단계
### 요구사항 2 : GET으로 회원가입 기능 구현
> "회원가입" 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
> 이 폼을 통해서 회원가입을 할 수 있다.
> 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.

```
/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
```

> HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클랙스에 저장한다.

#### HTTP Request Header 예

```
GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

#### Request Parameter 추출하기 힌트
- Header의 첫 번째 라인에서 요청 URL을 추출한다.
- 요청 URL에서 접근 경로와 이름=값을 추출해 User 클래스에 담는다.
  - 접근 경로(위의 예시의 경우 `/user/create`)와 패러미터를 분리하는 방법은 String의 split() 메서도를 활용하면 된다.
  - `?` 문자는 정규표현식의 예약어이기 때문에 split() 메소드를 활용할 `?`이 아닌 `\\?`를 사용해야 한다.
- 구현은 가능하면 junit을 활용해 단위 테스트를 진행하면서 하면 좀 더 효과적으로 개발 가능하다.
- 이름/값 파싱은 `util.HttpRequestUtils` 클래스이 `parseQueryString()` 메서드를 활용한다.
