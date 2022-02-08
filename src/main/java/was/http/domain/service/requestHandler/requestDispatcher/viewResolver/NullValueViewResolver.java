package was.http.domain.service.requestHandler.requestDispatcher.viewResolver;

import di.annotation.Bean;
import was.http.domain.service.view.View;
import was.http.meta.HttpHeaders;
import was.http.meta.HttpStatus;
import was.http.meta.MediaTypes;

@Bean
public class NullValueViewResolver implements ViewResolver {

    @Override
    public View resolve(Object result) {
        return new View(HttpStatus.OK)
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaTypes.TEXT_PLAIN.getValue());
    }
}
