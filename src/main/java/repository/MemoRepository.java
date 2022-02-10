package repository;

import db.H2Repository;
import entity.MemoEntity;

public class MemoRepository extends H2Repository<MemoEntity, Long> {

    private static final MemoRepository memoRepository = new MemoRepository(MemoEntity.class);

    private MemoRepository(Class<MemoEntity> classType) {
        super(classType);
    }

    public static MemoRepository getInstance() {
        return memoRepository;
    }

}
