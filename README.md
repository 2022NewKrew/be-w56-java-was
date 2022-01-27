# be-w56-java-was - 56주차 간단 웹 서버 구현
## 프로젝트 목표
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.

## 웹 서버 구현 1단계
### 요구사항
- 정적인 html 파일 응답
  - ex> http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### 구현 내용
- request header를 파싱하여 RequestInfo 클래스에 필요한 정보 저장
- requestInfo 를 바탕으로 response header 작성
- requestInfo 를 바탕으로 static file을 바이트 코드로 변환하여 response body에 담아 전달

## 웹 서버 구현 2단계
### 요구사항
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
- 이 폼을 통해서 회원가입을 할 수 있다.
- 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.

### 요구사항
- POST로 회원가입 구현
- http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.
- 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.
- HTTP Reqeust Header 예
```
POST /user/create HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Content-Length: 59
Content-Type: application/x-www-form-urlencoded
Accept: */*

userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
```

### 구현 내용
프로젝트 구조를 스프링과 유사한 구조로 변경
  - ![img.png](img.png)
  - FrontController class
    - spring에서 DispatcherServlet과 유사한 기능 수행
    - request를 파싱하여 HttpRequest 객체에 저장
    - handlerMapping 클래스에 HttpRequest를 넘겨주고 response에 담을 view name을 리턴받음
    - view name을 ViewResolver에 전달하여 HttpResponse를 받아옴
    - 위 두 과정에서 exception 발생 시 FrontController에서 에러 핸들링 수행(error 페이지 렌더링)
  - HandlerMapping class
    - FrontController로부터 전달받은 HttpRequest에 매핑되는 클래스, 매서드를 찾는 기능 수행
    - HttpRequest의 isStaticResource가 true인 경우 resourceController로 매핑
    - 그렇지 않을 시 Beans에 수동으로 등록한 List<Controller>에서 해당하는 Controller를 찾음 (현재는 Controller가 하나밖에 없음)
    - 해당하는 클래스와 매서드를 찾아 호출
  - Controller, Service, Repository(현재 in memory db로 DataBase 사용)는 기존 spring 프로젝트와 거의 유사
  - ViewResolver class
    - 현재 ViewResolver와 View의 역할이 어떻게 분리되는 것인지 잘 이해되지 않아 ViewResolver에서 HttpResponse를 만들어서 리턴하는 방식으로 구현되어 있음
    - redirect 시 3xx response, 아닐 시 2xx response를 만드는 메서드 호출
    - 현재는 200 OK 와 302 Found 두 가지의 status code만 있는 상태
    
GET으로 param을 받아 회원가입하는 기능 구현  
POST로 request body의 내용을 받아 회원가입 하는 기능 구현  
redirect 기능 구현  
