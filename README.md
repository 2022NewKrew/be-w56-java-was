# 웹 서버 구현
- 그림으로 배우는 HTTP 책구입 (설 연휴에 읽을 예정)
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
