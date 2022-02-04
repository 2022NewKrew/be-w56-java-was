package util;

import model.UserAccount;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HtmlTemplate {
    public static StringBuilder includeHtml(String fileName) throws IOException {
        List<String> fileData = Files.readAllLines(new File("./webapp/" + fileName + ".html").toPath());
        StringBuilder body = new StringBuilder();

        for(String line: fileData){
            line = line.trim();

            body.append(line);
        }
        return body;
    }

    public static StringBuilder iterHtmlTag(StringBuilder body, Object datas){
        if(!(datas instanceof List)){
            return new StringBuilder();
        }

        StringBuilder addedBody = new StringBuilder();
        List<Object> dataList = (List<Object>) datas;

        for(Object data: dataList){
            if(data instanceof UserAccount){
                UserAccount userAccount = (UserAccount) data;

                String[] bodySplit = new String(body).split("\\{\\{");
                StringBuilder changeString = new StringBuilder(bodySplit[0]);

                if(bodySplit.length == 1)
                    return changeString;

                for(String splitString: bodySplit){
                    String[] querySplit = splitString.split("\\}\\}");

                    if(querySplit.length == 2) {
                        String param = querySplit[0];
                        String content = querySplit[1];

                        try {
                            Field declaredField = userAccount.getClass().getDeclaredField(param);
                            declaredField.setAccessible(true);
                            changeString.append(declaredField.get(userAccount));
                            declaredField.setAccessible(false);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            continue;
                        }

                        changeString.append(content);
                    }
                }
                addedBody.append(changeString);
            }
        }

        return addedBody;
    }
}
