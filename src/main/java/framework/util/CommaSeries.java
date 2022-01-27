package framework.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ','로 구분되는 정보를 담을 일급 컬렉션 클래스,
 * 요청 헤더에서 'Accept', 'Accept-Language', 'Accept-Encoding'이 해당 클래스를 사용
 */
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
