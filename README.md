# be-w56-java-was
- 56주차 간단 웹 서버 구현

<br>

## Step 7. 데이터베이스 연동 및 한줄 메모장 구현

> Step7에서는 DataBase를 로컬에 있는 HashMap에서 Mysql DB로 전환하고, 
> index.html에 로그인한 사용자가 글을 쓸 수 있는 한 줄 메모장을 구현하였습니다. 
- DataBase MySql로 변경
  - JDBC Api를 활용하여 krane에 있는 Mysql DataBase 사용
  - 데이터베이스를 변경함에 따라, 각 Model에 맞는 Repository를 생성하고, 해당 Repository에서 쿼리문을 실행
  - Intellij의 기능을 활용하여 Database 내부에 table 생성
- Model `Article` 추가
  - `index.html`에 나타나는 한줄 메모장을 보여주기 위한 도메인
  - 새로운 모델이 추가됨에 따라, 부수적인 기능 추가
    - `ArticleController`
    - `ArticleService`
    - `ArticleRepository`
    - `HandlerMapper`, `ViewResolver`에 해당 url과 관련된 값 put
- 인덱스 페이지 변경
  - `Article`이 인덱스 페이지에서 나타나도록 변경
  - `질문하기`버튼의 경우, 로그인한 사용자만 해당 버튼을 통해 메모장 생성 페이지로 이동
    - 로그인하지 않은 경우, 로그인 페이지로 이동

<br>

## Step 6. Content-Type 처리

> Step6의 경우, 이미 이전 Step을 진행하는 과정에서 처리하였습니다.


<br>

## Step 5. 동적인 html 구현

> Step5에서는 동적인 html을 표현할 수 있도록 TemplateEngine을 추가하였습니다. 기존 mustache template의 
> 문법을 기반으로 Custom TemplateEngine을 추가하였고, 이를 사용자 목록을 출력하는 부분에 적용하였습니다.
> 원활한 구현을 위해 View 객체를 새로 생성하고, 조건에 따른 생성 방식을 변경하여 나타날 수 있도록 하였습니다.
- `HttpMethod` enum 추가
  - `GET`, `POST` 나타내는 방식 변경
- `View` 추가
  - render, redirect 메세지에 따라 응답하고 보여지는 방식을 바꾸기 위한 `View` 객체 생성
  - Static File인 경우와 그렇지 않은 경우를 분리하여 렌더링 될 수 있도록 설정
- `TemplateEngine` 추가
  - 동적인 html 구현을 위한 Custom Template Engine(html에서만 적용)
  - Mustache 문법에서 필요한 부분을 활용
- `UserController`에 사용자 목록을 보여주는 메서드 `showUsers` 추가
  - 로그인이 되지 않은 경우, login 페이지로 이동(Redirect)
  - 로그인 된 경우, 사용자 목록을 출력

<br>

## Step 4. Cookie를 이용한 로그인 구현

> Step4에서는 쿠키를 이용하여 로그인 여부를 보여줄 수 있도록 설정했습니다.
> Step4를 구현하는 과정에서, 하나의 기능이 생길 때마다 Controller가 추가되는 단점이 존재하였고, 이를 보완하고자 Custom Annotation인 `@RequestMapping`을 구현하여 리팩토링을 진행하였습니다.

- 유저 로그인 기능 추가
  - 로그인 성공 시, Cookie를 설정하여 응답
- `HandlerMapper`, `Controller` 리팩토링
  - `HandlerMapper`에서 받는 URL을 전체("/users/form")에서 앞부분("/users")으로 감소
  - 맞는 컨트롤러를 받아오는 부분을 HashBasedTable에서 ConcurrentHashMap으로 변환
  - `Controller`는 기능에 따라 분리하고, 컨트롤러 내부에서 `@RequestMapping`을 통해 URL에 맞는 메소드를 실행
- `@RequestMapping` 추가
  - 컨트롤러의 여러 메서드 중 URL과 method에 맞는 메소드를 활용하기 위해 추가한 어노테이션

<br>

## Step 2 & 3. 회원가입 구현

> Step1에서 Step2, Step3으로 진행하는 과정 중 전반적인 코드 리팩토링 과정을 진행했습니다.
> 구조적으로 엉켜있고, 제대로 구성되지 않은 부분들이 많았는데, 해당 부분들을 `Spring MVC` 동작 방식을 기반으로 수정했습니다.
> 유사하게 구현하려고 하였으나, 일부 다른 부분이 있습니다.

<br>

### HTTP REQUEST & RESPONSE 동작 방식
1. 클라이언트가 요청을 보낸다.
2. `DispatchServlet`(기존 `RequestHandler`)가 요청을 받는다.
3. `DispatchServlet`이 수신한 Http Request Message를 `Request` 클래스를 통해 request line, header, body를 확인한다.
4. 요청 URL에 매핑되는 Controller가 존재하는 지 `HandlerMapper`를 통해 확인한다. 
5. 존재하는 경우, 해당 `Controller`를 반환하고, 아니면 `null`을 반환한다.
6. `DispatchServlet`에서 `.checkController` 메소드를 통해 컨트롤러가 존재하는 경우, `HandlerAdapter`가 `Controller`에 따른 메소드를 호출하도록 요청한다. 존재하지 않는 경우, `ModelAndView`에 URL을 바로 담는다.(이 경우, 바로 (8) 로 진행한다.)
7. `HandlerAdapter`가 `Controller`의 메소드를 호출하면, 로직에 따라 처리하고, 결과를 `ModelAndView`에 담아 반환한다.
8. `DispatchServlet`은 반환받은 `ModelAndView`를 `ViewResolver`로 전송한다.
9. `ViewResolver`는 해당하는 `view`를 찾아 `ModelAndView` 내부의 `viewName`을 바꿔 반환한다. 해당하는 `view`가 없다면, 원래 값을 그대로 반환한다.
10. 최종 처리된 `ModelAndView`을 `Response`로 보낸다.
11. `Response`는 `HttpStatusCode`를 생성하고, Http Response Message를 응답한다.


<br>

----

## Step1. 정적인 html 파일 응답

> Request Header를 출력하고, Request Line에서 path를 분리한다. 
> 또한, 이것을 읽어내 `localhost:8080/index.html`을 실행하였을 때, `.webapp/index.html`을 응답할 수 있도록 합니다.

- `IOUtils.readMimeType` : url에 해당하는 파일의 mime type을 반환하는 메서드
- `HttpRequestUtils.parseRequestUrl` : request 요청 중 url만 분리하는 메서드

<br>

## File의 Mime Type을 알아내는 방법
- `Apache Tika`
  - build.gradle에 추가한 이후 활용
    - `org.apache.tika:tika-core`
    - Mime type을 찾는 메서드만 활용하기 위해, `tika-core` 적용
```java
class TikaTest{
    @Test
    public void whenUsingTika_thenSuccess() {
        File file = new File("product.png");
        Tika tika = new Tika();
        String mimeType = tika.detect(file);

        assertEquals(mimeType, "image/png");
    }
}
```
