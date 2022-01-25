# be-w56-java-was
56주차 간단 웹 서버 구현

## HTTP
- CSS, JS가 제대로 불러오지 않는 현상
  - Response에 content-type 헤더를 없애니까 정상 작동함
  - 관련 사항에 대해 확인해보고 공부 예정

## OOP
- 디미터의 법칙
  - 각각의 객체는 다른 객체의 근접한 객체만 알면 된다.
  - 너무 구구절절 모든 정보를 가지고 있을 필요가 없다.
  - get().get().get() ... 의 연속되는 코드를 지양해야 한다.
  - 이전 코드
    ```java
    httpResponse.getStatusLine().getStatus();
    httpResponse.getBody().getBodyLength();
    ```
  - 개선 코드
    ```java
    httpResponse.getStatus();
    httpResponse.getBodyLength();
    ```
## 기타
- javadoc
  - 자바 소스 코드 주석 표준으로 문서화 할 때 사용
  - [javadoc.html](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html)
  - 태그(어노테이션), 각종 작성 예시 참고
