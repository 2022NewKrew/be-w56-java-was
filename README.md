# be-w56-java-was
56주차 간단 웹 서버 구현

## 기능 요구사항
- [x] 정적 파일 응답하기
- [x] GET 요청으로 회원가입
- [x] 회원가입 API를 POST로 변경
- [ ] Cookie를 이용하여 로그인 구현

## API
| 메소드            | URL               | 내용      | 비고  |
|----------------|-------------------|---------|-----|
| GET| /index.html       | 메인 페이지  |     |
|GET| /user/form/html   | 회원가입 페이지 |     |
|POST| /user/create      | 회원가입 |     |

## 프로젝트 구조
| 위치                                                 | 내용                                                                                                                       |
|----------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------|
| /webserver/WebServer.java                          | connection 생성 후 RequestHandler에게 전달                                                                                      |
| /webserver/RequestHandler.java                     | InputStream으로부터 HttpRequestBuilder를 이용하여 HttpRequest를 빌드.<br>정적 파일 요청이면 StaticResourceManager에게, 아니라면 WebController에게 전달 |
| /webserver/controller/WebController.java           | 컨트롤러 역할 수행<br>route() 안에서 요청에 맞는 메소드로 매핑                                                                                 |
| /webserver/core/StaticResourceManager.java         | 정적 파일 관리 담당<br>로드 시점에 "./webapp" 하위 경로에 있는 파일들의 이름을 가져옴.                                                                 |
| /webserver/core/TemplateEngine.java                | WebController가 responseBody를 생성할 때 사용<br>나중에 서버사이드에서 렌더링 할 일이 생기면 이 곳에 추가                                                |
| /webserver/core/http/Header.java                   | 요청과 응답에 있는 헤더 정보. 키값쌍으로 구성됨                                                                                              |
| /webserver/core/http/HttpClientErrorException.java |                                                                                                                          |
| /webserver/core/http/request/                      |                                                                                                                          |                                                                                                               
| /webserver/core/http/response/                     |                                                                                                                          |

