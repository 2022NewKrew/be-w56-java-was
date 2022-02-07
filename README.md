# be-w56-java-was
56주차 간단 웹 서버 구현

## Step 1

### 요구사항

- [X]  정적인 html 파일 응답
    - [X]  httpRequest header 출력
    - [X]  httpRequest line에서 path 분리
    - [X]  path에 해당하는 파일 읽어 응답

### 구현 내용

- [X] Controller 인터페이스 선언
- [X] ControllerMapping이 url에 매칭되는 Controller 반환하도록 구현
- [X] Request Header 파싱하는 것을 RequestParser가 담당
- [X] RequestHandler가 RequestParser와 ControllerMapping에서 받은 Controller를 이용해 사용자 요청을 처리하도록 구현

## step 2

### 요구사항

- [ ] GET으로 회원가입 기능 구현
  - [X] QueryPrameter 파싱하기
  - [X] /user/create 로 매핑
  - [ ] Repository 구현

### 구현 내용

- [X] Controller에 테스트 코드를 추가하였습니다.
- [X] RequestParser에 테스트 코드를 추가하였습니다.
- [X] 리팩토링을 진행하였습니다.

### 알게된점

1. mockito 사용 시 final method와 final class는 확장이 안된다. 확장하려면 추가 설정이 필요하다. [Mock Final Classes and Methods with Mockito](https://www.baeldung.com/mockito-final)

### 구현 예정

- [ ] 도메인 객체의 검증
- [ ] HttpRequest를 도메인 객체로 바꿔주는 매퍼(컨버터?) 구현
- [ ] 리포지터리 추가
- [ ] 유저 컨트롤러 구조화


## step3

### 요구사항

- [ ] POST로 회원 가입 구현 
  - [X] Request Body 값 추출
  - [X] redirect 구현
  - [ ] repository 구현

### 추가 구현

- [X] 컨트롤러
  - [X] 뷰 랜더링하는 책임을 Controller에서 ViewRender로 추출
  - [X] HttpResponse를 반환하도록 수정
  - [X] HttpRequest 클래스 생성
- [X] 테스트
  - [X] .http 테스트 작성

### 구현 예정

- [ ] 도메인 객체의 검증
- [ ] 유저 컨트롤러 테스트 코드 추가
- [ ] 유저 컨트롤러 구조화
- [ ] 리포지토리 추가
- [ ] HttpRequest를 도메인 객체로 바꿔주는 매퍼(컨버터?) 구현

## step 4

### 요구사항

- [X] Cookie를 이용한 로그인 구현

### 추가 구현

- [X] 유저 컨트롤러 구조화
- [X] 리포지토리 추가
- [X] HttpRequest를 도메인 객체로 바꿔주는 매퍼(컨버터?) 구현
- [X] HttpRequestReader 리팩토링

### 구현 예정

- [ ] 도메인 객체의 검증
- [ ] 유저 컨트롤러 테스트 코드 추가 
- [ ] dto 

## step 5

### 요구사항

- [X] for문이 있는 동적 html 생성 구현
- [X] user list 조회하는 api 제공
- [ ] Session 여부에 따라 user list 선택적으로 제공

### 구현 예정

- [ ] Session 여부에 따라 user list 선택적으로 제공
- [ ] 도메인 객체의 검증
- [ ] 유저 컨트롤러 테스트 코드 추가
- [ ] dto 
