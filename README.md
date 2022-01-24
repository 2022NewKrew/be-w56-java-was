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
