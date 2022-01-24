# be-w56-java-was
56주차 간단 웹 서버 구현

### 습득 지식 정리(21.01.24)
- #### URL과 URI의 차이
  - /index(URI O, URL X) -> /index.html(URI O, URL O)
- #### InputStream vs InputStreamReader vs BufferedReader
  - InputStream -> 1Byte씩 읽기(read())
  - InputStreamReader -> 1char씩 읽기(read())
  - BufferedReader -> String 읽기(readLine())
- #### Try-with-resources
  ```
  try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            TODO
        } catch (IOException e) {
            log.error(e.getMessage());
        }
  ```
  - 코드의 실행 위치가 try 문을 벗어날 때, InputStream 및 OutputStream 자동 close()
  - AutoCloseable를 구현한 객체만 해당
- #### HTTP 메시지 특징
  - Request
    - 첫 줄은 RequestLine -> ex) GET /index.html HTTP/1.1
    - Header와 Body는 ""Line으로 나누어짐
  - Response
    - 첫 줄은 StatusLine -> ex) HTTP/1.1 200 OK
    - Header와 Body는 ""Line으로 나누어짐
- #### PUT vs PATCH
  - PUT -> 자원의 전체 교체 -> 자원교체 시 모든 필드 필요
  - PATCH -> 자원의 부분 교체 -> 자원교체 시 일부 필드 필요