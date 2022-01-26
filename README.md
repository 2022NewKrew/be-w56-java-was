# be-w56-java-was
56주차 간단 웹 서버 구현

### 요구사항
---
- `http://localhost:8080/index.html` 로 접속했을 때 webapp 디렉토리의 index.html 파일을 읽어 클라이언트에 응답한다
- “회원가입” 메뉴를 클릭하면 `http://localhost:8080/user/form.html` 으로 이동하면서 회원가입 폼을 표시한다.
    - 이 폼을 통해서 회원가입을 할 수 있다.
    - 회원가입을 하면 다음과 같은 형태로 사용자가 입력한 값이 서버에 전달된다.
        - `/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net`
    - HTML과 URL을 비교해 보고 사용자가 입력한 값을 파싱해 model.User 클래스에 저장한다.
