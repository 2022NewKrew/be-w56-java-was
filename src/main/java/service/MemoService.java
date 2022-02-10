package service;

import model.Memo;
import model.RequestHeader;
import repository.MemoRepository;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class MemoService {
    private final MemoRepository memoRepository;

    private MemoService() {
        memoRepository = MemoRepository.getInstance();
    }

    private static class MemoServiceHelper {
        private static final MemoService INSTANCE = new MemoService();
    }

    public static MemoService getInstance() {
        return MemoServiceHelper.INSTANCE;
    }

    public void writeMemo(RequestHeader requestHeader) throws SQLException {
        Memo memo = Memo.builder()
                .writer(requestHeader.getParameter("writer"))
                .content(requestHeader.getParameter("content"))
                .date(getLocalDateTimeNow())
                .build();
        memoRepository.join(memo);
    }

    public List<Memo> findAll() throws SQLException {
        return memoRepository.findAll();
    }

    private String getLocalDateTimeNow() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }
}
