package service;

import controller.Controller;
import mapper.MapMemoMapper;
import mapper.MapMemoMapperImpl;
import model.Memo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import repository.AppRepository;

import java.util.List;
import java.util.Map;

public class MemoService {

    private static final AppRepository APP_REPOSITORY = new AppRepository();
    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private static final MapMemoMapper MEMO_MAPPER = new MapMemoMapperImpl();

    public void post(Map<String, String> parameters) {
        if (parameters == null) {
            throw new IllegalArgumentException("필수 정보 부족");
        }

        Memo memo = MEMO_MAPPER.toRightObject(parameters);
        APP_REPOSITORY.create(memo);
    }

    public List<Memo> postList() {
        return APP_REPOSITORY.findAll();
    }


}
