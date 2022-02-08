package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import di.annotation.Bean;
import was.http.domain.service.view.ResponseEntity;
import was.http.domain.service.view.ModelAndView;
import was.http.domain.service.view.View;
import was.http.domain.service.view.ViewType;

@Bean
public class ViewResolverImpl implements ViewResolver {
    private final ViewResolver objectViewResolver;
    private final ViewResolver redirectViewResolver;
    private final ViewResolver responseEntityViewResolver;
    private final ViewResolver staticResourceViewResolver;
    private final ViewResolver nullViewResolver;
    private final ViewResolver forwardViewResolver;
    private final ViewResolver cyEngineViewResolver;

    public ViewResolverImpl(ObjectViewResolver objectViewResolver,
                            RedirectViewResolver redirectViewResolver,
                            ResponseEntityViewResolver responseEntityViewResolver,
                            StaticResourceViewResolver staticResourceViewResolver,
                            NullValueViewResolver nullValueViewResolver,
                            ForwardViewResolver forwardViewResolver,
                            CyEngineViewResolver cyEngineViewResolver) {
        this.objectViewResolver = objectViewResolver;
        this.redirectViewResolver = redirectViewResolver;
        this.responseEntityViewResolver = responseEntityViewResolver;
        this.staticResourceViewResolver = staticResourceViewResolver;
        this.nullViewResolver = nullValueViewResolver;
        this.forwardViewResolver = forwardViewResolver;
        this.cyEngineViewResolver = cyEngineViewResolver;
    }

    @Override
    public View resolve(Object result) {
        final ViewResolver viewResolver = getViewResolver(result);

        return viewResolver.resolve(result);
    }

    public ViewResolver getViewResolver(Object result) {
        if (result == null) {
            return nullViewResolver;
        }

        if (result instanceof ModelAndView) {
            final ViewType viewType = ((ModelAndView) result).getViewType();

            if (viewType == ViewType.REDIRECT) {
                return redirectViewResolver;
            }

            if (viewType == ViewType.FORWARD) {
                return forwardViewResolver;
            }

            if (viewType == ViewType.STATIC_RESOURCE) {
                final String path = ((ModelAndView) result).getPath();

                if (path.startsWith("/templates/")) {
                    return cyEngineViewResolver;
                }

                return staticResourceViewResolver;
            }
        }

        if (result instanceof ResponseEntity) {
            return responseEntityViewResolver;
        }

        return objectViewResolver;
    }
}
