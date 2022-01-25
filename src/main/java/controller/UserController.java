package controller;

import util.RequestMapping;
import util.RequestMethod;

public class UserController implements Controller{

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String getDefaultIndexPage() {
        return "index";
    }

    @RequestMapping(value="/index.html", method=RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }
}
