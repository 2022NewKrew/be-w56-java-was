# be-w56-java-was
56주차 간단 웹 서버 구현

## 새롭게 알게 된 내용

### 1단계

- HTTP header가 map 형식의 데이터이며 다양한 key가 존재한다는 것을 알게됨.
- HTTP 자체는 단순한 문자로 된 데이터를 주고 받으나, 정해진 스펙에 따라 데이터를 읽어서 다양한 처리가 가능하다는 것을 알게 됨.

### 2단계

- Content-Type이 파일과 맞지 않으면, 웹 브라우저에서 로드를 막을 수 있다는 것을 알게됨.
- Http 301 Response에는 body 없이도 작동하는 것을 확인함.
- ParameterizedTest의 CsvSource를 사용할 때, 테스트 함수의 매개 변수로 input, expected 만 사용이 가능한 것을 알게됨.

### 3단계

- HTTP 301, 302가 브라우저 레벨에서는 유사하지만, 검색엔진의 봇이 처리하는 방식이 다르다는 것을 알게됨.
- favicon의 content type이 별도로 있다는 것을 알게됨.
