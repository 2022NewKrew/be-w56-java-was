package util.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Builder
@Getter
public class HttpResponse {
    private HttpStatus status;

    @Builder.Default
    private Map<String, String> headers = Collections.emptyMap();

    private ModelAndView modelAndView;

    public static HttpResponse of(Exception e){
        ModelAndView modelAndView = new ModelAndView("error.html", FileType.STRING);
        modelAndView.addAttribute("message", e.getMessage());

        return HttpResponse.<String>builder()
                .status(HttpStatus.INTERNAL_ERROR)
                .modelAndView(modelAndView)
                .build();
    }
}
