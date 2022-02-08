package model.builder;

import db.DataBase;
import dynamic.DynamicHtmlBuilder;
import model.RequestHeader;
import model.ResponseHeader;
import util.HtmlResponseHeader;
import util.Links;

import java.io.IOException;

public class PostUserLoginBuilder extends ResponseBuilder {
    @Override
    public ResponseHeader build(RequestHeader requestHeader) throws IOException {
        String id = requestHeader.getParameter("userId");
        String password = requestHeader.getParameter("password");
        if (DataBase.isExistUserId(id) &&
                DataBase.findUserById(id)
                        .getPassword()
                        .equals(password)) {
            return buildSuccessLogin(requestHeader);
        }
        return buildFailLogin(requestHeader);
    }

    private ResponseHeader buildSuccessLogin(RequestHeader requestHeader) throws IOException {
        return ResponseHeader.builder()
                .uri(Links.MAIN)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.MAIN), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302_WITH_LOGIN_COOKIE)
                .accept(requestHeader.getAccept())
                .build();
    }

    private ResponseHeader buildFailLogin(RequestHeader requestHeader) throws IOException {
        return ResponseHeader.builder()
                .uri(Links.LOGIN_FAILED)
                .body(DynamicHtmlBuilder.getDynamicHtml(readBody(Links.LOGIN_FAILED), model))
                .htmlResponseHeader(HtmlResponseHeader.REDIRECT_302)
                .accept(requestHeader.getAccept())
                .build();
    }
}
