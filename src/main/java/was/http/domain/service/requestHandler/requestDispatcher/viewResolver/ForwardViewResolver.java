package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import di.annotation.Bean;
import was.http.domain.service.view.ModelAndView;
import was.http.domain.service.view.View;
import was.http.domain.service.view.ViewType;
import was.http.meta.HttpStatus;

@Bean
public class ForwardViewResolver implements ViewResolver {

    @Override
    public View resolve(Object result) {
        if (result instanceof ModelAndView) {
            final ModelAndView modelAndView = ((ModelAndView) result);

            if (modelAndView.getViewType() == (ViewType.FORWARD)) {
                final String path = modelAndView.getPath();

                return new View(HttpStatus.OK, path, true);
            }
        }

        throw new RuntimeException();
    }
}
