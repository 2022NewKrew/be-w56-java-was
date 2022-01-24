# be-w56-java-was
56주차 간단 웹 서버 구현



## 프로젝트 목표

- HTTP 를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.



## 새롭게 알게 된 내용

### step1

- `InputStream` 은 10진수의 UTF-16 값으로 int 형으로 저장되며, 1 byte 씩 읽을 수 있다...
  - 아스키코드 값을 출력한다.
- `InputStreamReader` 는 byte 단위의 InputStream 을 문자 단위 데이터로 변환하는 중개자 역할을 한다.
  - `new InputStreamReader(in, StandardCharsets.UTF-8))` 이 `UTF-8` 형식으로 `in` 이라는 InputStream 을 읽게 하는 것이다.
  - 아스키코드 값을 문자열로 바꿔준다.
  -  char 배열로 데이터를 받을 수 있다.
- `BufferedReader` 는 버퍼를 통해 입력받은 문자를 쌓아둔 뒤 한 번에 문자열처럼 처리할 수 있다...
  - `InputStreamReader` 는 문자열이 아닌 문자를 처리하는 것일 뿐이다.
  - `line()` 을 통해 한 줄 씩 읽을 수 있다.



- 잘못된 `Content-Type` 을 헤더에 넣어주면 브라우저에서 css, js 파일 적용이 제대로 안 된다.
- `tika` 라는 Apache 라이브러리를 통해 파일의 MimeType 을 확인할 수 있다.
- `Files.probeContentType(filePath)` 로 확장자를 이용해 MimeType 을 확인할 수도 있는데, 파일명에 `.` 이 있다거나 하는 등의 이유로 MimeType 을 확인하지 못하면 `null` 을 반환한다.  



### step2





## 궁금한 내용

### step1

- [ ] 처음에 받은 코드대로 `Content-Type` 이 `text/html` 로 통일되어 있었을 때는 css 나 js 파일이 브라우저에서 적용이 안 되는 것 같았다. 그래서 해당 line 을 주석처리했더니 css 적용이 되었다. 그렇다면 response 헤더에 Content 종류에 따라 `Content-Type` 을 만들어주는 메서드가 필요한가? 아니면 그냥 `Content-Type` 을 비워두면 되는가?

- [ ] `Files.probeContentType(filePath)` 로 mimeType 을 찾아주었는데, `bootstrap.min.css` 나 `jquery-2.2.0.min.js`  같은 파일의 mimeType 이 `null` 로 반환된다. 어떻게 해야할까?

  

### step2

