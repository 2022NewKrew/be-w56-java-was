package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import di.annotation.Bean;
import was.http.domain.service.view.ModelAndView;
import was.http.domain.service.view.View;
import was.http.domain.service.view.ViewType;
import was.http.meta.HttpStatus;
import templateEngine.CyEngine;

@Bean
public class CyEngineViewResolver implements ViewResolver {
    final CyEngine cyEngine = new CyEngine();

    @Override
    public View resolve(Object result) {
        if (result instanceof ModelAndView) {
            final ModelAndView modelAndView = ((ModelAndView) result);

            if (modelAndView.getViewType() == (ViewType.STATIC_RESOURCE)) {
                final String path = modelAndView.getPath();

                if (!path.startsWith("/templates/")) {
                    throw new RuntimeException();
                }

                final String resourcePath = "src/main/resources";
                final byte[] body = cyEngine.compile(resourcePath + path, modelAndView.getModels());

                return new View(HttpStatus.OK, path, body);
            }
        }
        throw new RuntimeException();
    }
}
