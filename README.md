# be-w56-java-was
## by keith.lee
### 1일차 정리
- BufferedReader: 문자열, 배열 등을 효율적으로 읽기 위해 버퍼를 이용한 character-input stream
- HTTP Request Header의 양이 내 생각 이상으로 많았다
- BufferedReader#lines를 통해 생성되는 Stream<String>을 collect하려고 했는데, 이렇게 하려고 하면 무한 루프를 도는 것 처럼 멈춘다. (원인 파악중)
- 하나의 스레드로 여러 클라이언트를 처리하기 위해, nio패키지를 이용한 비동기 방식으로 구현하는 방법도 있었다. (andy.leecy의 작업물 참고)
