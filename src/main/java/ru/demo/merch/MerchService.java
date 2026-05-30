package ru.demo.merch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchDetail;
import ru.demo.merch.model.MerchModify.MerchUpdate;
import ru.demo.merch.model.MerchShort;
import ru.demo.merch.model.MerchModify;

import java.util.List;
import java.util.UUID;

public interface MerchService {
    MerchShort createMerch(MerchCreate request);

    Page<MerchShort> search(Pageable pageable);

    void deleteByListId(List<UUID> request);

    MerchDetail findById(UUID merchId);

    void update(UUID merchId, MerchUpdate request);
}
