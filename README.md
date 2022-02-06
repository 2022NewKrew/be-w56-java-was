# be-w56-java-was
56주차 간단 웹 서버 구현

## 1-1단계
### 요구사항
- [X] 정적인 html 파일 응답 
  - http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
### 추가 구현 사항
- html, css, js, tff등 request 속 accept를 활용해 리소스도 반환하게 구현
- Request와 Response 객체를 구현해서 코드 중복을 줄임

## 1-2단계
### 요구사항
- [X] GET으로 회원가입 기능 구현
  - “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
    이 폼을 통해서 회원가입을 할 수 있다.
### 추가 구현 사항
- @Controller, @RequestMapping을 구현해 사용
- 요청 url에 따라 적절한 메소드 호출 및 응답이 가능하도록 ControllerHanlder 개발
