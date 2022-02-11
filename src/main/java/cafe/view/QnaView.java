package cafe.view;

import cafe.dto.QnaDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class QnaView {
    private static final String WEBAPP_PATH = "/webapp";

    public String getQnaListHtml(List<QnaDto> qnaDtoList) throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(WEBAPP_PATH + "/index.html");
        byte[] bytes = resourceAsStream.readAllBytes();
        String htmlString = new String(bytes);

        StringBuilder qnaListHtml = new StringBuilder();

        AtomicInteger index = new AtomicInteger(1);
        qnaDtoList.stream()
                .forEach(qnaDto -> {
                    qnaListHtml.append("<li>");
                    qnaListHtml.append("<div class=\"wrap\">");
                    qnaListHtml.append("<div class=\"main\">");

                    qnaListHtml.append("<strong class=\"subject\">");
                    qnaListHtml.append(qnaDto.getTitle());
                    qnaListHtml.append("</strong>");

                    qnaListHtml.append("<class=\"contents\">");
                    qnaListHtml.append(qnaDto.getContents());
                    qnaListHtml.append("</class>");

                    qnaListHtml.append("<div class=\"auth-info\">");
                    qnaListHtml.append("<i class=\"icon-add-comment\">").append("</i>");
                    qnaListHtml.append("<span class=\"time\">").append(qnaDto.getCreatedAt().toString() + " ").append("</span>");
                    qnaListHtml.append("<a href=\"./user/profile.html\" class=\"author\">").append(qnaDto.getWriter()).append("</a>");
                    qnaListHtml.append("</div>");

                    qnaListHtml.append("<div class=\"reply\" title=\"index\">");
                    qnaListHtml.append("<i class=\"icon-reply\">").append("</i>");
                    qnaListHtml.append("<span class=\"point\">").append(index.get()).append("</span>");
                    qnaListHtml.append("</div>");

                    qnaListHtml.append("</div>");
                    qnaListHtml.append("</div>");
                    qnaListHtml.append("</li>");
                    index.getAndIncrement();
                });

        return htmlString.replace("{{qnaList}}", qnaListHtml.toString());
    }
}
