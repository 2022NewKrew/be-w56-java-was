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
    - 해당 방식은, 실제 파일의 데이터 타입과 정보불일치가 발생할 수 있어서 MIME 타입을 얻어 반환하는 형태로 수정
      - https://offbyone.tistory.com/330
- 데이터를 받는 절차를 구성
  1. Request 헤더 파싱 
  2. Get/Post 에 따라 수행할 요청을 구분 ( interface )
  3. 경로에 따라 세부적인 작업을 수행 ( service 함수에서 작업을 선택 )
- 쿼리문을 저장하는 Map 을 추가로 구현
- RequestBody 는 Header + 한 줄 공백 뒤에 등장
  - body 데이터는 문자열 뒤에 \n 또는 EOF 가 존재하지 않음
  - 기존 BufferedReader 의 ReadLine 에서 IO-Blocking 이 발생하는 원인
  - read(char[]) 함수를 사용하도록 구조를 변경 ( content-type 크기만큼만 읽도록 수정 )
- Redirect 의 경우, 상태값을 302 를 사용한다고 함
  - 하지만 302는 기존 메소드를 그대로 사용하고, POST/PUT 을 GET 으로 바꾸는 용도로 303을 사용
    - https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/303
  - Header 에 Location 정보를 입력하면, 클라이언트는 해당 Location 을 GET 으로 요청 ( redirect )
  - 클라이언트의 URL 도 함께 바뀌게되고, 해당 페이지가 허용하는 경로 내에선 Path 정보만으로 자유롭게 이동도 가능
- interface 의 default Method 는 별도의 접근 지정자 선언이 불가능
- 303 상태값을 사용하니 set-cookie 명령이 작동하지 않는 문제 발생
  - https://stackoverflow.com/questions/63049618/browser-does-not-send-cookies-when-cookie-is-set-on-a-303-redirect
  - ~~-정확한 설명은 찾기 힘들지만, 다른 페이지로도 완전히 넘길 수 있는 redirect 특징때문에 적용되지 않는 것으로 유추됨~~
  - 근데 또 내용상으로는 가능하다고 언급은 되는 것 같은데, 코드상 뭐가 문제가 생긴건지 잘 파악이 안되는 상태
    - https://bugs.chromium.org/p/chromium/issues/detail?id=696204
    - Cookie 세팅 과정에서 데이터 삽입 조건에서 문제를 발견 
      - cookie!=null && [ ! ] "".equals(cookie.trim())
      - 항상 조건식이 false 로 나와서 적용이 안되었던 것
      - 또한 각 Header 는 \r\n 을 반드시 필요로 한다는 점에 유의
- 깃허브 토근 만료 문제
  - 해당 토큰의 regenerate 를 통해서 해결
  - 비밀번호 대신, PAT 의 문자열을 입력

# 참고 사이트
- HTTP Header
  - https://dev-ezic.tistory.com/8?category=773711
- ACCEPT 와 CONTENT-TYPE
  - https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Accept
  - https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Content-Type
- MIME TYPE
  - https://offbyone.tistory.com/330
  - https://stackoverflow.com/questions/51438/how-to-get-a-files-media-type-mime-type
- 302 Status ( redirect )
  - https://developer.mozilla.org/ko/docs/Web/HTTP/Status/302
  - https://nsinc.tistory.com/168
  - https://cherrypick.co.kr/avoid-location-header-cache-in-brower-using-303-http-code/
- 303 with set-cookie
  - https://stackoverflow.com/questions/63049618/browser-does-not-send-cookies-when-cookie-is-set-on-a-303-redirect
  - https://bugs.chromium.org/p/chromium/issues/detail?id=696204
- github > personal access token
  - https://github.com/settings/tokens
  - https://miracleground.tistory.com/entry/GitHub-%ED%86%A0%ED%81%B0-%EC%9D%B8%EC%A6%9D-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%95%98%EA%B8%B0-%EC%98%A4%EB%A5%98-%ED%95%B4%EA%B2%B0-remote-Support-for-password-authentication-was-removed-on-August-13-2021-Please-use-a-personal-access-token-instead


