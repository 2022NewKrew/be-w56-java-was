package webserver.configures;

import webserver.context.HttpSession;

public interface HttpServletRequest {
    HttpSession getSession();

    void setAttribute(String userSeq, long userSeq1);
}
