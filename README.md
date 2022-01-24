# be-w56-java-was
56주차 간단 웹 서버 구현

## 1주차

### 2022-01-24 (월)

#### 구현 내용

- `index.html` 요청 시 `webapp/index.html` 반환

#### 알게 된 내용

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
