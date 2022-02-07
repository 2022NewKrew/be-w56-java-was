package myspring.main;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PageBarDto {

    private final PageDto prevPage;

    private final List<PageDto> pages;

    private final PageDto nextPage;

    public PageBarDto(PageDto prevPage, List<PageDto> pages, PageDto nextPage) {
        this.prevPage = prevPage;
        this.pages = pages;
        this.nextPage = nextPage;
    }

    static PageBarDto of(int barStartIndex, int barEndIndex, int totalPageCount, int size){
        int prevOffset = Math.max(barStartIndex-1, 0);
        int nextOffset = Math.min(barEndIndex+1, totalPageCount);
        return new PageBarDto(
                new PageDto(prevOffset, size),
                IntStream.range(barStartIndex,Math.min(barEndIndex, totalPageCount)).mapToObj(offset -> new PageDto(offset, size)).collect(Collectors.toList()),
                new PageDto(nextOffset, size)
        );
    }

    public PageDto getPrevPage() {
        return prevPage;
    }

    public List<PageDto> getPages() {
        return pages;
    }

    public PageDto getNextPage() {
        return nextPage;
    }
}
