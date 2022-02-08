package webserver.response;

import mapper.ResponseSendDataModel;
import model.UserAccount;
import model.UserAccountDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HtmlTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpResponseBodyTest {
    @DisplayName("response body 생성 테스트")
    @Test
    void makeHttpResponseBody() {
        ResponseSendDataModel responseSendDataModel = new ResponseSendDataModel("/test_string.html");
        UserAccountDTO userAccountDTO = new UserAccountDTO.Builder()
                .setUserId("aa")
                .setPassword("aa")
                .setName("aa")
                .setEmail("aa@com").build();
        UserAccountDTO userAccountDTO2 = new UserAccountDTO.Builder()
                .setUserId("bb")
                .setPassword("bb")
                .setName("bb")
                .setEmail("bb@com").build();

        List<UserAccount> userAccountList = List.of(new UserAccount(userAccountDTO,0), new UserAccount(userAccountDTO2, 1));

        responseSendDataModel.add("user", userAccountList);

        try {
            HttpResponseBody httpResponseBody = HttpResponseBody.makeHttpResponseBody(responseSendDataModel);

            assertThat(new String(httpResponseBody.getBody())).isEqualTo("123450 aa aa 1 bb bb aaa");

            responseSendDataModel.add("test", Optional.empty());
            httpResponseBody = HttpResponseBody.makeHttpResponseBody(responseSendDataModel);

            assertThat(new String(httpResponseBody.getBody())).isEqualTo("123450 aa aa 1 bb bb bbb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
