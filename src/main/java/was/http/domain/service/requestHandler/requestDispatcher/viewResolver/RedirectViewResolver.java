package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import di.annotation.Bean;
import was.http.domain.service.view.ViewType;
import was.http.domain.service.view.View;
import was.http.domain.service.view.ModelAndView;
import was.http.meta.HttpHeaders;
import was.http.meta.HttpStatus;

@Bean
public class RedirectViewResolver implements ViewResolver {

    @Override
    public View resolve(Object result) {
        if (result instanceof ModelAndView) {
            final ModelAndView modelAndView = ((ModelAndView) result);

            if (modelAndView.getViewType() == (ViewType.REDIRECT)) {
                final String path = modelAndView.getPath();

                return new View(HttpStatus.FOUND, path, false)
                        .addHeader(HttpHeaders.LOCATION, path);
            }
        }

        throw new RuntimeException();
    }
}
