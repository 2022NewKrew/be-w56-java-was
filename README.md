# be-w56-java-was
56주차 간단 웹 서버 구현

# Step 1
Request 정보를 저장하는 RequestHeader Class 추가
request 정보를 읽어와 method / uri / protocol로 분리.
uri에 .이 없을 시 .html 추가

(kina의 도움으로) Content Type 수정하는 부분 추가.

# Step 2
GET 방식으로 회원가입 구현.

# Step 3
POST 방식으로 회원가입 구현.
ResponseHeader를 추가하여 응답 정보를 저장하도록 함.
302 Found -> 302 Redirect로 변경.
해당 부분에서 애를 많이 먹음. (Found사용 시, /user/create에서 이동한 뒤 다른 메뉴를 누르면 /user/index, /user/user/form등 기존 경로가 남아있게 됨.)
