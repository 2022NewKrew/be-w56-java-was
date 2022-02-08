package templates;

import model.request.Body;
import util.Checker;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TemplateEngine {
    private static final String TPL_PREFIX = "./webapp";
    private static final String TPL_POSTFIX = ".html";
    private static final String TPL_PRETAG_F = "{{#";
    private static final String TPL_PRETAG_R = "}}";
    private static final String TPL_POSTTAG_F = "{{/";
    private static final String TPL_POSTTAG_R = "}}";
    private static final String TPL_PROPERTY_F = "{{";
    private static final String TPL_PROPERTY_R = "}}";

    private static final String TPL_COLLECTION_INDEX = ".index";
    private static final String TPL_COLLECTION_INDEX_FROM_1 = ".index1";

    private final String data;
    private final List<TemplateIndex> index;

    public TemplateEngine(final String loc) {
        final File file = new File(TPL_PREFIX + loc + TPL_POSTFIX);
        if (!file.exists() || !file.isFile()) {
            throw new RuntimeException("Not found file in location: " + loc);
        }

        try {
            final InputStream input = new FileInputStream(file);
            this.data = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            input.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + file.getPath());
        }

        index = generateIndex();
    }

    private List<TemplateIndex> generateIndex() {
        Checker.checkString(data);
        final List<TemplateIndex> list = new ArrayList<>();

        int idxPreTagF = data.indexOf(TPL_PRETAG_F);
        while (idxPreTagF != -1) {
            final int idxPreTagR = data.indexOf(TPL_PRETAG_R, idxPreTagF);
            final int idxPostTagF = data.indexOf(TPL_POSTTAG_F, idxPreTagF);
            final int idxPostTagR = data.indexOf(TPL_POSTTAG_R, idxPostTagF);
            Checker.checkIntMinMax(idxPreTagF, idxPreTagR);
            Checker.checkIntMinMax(idxPreTagR, idxPostTagF);
            Checker.checkIntMinMax(idxPostTagF, idxPostTagR);

            final String tagFront = data.substring(idxPreTagF + TPL_PRETAG_F.length(), idxPreTagR);
            final String tagRear = data.substring(idxPostTagF + TPL_POSTTAG_F.length(), idxPostTagR);
            if (tagFront.equals(tagRear)) {
                list.add(
                        new TemplateIndex(
                                tagFront,
                                idxPreTagF,
                                idxPreTagR + TPL_PRETAG_R.length(),
                                idxPostTagF,
                                idxPostTagR + TPL_POSTTAG_R.length()
                        )
                );
            }

            idxPreTagF = data.indexOf(TPL_PRETAG_F, idxPreTagF + 1);
        }

        return Collections.unmodifiableList(list);
    }

    public <T> Body processTemplate(final TemplateAttribute<T> tplAttr) {
        final StringBuilder sb = new StringBuilder();

        int cursor = 0;
        for (TemplateIndex i: index) {
            sb.append(data, cursor, i.getFrontOut());
            sb.append(processTag(i, tplAttr));
            cursor = i.getRearOut();
        }
        sb.append(data, cursor, data.length());

        return new Body(sb.toString());
    }

    private <T> String processTag(final TemplateIndex index, final TemplateAttribute<T> tplAttr) {
        final Collection<T> col = tplAttr.get(index.getTag());
        final StringBuilder sb = new StringBuilder();
        final String block = data.substring(index.getFrontIn(), index.getRearIn());

        int tIdx = 0;
        for (T t: col) {
            sb.append(processBlock(block, t, tIdx++));
        }

        return sb.toString();
    }

    private <T> String processBlock(final String block, final T t, final int tIdx) {
        final StringBuilder sb = new StringBuilder();

        int cursor = 0;
        int idxPropertyF = block.indexOf(TPL_PROPERTY_F);
        while (idxPropertyF != -1) {
            final int idxPropertyR = block.indexOf(TPL_PROPERTY_R, idxPropertyF);
            Checker.checkIntMinMax(idxPropertyF, idxPropertyR);
            sb.append(block, cursor, idxPropertyF);

            final String property = block.substring(idxPropertyF + TPL_PROPERTY_F.length(), idxPropertyR);
            if (TPL_COLLECTION_INDEX.equals(property)) {
                sb.append(tIdx);
            } else if (TPL_COLLECTION_INDEX_FROM_1.equals(property)) {
                sb.append(tIdx + 1);
            } else {
                sb.append(getProperty(t, property));
            }

            cursor = idxPropertyR + TPL_PROPERTY_R.length();
            idxPropertyF = block.indexOf(TPL_PROPERTY_F, idxPropertyF + 1);
        }

        sb.append(block, cursor, block.length());
        return sb.toString();
    }

    private <T> String getProperty(final T t, final String property) {
        PropertyDescriptor pd;
        try {
            // PropertyDescriptor 생성자 정의 참조하여 읽기 전용으로 구성
            pd = new PropertyDescriptor(property, t.getClass(),
                    "is" + Character.toUpperCase(property.charAt(0)) + property.substring(1),
                    null);
            if (pd.getReadMethod() != null && !"class".equals(pd.getName())) {
                return String.valueOf(pd.getReadMethod().invoke(t));
            }
        } catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException("Failed to get property(" + property + ") in " + t.getClass().getName());
        }
        return null;
    }
}
