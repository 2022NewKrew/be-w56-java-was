package mvc.controller;

import framework.RequestMapping;
import framework.variable.RequestMethod;

public class ResourceController implements Controller {

    @RequestMapping(value = "/*.html", method = RequestMethod.GET)
    public String getStaticHtml(String url) {
        return url;
    }

    @RequestMapping(value = "/*.ico", method = RequestMethod.GET)
    public String getIcon(String url) {
        return url;
    }

    @RequestMapping(value = "/*.css", method = RequestMethod.GET)
    public String getCss(String url) {
        return url;
    }

    @RequestMapping(value = "/*.js", method = RequestMethod.GET)
    public String getJs(String url) {
        return url;
    }

    @RequestMapping(value = "/*.woff", method = RequestMethod.GET)
    public String getWoff(String url) { return url; }

    @RequestMapping(value = "/*.ttf", method = RequestMethod.GET)
    public String getTtf(String url) { return url; }
}
