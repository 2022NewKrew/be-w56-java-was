# be-w56-java-was

## 1단계
- 정적 파일 응답
  - File 객체를 이용해서 html, css, js 파일등의 응답
  - html, css, js 등 text/html, text/css, text/javascript 와 같이 content-type을 다르게 설정 해주어야 하는데 아예 지워버리니 무슨 이유인지 모르겠지만 일단 정상작동,,,
  - html이 참조하는 파일에 대해서는 별도의 조작을 하지 않아도 자동으로 요청을 보냄