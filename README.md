# be-w56-java-was
56주차 간단 웹 서버 구현

# Step 1
### 구현 내용

- RequestHeader 추가
- request로 받은 내용 저장
- 정적인 html 파일 응답

### 새롭게 알게 된 내용

- css 적용이 안되서 팀원들의 pr을 보고 해결했다. content-type에 Accept를 적용했다.
- Stream.takeWhile : Stream.filter와 달리 조건이 참이 아닐 경우 멈춘다.
