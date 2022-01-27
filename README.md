# be-w56-java-was
56주차 간단 웹 서버 구현


### 웹 서버 역할
1. HTTP Parsing
2. 정적 파일 Serving
3. TCP Connection 관리
4. 웹 트랜잭션 Logging


### 정리하기

- InputStream이 닫히면 Socket도 닫힌다.
- ClassLoader.getSystemResource()를 통해서 classpath 내의 리소스 파일을 읽을 수 있다.
- HTTP의 RequestLine과 헤더부는 US-ASCII Encode를 이용한다.
