# be-w56-java-was
56주차 간단 웹 서버 구현

## step1
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

## step2
> Response Content Type
- 응답을 내려줄 때 어떤 종류의 데이터인지 명시
- 확장자에 따라 다른 방식으로 명시해줄 수 있고, 특정 범주에 없다면 application/octet-stream 으로 default
- 똑같이 html 파일을 응답한다해도, text/html 로 명시하면 브라우저에 html 파일이 표시되지만 default 값으로 내려주면 html 파일 자체가 다운로드 처리됨
- Request Header에서 브라우저가 받아들일 수 있는 타입(들)을 명시해주면, 이에 맞춰 내려주고 브라우저는 응답으로 온 content-type에 따라 데이터를 해석하는 듯 보임.

> 참고 자료
- https://developer.mozilla.org/ko/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types

## step3
> HTTP Redirection
- 302 상태코드와, Location을 응답으로 내려주면 브라우저가 해당 Location으로 다시 Get 요청을 보내 redirection이 이루어지는 것으로 보인다.

## step4
> Cookie를 통한 로그인 처리
- Set-Cookie를 통해 로그인 되어있는 상태인지의 여부를 쿠키에 저장하여, 다음 요청 때마다 들어오는 logined 값으로 로그인 된 상태인지 확인할 수 있다.
- post 메서드로 회원가입을 처리한 것과 마찬가지로 302 리다이렉션을 통해 로그인 성공여부에 따른 페이지로 get 메서드가 호출된다.
> Spring 구조와의 차이점
- spring과의 차이점이 존재한다. spring은 controller에서 3가지 중요 파라미터가 존재한다.
  - HTTP Method 종류
  - 요청된 url
  - 응답으로 보내줄 서버 내 파일 path (어노테이션 아래 String 리턴타입 메서드의 리턴값)
- 지금 현재의 구조는 Request가 들어오면 이를 기반으로 Response객체를 바로 생성한다. 그러나 이는 GET 요청에서 url과 파일 path가 다를 경우 작동하지 않을 것이다.

## step5
> HTTP keep alive
- 요청 헤더에서 서버에게 연결을 바로 끊지 않을 것을 요청하는 것
- 서버는 기본적으로 HTTP의 특성때문에 바로바로 연결을 끊지만, keep alive 요청이 있으면 바로 끊지 않고 다음 요청이 없을 때까지 연결을 지속.
- timeout을 둬서 시간 내 새로운 요청이 들어온다면 연결을 지속. (소켓을 닫지않음)
