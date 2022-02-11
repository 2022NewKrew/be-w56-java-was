package webserver.handler;

import context.Context;
import webserver.request.Request;
import webserver.response.Response;
import webserver.response.TemplateResponse;
import webserver.response.TemplateResponse.Model;

class GetHandler implements Handler {

    private static final GetHandler getHandler = new GetHandler();

    private GetHandler() {
    }

    public static GetHandler getInstance() {
        return getHandler;
    }

    @Override
    public Response handle(Request request) throws Exception {
        Model model = new Model();
        Response response = (Response) Context.invokeGetMappingMethod(request.getUri(), request,
            model);
        if (response instanceof TemplateResponse) {
            ((TemplateResponse) response).parseTemplate(model);
        }
        return response;
    }
}
