# be-w56-java-was
5,6주차 간단 웹 서버 구현

# step1 
- 알게된점
    1. 제일 처음 html을 가져오고 해당 파일 안에있는 css,js정보를 통해 추가 request를 요청한다는점

- 문제 해결
    1. 처음 index.html을 요청하니 css가 적용이 되지 않았다. -> response에 context-type을 text/html로 고정되어 있던부분을 수정해줬다.