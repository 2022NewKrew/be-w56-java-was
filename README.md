# be-w56-java-was
56주차 간단 웹 서버 구현

## 1주차

### 2022-01-24 (월)

#### 구현 내용

- `index.html` 요청 시 `webapp/index.html` 반환

#### 구현 관련 고찰

- favicon.ico: favorite + icon 의 합성어로 웹 브라우저 주소창에 표시되는 아이콘
- 웹 서버 코드 분석
  - `WebServer::main` 동작 과정 
    1. `ServerSocket listenSocket = new ServerSocket(port)`: 서버 소켓 생성
    2. 요청 들어올 경우 `connection = listenSocket.accept()`: 빈 소켓 할당
    3. `RequestHandler requestHandler = new RequestHandler(connection)`:
    4. `requestHandler.start()`: `Threads::start` 상속 받음 

  - `RequestHandler::run` 동작 과정
    1. `InputStream in = connection.getInputStream()`
    2. `OutputStream out = connection.getOutputStream()`
    3. `BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));`: `BufferedReader` 생성
    4. `br.readLine()` 등으로 InputStream 처리
    5. `DataOutputStream dos = new DataOutputStream(out);`: `DataOutputStream` 생성
    6. `dos.writeBytes()` 등으로 OutputStream 처리
    7. `dos.flush()`: 버퍼에 저장된 내용을 강재로 내보낸다.
    
### 2022-01-25 (화)

#### 구현 내용

- `Request`, `Response` 객체 분리
- GET 으로 회원가입 기능 구현
- css, js 적용

#### 구현 관련 고찰

- `RequestHandler::run`에 많은 기능이 집중되어 있음
  1. 요청 Parsing
  2. 응답 쓰기
- 다음과 같이 코드 작성 시 `NoClassDefFoundError` 발생
```java
public enum ContentType {    
    private String contentType;
    private static Map<String, ContentType> contentType;
    
    ContentType(String contentType) {
        this.contentType = contentType;
        initializeContentTypeMap();
    }
    
    private static void initializeContentTypeMap() {
        // ...   
    }

  public static ContentType of(String requestType) {
    return contentTypeMap.getOrDefault(requestType, ContentType.HTML);
  }
}
```
- 아래와 같이 수정하여 에러 해결
```java
public enum ContentType {
  private String contentType;
  private static Map<String, ContentType> contentTypeMap;

  ContentType(String contentType) {
    this.contentType = contentType;
  }

  private static Map<String, ContentType> initializeContentTypeMap() {
    Map<String, ContentType> contentTypeMap = new HashMap<>();
    ContentType[] contentTypes = values();
    for (ContentType contentType : contentTypes) {
      contentTypeMap.put(contentType.contentType, contentType);
    }
    return contentTypeMap;
  }

  public static ContentType of(String requestType) {
    if (contentTypeMap == null) {
      contentTypeMap = initializeContentTypeMap();
    }
    return contentTypeMap.getOrDefault(requestType, ContentType.HTML);
  }
}
```

### 2022-01-26 (수)

#### 구현 내용

- 회원 가입 POST 방식으로 변경, 리다이렉트 적용
- `HttpStatus` 추가
- `Response` 추가
- `Request` 구조 변경

#### 구현 관련 고찰

- `BufferedReader::readLine` 사용 시 `\r` 또는 `\n`이 없을 경우 메소드가 종료되지 않음.  
따라서 POST body를 가져올 경우 `BufferedReader::read` 사용
