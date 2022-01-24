# be-w56-java-was
56주차 간단 웹 서버 구현
## 1단계 ([링크](https://lucas.codesquad.kr/2022-kakao/course/%EC%9B%B9%EB%B0%B1%EC%97%94%EB%93%9C/Java-Web-Server/%EC%9B%B9-%EC%84%9C%EB%B2%84-%EA%B5%AC%ED%98%84-1%EB%8B%A8%EA%B3%84))
### 프로젝트 목표
- HTTP를 학습하고 학습 지식을 기반으로 웹 서버를 구현한다.
- Java 멀티스레드 프로그래밍을 경험한다.
- 유지보수가 용이한 구조에 대해 스스로 고민하고 개선해 본다.
### 요구사항 1: 정적인 HTML 파일 응답
> http://localhost:8080/index.html 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다.
#### HTTP Request Header 예
```
GET /index.html HTTP/1.1
Host: localhost:8080
Connection: keep-alive
Accept: */*
```
### 실행 화면
<details>
    <summary>펼치기</summary>
    <h4><  ></h4>
    <img src="" alt="step1_1_">
</details>