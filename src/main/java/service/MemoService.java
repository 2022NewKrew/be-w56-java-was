package service;

import dto.memo.MemoItemDto;

import java.util.List;

public interface MemoService {
    List<MemoItemDto> findAll();
}
