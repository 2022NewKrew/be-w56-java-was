# be-w56-java-was
56주차 간단 웹 서버 구현

## step 1 
- 요구사항
  - 정적인 html 파일 응답
    - http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
    - HTTP Request Header 예 
    ``` 
      GET /index.html HTTP/1.1 
      Host: localhost:8080 
      Connection: keep-alive 
      Accept: */* 
    ```
- InputStream 을 통해서 header의 정보를 읽어오는 것을 알 수 있었다.
- favicon.ico, css, js, font 등이  제대로 호출되지 않는 문제가 있음
    - Content-Type 이 text로 되어있어서 적절한 파일 형식을 제대로 읽어오지 못해서 발생하는 문제
    - Header에서 Accept가 존재하면 해당 부분을 content type으로 헤더에 넣어주면 문제가 해결이 됨


