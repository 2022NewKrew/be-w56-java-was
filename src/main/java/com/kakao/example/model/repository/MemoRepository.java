package com.kakao.example.model.repository;

import com.kakao.example.model.domain.Memo;

import java.util.List;
import java.util.Optional;

public interface MemoRepository {
    Optional<Memo> addMemo(Memo memo);

    List<Memo> findAll();
}
