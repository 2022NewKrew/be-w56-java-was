package service;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public class WebService {

    private static final String URL_PREFIX = "./webapp";
    private static final Logger log = LoggerFactory.getLogger(WebService.class);

    public static HashMap<String, String> parseRequest(BufferedReader bufferedReader) throws Exception {

        String method = "";
        String toURL = "";
        String contentLength = "";

        while(true){
            String next = bufferedReader.readLine().trim();
            if(next.isEmpty()){
                break;
            }
            String[] splitLine = next.split(" ");
            if (splitLine[0].equals("POST")) {
                method = "POST";
                toURL = splitLine[1];
            }
            if (splitLine[0].equals("GET")) {
                method = "GET";
                toURL = splitLine[1];
            }
            if (splitLine[0].equals("Content-Length:")) {
                contentLength = splitLine[1];
            }
        }

        log.debug("Extract results, method : {}, toURL : {}, contentLength : {}", method, toURL, contentLength);
        HashMap<String, String> requestParse = new HashMap<>();
        requestParse.put("method", method);
        requestParse.put("URL", toURL);
        requestParse.put("contentLength", contentLength);

        if (method.equals("POST") & !contentLength.isEmpty()){
            String body = IOUtils.readData(bufferedReader, Integer.parseInt(contentLength));
            requestParse.put("body", body);
        }

        return requestParse;
    }

    public static User createUser(String testURL){

        HashMap<String, String> parameterMap = parseURLBody(testURL);

        User createUser = new User(parameterMap.get("userId"), parameterMap.get("password"), parameterMap.get("name"), parameterMap.get("email"));
        DataBase.addUser(createUser);

        return createUser;
    }

    public static Boolean loginUser(String urlBody){

        HashMap<String, String> parameterMap = parseURLBody(urlBody);
        User loadUser = DataBase.findUserById(parameterMap.get("userId"));

        if (loadUser==null){
            return false;
        }
        else if (parameterMap.get("password").equals(loadUser.getPassword())){
            return true;
        }
        return false;
    }

    public static String userList(){
        Path path = Path.of(URL_PREFIX + "/user/list.html");
        Collection<User> userCollection = DataBase.findAll();
        try {
            StringBuilder htmlFile = new StringBuilder(Files.readString(path));
            StringBuilder sb = new StringBuilder();

            userCollection.forEach(user ->
                    sb.append("                <tr>\n" + "                    <th scope=\"row\">1</th> <td>")
                            .append(user.getUserId())
                            .append("</td> <td>")
                            .append(user.getName())
                            .append("</td> <td>")
                            .append(user.getEmail())
                            .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n")
                            .append("                </tr>\n"));

            htmlFile.append(sb);
            htmlFile.append(
                    "              </tbody>\n" +
                            "          </table>\n" +
                            "        </div>\n" +
                            "    </div>\n" +
                            "</div>\n" +
                            "\n" +
                            "<!-- script references -->\n" +
                            "<script src=\"../js/jquery-2.2.0.min.js\"></script>\n" +
                            "<script src=\"../js/bootstrap.min.js\"></script>\n" +
                            "<script src=\"../js/scripts.js\"></script>\n" +
                            "\t</body>\n" +
                            "</html>\n"
            );

            log.debug(htmlFile.toString());
            return htmlFile.toString();

        } catch (Exception e){
            log.debug(e.getMessage());
        }

        return "";
    }

    public static HashMap<String, String> parseURLBody(String body){
        String[] parameter = body.split("&");
        HashMap<String, String> parameterMap = new HashMap<>();

        Arrays.stream(parameter).forEach(s -> parameterMap.put(s.split("=")[0], s.split("=")[1]));
        log.debug("parseURLBody, parameter get {}", parameterMap.toString());

        return parameterMap;
    }

    public static String extractFunction(String ansURL){
        String[] list = ansURL.split("\\?")[0].split("/");

        if (list.length > 0){
            return list[list.length-1];
        }
        return "";
    }

    public static byte[] openUrl(String url){

        String fullURL = URL_PREFIX + url;
        Path openURL = Paths.get(fullURL);

        try{
            return Files.readAllBytes(openURL);
        } catch(Exception e){
            log.debug("openURL exception {}", openURL);
            byte[] bytes = {};
            return bytes;
        }
    }

}
