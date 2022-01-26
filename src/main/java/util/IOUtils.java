package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

public class IOUtils {

  /**
   * @param BufferedReader는 Request Body를 시작하는 시점이어야
   * @param contentLength는  Request Header의 Content-Length 값이다.
   * @return
   * @throws IOException
   */
  public static String readData(BufferedReader br, int contentLength) throws IOException {
    char[] body = new char[contentLength];
    br.read(body, 0, contentLength);
    return String.copyValueOf(body);
  }


  /**
   * 파일 확장자 가져 옴.
   *
   * @param fileName
   * @return
   */
  public static Optional<String> getExtension(String fileName) {
    return Optional.ofNullable(fileName)
        .filter(f -> f.contains("."))
        .map(f -> f.substring(fileName.lastIndexOf(".") + 1));
  }

}
