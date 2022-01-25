# be-w56-java-was
5,6주차 간단 웹 서버 구현

# step1 
- [x] path에 해당하는 파일 읽어 응답하기 힌트(1단계 요구사항)


### 알게된점
- 제일 처음 html을 가져오고 해당 파일 안에있는 css,js정보를 통해 추가 request를 요청한다는점

### 문제 해결
- 처음 index.html을 요청하니 css가 적용이 되지 않았다. -> response에 context-type을 text/html로 고정되어 있던부분을 수정해줬다.


# step2
- [ ] Get방식 회원가입 구현(2단계 요구사항)

### 알게된점
- 정적 팩토리 메소드(Request 클래스에서 queryString을 String형태에서 Map형태로 바꾸기 위해 처음엔 set함수를 사용했지만, 다른곳에서 set함수를 무분별하게 사용할 수 있기에 정적팩토리 메소드를 구현해주었습니다)