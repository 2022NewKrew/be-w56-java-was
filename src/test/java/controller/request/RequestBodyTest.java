package controller.request;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by melodist
 * Date: 2022-01-26 026
 * Time: 오전 11:50
 */
class RequestBodyTest {
    @Test
    public void requestBody(){
        // given
        String queryString = "userId=melodist&name=melodist";

        // when
        RequestBody requestBody = RequestBody.from(queryString);

        // then
        assertThat(requestBody.getParameter("userId")).isEqualTo("melodist");
        assertThat(requestBody.getParameter("name")).isEqualTo("melodist");
    }

    @Test
    public void requestBodyNull(){
        // given
        String queryString = "userId=melodist&name=melodist";

        // when
        RequestBody requestBody = RequestBody.from(queryString);

        // then
        assertThat(requestBody.getParameter("password")).isNull();
    }

}
