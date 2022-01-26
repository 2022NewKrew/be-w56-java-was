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
