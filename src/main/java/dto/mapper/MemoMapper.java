package dto.mapper;

import dto.MemoCreateDto;
import dto.MemoResponseDto;
import dto.UserCreateDto;
import dto.UserResponseDto;
import model.Memo;
import model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface MemoMapper {
    MemoMapper INSTANCE = Mappers.getMapper(MemoMapper.class);

    Memo toEntityFromSaveDto(MemoCreateDto MemoCreateDto);

    List<MemoResponseDto> toDtoList(List<Memo> memos);
}
