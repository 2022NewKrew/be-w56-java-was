# be-w56-java-was

56주차 간단 웹 서버 구현

### 요구사항 1: 정적인 html 파일 응답

> <http://localhost:8080/index.html>로 접속했을 때 webapp 디렉토리의 `index.html` 파일을 읽어 클라이언트에 응답한다.

- HTTP Request Header 예시

```
HTTP
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```
