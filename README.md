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

# Step 2
### 구현 내용

- 로저 코드 보면서 리팩토링
- GET 으로 회원가입 기능 구현

### 새롭게 알게 된 내용

- MIME
    - (이메일과 함께 동봉할) 바이너리 파일을 텍스트 파일로 변환하는 기술
    - MIME로 인코딩한 파일은 Content-type 정보를 파일 앞 부분에 담는다.
    - Content-type
    - type/subtype
    - text/html, text/css, text/javascript

# Step 3
### 구현 내용

- 응답 헤더에 Location 추가
- POST로 회원가입 하도록 수정

### 새롭게 알게 된 내용

- response header의 Location
  - 응답 헤더에 페이지를 리디렉션 할 URL을 나타낸다. 
  - 3xx (리디렉션) 또는 201 (만든) 상태 응답과 함께 제공될 때만 의미를 제공한다.

# Step 4
### 구현 내용

- 로그인 요청하면 DB에서 데이터를 불러와 확인한다.
- 로그인 성공시 logined=true 를 응답 쿠키에 넣어서 보낸다.
- 로그인 실패시 logined=false 를 응답 쿠키에 넣어서 보낸다.
