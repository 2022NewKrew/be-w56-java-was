# be-w56-java-was
56주차 간단 웹 서버 구현

### 새롭게 알게 된 내용
- http통신은 tcp,udp 프로토콜중 아무거나 사용해도 된다. (통상적으로 tcp 소켓)
- 소켓을 생성한다음 일정시간 유지해서 모든 요청에 대해 소켓이 생성되지 않도록 한다.
- 실제로는 스레드 풀을 이용해서 재활용한다.
- http request, response는 개행을 이용해서 header, body, 마지막을 구분한다.

### 새롭게 알게 된 용어
- start-line : request의 첫 라인. 메소드, 타겟 uri, http버전에 대한 정보