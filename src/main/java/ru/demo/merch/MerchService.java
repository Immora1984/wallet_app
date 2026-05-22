package ru.demo.merch;

import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;

public interface MerchService {
    MerchShort createMerch(MerchCreate request);
}
