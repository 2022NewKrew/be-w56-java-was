package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import was.http.domain.service.view.View;

public interface ViewResolver {
    View resolve(Object result);
}
