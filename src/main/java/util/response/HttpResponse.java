package util.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class HttpResponse {
    private HttpStatus status;

    private ResponseHeaders headers;

    private ModelAndView modelAndView;

    public static HttpResponse of(Exception e){
        ModelAndView modelAndView = new ModelAndView("error.html");
        modelAndView.addAttribute("message", e.getMessage());

        ResponseHeaders responseHeaders = ResponseHeaders.builder()
                .contentType(ContentType.HTML)
                .build();

        return HttpResponse.<String>builder()
                .status(HttpStatus.INTERNAL_ERROR)
                .headers(responseHeaders)
                .modelAndView(modelAndView)
                .build();
    }
}
