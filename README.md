# be-w56-java-was
5, 6주차 간단 웹 서버 구현


## HTTP Message 구조

### 공식 스펙  
>start-line  
*( header-field CRLF )  
CRLF  
[ message-body ]
> - CRLF : Carriage Return Line Feed = End Of Line(EOL) 개행
>   - Carriage Return : 현재 줄 커서 위치를 맨 앞으로 이동
>   - Line Feed : 현재 커서의 위치에서 하나의 줄을 아래로 이동

## start-line
### request-line (Request Message)
>method SP request-target SP HTTP-version CLRF  
> - method : 서버가 수행할 동작(GET, POST, PUT ...)
> - request-target : absolute-path[?query]
> - HTTP-version
> - SP : US-ASCII SP, space 공백
### status-line (Response Message)
>HTTP-version SP status-code SP reason-phase CLRF
> - HTTP-version
> - status-code : 요청 성공, 실패
> - reason-phase : 사람이 이해할 수 있는 상태 코드 설명 글

## header-field
- 전송에 필요한 모든 부가 정보
- 표준 헤더 외에 임의 헤더 추가 가능
>field-name ":" OWS field-value OWS  
> - field-name : 대소문자 구분 없음
> - OWS : 띄어쓰기 허용

## message-body
- 실제 전송할 데이터
- byte로 표현할 수 있는 모든 데이터 전송 가능


# 요구사항
## 구현 요구사항
### 1단계 : 정적 html 파일 응답
- [x] request 메시지를 받아서 파싱한다.
  - [x] 첫 번째 줄은 request-line 이다.
  - [x] 2째줄 ~ CRLF 전까지는 header-field 이다.
  - [x] 일반적으로 body는 없다.
- [ ] response 메시지를 만들어 보낸다.
  - [x] request-line의 path에 맞는 파일을 찾는다.
  - [x] 파일이 있다면 response-line의 status-code를 200으로 reason-phase를 OK로 설정한다.
    - [x] html 파일을 byte로 변환해서 body에 담는다.
  - [ ] 파일이 없다면 response-line의 status-code를 404로 reason-phase를 NotFound로 설정한다.
    - [ ] error page를 던진다...?
  - [ ] response header의 accept를 참고해서 header의 content-type을 세팅한다.
  - [x] header에 content-length를 추가한다.

## 도메인 요구사항
### message
- [ ] request, response 타입으로 나뉜다.
- [ ] start-line, header-field, empty-line, message-body로 구성되어있다.
- [ ] start-line, header-field는 필수 이다.
- [ ] message-body는 null 일 수 있다.

### start line
- [ ] request인 경우 request-line
  - [x] method, request-target, http-version으로 이루어져 있다.
  - [x] 모두 null일 수 없다.
- [ ] response인 경우 status-line
  - [x] http-version, status-code, reason-phase로 이루어져있다.
  - [x] 모두 null일 수 없다.

### request-target

### header
- [x] header는 key(field-name)-value(field-value) 모음이다.

### body
- [ ] 반환할 파일을 date type으로 가진다.
- [ ] null 일 수 있다.

## 고민 사항
- 인터페이스를 만드는 것이 좋을까?
