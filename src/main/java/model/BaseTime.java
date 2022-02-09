package model;

import java.time.LocalDateTime;

public abstract class BaseTime {

    private final LocalDateTime createTime;
    private LocalDateTime modifiedTime;

    public BaseTime(LocalDateTime createTime, LocalDateTime modifiedTime) {
        checkCreateTime(createTime);
        checkModifiedTime(modifiedTime);

        this.createTime = createTime;
        this.modifiedTime = modifiedTime;
    }

    private void checkCreateTime(LocalDateTime createTime) {
        if (createTime == null) {
            throw new IllegalArgumentException("illegal CreateTime");
        }
    }

    private void checkModifiedTime(LocalDateTime modifiedTime) {
        if (modifiedTime == null) {
            throw new IllegalArgumentException("illegal ModifiedTime");
        }
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(LocalDateTime modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}
