package dto;

public class MemoDto {

    private Long id;
    private String name;
    private String contents;
    private String createdAt;
    private UserDto userDto;

    public MemoDto() {
    }

    public MemoDto(String name, String contents, UserDto userDto) {
        this.name = name;
        this.contents = contents;
        this.userDto = userDto;
    }

    public MemoDto(Long id, String name, String contents, String createdAt, UserDto userDto) {
        this.id = id;
        this.name = name;
        this.contents = contents;
        this.createdAt = createdAt;
        this.userDto = userDto;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContents() {
        return contents;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    @Override
    public String toString() {
        return "MemoDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                ", userDto=" + userDto +
                '}';
    }
}
