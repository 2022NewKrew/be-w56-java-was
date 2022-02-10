package service;

import dto.memo.MemoCreateDto;
import dto.memo.MemoItemDto;

import java.util.List;

public interface MemoService {
    List<MemoItemDto> findAll();

    void create(MemoCreateDto memoCreateDto, String sessionedUserId);
}
