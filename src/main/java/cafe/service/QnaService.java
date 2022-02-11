package cafe.service;

import cafe.dto.QnaCreateDto;
import cafe.dto.QnaDto;
import cafe.repository.QnaRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class QnaService {
    private final QnaRepository qnaRepository;

    public QnaService() {
        this.qnaRepository = new QnaRepository();
    }

    public void makeQna(QnaCreateDto createDto) {
        qnaRepository.addQna(createDto.toEntity());
    }

    public List<QnaDto> getQnaList() throws IOException {
        return qnaRepository.findAll().stream().map(QnaDto::of).collect(Collectors.toList());
    }

}
