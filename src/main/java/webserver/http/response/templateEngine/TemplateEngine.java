package webserver.http.response.templateEngine;

import webserver.config.WebConst;
import webserver.http.response.HttpResponse;
import webserver.http.response.templateEngine.exception.TemplateFormatMismatchException;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TemplateEngine {

    public static byte[] run(HttpResponse res) {
        String staticHtml = getStaticHtml(res.getUrl());

        if(res.getUrl().endsWith(".html")) {
            List<Node> nodes = TemplateParser.parse(staticHtml);
            return buildHtml(nodes, res);
        } else {
            return staticHtml.getBytes(StandardCharsets.UTF_8);
        }
    }

    private static String getStaticHtml(String url) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = getBufferReader(url);
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] buildHtml(List<Node> nodes, HttpResponse res) {
        StringBuilder sb = new StringBuilder();
        Iterator<Node> iterator = nodes.iterator();

        while(iterator.hasNext()) {
            Node node = iterator.next();
            NodeType type = node.getType();
            switch (type) {
                case PLAIN:
                    sb.append(node.getData());
                    break;
                case IMPORT:
                    String filePath = getPath(node.getData());
                    String staticHtml = getStaticHtml(filePath);
                    sb.append(staticHtml);
                    break;
                case DATA:
                    appendToResult(sb, node, res);
                    break;
                case LOOP:
                    List<Node> loopNodes = getNodeInLoop(iterator);
                    String attribute = node.getData();
                    List<Object> domainList = (List)res.getModel().getAttribute(attribute);

                    for (Object domainClass : domainList) {
                        for (Node loopNode : loopNodes) {
                            if(loopNode.getType() == NodeType.PLAIN) {
                                sb.append(loopNode.getData());
                            } else if(loopNode.getType() == NodeType.DATA) {
                                Object attributeValue = getAttributeValue(domainClass, loopNode.getData());
                                sb.append(attributeValue);
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static List<Node> getNodeInLoop(Iterator<Node> iterator) {
        List<Node> results = new ArrayList<>();
        while(iterator.hasNext()) {
            Node nextNode = iterator.next();
            if(nextNode.getType() == NodeType.LOOP_END) {
                return results;
            }
            results.add(nextNode);
        }
        //no End
        throw new TemplateFormatMismatchException();
    }

    private static void appendToResult(StringBuilder sb, Node node, HttpResponse res) {
        try {
            Object attributeType = getAttributeType(node, res);
            String fieldName = getFieldNameFromNode(node);
            Object attributeValue = getAttributeValue(attributeType, fieldName);
            sb.append(attributeValue);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String getFieldNameFromNode(Node node) {
        String data = node.getData();
        String[] nameAndData = data.split(".");
        return nameAndData[1];
    }

    private static Object getAttributeType(Node node, HttpResponse res) {
        String data = node.getData();
        String[] nameAndData = data.split(".");
        return res.getModel().getAttribute(nameAndData[0]);
    }

    private static Object getAttributeValue(Object obj, String fieldName) {
        try {
            Class<?> aClass = obj.getClass();
            Field declaredFields = aClass.getDeclaredField(fieldName);
            declaredFields.setAccessible(true);
            return declaredFields.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static BufferedReader getBufferReader(String url) throws FileNotFoundException {
        try {
            return new BufferedReader(new FileReader(WebConst.URL_PREFIX + url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String getPath(String data) {
        String[] split = data.split(".");
        return Arrays.stream(split).reduce("", (a, acc) -> a + "/" + acc);
    }
}
