package view;

import http.HttpStatusCode;
import model.ModelAndView;

import static util.ConstantValues.REDIRECT_COMMAND;
import static util.ConstantValues.ROOT_URL;

public class ViewRedirect implements View{

    private final ModelAndView mv;
    private final String url;

    public ViewRedirect(ModelAndView mv){
        this.mv = mv;
        this.url = ROOT_URL + mv.getViewName().replaceFirst(REDIRECT_COMMAND, "");
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.REDIRECT;
    }

    public boolean login(){
        return mv.getValue("login") != null;
    }

    public String getUrl(){
        return url;
    }
}
