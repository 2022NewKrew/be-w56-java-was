# be-w56-java-was

56주차 간단 웹 서버 구현

### 요구사항 1: 정적인 html 파일 응답

> <http://localhost:8080/index.html>로 접속했을 때 webapp 디렉토리의 `index.html` 파일을 읽어 클라이언트에 응답한다.

- HTTP Request Header 예시

```
HTTP
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

### 요구사항 2: GET으로 회원가입 기능 구현

> “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다. 이 폼을 통해서 회원가입을 할 수 있다. 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.

```
/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
```

> HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.

- HTTP Request Header 예

```
GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

### 요구사항 3: POST로 회원 가입 구현

> http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다. 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.

- HTTP Request Header 예

```
POST /user/create HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Content-Length: 59
Content-Type: application/x-www-form-urlencoded
Accept: */*

userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
```

### 요구사항 4: Cookie를 이용한 로그인 구현

> “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다. 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
>
> 앞에서 회원가입한 사용자로 로그인할 수 있어야 한다. 로그인이 성공하면 cookie를 활용해 로그인 상태를 유지할 수 있어야 한다. 로그인이 성공할 경우 요청 header의 Cookie header 값이 logined=true, 로그인이 실패하면 Cookie header 값이 logined=false로 전달되어야 한다.

- HTTP Request Header 예

```
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
Cookie: logined=true
```

- HTTP Response Header 예

```
HTTP/1.1 200 OK
Content-Type: text/html
Set-Cookie: logined=true; Path=/
```
