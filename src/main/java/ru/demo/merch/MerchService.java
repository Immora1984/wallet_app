package ru.demo.merch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;

public interface MerchService {
    MerchShort createMerch(MerchCreate request);

    Page<MerchShort> search(Pageable pageable);
}
