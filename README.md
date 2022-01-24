# be-w56-java-was

56주차 간단 웹 서버 구현

## 1.24 알게된 것

- dataOutputStream: dataOutputStream은 자바의 기본 데이터 타입을 출력해주는 메소드들을 가지고 있다고 한다. 이번 과제에서는 클라이언트에서 요청한 데이터를 가지고 적절히 파싱한 후에 다시
  response에 실어서 보내주는 역할을 하였다.
- content-Type: 이는 클라이언트가 요청한 데이터에 대한 응답 타입이다. 이번 프로젝트에서 index.html과 같은 html파일 말고도 동시에 요청되는 css, js파일 등에 대해서 이 파일들에 맞는
  content-Type을 지정해줘야 제대로 작동한다는 것을 알게 되었다. 가령 text/html과 같이 지정되면 css, js로서 동작하지 않는 것을 볼 수 있었다.