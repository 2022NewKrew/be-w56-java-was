package service;

import controller.Controller;
import dto.PostMemoRequestParameterDto;
import mapper.PostMemoRequestParameterDtoMemoMapper;
import mapper.PostMemoRequestParameterDtoMemoMapperImpl;
import model.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AppRepository;

import java.util.List;

public class MemoService {

    private static final AppRepository APP_REPOSITORY = new AppRepository();
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private static final PostMemoRequestParameterDtoMemoMapper postMemoRequestParameterDtoMemoMapper = new PostMemoRequestParameterDtoMemoMapperImpl();

    public void post(PostMemoRequestParameterDto postMemoRequestParameterDto) {
        if (postMemoRequestParameterDto == null | !postMemoRequestParameterDto.isValid()) {
            throw new IllegalArgumentException("필수 정보 부족");
        }

        Memo memo = postMemoRequestParameterDtoMemoMapper.toRightObject(postMemoRequestParameterDto);
        APP_REPOSITORY.create(memo);
    }

    public List<Memo> postList() {
        return APP_REPOSITORY.findAll();
    }


}
