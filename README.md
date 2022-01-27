# be-w56-java-was

## 1단계
- 정적 파일 응답
  - File 객체를 이용해서 html, css, js 파일등의 응답
  - html, css, js 등 text/html, text/css, text/javascript 와 같이 content-type을 다르게 설정 해주어야 하는데 아예 지워버리니 무슨 이유인지 모르겠지만 일단 정상작동,,,
  - html이 참조하는 파일에 대해서는 별도의 조작을 하지 않아도 자동으로 요청을 보냄료

## 2단계
- GET method 를 이용한 회원가입 구현
  - request header 에서 필요한 정보를 추출
  - uri 에서 query string 을 parse
  - 일단 index.html이 보여지도록 설정
- Request header 를 이용해서 request 정보를 관리
- Controller 를 통해 앞으로 추가될 요청에 대해 좀 더 자세하게 관리

## 3단계
- 404 page를 던질때 try catch 한번더 사용되는 문제
- buffered reader 에서 readline 으로 body가 읽히지 않는 문제

## 4단계
- cookie를 이용한 로그인 구현

## 해야할것
- html의 중복되는 부분처리
  - 우선순위 낮음 
  - mustache 를 적용시켜야 하나 생각중
    - 현재는 중요성이 높아 보이지 않아 추후에 시간이 나면 고민
- lombok 의 적용
  - 우선순위 높음
  - 리뷰어님이 피드백 주신대로 lombok을 사용한 코드를 보니 매우 간편하고 편리해보임
  - lombok에 대해 알아보고 적용 예정
- 코드 품질 개선 및 파일 구조에 대한 고민
  - 지속적으로 고민해야 함