# be-w56-java-was
5, 6주차 간단 웹 서버 구현


## HTTP Message 구조

### 공식 스펙  
>start-line  
*( header-field CRLF )  
CRLF  
[ message-body ]
> - CRLF : Carriage Return Line Feed = End Of Line(EOL) 개행
>   - Carriage Return : 현재 줄 커서 위치를 맨 앞으로 이동
>   - Line Feed : 현재 커서의 위치에서 하나의 줄을 아래로 이동

## start-line
### request-line (Request Message)
>method SP request-target SP HTTP-version CLRF  
> - method : 서버가 수행할 동작(GET, POST, PUT ...)
> - request-target : absolute-path[?query]
> - HTTP-version
> - SP : US-ASCII SP, space 공백
### status-line (Response Message)
>HTTP-version SP status-code SP reason-phase CLRF
> - HTTP-version
> - status-code : 요청 성공, 실패
> - reason-phase : 사람이 이해할 수 있는 상태 코드 설명 글

## header-field
- 전송에 필요한 모든 부가 정보
- 표준 헤더 외에 임의 헤더 추가 가능
>field-name ":" OWS field-value OWS  
> - field-name : 대소문자 구분 없음
> - OWS : 띄어쓰기 허용

## message-body
- 실제 전송할 데이터
- byte로 표현할 수 있는 모든 데이터 전송 가능


## WAS
참고자료
- https://gmlwjd9405.github.io/2018/10/28/servlet.html
- https://milancode.tistory.com/20?category=923476

## Spring MVC 동작원리
- https://starkying.tistory.com/entry/Spring-MVC-%EB%8F%99%EC%9E%91%EC%9B%90%EB%A6%AC-%EA%B5%AC%EC%84%B1%EC%9A%94%EC%86%8C

## Servlet
- https://riimy.tistory.com/87
- https://taes-k.github.io/2020/02/16/servlet-container-spring-container/
- https://gmlwjd9405.github.io/2018/10/28/servlet.html

## DispatcherServlet
- https://howtodoinjava.com/spring5/webmvc/spring-dispatcherservlet-tutorial/

## HandlerMapping, HandlerAdapter, HandlerInterceptor
- https://joont92.github.io/spring/HandlerMapping-HandlerAdapter-HandlerInterceptor/
- https://joont92.github.io/spring/@RequestMapping/
- https://www.baeldung.com/spring-handler-mappings

## Reflection
- https://tecoble.techcourse.co.kr/post/2020-07-16-reflection-api/
- https://www.baeldung.com/java-reflection
- https://www.baeldung.com/java-method-reflection

## Class Loading
- https://velog.io/@agugu95/%EC%9E%90%EB%B0%94-%ED%81%B4%EB%9E%98%EC%8A%A4-%EB%A1%9C%EB%94%A9%EA%B3%BC-%EC%86%8D%EB%8F%84-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%EA%B8%B0%EB%B2%95%EB%93%A4
- https://baeldung-cn.com/java-list-classes-class-loader
- https://www.baeldung.com/java-find-all-classes-in-package

# @AliasFor


# 요구사항
## 구현 요구사항
### 1단계 : 정적 html 파일 응답
- [x] request 메시지를 받아서 파싱한다.
    - [x] 첫 번째 줄은 request-line 이다.
    - [x] 2째줄 ~ CRLF 전까지는 header-field 이다.
    - [x] 일반적으로 body는 없다.
- [x] response 메시지를 만들어 보낸다.
    - [x] request-line의 path에 맞는 파일을 찾는다.
    - [x] 파일이 있다면 response-line의 status-code를 200으로 reason-phase를 OK로 설정한다.
        - [x] html 파일을 byte로 변환해서 body에 담는다.
    - [x] 파일이 없다면 response-line의 status-code를 404로 reason-phase를 NotFound로 설정한다.
    - [ ] response header의 accept를 참고해서 header의 content-type을 세팅한다.
    - [x] header에 content-length를 추가한다.

### 2단계 : GET으로 회원가입 기능 구현
- [x] request 메시지를 받아 파싱한다
    - [x] request-target은 path와 query로 나뉜다.
- [ ] 정적 리소스 형식이면
    - [x] 정적 리소스가 있다면 반환한다.
    - [x] 정적 리소스가 없으면 404로 반환 한다.
- [ ] 정적 리소스 형식이 아니면 서블릿 컨테이너로 넘겨서 서블릿을 통해 작업을 수행한다.
    - [x] 서블릿 컨테이너가 request를 보고 실행할 서블릿(컨테이너)을 찾는다.
    - [x] 서블릿에서 해당하는 동작을 실행하는 서비스를 수행한다.
    - [x] 실행할 서블릿이 없으면 404 반환한다.
- [x] 요청 받은 결과를 response body에 담아 반환한다.

### 3단계 : POST로 회원 가입 구현

- [x] request 메시지를 받아 파싱한다.
  - [x] post로 데이터를 전달할 경우 전달하는 데이터는 HTTP Body에 담긴다.
  - [x] get으로 받을 때와 동일한 형식이다.
  - [x] 읽을 때는 Content-Length 만큼 읽어야 한다.
- [x] "redirect:/"를 통해 index.html로 리다이렉트 한다.
  - [x] Header의 Location에 redirect할 url 추가

### 4단계 : Cookie를 이용한 로그인 구현

- [x] 회원가입한 데이터 유지하기
- [x] 아이디와 비밀번호가 같은지 확인한다.
- [x] 로그인이 성공할 경우 Set-Cookie 값을 logined=true로 설정한다.
- [x] 로그인이 실패할 경우 Set-Cookie 값을 logined=false로 설정한다.
- [x] Set-Cookie 설정시 모든 요청에 대해 Cookie 처리가 가능하도록 Path 설정 값을 /(Path=/)로 설정한다.

### 5단계 : 동적인 html 구현

- [x] 쿠키 생성하기
  - [x] 쿠키가 생성되면 request 헤더를 파싱해서 쿠키를 만든다.
  - [x] 컨트롤러에서 쿠키를 사용한다면 헤더로 만든 쿠키를 전해준다.
  - [x] 컨트롤러에서 쿠키를 생성해서 response에 추가할 수 있다.
- [x] html 동적 생성하기
  - [x] 컨트롤러의 Model에 전체 사용자 목록을 넣는다.
  - [x] html을 StringBuilder로 읽어서 model객체의 parameter와 비교하면서 넣는다.
- Filter 구현하기
  - 서블릿 실행 전 필터에서 요청 url과 Cookie를 비교한다.(필터에서 확인할 url을 설정한다.)
    - [x] “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 사용자 목록을 출력한다.
    - [x] 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.

## 도메인 요구사항

### message

- [x] request, response 으로 나뉜다.
- [x] start-line, header-field, empty-line, message-body로 구성되어있다.
- [x] start-line, header-field는 필수 이다.
- [x] message-body는 null 일 수 있다.

### start line

- [x] request인 경우 request-line
  - [x] method, request-target, http-version으로 이루어져 있다.
  - [x] 모두 null일 수 없다.
- [x] response인 경우 status-line
  - [x] http-version, status-code, reason-phase로 이루어져있다.
  - [x] 모두 null일 수 없다.

### request-target
- [x] path와 query로 나뉜다.
- [x] path는 반드시 존재 한다.
- [x] path가 "/"이면 "/index.html"을 자동으로 반환한다. -> 서블릿 컨트롤러로 이동해야할듯?
- [x] file path는 ".app/index.html" 형식이다.
- [x] query는 null일 수 있다.

### header
- [x] header는 key(field-name)-value(field-value) 모음이다.

### body
- [x] 반환할 파일을 date type으로 가진다.
- [x] null 일 수 있다.

## 고민 / 수정 사항

- 왜 서블릿은 싱글톤이여야 할까?
- 지금은 웹서버에서 스레드를 만드는데 보통 웹 컨테이너(서블릿 컨테이너)에서 만든다고 하는데 이게 이거가 맞을까...?
- 테스트 코드 작성하기
- 예외처리 한 곳에서 할 수 있도록 하기
- 컨테이너 자동으로 찾아서 서블릿 만들 수 있도록 하기

## 찾아 보기
- guava
- tika
- reflection
- thread pool로 구현하기