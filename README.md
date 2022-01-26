# be-w56-java-was
56주차 간단 웹 서버 구현

## 2일차
### TODO
* Request의 body를 parsing
* custom exception 구현(In next step)
### DONE
* GET으로 회원가입 기능 구현
  * service, DTO, controller 구현
  * controller에 전달할수 있도록 RequestHandler에 회원가입을 요청하는 경우를 다룰 수 있는 로직 추가
    * 각 메소드와 uri값에 대한 method mapping을 할 수 있도록 ControlMappingTable 객체 생성
    * uri 값에 대한 control 메소드가 존재하지 않을 경우에 사용되는 DefaultRequestHandler class 구현
    * 각각의 Response 값에 따른 Response를 작성해줄 수 있는 HttpResponseUtils 작성
      * 아직은 200, 404만 구현
  * Content Type을 enum 객체로 구현
  * Request의 body를 contentLength를 기반으로 입력받을 수 있는 기능
### LEARNED
* 403, 404 reponse의 구조
* 메소드를 저장할 수 있게 하는 java.util.function.Function에 대한 공부