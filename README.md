# be-w56-java-was

56주차 간단 웹 서버 구현

브라우저에서의 첫 요청에는 favicon 요청도 온다.

웹 서버 오픈소스의 코드를 보니 HttpRequest, HttpResponse의 default 클래스를 선언하고 상속 받아 사용하는 것 같다.

path "/"로 왔을 때 "index.html"로 서버가 매핑해줘야한다.

응답으로 html 파일을 전달하면 html 파일 안의 css나 js파일에 요청이 온다.