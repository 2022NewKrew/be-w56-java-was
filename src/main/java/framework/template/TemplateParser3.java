package framework.template;

import framework.modelAndView.Model;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser3 {

    public static Pattern TAG_TYPE_PATTERN = Pattern.compile("\\{\\{.+?}}");
    public static Pattern BLOCK_OPEN_PATTERN = Pattern.compile("\\{\\{#.+?}}");
    public static Pattern BLOCK_CLOSE_PATTERN = Pattern.compile("\\{\\{/.+?}}");

    private String template;
    private StringBuilder sb = new StringBuilder();
    private int index = 0;
    private Model model;

    public TemplateParser3(String template, Model model) {
        this.template = template;
        this.model = model;
    }

    public  String getTemplateWithModel() throws IOException {
        randerModel();
        return sb.toString();
    }

    public void randerModel() {

        char pre = 0;
        char cur;
        while (index < template.length()) {
            if ((cur = template.charAt(index)) != '{') {
                if (pre != 0) {
                    sb.append(pre);
                    sb.append(cur);
                } else {
                    sb.append(cur);
                }
                index++;
                continue;
            }
            if (cur == '{') {
                if (pre != 0 && pre == '{') {
                    switch (template.charAt(++index)) {
                        case '#':
                            String objectAttributeName = getAttribueByName(template, ++index);
                            index += objectAttributeName.length() + 2;
                            index = processBlock(objectAttributeName, model.getInAttribute(objectAttributeName));
                            break;
                        case '/':
                            break;
                        default:
                            String stringAttributeName = getAttribueByName(template, index);
                            index = processTag(stringAttributeName);
                    }
                    pre = 0;
                } else {
                    pre = cur;
                }
                index++;
            }


        }
    }

    private Integer processBlock(String targetAttributeName, Object inAttribute) {
        if (inAttribute instanceof List)
            return processListBlock(targetAttributeName, inAttribute);
        return processObjectBlock(targetAttributeName, inAttribute);
    }

    private Integer processObjectBlock(String targetAttributeName, Object inAttribute) {
        int returnIndex = index;
        int lastIndex = template.indexOf("{{/" + targetAttributeName +"}}") - 1;
        replaceAllTag(returnIndex, lastIndex, inAttribute);
        return lastIndex + targetAttributeName.length() + 5;
    }

    private Integer processListBlock(String targetAttributeName, Object inAttribute) {
        int returnIndex = index;
        int lastIndex = template.indexOf("{{/" + targetAttributeName +"}}") - 1;

        for (Object obj : (List) inAttribute) {
            replaceAllTag(returnIndex, lastIndex, obj);
        }

        return lastIndex + targetAttributeName.length() + 5;
    }

    private void replaceAllTag(int startIndex, int lastIndex, Object modelAttribute) {
        String template = this.template.substring(index, lastIndex);
        Matcher matcher = TAG_TYPE_PATTERN.matcher(template);
        while (matcher.find()) {
            int attributeStartIndex = matcher.start();
            String replaceTarget = matcher.group();
            String attributeName = getAttribueByName(template, attributeStartIndex + 2);
            String attribute = "";
            try {
                Field field = modelAttribute.getClass().getDeclaredField(attributeName);
                field.setAccessible(true);
                attribute = field.get(modelAttribute).toString();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            template = template.replace(replaceTarget, attribute);
            matcher = TAG_TYPE_PATTERN.matcher(template);
        }
        sb.append(template);
    }

    private Integer processTag(String target) {
        String stringAttribute = model.getInAttribute(target).toString();
        sb.append(stringAttribute);
        return index + target.length() + 2;
    }

    private String getAttribueByName(String template, int startIndex) {
        int end = template.indexOf("}}", startIndex);
        String attributeName = template.substring(startIndex, end);
        return attributeName;
    }

}
