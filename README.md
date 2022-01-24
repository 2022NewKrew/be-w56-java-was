# be-w56-java-was
56주차 간단 웹 서버 구현

## Step 1

### 요구사항
* http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.

### 새롭게 알게된 내용
* Http Request의 첫번째 라인은 Request-Line이라고 하는 것을 알게 됐다.
  * ex) GET /index.html HTTP/1.1
* Spring을 사용하지 않고 Web Server를 구현하는 방법을 알게 됐다.
* ServerSocket의 accept()를 이용하면 서버의 포트를 열고 응답이 올 때까지 대기할 수 있다는 점을 알게 됐다.
