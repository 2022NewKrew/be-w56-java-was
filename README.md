# be-w56-java-was
56주차 간단 웹 서버 구현

# be-w56-java-was - 56주차 간단 웹 서버 구현
## 프로젝트 목표
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.

## 웹 서버 구현 
### 요구사항
- 정적인 html 파일 응답
    - ex> http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
- “회원가입” 메뉴를 클릭하면 http://localhost:8080/user/form.html 으로 이동하면서 회원가입 폼을 표시한다.
  - 이 폼을 통해서 회원가입을 할 수 있다.
- POST로 회원가입 구현
  - http://localhost:8080/user/form.html 파일의 form 태그 method를 get에서 post로 수정한 후 회원가입 기능이 정상적으로 동작하도록 구현한다.
  - 가입 후 페이지 이동을 위해 redirection 기능을 구현한다.
- Cookie를 이용한 로그인 기능 구현
- 동적인 html 구현
    - 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
      만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.
- - 다양한 mimetype에 대응하여 response 보내주기. ex> css 요청은 text/css로 보내주기

## 구현 내용
### WebServer - NIO
- SocketChannel, Selector를 사용하여 nio webserver 구현
- KeepAlive 설정
- ThreadPool을 통한 비즈니스 로직 수행

### HTTP

- HttpRequest, HttpResponse 객체로 요청과 응답 관리

### MVC framework

- Spring MVC 구조를 적극 참조
- FrontController가 DispatcherServlet 역할을 함
- ServerConfig 
  - Controller 등록 : @RequestMapping으로 Method를 찾고 RequestMappingHandler에 HandlerMethod 등록
  - Handler 등록 : HandlerMapping에 등록
- Beans
  - application 내 의존 관계 주입
- HandlerMapping에서 각 Handler의 isSupport()를 호출하여 Mapping
  - 현재 ResourceRequestHandler(정적 리소스 요청), RequestMappingHandler(등록된 Controller) 두 가지 Handler 등록
  - Handler는 ModelAndView를 반환함
- ViewResolver에서 viewName을 기반으로 적절한 View를 반환
  - View는 render() 함수로 Response Body를 생성
  - 현재 View는 RedirectView, StaticView, TemplateView 등록
- TemplateEngine은 객체, 루프 참조 가능
  - 기능 한계: 중첩으로 객체, 루프 참조 불가
  - mustash 문법 참조
  - ```{{key}}``` : 모델에서 key 값 매핑
  - ```{{#model 속 객체}}...{{객체 필드}}...{{/객체}}``` : 모델 속 객체에서 reflection으로 값을 가져옴 
  - ```{{#List인 model 속 객체}}``` : 반복문으로 html 생성

    