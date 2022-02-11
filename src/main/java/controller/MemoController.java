package controller;

import dynamic.DynamicModel;
import model.HttpResponse;
import model.RequestHeader;
import service.MemoService;
import util.ControllerUtils;
import util.Links;

import java.io.IOException;
import java.sql.SQLException;

public class MemoController {
    private static final MemoService memoService = MemoService.getInstance();

    public static HttpResponse controlRequest(RequestHeader requestHeader, String uri, String method) throws Exception {
        DynamicModel model = new DynamicModel();

        if (uri.startsWith("/memo/write") && method.equals("POST")) {
            return postMemoWrite(requestHeader, model);
        }

        return null;
    }

    private static HttpResponse postMemoWrite(RequestHeader requestHeader, DynamicModel model) throws SQLException {
        memoService.writeMemo(requestHeader);
        model.addAttribute("memo", memoService.findAll());

        return ControllerUtils.redirect(Links.MAIN);
    }
}
