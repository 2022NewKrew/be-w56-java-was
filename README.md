# be-w56-java-was
56주차 간단 웹 서버 구현

# 프로젝트 목표
- HTTP 를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.

# 구현 1단계
- [x] 요구사항 1: 정적인 html 파일 응답
   - enum 클래스 `HttpStatus`, `MIME`, `RequestMappingInfo` 추가
   - request, response 정보들을 담는 `MyHttpRequest`, `MyHttpResponse` 클래스를 추가
   - `MyHttpResponse` 의 경우 빌더 패턴을 적용해보았습니다. (각각의 응답을 간단히 처리하기 위함)
   - `RequestMappingInfo` 의 경우 스프링 `@RequestMapping` 을 흉내내보았습니다. (추가적인 url 매핑 정보를 간단하게 설정하기 위함)

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
   - MIME
   - java.net 패키지
   - ServerSocket 클래스
   - InputStream, OutputStream 클래스들

# 구현 2단계
- [x] 요구사항 2: GET 으로 회원가입 기능 구현
   - 유저 회원가입 요청을 처리하기 위한 `UserCreateRequest` 클래스 추가
   - `MyHttpResponse` 에서 response header 값도 추가할 수 있도록 구현
   - 모든 정적 `.html` 파일 또한 추가적인 매핑이 없어도 요청에 응답하도록 변경
   - 400, 500 예외 처리 추가
   - response writeBytes() & flush() 메서드를 `RequeestHandler` 에서 처리하도록 변경

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
   - redirect response

# 구현 2단계 코드리뷰

- MIME 와 static resource 에 대한 고민
- 하나의 클래스에는 하나의 역할만 가지도록 할 것
- 예외 처리에 대한 고민
- 중복되는 I/O 작업에 의한 비용을 캐싱으로 개선
- 파라미터 중복 파싱에 대한 고민

# 구현 3,4단계
- [x] 요구사항 3: POST 로 회원 가입 구현
   - HttpMethod enum 클래스 추가
   - MyHttpRequest 에서 post 요청으로 넘어오는 body 값을 멤버 변수로 추가
   - post 요청에 대한 회원가입 처리
   - UTF-8 decode
- [x] 요구사항 4: Cookie를 이용한 로그인 구현
   - UserLoginRequest dto 추가
   - UserUnauthorizedException 예외 추가
   - MyHttpResponse 멤버 변수 cookies 추가
   - 유저 로그인 구현

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
   1. 실제 url 요청 파라미터의 경우 동일한 key로 여러 값들이 들어올 수 있다.
      - `?role=ADMIN&role=USER&name=pug` 와 같이 여러 개의 `role`을 가지는 경우!
      - 스프링에서도 동일한 key에 대해 여러 값을 받을 수 있도록 `String[]` 값으로 value를 파싱함. ex) `HttpServletRequest.getParameterMap()`
      - 이번 미션에서 기본 제공된 `HttpRequestUtils.parseQueryString()` 을 수정했음
      - 다양한 값을 가진 요청 파라미터를 넘기는 방법에 대한 표준은 없다. [(참고)](https://hugomartins.io/essays/2021/02/how-to-pass-multiple-values-to-http-query-parameter/)
   2. 리다이렉트를 위한 Location 값은 host를 명시해주지 않아도 브라우저에서 현재 host를 따라간다.
      - `HTTP/1.1 302 Found` `Location: /foo/bar` 로 response 헤더 값이 넘어온 경우
      - `http://localhost:8080/foo/bar` 로 리다이렉트 하게 됨.
   3. Set-Cookie 는 반드시 쿠키를 선언한 후, 쿠키에 대한 설정 값들을 추가해주어야 한다. (순서가 중요함)
      - `Set-Cookie: login=true;Path=/` (O)
      - `Set-Cookie: Path=/;login=true` (X)

# 구현 5단계
- [x] 요구사항 5: 동적인 html 구현
   - 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
   - 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
  1. HTTP 에서 request cookie와 response cookie는 성격이 다르다.
     - Request Cookie: 브라우저의 쿠키 값(Cookie: foo=bar)
     - Response Cookie: 쿠키를 생성할 때의 추가 조건들이 붙음(Set-Cookie: foo=bar;Path="/";Expires=???;Secure;)
