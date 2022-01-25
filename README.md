# be-w56-java-was
56주차 간단 웹 서버 구현

### Request 구조
- [HTTP/1.1: Request - w3](https://www.w3.org/Protocols/rfc2616/rfc2616-sec5.html)
- [HTTP 헤더 - mozilla](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers)
- ```text
  Request = Request-Line
            ((general-header | request-header | entity-header) CRLF)
            CRLF
            [ message-body ]
  
  Request-Line = Method SP Request-URI SP HTTP-VERSION CRLF
  ```
  
### 정적 팩토리 메소드 
- `이펙티브 자바 Effective Java 3/E`
- from: 매개변수 하나 -> 해당 타입 인스턴스
- of: 매개변수 여러개 -> 적합한 타입 인스턴스
- valueOf: from과 of의 더 자세한 버전(?)
- instance | getInstance: 보통 Singleton 패턴에서 사용. 다른 인스턴스일 수도 있음
- create | newInstance: 새로운 인스턴스 생성, 반환 및 보장
- get*Type*: *Type*에 해당하는 클래스를 반환
- new*Type*: *Type*에 해당하는 클래스를 새로 생성해서 반환
- *type*: get*Type*과 new*Type*의 간결한 버전

### Java annotation & reflection
- [Java Annotation: 인터페이스 강요로부터 자유를... - nextree](https://www.nextree.co.kr/p5864/)
- [Java - Reflection 쉽고 빠르게 이해하기 - codechacha](https://codechacha.com/ko/reflection/)
- [Guide to Java Reflection - baeldung](https://www.baeldung.com/java-reflection)
- [Finding All Classes in a Java Package - baeldung](https://www.baeldung.com/java-find-all-classes-in-package)
- Annotation을 이용하여 비즈니스 로직에는 영향을 주지 않지만 타겟의 연결이나 코드 구조 변경 가능
- Reflection을 통해 class, interface, fields, methods의 런타임 속성들을 검사하거나 변경할 수 있음
- Reflection을 통해 annotation 속성을 검사하고, url을 연결해줄 수 있음

### Spring Container
- [IoC(Inversion of Control, 제어의 역전) / DI(Dependency Injection, 의존관계 주입) - tistory](https://beststar-1.tistory.com/33)
- [스프링 컨테이너(BeanFactory, ApplicationContext) - tistory](https://beststar-1.tistory.com/39)
- IoC: 오프젝트의 제어권을 프레임워크(스프링)에게 위임하는 것, Bean들의 lifecycle과 실행 등을 관리
- DI: 메모리에 올라가있는 Bean 객체들을 받아서 실행할 수 있도록 하는 것
- Singleton Registry: 스프링에서는 만들어진 Singleton을 Registry에서 관리

### Issues
- Request에 body가 있을 때 오류
  - BufferedReader를 readLine으로 읽으면 body의 마지막이 개행문자가 아니므로 무한대기
  - BufferedReader의 read 메소드를 통해 header의 Content-Length만큼 읽어야 됨
