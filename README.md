# be-w56-java-was
56주차 간단 웹 서버 구현

## step 1 
- 요구사항
  - 정적인 html 파일 응답
    - http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
    - HTTP Request Header 예 
    ``` 
      GET /index.html HTTP/1.1 
      Host: localhost:8080 
      Connection: keep-alive 
      Accept: */* 
    ```
- InputStream 을 통해서 header의 정보를 읽어오는 것을 알 수 있었다.
- favicon.ico, css, js, font 등이  제대로 호출되지 않는 문제가 있음
    - Content-Type 이 text로 되어있어서 적절한 파일 형식을 제대로 읽어오지 못해서 발생하는 문제
    - Header에서 Accept가 존재하면 해당 부분을 content type으로 헤더에 넣어주면 문제가 해결이 됨

## Step2
- 요구사항 
  - GET으로 회원가입 기능 구현
  - HTTP Request Header 예
  ```
  GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1
  Host: localhost:8080
  Connection: keep-alive
  Accept: */*
  ```
- RequestLine에 있는 QeuryString을 파싱해서 사용
- Reflection을 사용해서 어노테이션이 사용된 클래스, 메소드를 찾는 것이 가능

## Step3
- 요구사항
  - POST로 회원가입 구현
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
- Request Body는 Http Header 이후 빈 공백을 가지는 한출 뒤부터 시작한다.
- IOUtils 클래스의 readData로 request body를 가져올 수 있지만, 이 때 Content-Length 값이 필요하다
- HTTP 응답해더는 정말 많다. 용도에 맞도록 적절히 사용할 수 있도록 알아두면 용이할 것 같다.

## Step4
- 요구사항
  - Cookie를 이용한 로그인 구현
  - Http ResponseHeader 예
  ```
  HTTP/1.1 200 OK
  Content-Type: text/html
  Set-Cookie: logined=true; Path=/
  ```
- Reflection 을 사용해 Exception Handler를 구현
- Http Header 가 Request, Response 에 따라 필드가 많이 상이함

## Step5
- 요구사항
  - 접근하고 있는 사용자가 "로그인" 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
  - 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.
- Annotation을 사용해 Interceptor를 구현
- 기존의 Interceptor
  - HandlerIntercpetor 를 상속받아서 구현
  - WebMvcConfigurer에서 addInterceptors 를 통해 interceptor를 등록하고 사용
  - addInterceptor를 할 때, 우선순위, includePathPattern, excludePathPattern 등을 등록함.
