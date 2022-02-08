package http.header;

import util.ParsingUtils;

public class Header {
    private static final String DELIMITER = ": ";
    private static final int PARAMETER_COUNT = 2;

    private final FieldName fieldName;
    private final FieldValue fieldValue;

    private Header(FieldName fieldName, FieldValue fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public static Header create(String fieldName, String fieldValue) {
        return new Header(new FieldName(fieldName), new FieldValue(fieldValue));
    }

    public static Header create(String header) {
        String[] tokens = ParsingUtils.parse(header, DELIMITER, PARAMETER_COUNT);
        return new Header(new FieldName(tokens[0]), new FieldValue(tokens[1]));
    }

    public FieldName getFieldName() {
        return fieldName;
    }

    public FieldValue getFieldValue() {
        return fieldValue;
    }
}
