package myspring.main;

public class PageDto {

    private final int offset;

    private final int number;

    private final int size;

    public PageDto(int offset, int size) {
        this.offset = offset;
        this.number = offset + 1;
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

}
