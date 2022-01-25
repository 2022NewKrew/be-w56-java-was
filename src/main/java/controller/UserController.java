package controller;

import framework.variable.RequestMethod;
import util.RequestMapping;

public class UserController implements Controller{

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String getDefaultIndexPage() {
        return "index";
    }

    @RequestMapping(value="/index.html", method=RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }
}
