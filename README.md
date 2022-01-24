# be-w56-java-was

56주차 간단 웹 서버 구현

### HTTP: HyperText Transfer Protocol

- 클라이언트-서버 프로토콜
- 상태는 없지만(stateless) 세션은 가질 수 있다 (not sessionless)
- 확장성

HTTP Request 구성

```
GET / HTTP/1.1
HOST: localhost:8080
...
```

첫 줄은 HTTP 메소드, 경로, 그리고 프로토콜 버전으로 구성된다. 두번째 줄부터는 헤더들이 위치한다.

PUT VS PATCH

| PUT |     PATCH      |
|:---:|:--------------:|
| idempotent | not idempotent |
| replaces whole entity | updates select fields |
