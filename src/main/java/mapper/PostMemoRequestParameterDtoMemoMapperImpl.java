package mapper;

import dto.PostMemoRequestParameterDto;
import model.Memo;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class PostMemoRequestParameterDtoMemoMapperImpl implements PostMemoRequestParameterDtoMemoMapper {

    @Override
    public Memo toRightObject(PostMemoRequestParameterDto postMemoRequestParameterDto) {
        if (postMemoRequestParameterDto == null) {
            return null;
        }

        Memo memo = new Memo();

        memo.setName(postMemoRequestParameterDto.getName());
        memo.setContent(postMemoRequestParameterDto.getContent());
        memo.setCreatedAt(LocalDateTime.now());

        return memo;
    }

    @Override
    public PostMemoRequestParameterDto toLeftObject(Memo memo) {
        if (memo == null) {
            return null;
        }

        PostMemoRequestParameterDto postMemoRequestParameterDto = new PostMemoRequestParameterDto();

        postMemoRequestParameterDto.setName(memo.getName());
        postMemoRequestParameterDto.setContent(memo.getContent());

        return postMemoRequestParameterDto;
    }
}
