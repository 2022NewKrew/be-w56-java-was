package repository;

import db.H2EntityManagerFactory;
import db.H2Repository;
import entity.MemoEntity;

import javax.persistence.EntityManager;
import java.util.List;

public class MemoRepository extends H2Repository<MemoEntity, Long> {

    private static final MemoRepository memoRepository = new MemoRepository(MemoEntity.class);

    private MemoRepository(Class<MemoEntity> classType) {
        super(classType);
    }

    public static MemoRepository getInstance() {
        return memoRepository;
    }

    @Override
    public List<MemoEntity> findAll() {
        EntityManager em = H2EntityManagerFactory.emf.createEntityManager();
        List<MemoEntity> findAll = em.createQuery(String.format("select entity from %s entity order by entity.createdAt desc", MemoEntity.class.getName()), MemoEntity.class)
                .getResultList();
        em.close();
        return findAll;
    }
}
