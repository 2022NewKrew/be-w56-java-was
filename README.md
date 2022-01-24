# be-w56-java-was
56주차 간단 웹 서버 구현

### Step1 - 정적인 html 파일 응답

1. 파일 파싱 후 바이트로 변환
    ``` java
    byte[] body = Files.readAllbytes(new File("./webapp" + url).toPath());
    ```
   - 파일을 읽어서 String으로 변환하는 방법 외에도 byte 배열을 전달하는 방법
   

2. DataOutputStream
    ``` java
   DataOutputStream dos = new DataOutputStream(out);
   dos.write(body, 0, body.length);
   dos.flush();
    ```
   - DataOutputStream : String 뿐만 아니라 int, double, byte와 같은 primitive타입들을 전달할 수 있는 OutputStream


3. URL Parsing 구현
   ``` java
   public static String parseUrl(String requestLine){
        String[] requestLineTokens = requestLine.split(" ");
        String url = requestLineTokens[1];

        if(url.equals("/")){
            return "/index.html";
        }

        return url;
    }
   ```
   - requestLine을 받아서 URL을 파싱하는 부분
   - / 로 요청하는 경우에는 /index.html로 파싱되도록 설정
