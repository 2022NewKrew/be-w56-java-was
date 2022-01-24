# be-w56-java-was
56주차 간단 웹 서버 구현

## 요구사항 1-1 : 정적인 HTML 파일 응답

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

HTML 요청은 위와 같이 구분할 수 있다.
Request Line : 첫 줄은 Http Request 의 첫 라인으로, 
- 요청의 Action 을 정의하는 `HTTP Method` (GET, POST, ...) 
- Request 가 전송될 URI 를 뜻하는 `Request Target`
- `HTTP Version`

이렇게 세 가지의 구성으로 이루어진다.

Headers : Request Line 의 다음 줄부터는 Request 에 대한 Meta 정보를 담고 있고, `Key:Value` 의 형식으로 되어 있다.
- `Host` : 요청이 전송되는 타겟의 Host URL
- `User-Agent` : 요청을 보내는 클라이언트에 대한 정보 (사용하는 웹 브라우저, 버전, OS 등)
- `Accept` : 해당 요청이 받을 수 있는 응답 (Response) 타입
- `Connection` : 해당 요청이 종료된 후, 클라이언트와 서버가 커넥션을 유지할 지 끊을 지에 대해 지시하는 부분 (Keep-alive / cancel)
- `Content-Type` : 해당 요청이 보내는 메세지 Body 타입.
- `Content-Length` : Body 의 길이.

Empty Line : 요청에 대한 Meta 정보가 전송 완료됨을 알린다.

Body : 해당 요청이 보내는 실제 메시지 / 내용이 들어있다.
- XML 이나 JSON 등의 데이터를 입력할 수 있다.
- `GET` 요청은 body 가 대부분 없다.

### 정적 css 파일 로드

`index.html` 을 로드하면, 해당 파일에서 사용하는 css, js 파일을 함께 요청한다.
하지만 코드 상에 Response Header 에 다음과 같이 html 타입의 파일을 응답하는 것으로 하드코딩 되어 있어 읽어온 정적 HTML 파일에 스타일이 적용되지 않는다.
```java
dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
```
위의 코드를 다음과 같이 로드한 파일의 타입에 맞게 Content-Type 을 Response Header 에 지정하면 브라우저에서 파일을 읽어 HTML 에 스타일을 적용할 수 있다.
```java
dos.writeBytes("Content-Type: " + response.getContextType() + ";charset=utf-8\r\n");
```

___
#### 참고자료
- [HTTP Status Code](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)
- [HTTP Message Format](https://velog.io/@rosewwross/Http-and-Request-and-Response-hok6exbnfb)
