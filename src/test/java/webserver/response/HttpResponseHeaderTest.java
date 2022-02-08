package webserver.response;

import mapper.ResponseSendDataModel;
import model.UserAccount;
import model.UserAccountDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HttpResponseHeaderTest {
    @DisplayName("response header 생성 테스트")
    @Test
    void makeHttpResponseHeader() {
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

            HttpResponseHeader httpResponseHeader = HttpResponseHeader.makeHttpResponseHeader(responseSendDataModel, httpResponseBody.getBody().length);

            assertThat(new String(httpResponseHeader.getHeader()).contains("200 OK")).isEqualTo(true);
            assertThat(new String(httpResponseHeader.getHeader()).contains("302")).isEqualTo(false);

            httpResponseBody = new HttpResponseBody();
            responseSendDataModel = new ResponseSendDataModel("redirect:/");
            httpResponseHeader = HttpResponseHeader.makeHttpResponseHeader(responseSendDataModel, httpResponseBody.getBody().length);

            assertThat(new String(httpResponseHeader.getHeader()).contains("302")).isEqualTo(true);
            assertThat(new String(httpResponseHeader.getHeader()).contains("200 OK")).isEqualTo(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
