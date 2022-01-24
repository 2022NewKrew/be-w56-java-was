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

# 새롭게 알게 된 내용 & 구현 중 학습하게 된 내용
- MIME
- java.net 패키지
- ServerSocket 클래스
- InputStream, OutputStream 클래스들
