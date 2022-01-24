# be-w56-java-was
56주차 간단 웹 서버 구현

## Step 1

### 요구사항

- [X]  정적인 html 파일 응답
    - [X]  request header 출력
    - [X]  request line에서 path 분리
    - [X]  path에 해당하는 파일 읽어 응답

### 구현 내용

- [X] Controller 인터페이스 선언
- [X] ControllerMapping이 url에 매칭되는 Controller 반환하도록 구현
- [X] Request Header 파싱하는 것을 RequestParser가 담당
- [X] RequestHandler가 RequestParser와 ControllerMapping에서 받은 Controller를 이용해 사용자 요청을 처리하도록 구현
