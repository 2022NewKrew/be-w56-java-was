package mapper;

import collections.RequestBody;
import dto.PostMemoRequestParameterDto;

import javax.annotation.processing.Generated;
import java.util.HashMap;
import java.util.Map;

@Generated(value = "org.mapstruct.ap.MappingProcessor")
public class RequestBodyPostMemoRequestParameterDtoImpl implements RequestBodyPostMemoRequestParameterDtoMapper {

    @Override
    public PostMemoRequestParameterDto toRightObject(RequestBody requestBody) {
        if (requestBody == null | !requestBody.getBodies().containsKey("writer") | !requestBody.getBodies().containsKey("contents")) {
            return null;
        }

        PostMemoRequestParameterDto postMemoRequestParameterDto = new PostMemoRequestParameterDto();
        postMemoRequestParameterDto.setName(requestBody.getParameter("writer"));
        postMemoRequestParameterDto.setContent(requestBody.getParameter("contents"));

        return postMemoRequestParameterDto;
    }

    @Override
    public RequestBody toLeftObject(PostMemoRequestParameterDto postMemoRequestParameterDto) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("writer", postMemoRequestParameterDto.getName());
        parameters.put("contents", postMemoRequestParameterDto.getContent());

        return new RequestBody(parameters);
    }
}
