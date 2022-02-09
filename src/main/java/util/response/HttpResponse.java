package util.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class HttpResponse {
    private HttpStatus status;

    private ResponseHeaders headers;

    private ModelAndView modelAndView;

    public static HttpResponse of(Exception e){
        ModelAndView modelAndView = new ModelAndView("error.html");
        modelAndView.addAttribute("message", e.getMessage());

        return HttpResponse.builder(HttpStatus.INTERNAL_ERROR, ContentType.HTML)
                .modelAndView(modelAndView)
                .build();
    }

    public static Builder builder(HttpStatus status, ContentType contentType){
        return new Builder(status, contentType);
    }

    public static class Builder {
        private HttpStatus status;
        private ResponseHeaders headers;
        private ModelAndView modelAndView = ModelAndView.emptyModelAndView();

        Builder(HttpStatus status, ContentType contentType){
            this.status = status;
            this.headers = new ResponseHeaders(contentType);
        }

        public Builder modelAndView(ModelAndView modelAndView){
            this.modelAndView = modelAndView;
            return this;
        }

        public Builder header(String key, String val){
            this.headers.addHeader(key, val);
            return this;
        }

        public HttpResponse build(){
            return new HttpResponse(status, headers, modelAndView);
        }
    }
}
