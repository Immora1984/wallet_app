package ru.demo.merch.impl;

import org.springframework.stereotype.Component;
import ru.demo.merch.MerchMapper;
import ru.demo.merch.impl.jpa.Merch;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchDetail;
import ru.demo.merch.model.MerchShort;

@Component
public class MerchMappers implements MerchMapper {

    @Override
    public Merch fromCreate(MerchCreate merchCreate) {
        var merch = new Merch();
        merch.setBand(merchCreate.getBand());
        merch.setColor(merchCreate.getColor());
        merch.setSize(merchCreate.getSize());
        merch.setPrice(merchCreate.getPrice());
        merch.setCompound(merchCreate.getCompound());
        merch.setDescription(merchCreate.getDescription());
        return merch;
    }

    @Override
    public MerchShort toShort(Merch merch) {
        var target = new MerchShort();
        target.setId(merch.getId());
        target.setBand(merch.getBand());
        target.setSize(merch.getSize());
        target.setColor(merch.getColor());
        target.setPrice(merch.getPrice());
        target.setPhotos(merch.getPhotos());
        target.setCompound(merch.getCompound());
        target.setDescription(merch.getDescription());
        return target;
    }

    @Override
    public MerchDetail toDetail(Merch merch) {
        var target = new MerchDetail();
        target.setId(merch.getId());
        target.setBand(merch.getBand());
        target.setSize(merch.getSize());
        target.setPrice(merch.getPrice());
        target.setColor(merch.getColor());
        target.setPhotos(merch.getPhotos());
        target.setCompound(merch.getCompound());
        target.setDescription(merch.getDescription());
        return target;
    }
}
