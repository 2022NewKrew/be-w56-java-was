# be-w56-java-was
56주차 간단 웹 서버 구현

###요구사항 1: 정적인 html 파일 응답
```java
BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
```
활용하여 InputStreamReader 를 BufferedReader로 읽을 수 있게 할 수 있다.
```java
String[] tokens = line.split(" ");
```
활용하여 string을 split할 수 있다.
```java
byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
```
활용하여 url에 있는 file 경로를 읽을 수 있다.

####추가사항
``` java
Map<String, String> headerMap = HttpServletRequestUtils.readHeader(br);
if(headerMap.containsKey("Accept")){
    contextType = headerMap.get("Accept").split(",")[0];
}

dos.writeBytes("Content-Type: "+contextType+";charset=utf-8\r\n");
```
를 활용하여 contextType을 읽은 후에 writeBytes 를 통하여 response에 추가하여 준다.

