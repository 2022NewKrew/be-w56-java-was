package DTO;

import util.HtmlParser;
import util.HttpResponseUtils;
import util.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    final static private String PREFIX_REDIRECT = "redirect:";
    Map<String, Object> model = new HashMap<>();
    String view;
    // todo : model paramter 전달 시, 해당 내용 추가!
    public ModelAndView(String viewName){
        view = viewName;
    }


    public void setViewName(String name){
        view = name;
    }

    public String getViewName() { return view;}

    public void addObject(String name, Object value){
        model.put(name, value);
    }

    private boolean checkAndRemoveRedirect(){
        if (view.startsWith(PREFIX_REDIRECT)){
            view = view.replace(PREFIX_REDIRECT, "");
            return true;
        }

        return false;
    }

    // todo: path 복잡해질 경우 view object 따로 생성 후 render() 옮겨야함
    // todo: model 값 있을 경우 body 수정해야함 !
    public void render(RequestHeader requestHeader, ResponseHeader responseHeader, DataOutputStream dos) throws IOException {
        if (checkAndRemoveRedirect()){ // if redirect
            responseHeader.setRedirect(view);
        }

        byte[] body = IOUtils.readHeaderPathFile(view, responseHeader);
        byte[] dynamicBody = HtmlParser.fillDynamicHtml(body);

        HttpResponseUtils.writeResponseHeader(responseHeader, dos);
        HttpResponseUtils.writeResponseBody(dos, dynamicBody);

    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ModelAndView{");
        sb.append("model=").append(model);
        sb.append(", view='").append(view).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
