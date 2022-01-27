# be-w56-java-was
## by keith.lee
### 1일차 정리
- BufferedReader: 문자열, 배열 등을 효율적으로 읽기 위해 버퍼를 이용한 character-input stream
- HTTP Request Header의 양이 내 생각 이상으로 많았다.
- BufferedReader#lines를 통해 생성되는 Stream<String>을 collect하려고 했는데, 이렇게 하려고 하면 무한 루프를 도는 것 처럼 멈춘다. (원인 파악중)
- 하나의 스레드로 여러 클라이언트를 처리하기 위해, nio패키지를 이용한 비동기 방식으로 구현하는 방법도 있었다. (andy.leecy의 작업물 참고)

### 2일차 정리
- 정적 리소스들에 대해 Content-Type를 제대로 정의해야 올바르게 전달됐다.

### 3일차 정리
- POST의 데이터가 body에 있다는건 알고 있었는데, 이를 어떻게 파싱하는지 알았다.
- Status를 404로 설정한다고 해서 브라우저에서 Not Found가 알아서 띄워지는 건 아니었다. Body를 직접 설정할 필요가 있었다.

### 4일차 정리
- Set-Cookie는 302 Request의 헤더에 넣어도 작동했다. 사실 어떤 Request던 간에 대해 Set-Cookie가 작동하는 건 아닐까?
- 컨트롤러를 맵핑할 때 각 컨트롤러 클래스 자체를 반환해서 static 메소드를 호출하려 했는데, 클래스 자체를 반환하는 건 불가능하다고 한다. 어쩔 수 없이 instance를 반환하도록 하고 메소드를 static하지 않게 수정했다.