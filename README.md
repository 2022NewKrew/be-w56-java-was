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

## Step 5

### 요구사항
* 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
* 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.

#### 동적인 html 생성 힌트
* 자바 클래스 중 StringBuilder를 활용해 사용자 목록을 출력하는 html을 동적으로 생성한 후 응답으로 보낸다.
* 구글에서 “java stringbuilder example”로 검색해 StringBuilder 사용법을 찾는다.

### 새롭게 알게된 내용
* URLDecoder를 통해 URL의 한글, 특수문자 등을 decode할 수 있는 것을 알게 됐다.
  * HttpRequestBody의 내용도 같은 방식으로 decode할 수 있는 것을 알게 됐다.
* TemplateEngine이 reflection을 통해 구현된 것을 알게 됐다.
* Interface의 default method에 대해서 알게 됐다.

## Step 7

### 요구사항
* H2 또는 MySQL 혹은 NoSQL 등 데이터베이스를 활용하여 회원정보를 DB에 저장한다.
* index.html에 로그인한 사용자가 글을 쓸 수 있는 한 줄 메모장을 구현한다.
* 로그인하지 않은 사용자도 게시글을 볼 수 있다.

### 새롭게 알게된 내용
* Singleton 패턴에 대해서 더 알게 됐다.
  * LazyHolder를 이용한 Singleton 패턴 구현에 대해 알게 됐다.
  * reference : https://injae-kim.github.io/dev/2020/08/06/singleton-pattern-usage.html
