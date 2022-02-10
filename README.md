# be-w56-java-was
56주차 간단 웹 서버 구현

## 1단계
### 요구사항
- [X] 정적인 html 파일 응답 
  - http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
### 추가 구현 사항
- html, css, js, tff등 request 속 accept를 활용해 리소스도 반환하게 구현
- Request와 Response 객체를 구현해서 코드 중복을 줄임

## 2단계
### 요구사항
- [X] GET으로 회원가입 기능 구현
  - “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
    이 폼을 통해서 회원가입을 할 수 있다.
### 추가 구현 사항
- @Controller, @RequestMapping을 구현해 사용
- 요청 url에 따라 적절한 메소드 호출 및 응답이 가능하도록 ControllerHandler 개발

## 3단계
### 요구사항
- [X] POST로 회원 가입 구현
  - [X] http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.
  - [X] 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.

## 4단계
### 요구사항
- [X] Cookie를 이용한 로그인 구현
  - “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다. 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
  - 앞에서 회원가입한 사용자로 로그인할 수 있어야 한다. 로그인이 성공하면 cookie를 활용해 로그인 상태를 유지할 수 있어야 한다. 로그인이 성공할 경우 요청 header의 Cookie header 값이 logined=true, 로그인이 실패하면 Cookie header 값이 logined=false로 전달되어야 한다.
