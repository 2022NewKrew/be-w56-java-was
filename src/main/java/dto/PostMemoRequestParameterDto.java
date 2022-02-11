package dto;

public class PostMemoRequestParameterDto {

    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isValid() {
        if (name == null | name.isBlank() | content == null | content.isBlank()) {
            return false;
        }

        return true;
    }
}
