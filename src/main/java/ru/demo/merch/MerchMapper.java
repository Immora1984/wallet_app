package ru.demo.merch;

import ru.demo.merch.impl.jpa.Merch;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchDetail;
import ru.demo.merch.model.MerchShort;

public interface MerchMapper {

    Merch fromCreate(MerchCreate merchCreate);

    MerchShort toShort(Merch merch);

    MerchDetail toDetail(Merch merch);
}
