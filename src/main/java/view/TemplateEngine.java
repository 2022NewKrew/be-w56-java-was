package view;

import domain.Constants;
import model.Memo;
import model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TemplateEngine {

    private static final String CLASS_PATH = "./webapp";
    private static final String SUFFIX = ".html";

    public static byte[] resolveTemplate(String requestPath) throws IOException {
        return Files.readAllBytes(new File(CLASS_PATH, requestPath).toPath());
    }

    public static byte[] resolveUsersTemplate(String requestPath ,List<User> users) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader bf = new BufferedReader(new FileReader(new File(CLASS_PATH, requestPath + SUFFIX)));

        String str;
        while ((str = bf.readLine()) != null) {
            if (str.contains("{{start users}}")) {
                StringBuffer tempStringBuffer = new StringBuffer();
                while (!(str = bf.readLine()).contains("{{end}}")) {
                    tempStringBuffer.append(str);
                }

                for (int i = 0; i < users.size(); i++) {
                    User findUser = users.get(i);
                    StringBuffer copyStringBuffer = new StringBuffer(tempStringBuffer);
                    copyStringBuffer.replace(copyStringBuffer.indexOf("{{index}}"), copyStringBuffer.indexOf("{{index}}") + "{{index}}".length(), String.valueOf(i + 1));
                    copyStringBuffer.replace(copyStringBuffer.indexOf("{{userId}}"), copyStringBuffer.indexOf("{{userId}}") + "{{userId}}".length(), findUser.getUserId());
                    copyStringBuffer.replace(copyStringBuffer.indexOf("{{name}}"), copyStringBuffer.indexOf("{{name}}") + "{{name}}".length(), findUser.getName());
                    copyStringBuffer.replace(copyStringBuffer.indexOf("{{email}}"), copyStringBuffer.indexOf("{{email}}") + "{{email}}".length(), findUser.getEmail());
                    stringBuffer.append(copyStringBuffer);
                }

                str = Constants.EMPTY_STRING;
            }

            stringBuffer.append(str);
        }

        return stringBuffer.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] resolveMemosTemplate(String requestPath ,List<Memo> memos) throws IOException {
        StringBuffer stringBuffer = new StringBuffer();

        BufferedReader bf = new BufferedReader(new FileReader(new File(CLASS_PATH, requestPath + SUFFIX)));

        String str;
        while ((str = bf.readLine()) != null) {
            if (str.contains("{{start memos}}")) {
                StringBuffer tempStringBuffer = new StringBuffer();
                while (!(str = bf.readLine()).contains("{{end}}")) {
                    tempStringBuffer.append(str);
                }

                for (Memo findMemo : memos) {
                    StringBuffer copyStringBuffer = new StringBuffer(tempStringBuffer);
                    copyStringBuffer.replace(copyStringBuffer.indexOf("{{author}}"), copyStringBuffer.indexOf("{{author}}") + "{{author}}".length(), findMemo.getAuthor());
                    copyStringBuffer.replace(copyStringBuffer.indexOf("{{content}}"), copyStringBuffer.indexOf("{{content}}") + "{{content}}".length(), findMemo.getContent());
                    copyStringBuffer.replace(copyStringBuffer.indexOf("{{createAt}}"), copyStringBuffer.indexOf("{{createAt}}") + "{{createAt}}".length(), findMemo.getCreateAt().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    stringBuffer.append(copyStringBuffer);
                }

                str = Constants.EMPTY_STRING;
            }

            stringBuffer.append(str);
        }

        return stringBuffer.toString().getBytes(StandardCharsets.UTF_8);
    }
}
