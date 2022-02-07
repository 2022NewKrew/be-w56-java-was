# be-w56-java-was
56주차 간단 웹 서버 구현

## 공부내용 정리

**참고 자료**
[MDN HTTP 문서](https://developer.mozilla.org/ko/docs/Web/HTTP/Messages)로 기본 구조를 파악했고 상세내용은 [HTTP 완벽 가이드](https://www.aladin.co.kr/shop/wproduct.aspx?ItemId=49731592)를 찾아보았습니다.


## HTTP 요청과 응답 구조
- start line: 실행할 요청, 요청 수행에 대한 성공 또는 실패가 기록됨
- header: 요청에 대한 설명, 메시지 본문에 대한 설명
- blank line: 메타 정보가 끝났다
- body: 요청과 관련된 내용, 응답과 관련된 문서가 들어간다. body의 존재 유무 및 크기는 첫 줄과 HTTP 헤더에 명시된다
- 각 줄은 캐리지 리턴과 개행문자로 구성된 두 글자의 줄바꿈 문자열로 끝난다.

# HTTP request

## Start line

서버가 특정 동작을 하게 만들기 위해 클라이언트에 전송하는 메시지이다. 다음과 같은 세 가지 요소로 이루어져있다.

- HTTP 메서드
- 요청 타겟
- HTTP 버젼

### HTTP 메서드

서버가 수행해야할 동작을 나타낸다

- GET: 데이터 취득
- HEAD: GET 메서드에서 바디를 뺀 것
- POST: 데이터 신규 작성, 본문 O
- PUT: 데이터 갱신(리소스가 이미 있을 때), 본문 O
- DELETE: 리소스 삭제
- CONNECT: 목적 리소스로 식별되는 서버로 터널을 맺음 (프록시 서버를 거져 대상 서버에 접속)
- OPTIONS:사용할 수 있는 메소드 목록 (CORS에서 사용된다)
- TRACE: loop-back, content-type에 message/http를 설정하고 헤더와 바디를 그대로 반환 (취약점 때문에 더의 사용하지 않음)
- PATCH: 차이에 따른 데이터 갱신(리소스가 이미 있을 때)


### 요청 타겟

주로 url, 프로토콜, 포트, 도메인의 절대 경로가 들어온다
요청 타겟 포멧은 HTTP 메서드에 따라 달라진다

#### origin 형식

끝에 ?과 쿼리 문자열이 붙는 절대 경로이다. 가장 일반적인 형식이며 GET, POST, HEAD, OPTIONS 메서드와 함께 사용한다

POST / HTTP 1.1
GET /background.png HTTP/1.0
HEAD /test.html?query=alibaba HTTP/1.1
OPTIONS /anypage.html HTTP/1.0

#### absolute 형식

완전한 url 형식이다. 프록시에 연결하는 경우 대부분 GET과 함께 사용된다.

GET http://developer.mozilla.org/en-US/docs/Web/HTTP/Messages HTTP/1.1

#### authority 형식

도메인 이름 및 옵션포트로 이루어져있다. HTTP 터널을 구축하는 경우에만 CONNECT와 함께 사용할 수 있다

CONNECT developer.mozilla.org:80 HTTP/1.1

#### asterisk 형식

OPTIONS와 함께 별표 하나로 서버 전체를 나타낸다
OPTIONS * HTTP/1.1

### HTTP 버젼

- HTTP/1.1

## Header

- 대소문자 구분없는 문자열 다음에 `':'` 콜론이 붙고 뒤에 값이 붙는다. key: value
- 여러 줄로 작성할 수 있는데, 추가 줄 앞에는 최소 하나의 스페이스 혹은 텝 문자가 와야 한다.

### Request Header

요청에 대한 부가 정보 제공

- Host
- User-Agent (en-US), Accept-Type, Accept-Language : 요청의 내용을 구체화
- Referer: 컨텍스트 제공
- If-none: 조건에 따른 제약사항

### General Header

요청과 응답 양쪽에 모두 나타날 수 있음. 바디에서 최종적으로 전송되는 데이터와는 관련이 없다.

- Via 헤더

### Entity Header

컨텐츠 길이나 MIME 타입과 같이 엔티티 바디에 대한 자세한 정보를 포함하는 헤더. body가 없는 경우 entity 헤더는 전송되지 않는다.

- Content-Type, Content-Length


## Body

빈 줄 다음에는 어떤 종류의 데이터든 들어갈 수 있는 메시지 본문이 필요에 따라 올 수 있다. request의 body는 웹 서버로 데이터를 실어보내며, response의 body는 클라이언트로 데이터를 반환한다. body에는 텍스트 뿐만 아니라 임의의 이진 데이터를 포함할 수 있다.

- single resource bodies: 헤더 두 개(Content-Type, Content-Length)로 정의된 단일 파일로 구성된다
- multiple-resource bodies: 파트마다 다른 정보를 지닌다. 보통 HTML 폼과 관련 있다.


# HTTP responses

## Status line

HTTP/1.1 404 Not Found.

다음과 같은 정보를 가지고 있다

- 프로토콜 버젼 `HTTP/1.1`
- 상태 코드: 요청의 성공 여부 `200`, `404`, `302`
- 상태 텍스트: 상태코드에 대한 설명

### 상태 코드

1XX: 100-101 정보
2XX: 200-206 성공
3XX: 300-305 리다이렉션
4XX: 400-415 클라이언트 에러
5XX: 500-505 서버 에러


## Headers

### Response Header

응답에 대한 부가 정보 제공

나머지는 Request Header와 같다

- General Header
- Entity Header

## Body

모든 응답에 본문이 들어가지는 않는다. 201 (Created), 204(No Content)와 같은 상태코드를 가진 응답에는 보통 본문이 없다.

- Single-resource body (known length): Content-Type, Content-Length로 정의
- Single-resource body (unknown length): chunks로 인코딩되어 있다. Transfer-Encoding이 chunked로 세팅되어 있음
- Multiple-resource body: 서로 다른 정보를 담고 있는 멀티파트로 이루어진 다중 리소스 본문

---

## MIME 타입

Multipurpose Internet Mail Extension

웹 서버는 모든 Http 객체 데이터에 MIME 타입을 붙인다. 웹 브라우저는 서버로부터 객체를 돌려받을 때 다룰 수 있는 객체인지 MIME 타입을 통해 확인한다. 대부분 웹 브라우저는 잘 알려진 객체 타입 수백 가지를 다룰 수 있다. 이미지 파일을 보여주고, HTML 파일을 분석하거나 포맷팅하고 오디오 파일을 재생한다.

MIME 타입은 사선으로 구분된 주 타입과 부 타입으로 이루어진 문자열 라벨이다.

`primary object type / specific subtype`

- html로 작성된 텍스트 문서는 text/html 라벨이 붙는다
- plain ASCII 텍스트 문서는 text/plain 라벨이 붙는다
- JPEG 이미지는 image/jpeg가 붙는다
- GIF 이미지는 image/gif가 붙는다
- 애플 퀵타임 동영상은 video/quicktime이 붙는다
- 마이크로소프트 파워포인트 프레젠테이션은 application/vnd.ms-powerpoint가 붙는다

수백가지의 잘 알려진 MIME 타입과, 그보다 더 많은 실험용 혹은 특정 용도의 MIME 타입이 존재한다. 

---

## URI

**uniform resource identifier**

uri는 인터넷의 우편물 주소 같은 것으로, 정보 리소스를 고유하게 식별하고 위치를 지정할 수 있다. URI는 URL과 URN 두 종류가 있다.

### URL

특정 서버의 한 리소스에 대한 구체적인 위치를 서술한다. URL은 세 부분으로 이루어진 표준 포멧을 따른다.

`https://www.oreilly.com/online-learning/teams.html`

- URL의 첫 번째 부분은 scheme이라고 불리는데, 리소스에 접근하기 위해 사용되는 프로토콜을 적는다. `https://`
- 두 번째 부분은 서버의 인터넷 주소를 제공한다. `www.oreilly.com`
- 마지막은 웹 서버의 리소스를 가리킨다 `online-learning/teams.html`

오늘날 대부분의 URI는 URL이다.

### URN

uniform resiurxe name

URN은 콘텐츠를 이루는 한 리소스에 대해, 그 리소스의 위치에 영향 받지 않는 유일무이한 이름 역할을 한다. 리소스를 옮기더라도 문제 없이 동작한다.

`urn:ietf:rfc:2141`

위의 URN은 인터넷 표준 문서 RFC 2141이 어디에 있는지 상관없이 그것을 지칭하기 위해 사용할 수 있다. 
