# be-w56-java-was
56주차 간단 웹 서버 구현



## 프로젝트 목표

- HTTP 를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.



## 구현 내용

- WebServer 가 클라이언트의 연결을 기다리다가, 연결이 되면 RequestHandler 인스턴스를 생성해서 start 한다.
- requestHandler 는 클라이언트로부터의 InputStream 으로 HttpRequest 인스턴스를 생성하고, 클라이언트로의 OutputStream 으로 HttpResponse 를 생선한다.
- HttpRequest 에 url 이 존재하면, RequestMapper 를 통해 적당한 Controller 를 찾아서 응답을 처리한다.



- 처리하고 싶은 URL 을 추가하기 위해서는  Controller 인터페이스의 구현체를 만들고, RequestMapper 에 등록한다.



## 새롭게 알게 된 내용

### step1

- `InputStream` 은 10진수의 UTF-16 값으로 int 형으로 저장되며, 1 byte 씩 읽을 수 있다...
  - 아스키코드 값을 출력한다.
- `InputStreamReader` 는 byte 단위의 InputStream 을 문자 단위 데이터로 변환하는 중개자 역할을 한다.
  - `new InputStreamReader(in, StandardCharsets.UTF-8))` 이 `UTF-8` 형식으로 `in` 이라는 InputStream 을 읽게 하는 것이다.
  - 아스키코드 값을 문자열로 바꿔준다.
  -  char 배열로 데이터를 받을 수 있다.
- `BufferedReader` 는 버퍼를 통해 입력받은 문자를 쌓아둔 뒤 한 번에 문자열처럼 처리할 수 있다...
  - `InputStreamReader` 는 문자열이 아닌 문자를 처리하는 것일 뿐이다.
  - `line()` 을 통해 한 줄 씩 읽을 수 있다.



- 잘못된 `Content-Type` 을 헤더에 넣어주면 브라우저에서 css, js 파일 적용이 제대로 안 된다.
- `tika` 라는 Apache 라이브러리를 통해 파일의 MimeType 을 확인할 수 있다.
- `Files.probeContentType(filePath)` 로 확장자를 이용해 MimeType 을 확인할 수도 있는데, 파일명에 `.` 이 있다거나 하는 등의 이유로 MimeType 을 확인하지 못하면 `null` 을 반환한다.  



- http 의 header 와 body 사이에는 공백이 하나 들어있다.

  

### step2

- 브라우저가 요청을 보냈는데 서버로부터 응답이 오지 않으면 브라우저는 계속해서 동일한 요청을 보낸다.

  

### step3

- `bufferedReader.readLine()` 은 클라이언트가 socket output stream 을 close 할 때 까지 읽는다.
- Body 는 `bufferedReader.read()` 로 Content-Length 만큼만 읽어야 한다.
- HTTP/1.1 은 default 로 HTTP keep-alive 설정을 갖고 있어서 서버는 클라이언트로부터 더 많은 요청을 수용하기 위해 응답 후에도 TCP connection 을 열어둔다.





## 하다가 생긴 고민

### step1

- [x] 처음에 받은 코드대로 `Content-Type` 이 `text/html` 로 통일되어 있었을 때는 css 나 js 파일이 브라우저에서 적용이 안 되는 것 같았다. 그래서 해당 line 을 주석처리했더니 css 적용이 되었다. 그렇다면 response 헤더에 Content 종류에 따라 `Content-Type` 을 만들어주는 메서드가 필요한가? 아니면 그냥 `Content-Type` 을 비워두면 되는가?

- [x] `Files.probeContentType(filePath)` 로 mimeType 을 찾아주었는데, `bootstrap.min.css` 나 `jquery-2.2.0.min.js`  같은 파일의 mimeType 이 `null` 로 반환된다. 어떻게 해야할까?

  - Tika 라는 라이브러리를 써서 mimeType 을 알아내어서 Response에 담아주는 것으로 바꿨다.

  

### step2

- [x] RequestHandler 가 만들어주던 Response body 를 Controller 가 만들어주는 것으로 수정했다. 그래도 되는 것일까?
  - HttpResponse 객체를 만들어서 해당 객체가 response 를 만들도록 수정했다.
- [x] Controller 에서 annotation 으로 RequestMapping 을 하지 않아 거대한 switch 문을 가진 makeResponse 메소드가 만들어졌다. annotation 을 만들어 봐야할까?
  - Controller 인터페이스를 만들고, 각 url 에 Controller 구현체를 매핑해둔 RequestMapper 를 만들어 switch 문을 없앴다.



### step3 

- [x] 브라우저에서 요청을 보낼 때는 괜찮은데, postman 으로 요청을 보내면 url 을 split 하는 부분에서 계속 NPE 가 발생한다.
  - postman 으로 요청 보낼 때 connection 이 두 개가 이뤄지는데, 하나는 url 부분이 없는 요청이다. url 이 null 일 때를 처리해주니 NPE 가 사라졌다.
