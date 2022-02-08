package com.kakao.example.application.service;

import com.kakao.example.application.dto.MemoDto;
import com.kakao.example.model.domain.Memo;
import com.kakao.example.model.repository.MemoRepository;
import com.kakao.example.util.exception.MemoNotFoundException;
import framework.util.annotation.Autowired;
import framework.util.annotation.Component;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static framework.util.annotation.Component.ComponentType.SERVICE;

@Component(type = SERVICE)
public class MemoServiceImpl implements MemoService {
    private final MemoRepository memoRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public MemoServiceImpl(MemoRepository memoRepository, ModelMapper modelMapper) {
        this.memoRepository = memoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public MemoDto addMemo(MemoDto memoDto) {
        return memoRepository.addMemo(modelMapper.map(memoDto, Memo.class)).stream()
                .map(m -> modelMapper.map(m, MemoDto.class))
                .findFirst()
                .orElseThrow(MemoNotFoundException::new);
    }

    @Override
    public List<MemoDto> findAll() {
        return memoRepository.findAll().stream()
                .map(memo -> modelMapper.map(memo, MemoDto.class))
                .collect(Collectors.toList());
    }
}
