# be-w56-java-was
56주차 간단 웹 서버 구현

## step1
### 새롭게 알게 된 내용 및 적용한 것들
- d2hub
  - 로컬의 이미지 d2hub에 푸쉬하기
    - docker login idock.daumkaka.io -u id -p password
    - docker login idock.daumkakao.io : 로그인 확인
    - docker tag 이미지명 d2hub저장소 : image tag 명 d2hub 와 동일하게 일치시켜주기
    - docker push d2hub 저장소 : push
- krane 에서 docker image pull
  - https://kakao.agit.in/g/300023877/wall/343060071

## step2
### 새롭게 알게 된 내용 및 적용한 것들
- request 를 담당할 HttpRequest 생성
- builder 패턴 적용(User)
- MIME mozila 를 보고 각 파일에 적합한 extension 을 반환했으나 적용 X ㅠ
- InputStream close

## step2 feedback
- response 에서 status 를 처리해주거나 controller, handle 에서 처리해주기

## step 5
- enum 공부
- controller handle 메서드 분리
- 테스트 메서드 작성
  - unit test
  - HttpResponseUtils 내부의 학습 test