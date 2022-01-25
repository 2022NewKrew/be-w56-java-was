# be-w56-java-was
56주차 간단 웹 서버 구현

### 구조
```
WebServer
    WebContainer
        WebService
        RequestHandler
        RequestParser
    HttpRequest
    HttpResponse
```

### 설명
WebContainer 가 클라이언트 요청을 받아 </br>
RequestHandler 스레드를 통해 Parser로 요청 분석 </br>
Service 에 미리 정의된 메소드 따라 응답
### 기능
Get 요청
