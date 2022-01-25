# be-w56-java-was
5,6주차 간단 웹 서버 구현

# step1 
- [x] path에 해당하는 파일 읽어 응답하기 힌트(1단계 요구사항)


### 알게된점
- 제일 처음 html을 가져오고 해당 파일 안에있는 css,js정보를 통해 추가 request를 요청한다는점

### 문제 해결
- 처음 index.html을 요청하니 css가 적용이 되지 않았다. -> response에 context-type을 text/html로 고정되어 있던부분을 수정해줬다.


# step2
- [x] Get방식 회원가입 구현(2단계 요구사항)

### 알게된점
- 정적 팩토리 메소드(Request 클래스에서 queryString을 String형태에서 Map형태로 바꾸기 위해 처음엔 set함수를 사용했지만, 다른곳에서 set함수를 무분별하게 사용할 수 있기에 정적팩토리 메소드를 구현해주었습니다)

### 회고
- 이번 프로젝트는 조금 새로운 주제라 프로젝트 구조를 어떻게 설정해야 하는지 고민을 하다가 결국 3-4주차 스프링프로젝트의 경험을 바탕으로 mvc패턴과 비슷하게 진행했습니다. mvc패턴을 기반으로 request와 response를 객체화 시켜서 주고받는 개념으로 진행하였습니다

# step3
- [x] post방식 회원가입 완료