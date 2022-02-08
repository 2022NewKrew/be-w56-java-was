package util;

import mapper.ResponseSendDataModel;
import model.UserAccount;
import model.UserAccountDTO;
import org.h2.engine.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class HtmlTemplateTest {
    @DisplayName("동적 html 파싱 정상 작동 여부 테스트")
    @Test
    void dynamicHtmlParsing() {
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
            List<String> fileData = Files.readAllLines(new File("./webapp" + responseSendDataModel.getName()).toPath());
            assertThat(new String(HtmlTemplate.dynamicHtmlParsing(fileData, responseSendDataModel))).isEqualTo("123450 aa aa 1 bb bb aaa");

            responseSendDataModel.add("test", Optional.empty());
            assertThat(new String(HtmlTemplate.dynamicHtmlParsing(fileData, responseSendDataModel))).isEqualTo("123450 aa aa 1 bb bb bbb");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
