package ru.demo.merch;

import org.springframework.stereotype.Component;
import ru.demo.merch.impl.jpa.Merch;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;

@Component
public interface MerchMapper {

    Merch fromCreate(MerchCreate merchCreate);

    MerchShort toShort(Merch merch);
}
