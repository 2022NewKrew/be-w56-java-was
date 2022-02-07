# be-w56-java-was
56주차 간단 웹 서버 구현

## step1
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.


## step2
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
  이 폼을 통해서 회원가입을 할 수 있다.
- 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.


## step3
- http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.
- 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.

## ETC
- 스프링 과제때 했던 내용과 같은 내용이며, Spring의 유/무 차이의 과제이다.
- 과제의 방향성을 간단한 Spring 프레임워크를 구현하여 이전 과제의 코드를 가져다 쓰는것으로 해볼까 한다.
- 과제의 구조 및 HTTP 학습도 하면서 Spring에 대한 깊은 고찰도 할 수 있을것 같다.
- 의존성 주입 위해 Google 오픈소스의 Reflections와 view를 위해 Mustach 엔진을 쓸 생각이다.

## step4
- “로그인” 메뉴를 클릭하면 http://localhost:8080/user/login.html 으로 이동해 로그인할 수 있다. 로그인이 성공하면 index.html로 이동하고, 로그인이 실패하면 /user/login_failed.html로 이동해야 한다.
- 앞에서 회원가입한 사용자로 로그인할 수 있어야 한다. 로그인이 성공하면 cookie를 활용해 로그인 상태를 유지할 수 있어야 한다. 로그인이 성공할 경우 요청 header의 Cookie header 값이 logined=true, 로그인이 실패하면 Cookie header 값이 logined=false로 전달되어야 한다.
  - **Cookie 설정은 과제에서 제시한것보다는 Random 16byte로 처리하였다.**

## step5
- 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
- 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.
  - **기존 Spring 과제를 마이그레이션 했기때문에 자동으로 처리되었다 StringBuilder는 공부만 했다**

## step6
- 지금까지 구현한 소스 코드는 stylesheet 파일을 지원하지 못하고 있다. Stylesheet 파일을 지원하도록 구현하도록 한다.
  - **resolveStaticView를 구현하여 Dispatcher 처음부분에서 해당 리소스 존재유무를 검증한다.**

## step7
- H2 또는 MySQL 혹은 NoSQL 등 데이터베이스를 활용하여 회원정보를 DB에 저장한다.
  - **DataSource를 이용한 JDBCTemplate를 구현하였다 기존 sql 셋팅은 DataSource를 Bean을 만들때 자동으로 업데이트되게 했다.**
- index.html에 로그인한 사용자가 글을 쓸 수 있는 한 줄 메모장을 구현한다.
  - **기존 Spring 과제 게시판으로 대체**
- 로그인하지 않은 사용자도 게시글을 볼 수 있다.
  - **기존 Spring 과제를 마이그레이션 했기때문에 자동으로 처리되었다.**

## ETC
- 커스텀 스프링 구현을 어느정도 완성하여 이전 Spring 과제를 그대로 포팅하여 동작 가능했습니다.
- Component와 Bean기능을 구현하였습니다.
- Interceptor와 HandlerMethodArgumentResolver기능 구현 하였습니다.
- BasicDataSource를 이용하여 JdbpTemplate를 구현하였습니다.
- RestController 및 ResponseBody와 같이 REST API 기능을 구현하였습니다.
- WebConfig에 DataSource관련 Bean설정을추가 하였습니다. (application.properties에서 세팅하던것을 WebConfig로 옮김)
- Java와 Spring 동작과정에 대해 전반적으로 공부할 수 있었고 기존 Spring 프레임워크가 어떻게 짜여져 있는지도 비교를 할 수 있었습니다.
- 추가로 어노테이션 프로세서를 통해 Java Compile단 까지 간섭할 수 있다는것도 배웠습니다.
- 리팩토링 및 테스트, 예외처리 Http keep-alive기능등 더 할것들이 있지만 일단 다음 챕터인 서버 배포, 로그 수집, 스트레스 테스트, GC 모니터링 먼저 진행하고자 합니다.