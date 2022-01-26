package framework.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommaSeries {
    private final List<String> eachInfos;

    public CommaSeries(String infosStr) {
        this.eachInfos = Arrays.stream(infosStr.split(","))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (String info : eachInfos) {
            sb.append(info).append(", ");
        }

        sb.setLength(sb.length() - 2);
        return sb.toString();
    }
}
