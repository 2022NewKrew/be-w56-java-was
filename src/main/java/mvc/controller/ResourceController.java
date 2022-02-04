package mvc.controller;

import framework.params.RequestMapping;
import framework.params.HttpRequest;
import framework.constant.RequestMethod;

public class ResourceController implements Controller {

    @RequestMapping(value = "/.*\\.html", method = RequestMethod.GET)
    public String getStaticHtml(HttpRequest request) {
        return request.getUrl();
    }

    @RequestMapping(value = "/.*\\.ico", method = RequestMethod.GET)
    public String getIcon(HttpRequest request) {
        return request.getUrl();
    }

    @RequestMapping(value = "/.*\\.css", method = RequestMethod.GET)
    public String getCss(HttpRequest request) {
        return request.getUrl();
    }

    @RequestMapping(value = "/.*\\.js", method = RequestMethod.GET)
    public String getJs(HttpRequest request) {
        return request.getUrl();
    }

    @RequestMapping(value = "/.*\\.woff", method = RequestMethod.GET)
    public String getWoff(HttpRequest request) {
        return request.getUrl();
    }

    @RequestMapping(value = "/.*\\.ttf", method = RequestMethod.GET)
    public String getTtf(HttpRequest request) {
        return request.getUrl();
    }
}
