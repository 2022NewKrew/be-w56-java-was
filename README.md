# be-w56-java-was
56주차 간단 웹 서버 구현

### 2022.01.24 (월)

#### HTTP
> HyperText Transfer Protocol
>
> 웹 상에서 전보를 주고받을 수 있는 프로토콜  

&nbsp;
#### HTTP 특징
1. 클라이언트 - 서버 구조
    - client : 서버에 컨텐츠를 요청
    - server : 클라이언트 요청에 대해 적당한 응답 제공
    - proxy : 클라이언트와 서버 사이에 존재하며 캐싱, 필터링, 로드 밸런싱, 인증, 로깅 등 다양한 기능 수행
2. 무상태 프로토콜 (Stateless)
   - 서버가 클라이언트의 상태를 보존하지 않는다. (서버는 클라이언트 식별이 불가능하다)
3. 비 연결성 (Connectionless)
   - 서버가 클라이언트 요청에 대한 응답을 마치면 연결을 끊는다.  
&nbsp;

#### HTTP Request Line
- HTTP Request 의 첫줄
- Method &nbsp; Request-URI &nbsp; HTTP-Version 형태로 구성   
ex. `GET /index.html HTTP/1.1`  
&nbsp;

#### Content-Type Header & Accept Header
- Content-Type : HTTP 메시지에 담겨 보내는 데이터의 형식을 알려주는 헤더
- Accept : 요청 시 허용 가능한 데이터 타입을 알려주는 헤더

### 참고자료
- [HTTP란?](https://hanamon.kr/%EB%84%A4%ED%8A%B8%EC%9B%8C%ED%81%AC-http-http%EB%9E%80-%ED%8A%B9%EC%A7%95-%EB%AC%B4%EC%83%81%ED%83%9C-%EB%B9%84%EC%97%B0%EA%B2%B0%EC%84%B1/)
- [HTTP 특성과 구성요소](https://victorydntmd.tistory.com/286)
- [Content-Type Header 와 Accept Header의 차이점](https://webstone.tistory.com/66)
