package util;

import mapper.ResponseSendDataModel;
import model.UserAccount;
import model.UserAccountHtmlMapper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class HtmlTemplate {
    private static StringBuilder includeHtml(String fileName, ResponseSendDataModel model) throws IOException {
        List<String> fileData = Files.readAllLines(new File("./webapp/" + fileName + ".html").toPath());

        return dynamicHtmlParsing(fileData, model);
    }

    private static StringBuilder iterHtmlTag(StringBuilder body, Object datas){
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

                        UserAccountHtmlMapper userAccountHtmlMapper = new UserAccountHtmlMapper(userAccount);

                        changeString.append(userAccountHtmlMapper.getMap().get(param).get());

                        changeString.append(content);
                    }
                }
                addedBody.append(changeString);
            }
        }

        return addedBody;
    }

    public static StringBuilder dynamicHtmlParsing(List<String> fileData, ResponseSendDataModel model) throws IOException{
        StringBuilder body = new StringBuilder();

        StringBuilder tempSaveBody = null;
        boolean isWrite = true;
        for(String line: fileData){
            line = line.trim();

            if(line.matches("^\\{\\{>.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();
                body.append(HtmlTemplate.includeHtml(subLine, model));

                continue;
            }

            if(line.matches("^\\{\\{\\^.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();

                if(!Objects.isNull(model.get(subLine))) {
                    tempSaveBody = new StringBuilder(body);

                    body = new StringBuilder();
                    isWrite = false;
                }

                continue;
            }

            if(line.matches("^\\{\\{\\$.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();

                if(Objects.isNull(model.get(subLine))) {
                    tempSaveBody = new StringBuilder(body);

                    body = new StringBuilder();
                    isWrite = false;
                }

                continue;
            }

            if(line.matches("^\\{\\{#.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();

                if(!Objects.isNull(model.get(subLine))) {
                    tempSaveBody = new StringBuilder(body);

                    body = new StringBuilder();
                }

                continue;
            }

            if(line.matches("^\\{\\{/.*\\}\\}$")){
                String subLine = line.substring(3, line.length()-2).trim();

                if(!Objects.isNull(tempSaveBody)) {
                    if (!Objects.isNull(model.get(subLine)) && model.get(subLine) instanceof List) {
                        tempSaveBody.append(HtmlTemplate.iterHtmlTag(body, model.get(subLine)));
                    } else if (isWrite) {
                        tempSaveBody.append(body);
                    }

                    body = new StringBuilder(tempSaveBody);
                }

                tempSaveBody = null;
                isWrite = true;

                continue;
            }
            if(isWrite)
                body.append(line);
        }

        return body;
    }
}
