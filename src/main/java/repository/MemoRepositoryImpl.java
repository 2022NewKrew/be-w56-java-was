package repository;

import webserver.annotations.Autowired;
import webserver.annotations.Component;

@Component
public class MemoRepositoryImpl implements MemoRepository {
    @Autowired
    public MemoRepositoryImpl() {
    }

}
