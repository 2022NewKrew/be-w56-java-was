package templateEngine;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Stack;

public class CyEngineParser {

    public CyEngineNode parse(Path path) {
        try {
            return parse(Files.readString(path));
        } catch (IOException e) {
            throw new RuntimeException("해당 탬플릿 파일이 없습니다.");
        }
    }

    private CyEngineNode parse(String html) {
        final StringBuilder sb = new StringBuilder();
        final Stack<CyEngineNode> nodeStack = new Stack<>();

        final CyEngineNode root = new CyEngineNode(NodeType.ROOT, null, null, null);
        nodeStack.push(root);

        int pos = 0;
        while (pos < html.length()) {
            if (isOpenBracket(pos, html)) {
                addTextNode(sb.toString(), nodeStack.peek());
                sb.setLength(0);
                pos += 2;

                NodeType nodeType = NodeType.VALUE;
                if (isLoopType(pos, html)) {
                    nodeType = NodeType.LOOP;
                    pos += 4;
                } else if (isIfType(pos, html)) {
                    nodeType = NodeType.Condition;
                    pos += 2;
                }

                final StringBuilder sb2 = new StringBuilder();
                while (pos < html.length() && isNotEndBracket(pos, html)) {
                    sb2.append(html.charAt(pos++));
                }
                pos += 2;

                final String index = sb2.toString().trim();
                final CyEngineNode parent = nodeStack.peek();

                final CyEngineNode node = new CyEngineNode(nodeType, parent, index, null);
                parent.addChild(node);

                if (nodeType == NodeType.Condition || nodeType == NodeType.LOOP) {
                    nodeStack.push(node);
                }

                continue;
            }

            if (isCloseBracket(pos, html)) {
                addTextNode(sb.toString(), nodeStack.peek());
                sb.setLength(0);
                pos += 3;

                final StringBuilder sb2 = new StringBuilder();
                while (pos < html.length() && isNotEndBracket(pos, html)) {
                    sb2.append(html.charAt(pos++));
                }
                pos += 2;

                final String index = sb2.toString().trim();
                final CyEngineNode topNode = nodeStack.pop();

                if (topNode.notEqualIndex(index)) {
                    throw new RuntimeException("파싱 에러 - 올바르지 않은 템플릿 입니다.");
                }
                continue;
            }

            sb.append(html.charAt(pos++));
        }

        if (nodeStack.size() != 1) {
            throw new RuntimeException("파싱 에러 - 올바르지 않은 템플릿 입니다.");
        }

        addTextNode(sb.toString(), nodeStack.peek());
        sb.setLength(0);

        return root;
    }

    private void addTextNode(String contents, CyEngineNode parent) {
        final CyEngineNode text = new CyEngineNode(NodeType.TEXT, parent, null, contents);
        parent.addChild(text);
    }

    private boolean isNotEndBracket(int pos, String html) {
        return !(html.charAt(pos) == '}' && (pos + 1 < html.length() && html.charAt(pos + 1) == '}'));
    }

    private boolean isOpenBracket(int pos, String html) {
        return html.charAt(pos) == '{' &&
                (pos + 1 < html.length() && html.charAt(pos + 1) == '{') &&
                (pos + 2 < html.length() && html.charAt(pos + 2) != '/');
    }

    private boolean isLoopType(int pos, String html) {
        return (pos < html.length() && html.charAt(pos) == 'l') &&
                (pos + 1 < html.length() && html.charAt(pos + 1) == 'o') &&
                (pos + 2 < html.length() && html.charAt(pos + 2) == 'o') &&
                (pos + 3 < html.length() && html.charAt(pos + 3) == 'p') &&
                (pos + 4 < html.length() && html.charAt(pos + 4) == ' ');
    }

    private boolean isIfType(int pos, String html) {
        return (pos < html.length() && html.charAt(pos) == 'i') &&
                (pos + 1 < html.length() && html.charAt(pos + 1) == 'f') &&
                (pos + 2 < html.length() && html.charAt(pos + 2) == ' ');
    }

    private boolean isCloseBracket(int pos, String html) {
        return html.charAt(pos) == '{' &&
                (pos + 1 < html.length() && html.charAt(pos + 1) == '{') &&
                (pos + 2 < html.length() && html.charAt(pos + 2) == '/');
    }
}
