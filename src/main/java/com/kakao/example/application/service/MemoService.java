package com.kakao.example.application.service;

import com.kakao.example.application.dto.MemoDto;

import java.util.List;

public interface MemoService {
    MemoDto addMemo(MemoDto memoDto);

    List<MemoDto> findAll();
}
