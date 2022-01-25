# be-w56-java-was
56주차 간단 웹 서버 구현

### step1-1
> HTTP
- 하이퍼본문전송규약
- 클라이언트 - 서버 사이의 소통
  - 클라이언트 : 서버에 요청을 보내는 주체
  - 서버 : 클라이언트의 요청에 응답하는 주체
- Stateless, Connectionless 한 특징

> HTTP Request
- 클라이언트가 서버에게 보내는 요청 메시지
- 요청 내용, 헤더, 빈 줄, 기타 메시지 등으로 구성
- 요청 내용
  - start-line이며 항상 한 줄로 끝난다.
  - GET /images/logo.gif HTTP/1.1
  - 메서드, url, protocol(version) 으로 구성
  - " " 으로 구분되어있다.
- 헤더
  - 요청에 대한 설명, 혹은 메시지 본문에 대한 설명
  - 대소문자 구분없는 문자열 다음 ":", 그리고 값
  - General, Request, Entity 헤더가 있음
- 빈 줄
  - 요청에 대한 모든 메타 정보가 전송되었음을 알림
  
> 참고 자료
- https://ko.wikipedia.org/wiki/HTTP
- https://developer.mozilla.org/ko/docs/Web/HTTP/Messages
