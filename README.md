# be-w56-java-was
56주차 간단 웹 서버 구현
## 1단계 ([링크](https://lucas.codesquad.kr/2022-kakao/course/%EC%9B%B9%EB%B0%B1%EC%97%94%EB%93%9C/Java-Web-Server/%EC%9B%B9-%EC%84%9C%EB%B2%84-%EA%B5%AC%ED%98%84-1%EB%8B%A8%EA%B3%84))
### 프로젝트 목표
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.
### 요구사항 1: 정적인 HTML 파일 응답
> http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
#### HTTP Request Header 예
```
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```
### 상세 구현사항
- Thread 기반으로 소켓 연결 구현
  - `ConnectionHandler`에서 `Thread` 상속, 새로운 소켓을 받아 요청 및 응답을 모두 처리
- 요청 및 응답을 처리하기 위한 Handler 구현
  - `RequestHandler`: 요청 헤더의 각 줄을 읽어들여 파싱
  - `ResponseHandler`: `RequestHandler`를 입력받아 알맞는 응답 헤더 및 몸체를 만들어서 반환
    - MIME: Apache Tika 활용 
    - HTTP 상태 코드: Apache HttpClient의 HttpStatus 인터페이스의 상수 값 활용
- 사용할 Util들을 직접 정의
  - `CommaSeries`: 요청 헤더의 내용 중 `,`로 구분이 되는 값을 갖는 정보를 담을 자료형
  - `Constants`: 상수 관리, 현재는 Context Path를 담아놓음
  - `ContentType`: 요청 헤더에서 Content-Type의 정보를 담을 자료형
  - `HttpRequestUtils`, `IOUtils`: 기존에 제공된 Utils, 파싱 관련 내용 포함