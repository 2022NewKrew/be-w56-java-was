# 웹 서버 구현
- 그림으로 배우는 HTTP 책구입
## HTTP 학습
###[쿨라임의 HTTP/1.1, HTTP/2, 그리고 QUIC](https://www.youtube.com/watch?v=xcrjamphIp4)
- HTTP는 어플리케이션 계층에서 존재
- HTTP는 신뢰성만 보장한다면 TCP, UDP 둘 중 어느것을 써도 상관없음.
- HTTP2.0
  - 바이너리 프레이밍 계층 사용 (어플리케이션 안에 있는 계층 새로생김)
  - 바이너리로 인코딩 (빠르다, 오류 발생가능성 작다)
  - Multiplexing 가능하다.
  - Stream Prioritization
  - Server Push
  - Header Compression (헤더의 크기를 줄여 페이지 로드 시간 감소)
- QUIC
  - 전송 계층 프로토콜
  - 2013년에 공개
  - UDP 기반으로 만들었습니다. (그래서 매우 빠름, 신뢰성은 보장하지 않지만 데이터 전송에 초점을 만듬)
  - 이점
    - 전송 속도 향상 (첫 연결 설정에서 필요한 정보와 함께 데이터를 전송, 연결 성공 시 설정을 캐싱하여 다음 연결 때 바로 성립 가능)
    - Connection UUID라는 고유한 식별자로 서버와 연결 , 커넥션 재수립 필요 X
    - TLS 기본 적용 (보안 향상)
    - 독립 스트림 -> 향상된 Multiplexing 가능 (TCP Head Of Line Blocking 문제 해결)
      - 로딩 시간 평균 3% 개선 , 유튜브 개선
    - HTTP 3.0이 QUIC 이다. 구글은 이미 HTTP3.0 사용중입니다.

## Thread Pool
- https://limkydev.tistory.com/55
- https://postitforhooney.tistory.com/entry/JavaThread-Java-Thread-Pool%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-Thread%EB%A5%BC-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0Thread-Runnable

## 그림으로 배우는 HTTP 책 공부내용
GET /index.html HTTP /1.1
Host: www.hackr.jp

if-Modified-Since: Thu. 12 Jul 2012 ~~ GMT

if-Modified는 갱신된 경우에만 돌려주고 아니면 304 반환

Post는 request안 Entity 전송에 목적을 둠

PUT 성공시 204

HEAD 는 GET과 비슷하지만 body는 안돌려줌 유효성과 리소스 갱신 유무 따질때 씀

OPTIONS * HTTP /1.1

제공하고있는 메소드 문의

TRACE / HTTP /1.1

(프록시)서버 같은 곳을 경유할때마다 초기설정한 Max-Forards를 1식 줄이고 0이 되는순간 동작확인

근데 사용 안함 보안문제

CONNECT proxy.hackr.jp:8080 HTTP /1.1

SSL 프로토콜로 암호화 된것을 프록시서버 터널링 할때사용

응답으로 200오면 그 뒤에 터널링 게시
