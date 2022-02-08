package templateEngine;


import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CyEngineCompiler {

    public byte[] compile(CyEngineNode rootNode, Map<String, Object> model) {
        final StringBuilder sb = new StringBuilder();

        for (CyEngineNode child : rootNode.getChildren()) {
            compile(child, sb, model);
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void compile(CyEngineNode curNode, StringBuilder sb, Map<String, Object> model) {
        final NodeType nodeType = curNode.getNodeType();

        final String index = curNode.getIndex();
        final Object value = getFieldValue(index, model);

        final String contents = curNode.getContents();

        final List<CyEngineNode> children = curNode.getChildren();

        if (nodeType == NodeType.TEXT) {
            sb.append(contents);
            return;
        }

        if (nodeType == NodeType.Condition) {
            if (value == null) {
                return;
            }

            if (value instanceof Collection && ((Collection<?>) value).isEmpty()) {
                return;
            }

            for (CyEngineNode child : children) {
                compile(child, sb, model);
            }
            return;
        }

        if (nodeType == NodeType.LOOP) {
            if (value == null) {
                return;
            }

            if (!(value instanceof Collection<?>) || ((Collection<?>) value).isEmpty()) {
                return;
            }

            final Iterator<?> it = ((Collection<?>) value).iterator();
            int i = 0;
            while (it.hasNext()) {
                final Object next = it.next();
                for (CyEngineNode child : children) {
                    compile(child, sb, i, next);
                }
                i++;
            }

            return;
        }

        if (nodeType == NodeType.VALUE) {
            if (value == null) {
                throw new RuntimeException("템플릿엔진 컴파일 에러 [" + index + "] 값이 없습니다.");
            }

            sb.append(value);
        }
    }

    private void compile(CyEngineNode curNode, StringBuilder sb, int id, Object obj) {
        final NodeType nodeType = curNode.getNodeType();
        final String index = curNode.getIndex();

        final String contents = curNode.getContents();

        final List<CyEngineNode> children = curNode.getChildren();

        if (nodeType == NodeType.TEXT) {
            sb.append(contents);
            return;
        }

        if (nodeType == NodeType.Condition) {
            final Object value = getFieldValue(index, obj);
            if (value == null) {
                return;
            }

            if (value instanceof Collection && ((Collection<?>) value).isEmpty()) {
                return;
            }

            for (CyEngineNode child : children) {
                compile(child, sb, id, value);
            }
            return;
        }

        if (nodeType == NodeType.LOOP) {
            final Object value = getFieldValue(index, obj);
            if (value == null) {
                return;
            }

            if (!(value instanceof Collection<?>) || ((Collection<?>) value).isEmpty()) {
                return;
            }

            final Iterator<?> it = ((Collection<?>) value).iterator();
            int i = 0;
            while (it.hasNext()) {
                final Object next = it.next();
                for (CyEngineNode child : children) {
                    compile(child, sb, i, next);
                }
                i++;
            }

            return;
        }

        if (nodeType == NodeType.VALUE) {
            if (obj == null) {
                throw new RuntimeException("템플릿엔진 컴파일 에러 [" + index + "] 값이 없습니다.");
            }

            if (index.equals("value")) {
                sb.append(obj);
            } else if (index.equals("index")) {
                sb.append((Object) id);
            } else {
                sb.append(getFieldValue(index, obj));
            }
        }
    }

    private Object getFieldValue(String index, Object value) {
        if (index == null)
            return null;

        final Iterator<String> it = Arrays.stream(index.split("\\.")).iterator();

        return getFieldValue(it, value);
    }

    private Object getFieldValue(String index, Map<String, Object> model) {
        if (index == null) return null;

        final Iterator<String> it = Arrays.stream(index.split("\\.")).iterator();

        if (it.hasNext()) {
            final String key = it.next();
            if (model.containsKey(key)) {
                return getFieldValue(it, model.get(key));
            }
        }

        return null;
    }

    private Object getFieldValue(Iterator<String> it, Object value) {
        if (it.hasNext()) {
            final String index = it.next();

            try {
                final Field field = value.getClass().getDeclaredField(index);
                field.setAccessible(true);

                return getFieldValue(it, field.get(value));

            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("필드 접근시 에러가 발생하였습니다.");
            }

        }

        return value;
    }
}
