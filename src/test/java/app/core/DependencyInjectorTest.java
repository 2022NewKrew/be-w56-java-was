package app.core;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DependencyInjectorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyInjector.class);

    @Test
    void injectorTest() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, Object> map = DependencyInjector.inject();
        assertThat(map.get("app.controller.PostController")).isNotNull();
        assertThat(map.get("app.repository.PostsRepository")).isNotNull();
        assertThat(map.get("app.configure.DbConfigure")).isNotNull();

        for (String key : map.keySet())
            LOGGER.debug("key : {}", key);

    }

}
