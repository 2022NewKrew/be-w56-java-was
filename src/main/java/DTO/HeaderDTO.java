package DTO;

import java.util.ArrayList;
import java.util.List;

public class HeaderDTO {
    private List<String> headerList = new ArrayList<>();

    public void addBufferLine(String line){
        headerList.add(line);
    }

    public String getFirstLine(){
        return headerList.get(0);
    }

}
