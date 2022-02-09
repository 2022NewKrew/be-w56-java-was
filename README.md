# be-w56-java-was
56주차 간단 웹 서버 구현

# 프로젝트 목표
- HTTP 를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.

# 구현 1단계
- [x] 요구사항 1: 정적인 html 파일 응답
   - enum 클래스 `HttpStatus`, `MIME`, `RequestMappingInfo` 추가
   - request, response 정보들을 담는 `MyHttpRequest`, `MyHttpResponse` 클래스를 추가
   - `MyHttpResponse` 의 경우 빌더 패턴을 적용해보았습니다. (각각의 응답을 간단히 처리하기 위함)
   - `RequestMappingInfo` 의 경우 스프링 `@RequestMapping` 을 흉내내보았습니다. (추가적인 url 매핑 정보를 간단하게 설정하기 위함)

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
   - MIME
   - java.net 패키지
   - ServerSocket 클래스
   - InputStream, OutputStream 클래스들

# 구현 2단계
- [x] 요구사항 2: GET 으로 회원가입 기능 구현
   - 유저 회원가입 요청을 처리하기 위한 `UserCreateRequest` 클래스 추가
   - `MyHttpResponse` 에서 response header 값도 추가할 수 있도록 구현
   - 모든 정적 `.html` 파일 또한 추가적인 매핑이 없어도 요청에 응답하도록 변경
   - 400, 500 예외 처리 추가
   - response writeBytes() & flush() 메서드를 `RequeestHandler` 에서 처리하도록 변경

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
   - redirect response

# 구현 2단계 코드리뷰

- MIME 와 static resource 에 대한 고민
- 하나의 클래스에는 하나의 역할만 가지도록 할 것
- 예외 처리에 대한 고민
- 중복되는 I/O 작업에 의한 비용을 캐싱으로 개선
- 파라미터 중복 파싱에 대한 고민

# 구현 3,4단계
- [x] 요구사항 3: POST 로 회원 가입 구현
   - HttpMethod enum 클래스 추가
   - MyHttpRequest 에서 post 요청으로 넘어오는 body 값을 멤버 변수로 추가
   - post 요청에 대한 회원가입 처리
   - UTF-8 decode
- [x] 요구사항 4: Cookie를 이용한 로그인 구현
   - UserLoginRequest dto 추가
   - UserUnauthorizedException 예외 추가
   - MyHttpResponse 멤버 변수 cookies 추가
   - 유저 로그인 구현

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
   1. 실제 url 요청 파라미터의 경우 동일한 key로 여러 값들이 들어올 수 있다.
      - `?role=ADMIN&role=USER&name=pug` 와 같이 여러 개의 `role`을 가지는 경우!
      - 스프링에서도 동일한 key에 대해 여러 값을 받을 수 있도록 `String[]` 값으로 value를 파싱함. ex) `HttpServletRequest.getParameterMap()`
      - 이번 미션에서 기본 제공된 `HttpRequestUtils.parseQueryString()` 을 수정했음
      - 다양한 값을 가진 요청 파라미터를 넘기는 방법에 대한 표준은 없다. [(참고)](https://hugomartins.io/essays/2021/02/how-to-pass-multiple-values-to-http-query-parameter/)
   2. 리다이렉트를 위한 Location 값은 host를 명시해주지 않아도 브라우저에서 현재 host를 따라간다.
      - `HTTP/1.1 302 Found` `Location: /foo/bar` 로 response 헤더 값이 넘어온 경우
      - `http://localhost:8080/foo/bar` 로 리다이렉트 하게 됨.
   3. Set-Cookie 는 반드시 쿠키를 선언한 후, 쿠키에 대한 설정 값들을 추가해주어야 한다. (순서가 중요함)
      - `Set-Cookie: login=true;Path=/` (O)
      - `Set-Cookie: Path=/;login=true` (X)

# 구현 5단계
- [x] 요구사항 5: 동적인 html 구현
   - 접근하고 있는 사용자가 “로그인” 상태일 경우(Cookie 값이 logined=true) 경우 http://localhost:8080/user/list 에서 사용자 목록을 출력한다.
   - 만약 로그인하지 않은 상태라면 로그인 페이지(login.html)로 이동한다.

- __새롭게 알게 된 내용 & 구현 중 학습하게 된 내용__
  1. HTTP 에서 request cookie와 response cookie는 성격이 다르다.
     - Request Cookie: 브라우저의 쿠키 값(Cookie: foo=bar)
     - Response Cookie: 쿠키를 생성할 때의 추가 조건들이 붙음(Set-Cookie: foo=bar;Path="/";Expires=???;Secure;)

# 구현 6단계
- 이전 단계에서 이미 구현했으므로 생략

# 구현 7단계
- [x] 요구사항 7: 데이터베이스 연동 및 한줄 메모장 구현
   - H2 또는 MySQL 혹은 NoSQL 등 데이터베이스를 활용하여 회원정보를 DB에 저장한다.
   - index.html에 로그인한 사용자가 글을 쓸 수 있는 한 줄 메모장을 구현한다.
   - 로그인하지 않은 사용자도 게시글을 볼 수 있다.

# HTTP 리팩터링
- Cookie 클래스 추가, HttpCookie 일급 컬렉션 구현
- DataOutputStream 객체 처리 리팩터링
- ResponseHandler 클래스 추가
- HttpVersion enum 추가
- Cookie, HttpRequest, HttpResponse 테스트 케이스 추가

### 스트링의 + 연산은 컴파일러에서 최적화 해준다.(JDK 버전마다 다르다!)

#### 스트링 + 연산의 역컴파일 결과

* 원래 소스
```java
public class StringTest {  
    public static void main(String[] args) {  
        String str0 = "It's a string....";  
        String str1 = "It's" + " a string" + "....";  
        String str2 = "It's a string...." + str0 + "000";  
        str2 = str0 + str1 + "1111" ;        
        str2 = str2 + "1111";  
        str2 += "1111";        
        for (int i=0;i<10;i++){  
            str2 = str2 + "1111";  
            str2 += "1111";        
        }  
    }  
}
```

* JDK 1.4
```java
public class StringTest{
 
    public StringTest()    {  
    }
 
    public static void main(String args[])    {  
        String str0 = "It's a string....";  
        String str1 = "It's a string....";  
        String str2 = "It's a string...." + str0 + "000";  
        str2 = str0 + str1 + "1111";  
        str2 = str2 + "1111";  
        str2 = str2 + "1111";  
        for(int i = 0; i < 10; i++)        {  
            str2 = str2 + "1111";  
            str2 = str2 + "1111";  
        }

    }  
} 
```

* JDK 1.5
```java
public class StringTest{  
    public StringTest()    {  
    }

    public static void main(String args[])    {  
        String str0 = "It's a string....";  
        String str1 = "It's a string....";  
        String str2 = (new StringBuilder("It's a string....")).append(str0).append("000").toString();  
        str2 = (new StringBuilder(String.valueOf(str0))).append(str1).append("1111").toString();  
        str2 = (new StringBuilder(String.valueOf(str2))).append("1111").toString();  
        str2 = (new StringBuilder(String.valueOf(str2))).append("1111").toString();  
        for(int i = 0; i < 10; i++)        {  
            str2 = (new StringBuilder(String.valueOf(str2))).append("1111").toString();  
            str2 = (new StringBuilder(String.valueOf(str2))).append("1111").toString();  
        }  
    }  
}
```

> JDK 1.5 버전 부터 StringBuilder로 자동 치환된다.    
> 하지만 new 키워드를 통해 계속해서 매 라인마다 StringBuilder 객체가 생성되므로 메모리에 비효율적이며 GC로 인해 성능이 저하될 수 있다!

* JDK 9

> JDK 9 버전 부터는 스트링 + 연산에서 StringBuilder 객체를 생성하지 않고, StringConcatFactory 의 메서드들을 호출한다.   
> 따라서, JDK 8 에 비해 엄청난 최적화가 되었다.
>    
> 관련 글   
> [https://dzone.com/articles/jdk-9jep-280-string-concatenations-will-never-be-t](https://dzone.com/articles/jdk-9jep-280-string-concatenations-will-never-be-t)   
> [https://medium.com/javarevisited/5-effective-string-practices-you-should-know-e9a75811b123](https://medium.com/javarevisited/5-effective-string-practices-you-should-know-e9a75811b123)   

# 구현 7단계 리팩터링

* 자바 Socket 으로부터 생성 된 InputStream, OutputStream을 close 하면 소켓도 함께 닫힌다.

> * Socket.getInputStream()의 javadoc   
> Closing the returned InputStream will close the associated socket.
>    
>    
> * Socket.getOutputStream()의 javadoc   
> Closing the returned OutputStream will close the associated socket.
