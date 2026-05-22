package ru.demo.merch.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.demo.merch.MerchMapper;
import ru.demo.merch.MerchRepository;
import ru.demo.merch.MerchService;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchServiceImpl implements MerchService {

    private final MerchRepository merchRepository;
    private final MerchMapper merchMapper;

    @Override
    public MerchShort createMerch(MerchCreate request) {
        return merchMapper.toShort(merchRepository.save(merchMapper.fromCreate(request)));
    }
}
