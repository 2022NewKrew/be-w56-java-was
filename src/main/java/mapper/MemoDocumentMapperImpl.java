package mapper;

import model.Memo;
import org.bson.Document;

import java.time.ZoneId;

public class MemoDocumentMapperImpl implements MemoDocumentMapper {

    @Override
    public Document toRightObject(Memo memo) {
        if (memo == null) {
            return null;
        }

        Document document = new Document();
        document.append("name", memo.getName());
        document.append("content", memo.getContent());
        document.append("createdAt", memo.getCreatedAt());

        return document;
    }

    @Override
    public Memo toLeftObject(Document document) {
        if (document == null) {
            return null;
        }

        Memo memo = new Memo();
        memo.setName(document.getString("name"));
        memo.setContent(document.getString("content"));
        memo.setCreatedAt(document.getDate("createdAt").toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

        return memo;
    }
}
