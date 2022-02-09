# be-w56-java-was
56주차 간단 웹 서버 구현

### [step1, step2 피드백](https://github.com/kakao-2022/be-w56-java-was/pull/193) 반영
- [x] 만드신 거 실제 실행을 해보았는데ㅠㅠ 안돌아가서.. 
  - 실행 테스트를 포스트맨으로만 테스트 하고 올렸었네요ㅜ 죄송합니다 이번엔 직접 실행해서 테스트 해봤습니다
  - 테스트 과정
    - 서버 실행(WebServer.java)
    - http://localhost:8080/index.html 검색
    - 회원 가입 클릭 후 아이디, 비밀번호, 이름 이메일 입력해서 회원가입 버튼으로 제출
- [x] RequestHeader.java, http에 사용되는 기본적인 헤더들은 메서드로 제공(매번 사용시마다 contains 및 파싱과정을 거쳐야 할테니)
- [x] HttpRequestUtils.java, 사용하지 않는 메서드들 제거 or RequestUri 클래스에서 활용
- [x] HttpRequestUtils.java, RequestHeader의 역할이 더 맞아보임
- [x] HttpResponseUtils.java, 만들어 놓은 Response class를 사용하면 좀 더 깔끔해질것 같음
- [ ] Router.java, get의 경우에 한정된 라우팅, 응답을 내려주는 경우 어떻게 할 것인가 문제, 인터페이스 이용해서 처리에 대한 로직을 구현하는 메서드를 만들어 보면 좋을듯
  - Router.java (get에 한정된 처리, 응답을 내려주는 경우 처리 보완했습니다) 인터페이스 이용해서 처리하는건 좀 더 공부 해보겠습니다   

---
## HTTP 정리
- 인터넷에서 데이터를 주고 받을 수 있는 프로토콜
- 특징
  - 클라이언트 - 서버 구조
  - 무상태 프로토콜(Stateless)
  - 비 연결성(Connectionless)
  - 응답 상태코드
  - 메서드 방식
  - HTTP 메시지

### 무상태 프로토콜(Stateless)
- 서버는 클라이언트를 식별할 수가 없음
- 상태를 기억하는 방법
  - 쿠키
  - 세션
  - 토큰 사용(OAuth, JWT)

### 비 연결성(Connectionless)
- 클라이언트와 서버가 한 번 연결을 맺은 후, 클라이언트 요청에 대해 서버가 응답을 마치면 맺었던 연결을 끊어버리는 성질
- why?
  - HTTP는 인터넷 상에서 불특정 다수의 통신 환경을 기반으로 설계됨
  - 만약 서버에서 다수의 클라이언트와 연결을 계속 유지해야 한다면, 이에 따른 많은 리소스가 발생하기 때문
- 단점
  - 매번 새로운 연결을 시도/해제의 과정을 거쳐야하므로 연결/해제에 대한 오버헤드가 발생
    - 이에 대한 대책으로 KeepAlive 속성을 사용할 수 있음

### 메서드 방식
- GET: 서버에게 리소스를 달라는 요청(조회)
- POST: 서버에 입력 데이터를 전송하면 요청 엔티티 본문에 데이터를 넣어 서버에 전송(삽입)
- PUT: 서버가 요청의 본문을 갖고 요청 URI의 이름대로 새 문서를 만들거나, 이미 URI가 존재한다면 요청 본문을 변경할 때 사용(수정)
- DELETE: 서버에서 요청 URI 리소스를 삭제하도록 요청(삭제)
- 등등!

### 응답 상태코드
- [자세한 내용 참고](https://ko.wikipedia.org/wiki/HTTP_%EC%83%81%ED%83%9C_%EC%BD%94%EB%93%9C)
- 1xx: 조건부 응답
- 2xx: 요청 성공
- 3xx: 리다이렉션
- 4xx: 클라이언트 에러
- 5xx: 서버 에러

### HTTP 메시지
- HTTP 프로토콜 상에서 클라이언트와 서버는 데이터를 패킷 단위로 잘게 쪼개서 통신
- 데이터 전송 단위인 패킷에는 요청/응답에 대한 메시지가 담겨 있음
- ???: 웹 개발자, 또는 웹 마스터가 손수 HTTP 메시지를 텍스트로 작성하는 경우는 드뭅니다.

- 요청(Request)
  - 시작라인(Request Line)
    - HTTP 메서드
    - URL 또는 프로토콜, 포트, 도메인의 절대 경로
      - origin 형식: 끝에 '?'와 쿼리 문자열이 붙는 절대 경로
        - 예: GET /background.png HTTP/1.0
      - absolute 형식: 완전한 URL 형식
        - 예: GET http://developer.mozilla.org/en-US/docs/Web/HTTP/Messages HTTP/1.1
      - authority 형식: 도메인 이름 및 옵션포트(':'가 앞에 붙음)로 이루어진 URL의 authority component
        - 예: CONNECT developer.mozilla.org:80 HTTP/1.1
      - asterisk 형식: OPTIONS와 함께 별표('*')하나로 간단하게 서버 전체를 나타냄
        - 예: OPTIONS * HTTP/1.1
    - HTTP 버전
  - HTTP 헤더 (HTTP Header)
    - 패킷에 대한 정보
    - 대소문자 구분없는 문자열 다음에 콜론(':')이 붙으며, 그 뒤에 오는 값은 헤더에 따라 달라짐
    - ![요청 헤더](https://mdn.mozillademos.org/files/13821/HTTP_Request_Headers2.png)
  - 본문 (Body)
    - 모든 요청에 들어가지는 않고, POST같은 요청시 데이터 전송
    
- 응답(Response)
  - 시작라인(Status Line)
    - 프로토콜 버전: 보통 HTTP/1.1
    - 상태 코드: 요청의 성공 여부, 200, 404 혹은 302 등
    - 상태 텍스트: 짧고 간결하게 상태 코드에 대한 설명을 글로 나타내어 사람들이 HTTP 메시지를 이해할 때 도움이 됨
  - HTTP 헤더 (HTTP Header)
    - 요청 헤더랑 비슷한 특징.
    - ![응답 헤더](https://mdn.mozillademos.org/files/13823/HTTP_Response_Headers2.png)
    - [Contents-Type Header 와 Accept Header의 차이점](https://webstone.tistory.com/66)
  - 본문 (Body)
    - 모든 요청에 들어가지는 않고, 201, 204 같은 상태코드를 가진 응답에는 보통 본문이 없음
    - 크게 세 가지 종류로 나뉨
      - 이미 길이가 알려진 단일 파일로 구성된 단일-리소스 본문: 헤더 두개(Content-Type와 Content-Length)로 정의 함
      - 길이를 모르는 단일 파일로 구성된 단일-리소스 본문: Transfer-Encoding가 chunked로 설정되어 있으며, 파일은 청크로 나뉘어 인코딩 되어 있음
      - 서로 다른 정보를 담고 있는 멀티파트로 이루어진 다중 리소스 본문: 이 경우는 상대적으로 위의 두 경우에 비해 보기 힘듦

### 참고 자료
> [네트워크-http-http란-특징-무상태-비연결성](https://hanamon.kr/%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC-http-http%EB%9E%80-%ED%8A%B9%EC%A7%95-%EB%AC%B4%EC%83%81%ED%83%9C-%EB%B9%84%EC%97%B0%EA%B2%B0%EC%84%B1/)
>
> [HTTP 특성(비연결성, 무상태)과 구성요소 그리고 Restful API](https://victorydntmd.tistory.com/286)
>
> [HTTP 메시지](https://developer.mozilla.org/ko/docs/Web/HTTP/Messages)
