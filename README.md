# be-w56-java-was
56주차 간단 웹 서버 구현

## 1일차
### TODO-From Review
* HttpRequestUtils에서 parseRequest의 로직을 Request의 생성자로 이동
* Request에 Lombok 다시 사용
* RequestHandler내의 try-catch문을 재구성
    * try 내의 로직을 메소드로 추출
    * catch 시에 적절한 Response를 날려보자
### DONE
* 정적인 html 파일 응답
    * 모든 Request Header 출력하기
    * Request Line에서 path 분리하기
    * path에 해당하는 파일 읽어 응답하기
### LEARNED
* GET Request format
* Response content type