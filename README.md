# be-w56-java-was
- 56주차 간단 웹 서버 구현

<br>

----

## Step1. 정적인 html 파일 응답

> Request Header를 출력하고, Request Line에서 path를 분리한다. 
> 또한, 이것을 읽어내 `localhost:8080/index.html`을 실행하였을 때, `webapp/index.html`을 응답할 수 있도록 한다.

- `IOUtils.readMimeType` : url에 해당하는 파일의 mime type을 반환하는 메서드
- `HttpRequestUtils.parseRequestUrl` : request 요청 중 url만 분리하는 메서드

<br>

## File의 Mime Type을 알아내는 방법
- `Apache Tika`
  - build.gradle에 추가한 이후 활용
    - `org.apache.tika:tika-core`
    - Mime type을 찾는 메서드만 활용하기 위해, `tika-core` 적용
```java
class TikaTest{
    @Test
    public void whenUsingTika_thenSuccess() {
        File file = new File("product.png");
        Tika tika = new Tika();
        String mimeType = tika.detect(file);

        assertEquals(mimeType, "image/png");
    }
}
```
