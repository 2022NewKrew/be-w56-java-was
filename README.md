# be-w56-java-was
56주차 간단 웹 서버 구현

File의 생성자 안에 상대경로 기준은 현재 폴더가 아닌 프로젝트 폴더의 기준이 된다.<br>
웹 서버의 루트 경로 (Document Root)를 아래의 코드와 같이 특정한 파일경로를 기준으로 설정이 가능하다.
```java
File file = new File(DOCUMENT_ROOT + requestUri);
```

