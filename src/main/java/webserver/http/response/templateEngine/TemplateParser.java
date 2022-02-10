package webserver.http.response.templateEngine;

import webserver.http.response.templateEngine.exception.TemplateFormatMismatchException;

import java.util.ArrayList;
import java.util.List;

public class TemplateParser {

    public static List<Node> parse(String body) {
        ArrayList<Node> nodes = new ArrayList<>();
        char[] chars = body.toCharArray();

        int start = 0;
        int pos = 0;

        while(pos != chars.length) {
            if(chars[pos] == '{' && chars[pos + 1] == '{') {
                if (start != pos) {
                    Node plainNode = createPlainNode(start, pos - 1, body);
                    nodes.add(plainNode);
                }
                if (chars[pos + 2] == '>') {
                    start = pos + 3;
                    pos = findClose(pos, chars);
                    Node importNode = createImportNode(start, pos - 1, body);
                    nodes.add(importNode);
                } else if (chars[pos + 2] == '#') {
                    start = pos + 3;
                    pos = findClose(pos, chars);
                    Node loopNode = createLoopNode(start, pos - 1, body);
                    nodes.add(loopNode);
                } else if (chars[pos + 2] == '/') {
                    start = pos + 3;
                    pos = findClose(pos, chars);
                    Node loopNode = createLoopEndNode(start, pos - 1, body);
                    nodes.add(loopNode);
                } else {
                    start = pos + 2;
                    pos = findClose(pos, chars);
                    Node dataNode = createDataNode(start, pos - 1, body);
                    nodes.add(dataNode);
                }
                start = pos + 2;
                pos = start;
            } else {
                pos += 1;
            }
        }
        if(pos != start) {
            Node plainNode = createPlainNode(start, pos - 1, body);
            nodes.add(plainNode);
        }

        return nodes;
    }

    private static int findClose(int pos, char[] chars) {
        while(chars[pos] != '}' || chars[pos + 1] != '}') {
            pos += 1;
            if(pos == chars.length - 1) {
                throw new TemplateFormatMismatchException();
            }
        }
        return pos;
    }

    private static Node createPlainNode(int start, int end, String body) {
        Node node = new Node();

        node.setData(body.substring(start, end + 1));
        return node;
    }

    private static Node createImportNode(int start, int end, String body) {
        Node node = new Node();
        node.setType(NodeType.IMPORT);
        node.setData(body.substring(start, end + 1));
        return node;
    }
    private static Node createLoopNode(int start, int end, String body) {
        Node node = new Node();
        node.setType(NodeType.LOOP);
        node.setData(body.substring(start, end + 1));
        return node;
    }
    private static Node createDataNode(int start, int end, String body) {
        Node node = new Node();
        node.setType(NodeType.DATA);
        node.setData(body.substring(start, end + 1));
        return node;
    }


    private static Node createLoopEndNode(int start, int end, String body) {
        Node node = new Node();
        node.setType(NodeType.LOOP_END);
        node.setData(body.substring(start, end + 1));
        return node;
    }

//    private static int addNode(int cur, char[] chars, ArrayList<Node> nodes) {
//        Node node = new Node();
//        List<Character> charArray = new ArrayList<>();
//
//        int start = cur;
//        int end = cur;
//
//        while(cur < chars.length) {
//            if()
//        }
//
//        nodes.add(node);
//        return end + 1;
//    }
}
