# be-w56-java-was
56주차 간단 웹 서버 구현

## 요구사항 1: 정적인 html 파일 응답
> http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

```html
<!-- Request Line -->
GET /index.html HTTP/1.1
<!-- Headers -->
Host: localhost:8080
Connection: keep-alive
Accept: */*
<!-- Empty Line -->
<!-- Body -->
```

## Step1 을 구현하며 새로 알게 된 내용
Connection 요청 헤더는 해당 요청이 종료된 이후에 커넥션을 계속 유지할지 설정할 수 있다.
(Keep-alive / cancel)

Accept 요청 헤더는 클라이언트가 이해 가능한 컨텐츠 타입이 무엇인지 알려준다.  
Response Header 작성시, Content-Type에 클라이언트가 읽을 수 있는 타입을 적어주어야 
css, js 파일이 적용된다.

응답 헤더의 첫번째 줄을 Status-Line 이라고 부른다.  
Status-Line의 마니막 이유 구문을 Reason-Phrase 라고 부른다.
