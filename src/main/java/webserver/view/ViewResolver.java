package webserver.view;

public class ViewResolver {
    private static final ViewResolver instance = new ViewResolver();

    private ViewResolver() {
    }

    public static ViewResolver getInstance() {
        return instance;
    }

    public View resolve(ModelAndView mv) {
       String view = mv.getView();

       if(view.contains("redirect:")){
           String redirectView = view.replace("redirect:", "");
           return new RedirectView(redirectView);
       }

       return new TemplateEngineView(view);
    }
}
