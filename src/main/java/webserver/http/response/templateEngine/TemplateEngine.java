package webserver.http.response.templateEngine;

import domain.model.Memo;
import domain.model.User;
import webserver.config.WebConst;
import webserver.http.response.HtmlCreatorMethod;
import webserver.http.response.HttpResponse;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

public class TemplateEngine {

    public static void run(HttpResponse res) {
        try {
            String staticHtml = getStaticHtml(res.getUrl());
            List<Node> nodes = TemplateParser.parse(staticHtml);
            buildHtml(nodes, res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void buildHtml(List<Node> nodes, HttpResponse res) {
        StringBuilder sb = new StringBuilder();

        int cur = 0;
        while (cur < nodes.size()) {
            Node node = nodes.get(cur);
            NodeType type = node.getType();
            if(type == NodeType.PLAIN) {
                sb.append(node.getData());
            } else if(type == NodeType.DATA) {
                try {
                    Object attributeType = getAttributeType(node, res);
                    String fieldName = getFieldNameFromNode(node);
                    Object attributeValue = getAttributeValue(attributeType, fieldName);
                    sb.append(attributeValue);
                } catch (Exception e){
                    e.printStackTrace();
                }
            } else if(type == NodeType.LOOP) {
                List<Node> loopNodes = new ArrayList<>();
                cur += 1;
                Node nextNode = nodes.get(cur);
                while(nextNode.getType() != NodeType.LOOP_END) {
                    loopNodes.add(nextNode);
                    cur += 1;
                    nextNode = nodes.get(cur);
                }

                String attribute = node.getData();
                List<Object> dataList = (List)res.getModel().getAttribute(attribute);
                for (Object o : dataList) {
                    for (Node loop : loopNodes) {
                        if(loop.getType() == NodeType.PLAIN) {
                            sb.append(loop.getData());
                        } else if(loop.getType() == NodeType.DATA) {
                            try {
                                Object attributeValue = getAttributeValue(o, loop.getData());
                                sb.append(attributeValue);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if(type == NodeType.IMPORT) {
                try {
                    String data = node.getData();
                    String[] split = data.split(".");
                    String filePath = Arrays.stream(split).reduce("", (a, acc) -> a + "/" + acc);
                    String staticHtml = getStaticHtml(filePath);
                    sb.append(staticHtml);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            cur += 1;
        }
        String body = sb.toString();
        res.setBody(body.getBytes(StandardCharsets.UTF_8));
    }

    private static String getFieldNameFromNode(Node node) {
        String data = node.getData();
        String[] nameAndData = data.split(".");
        return nameAndData[1];
    }

    private static Object getAttributeType(Node node, HttpResponse res) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        String data = node.getData();
        String[] nameAndData = data.split(".");
        String className = nameAndData[0];
        String firstName = className.substring(0, 1);
        String restName = className.substring(1, className.length());
        className = firstName.toUpperCase() + restName;
        Class<?> aClass = Class.forName(className);
        return aClass.cast(res.getModel().getAttribute(nameAndData[0]));
    }

    private static Object getAttributeValue(Object obj, String fieldName) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field declaredFields = aClass.getDeclaredField(fieldName);
        declaredFields.setAccessible(true);
        return declaredFields.get(obj);
    }



//    private static Map<String, HtmlCreatorMethod> handlers = new HashMap<>() {{
//       put("/user/list.html", (res) -> {
//           userList(res);
//       });
//
//       put("/index.html", (res) -> {
//            memoList(res);
//       });
//    }};
//
//    public static void createHtml(HttpResponse res) throws IOException {
//        if(res.getModel().isEmpty()) {
//            res.setBody(Files.readAllBytes(new File("./webapp" + res.getUrl()).toPath()));
//            return;
//        }
//
//        HtmlCreatorMethod htmlCreatorMethod = handlers.get(res.getUrl());
//        if(htmlCreatorMethod == null) {
//            return;
//        }
//        htmlCreatorMethod.create(res);
//    }
//
//
//    private static void userList(HttpResponse res) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = getBufferReader(res.getUrl());
//
//        String line;
//        while((line = br.readLine()) != null) {
//            sb.append(line);
//            if(line.contains("<tbody>")) {
//                break;
//            }
//        }
//
//        List<User> users = (List<User>) res.getModel().getAttribute("users");
//
//        users.stream().forEach(user -> {
//            sb.append(String.format(
//                    "<tr>\n" +
//                    "<th scope=\"row\">1</th> <td>%s</td> <td>%s</td> <td>%s</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>\n" +
//                    "                </tr>", user.getUserId(), user.getName(), user.getEmail()));
//        });
//
//        while((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
//        res.setBody(bytes);
//    }
//
//    private static void memoList(HttpResponse res) throws IOException {
//        StringBuilder sb = new StringBuilder();
//        BufferedReader br = getBufferReader(res.getUrl());
//
//        String line;
//        while((line = br.readLine()) != null) {
//            sb.append(line);
//            if(line.contains("<tbody>")) {
//                break;
//            }
//        }
//
//        List<Memo> memos = (List<Memo>) res.getModel().getAttribute("memos");
//        memos.stream().forEach(memo -> {
//            sb.append(String.format(
//                    "              <tr>\n" +
//                    "                  <th scope=\"row\">%d</th> <td>%s</td> <td>%s</td> <td>%s</td>\n" +
//                    "              </tr>", memo.getId(), memo.getCreatedAt(), memo.getAuthor(), memo.getContent()));
//        });
//
//        while((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
//        res.setBody(bytes);
//    }

    private static BufferedReader getBufferReader(String url) throws FileNotFoundException {
        try {
            return new BufferedReader(new FileReader(WebConst.URL_PREFIX + url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String getStaticHtml(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = getBufferReader(url);
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
