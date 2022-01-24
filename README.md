# be-w56-java-was
56주차 간단 웹 서버 구현

## 1단계

### 브라우저에서 url 입력 후 동작 (DNS, ARP 제외)
- http://127.0.0.1:8080/index.html 을 브라우저 주소창에 입력
- 클라이언트와 서버간 TCP 소켓 연결
- 브라우저가 HTTP request 메시지를 만들어서 서버로 전송
- 서버가 요청을 확인해서 적절한 리소스를 HTTP response 메시지로 만들어서 반환
- 브라우저가 받은 응답을 해석해서 표시
- 이때 index.html 파일 내에서 참조하는 .js .css 파일 등에 대한 GET 요청이 전송됨
- 서버가 응답을 보낼 때는 html 파일인지 css 파일인지 등에 따라 content-type을 적절하게 명시해야, 브라우저가 응답을 해석하여 화면에 표시할 수 있다.

### HTTP 1.0 과 1.1 차이
- HTTP 1.0은 non-persistent 지만, HTTP 1.1은 TCP 연결은 한 번 맺고 이를 유지해서 이후 요청 및 응답에도 사용한다.
- HTTP 1.1에는 pipelining 기능이 있다. 
 브라우저가 응답으로 index.html을 받은 뒤, 추가 요소(css, js)들을 한번에 요청했을 때 
 요청을 순서대로 처리하는게 아니라 pipelining으로 처리하는 것을 로그를 통해 볼 수 있다.

