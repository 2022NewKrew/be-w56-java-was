# be-w56-java-was

56주차 간단 웹 서버 구현

## 구현 중 알게 된 것

- Postman으로 요청을 하면 Broken Pipe(Write failed) 오류가 생긴다
  - Postman이 실제 요청 이전에 서버 확인용으로 요청을 한 개 더 날리는데, 연결 확인 되면 바로 끊어버려서 저런 오류가 난다고 한다
  - 
  