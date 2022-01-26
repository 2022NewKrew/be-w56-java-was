# be-w56-java-was
56주차 간단 웹 서버 구현

## 요구사항 1-1 : 정적인 HTML 파일 응답

```
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

___

## 요구사항 1-2 : GET 으로 회원가입 기능 구현

```
GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```

## 요구사항 1-3 : POST 로 회원가입 기능 구현

```
POST /user/create HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Content-Length: 59
Content-Type: application/x-www-form-urlencoded
Accept: */*

userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net
```

요구사항 `1-2` 와 `1-3` 은 Http 요청 Method 가 GET 이냐? POST 이냐? 의 차이 (와 더불어 회원 가입을 위한 정보가 URI Query String 으로 전달되는지, Request Body 의 차이로 전달되는지) 만 있다 생각해 함께 구현했다. \
처음에는 먼저 `1-2` 를 구현한 뒤 `1-3` 을 진행하려 했으나, 전체적인 설계를 고민하느라 시간을 많이 소비해 한 번에 두 단계를 진행하게 됐다.

프로젝트는 다음과 같이 구성했다. \
`RequestHandler` 는 클라이언트의 요청 정보를 읽고, 해당 정보를 `ControllerMapper` 에 전달한다. \
`ControllerMapper` 는 클라이언트의 요청을 타겟 URI 를 기준으로 적절한 `Controller` 에 매핑한다. \
`Controller` 는 요청의 URI 와 Method 를 읽어 컨트롤러 내부에 정의된 적절한 메소드를 호출한다. \
URI 에 대한 적절한 `Controller` 가 존재하지 않거나, `Controller` 에서 적절한 처리가 이루어지지 않은 경우 `StaticFileReader` 에 요청을 전달해 정적 파일을 응답하도록 한다.
