package mapper;

import dto.MemoDto;
import dto.UserDto;
import entity.MemoEntity;
import entity.UserEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MemoMapper {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static MemoEntity toMemoEntity(MemoDto memoDto) {
        if (memoDto == null) {
            return null;
        }
        String name = memoDto.getName();
        String contents = memoDto.getContents();
        UserDto userDto = memoDto.getUserDto();
        return new MemoEntity(name, contents, LocalDateTime.now(), UserMapper.toUserEntity(userDto));
    }

    public static MemoDto toMemoDto(MemoEntity memoEntity) {
        if (memoEntity == null) {
            return null;
        }
        Long id = memoEntity.getId();
        String name = memoEntity.getName();
        String contents = memoEntity.getContents();
        LocalDateTime createdAt = memoEntity.getCreatedAt();
        UserEntity userEntity = memoEntity.getUserEntity();
        return new MemoDto(id, name, contents, createdAt.format(formatter), UserMapper.toUserDto(userEntity));
    }
}
