# be-w56-java-was
56주차 간단 웹 서버 구현

## Step 1

### 요구사항
* http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### 새롭게 알게된 내용
* Http Request의 첫번째 라인은 Request-Line이라고 하는 것을 알게 됐다.
  * ex) GET /index.html HTTP/1.1
* Spring을 사용하지 않고 Web Server를 구현하는 방법을 알게 됐다.
* ServerSocket의 accept()를 이용하면 서버의 포트를 열고 응답이 올 때까지 대기할 수 있다는 점을 알게 됐다.

## Step 4

### 요구사항
* “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다. 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
* 앞에서 회원가입한 사용자로 로그인할 수 있어야 한다. 로그인이 성공하면 cookie를 활용해 로그인 상태를 유지할 수 있어야 한다. 로그인이 성공할 경우 요청 header의 Cookie header 값이 logined=true, 로그인이 실패하면 Cookie header 값이 logined=false로 전달되어야 한다.

### 새롭게 알게된 내용
* Header에 Set-Cookie를 이용해 쿠키를 설정할 수 있다는 것을 알게됐다.
* Spring Controller가 리플렉션을 이용해 구현한 것을 알게 됐다.
  * 다음 미션이 진행되면 어노테이션과 리플렉션을 이용해 구조를 변경할 수 있을 것 같다.
* 전략 패턴에 대해 알게 됐다.
