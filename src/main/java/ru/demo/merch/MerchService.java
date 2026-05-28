package ru.demo.merch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;

import java.util.List;
import java.util.UUID;

public interface MerchService {
    MerchShort createMerch(MerchCreate request);

    Page<MerchShort> search(Pageable pageable);

    void deleteByListId(List<UUID> request);
}
