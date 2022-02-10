package model.builder;

import dynamic.DynamicHtmlBuilder;
import lombok.extern.slf4j.Slf4j;
import model.HttpResponse;
import model.Memo;
import model.RequestHeader;
import service.MemoService;
import util.HttpResponseHeader;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class IndexResponseBuilder extends ResponseBuilder {
    private MemoService memoService;

    public IndexResponseBuilder() {
        memoService = MemoService.getInstance();
    }

    @Override
    public HttpResponse build(RequestHeader requestHeader) throws IOException, SQLException {
        List<Memo> memo = memoService.findAll();
        model.addAttribute("memo", memo);

        return HttpResponse.builder()
                .locationUri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HttpResponseHeader.RESPONSE_200)
                .accept(requestHeader.getAccept())
                .build();
    }
}
