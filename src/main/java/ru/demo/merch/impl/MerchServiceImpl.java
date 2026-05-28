package ru.demo.merch.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.demo.merch.MerchMapper;
import ru.demo.merch.MerchRepository;
import ru.demo.merch.MerchService;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MerchServiceImpl implements MerchService {

    private final MerchRepository merchRepository;
    private final MerchMapper merchMapper;

    @Override
    public MerchShort createMerch(MerchCreate request) {
        return merchMapper.toShort(merchRepository.save(merchMapper.fromCreate(request)));
    }

    @Override
    public Page<MerchShort> search(Pageable pageable) {
        return merchRepository.findAll(pageable).map(merchMapper::toShort);
    }

    @Override
    public void deleteByListId(List<UUID> request) {
        if (request != null && !request.isEmpty()) merchRepository.deleteAllById(request);
    }
}
