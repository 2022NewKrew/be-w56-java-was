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

## Step6
- 요구사항
  - 지금까지 구현한 소스코드는 stylesheet 파일을 지원하지 못하고 있다. Stylesheet 파일을 지원하도록 구현하도록 한다.
  - Http Request Header 예
  ```
  GET ./css/style.css HTTP/1.1
  Host: localhost:8080
  Accept: text/css,*/*;q=0.1
  Connection: keep-alive
  ```
- css, js, favicon에 맞도록 framework에 Controller를 두어서 처리하도록 만듦

## Step7
- 요구사항
  - H2 또는 MySQL 혹은 NoSQL 등 데이터베이스를 활용하여 회원정보를 DB에 저장한다. 
  - index.html에 로그인한 사용자가 글을 쓸 수 있는 한 줄 메모장을 구현한다. 
  - 로그인하지 않은 사용자도 게시글을 볼 수 있다.
- 구현 기능
  - Spring cafe에서 사용한 MySql을 사용하여 기능 구현

## Step8
- 요구사항
  - 스트레스 테스트 프로그램 등을 이용해서 테스트를 진행한다. 
  - 테스트를 기반으로 다양한 성능 분석을 해 본다. 
  - 마크다운 문서로 간단한 보고서를 만들어 결과를 README.md 에 추가한다.
- 서버 배포
  - 서버에 배포할때 static 파일을 제대로 불러오지 못하는 상황이 발생
    - file을 읽어오는 부분 변경하여 해결
- 로그 수집 및 분석
  - logback.xml 파일을 사용해 error, debug 로그를 분리해서 파일로 저장하도록 기능 추가
- 스트레스 테스트
  - ngrinder를 사용해 테스트 진행
  - ![ngrinder result](https://user-images.githubusercontent.com/49807087/152948354-6ef62781-e94d-49ef-9ee0-87bb0a70f2a8.png)
- GC 모니터링
  - https://d2.naver.com/helloworld/6043
  - CUI
    - CUI는 각 칼럼이 의미하는 것을 잘 구분하지 못해서 보기에 어려웠다. 
    - gc 관련 jstat 옵션
      - ![image](https://user-images.githubusercontent.com/49807087/153108434-98c42094-fa2c-4d8c-82a0-490d1b3535ae.png)
    - jstat의 옵션에 따른 칼럼 
      - ![image](https://user-images.githubusercontent.com/49807087/153108473-ef8a48ab-dd53-4f0e-a39b-a69b07a65c61.png)

  - gc monitoring
    - ![gc monitoring](https://user-images.githubusercontent.com/49807087/152948464-cbfb24fe-85bc-4ef5-84d9-bbee718e28d4.png)
    - 오후 5:07:30 에 ngrinder로 테스트를 진행했을때, 변화하는 것을 확인할 수 있었다. 
  - visual gc 
    - ![스크린샷 2022-02-09 오전 10 43 53](https://user-images.githubusercontent.com/49807087/153108068-71380bb4-0fbb-4453-a63c-a259e1d7261d.png)
    - visual gc plugin을 설치하면 다음과 같은 정보를 확인할 수 있다. 
- 슬로우 쿼리 분석 & 성능 개선
  - 아직 스트레스 테스트 결과와 GC 모니터링의 상태를 보는 것이 잘 이해가 되지 않아서, 조금 더 공부한 후에 진행을 해봐야겠다.
