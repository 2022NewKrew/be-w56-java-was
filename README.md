# be-w56-java-was
56주차 간단 웹 서버 구현

## step1-1
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
  
> Java 관련
- Try with Resource는 try 블록을 빠져나오면 close 된다.
- java 리플렉션 사용에 실패했지만, 개념 공부를 할 수 있었다.

> 참고 자료
- https://ko.wikipedia.org/wiki/HTTP
- https://developer.mozilla.org/ko/docs/Web/HTTP/Messages

## step1-2
> Response Content Type
- 응답을 내려줄 때 어떤 종류의 데이터인지 명시
- 확장자에 따라 다른 방식으로 명시해줄 수 있고, 특정 범주에 없다면 application/octet-stream 으로 default
- 똑같이 html 파일을 응답한다해도, text/html 로 명시하면 브라우저에 html 파일이 표시되지만 default 값으로 내려주면 html 파일 자체가 다운로드 처리됨
- Request Header에서 브라우저가 받아들일 수 있는 타입(들)을 명시해주면, 이에 맞춰 내려주고 브라우저는 응답으로 온 content-type에 따라 데이터를 해석하는 듯 보임.

> 참고 자료
- https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types

## step1-3
> HTTP Redirection
- 302 상태코드와, Location을 응답으로 내려주면 브라우저가 해당 Location으로 다시 Get 요청을 보내 redirection이 이루어지는 것으로 보인다.
