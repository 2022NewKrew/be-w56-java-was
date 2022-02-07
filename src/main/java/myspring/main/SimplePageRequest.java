package myspring.main;


public class SimplePageRequest implements Pageable {

    private static final int BAR_SIZE = 5;

    private final long offset;

    private final int size;

    public SimplePageRequest() {
        this(0, 5);
    }

    public SimplePageRequest(long offset, int size) {
        this.offset = offset;
        this.size = size;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public int getSize() {
        return size;
    }

    public int getStartPageIndex() {
        return (int)offset*size;
    }

    public int getEndPageIndex() {
        return (int)offset*size + size;
    }

    public int getStartBarIndex() {
        return (int)offset/BAR_SIZE;
    }

    public int getEndBarIndex() {
        return (int)offset/BAR_SIZE + BAR_SIZE;
    }

    public int getTotalPageCount(int totalArticleCount) {
        return (int)Math.ceil((double)totalArticleCount/(double)size);
    }

}
