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
import ru.demo.merch.impl.jpa.Merch_;
import ru.demo.merch.model.*;
import ru.demo.merch.model.MerchModify.MerchUpdate;

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
        var page = merchRepository.findAll(pageable);
        return page.map(merchMapper::toShort);
    }

    @Override
    public void deleteByListId(List<UUID> request) {
        if (request != null && !request.isEmpty()) merchRepository.deleteAllById(request);
    }

    @Override
    public MerchDetail findById(UUID merchId) {
        return merchMapper.toDetail(merchRepository.findById(merchId).orElseThrow(MerchException.NotFound::new));
    }

    @Override
    public void update(UUID merchId, MerchUpdate request) {
        merchRepository.update((rt, cu, cb) -> {

            if (request.getBand() != null) cu.set(rt.get(Merch_.BAND), request.getBand());
            if (request.getSize() != null) cu.set(rt.get(Merch_.SIZE), request.getSize());
            if (request.getPrice() != null) cu.set(rt.get(Merch_.PRICE), request.getPrice());
            if (request.getColor() != null) cu.set(rt.get(Merch_.COLOR), request.getColor());
            if (request.getCompound() != null) cu.set(rt.get(Merch_.COMPOUND), request.getCompound());
            if (request.getDescription() != null) cu.set(rt.get(Merch_.DESCRIPTION), request.getDescription());

            return cb.equal(rt.get(Merch_.ID), merchId);
        });
    }
}
