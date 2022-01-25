# be-w56-java-was
56주차 간단 웹 서버 구현

# 경험
- 요청에 대해서 응답 결과가 제대로 출력되지 않는 문제가 발생
  - 1차 원인은 요청에 대한 반환값
    - HTML 파일에 존재하는 CSS, image 등의 링크를 통해 서버에 2,3번째 요청을 계속적으로 보내는데
    - 코드상에선 요청의 종류와 관계없이 무조건적으로 HTML 파일을 읽어서 반환하는 형태를 사용함
    - 이런 점으로 인해 css 파일이 정상적인 파일이 아닌 HTML 파일로 사용자에게 노출되는 문제가 존재
    - Request 헤더의 최상단 문자열에서 split 으로 경로를 얻어오는 방식으로 읽어오는 파일을 결정하는 형태로 수정
  - 2차 원인은 Content-Type 의 문제
    - CSS / Image 는 일반적인 HTML 파일과는 다른 형태로 데이터를 인식시켜야 함
    - 하지만 제공된 코드 중, response200Header 에서 해당 Content-Type 을 text/html; 로 고정시켜 사용 중이었음
    - 해당 정보는 Accept 값 내에 존재하므로, 이를 사용하도록 코드를 수정 ( https://dev-ezic.tistory.com/8?category=773711 )
    - 이 과정에서 request 정보를 분리하고 담아야 할 필요성이 발생하여 Map 타입 객체에 정보를 담아 사용하도록 변경
    - 다만, 위의 내용만으로는 여전히 해결되지 않음
    - Accept 내용이 너무 과도하게 긴 점이 의심스러워, "," 를 기준으로 잘라내어 첫 문자열만을 사용하도록 변경하니 문제가 해결됨
      - Accept 의 경우, 클라이언트가 서버에서 전송을 요청하는 여러 데이터 타입 목록을 나열한 것
      - Content-type 의 경우, 서버가 클라이언트로 전송하는 데이터의 타입 (해석 방법)
- 데이터를 받는 절차를 구성
  1. Request 헤더 파싱 
  2. Get/Post 에 따라 수행할 요청을 구분 ( interface )
  3. 경로에 따라 세부적인 작업을 수행 ( service 함수에서 작업을 선택 )
- 쿼리문을 저장하는 Map 을 추가로 구현
- RequestBody 는 Header + 한 줄 공백 뒤에 등장
  - body 데이터는 문자열 뒤에 \n 또는 EOF 가 존재하지 않음
  - 기존 BufferedReader 의 ReadLine 에서 IO-Blocking 이 발생하는 원인
  - read(char[]) 함수를 사용하도록 구조를 변경 ( content-type 크기만큼만 읽도록 수정 )

# 참고 사이트
- HTTP Header
  - https://dev-ezic.tistory.com/8?category=773711
- ACCEPT 와 CONTENT-TYPE
  - https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Accept
  - https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type