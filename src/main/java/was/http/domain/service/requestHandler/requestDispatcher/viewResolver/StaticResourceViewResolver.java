package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import di.annotation.Bean;
import was.http.domain.service.view.ModelAndView;
import was.http.domain.service.view.View;
import was.http.domain.service.view.ViewType;
import was.http.meta.HttpStatus;
import was.server.util.StaticResourceReader;

@Bean
public class StaticResourceViewResolver implements ViewResolver {

    private final StaticResourceReader staticResourceReader;

    public StaticResourceViewResolver(StaticResourceReader staticResourceReader) {
        this.staticResourceReader = staticResourceReader;
    }

    @Override
    public View resolve(Object result) {
        if (result instanceof ModelAndView) {
            final ModelAndView modelAndView = ((ModelAndView) result);

            if (modelAndView.getViewType() == (ViewType.STATIC_RESOURCE)) {
                final String path = modelAndView.getPath();
                final byte[] body = staticResourceReader.read(path);

                return new View(HttpStatus.OK, path, body);
            }
        }

        throw new RuntimeException();
    }
}
