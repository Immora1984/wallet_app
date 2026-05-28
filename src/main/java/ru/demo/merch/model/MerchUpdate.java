package ru.demo.merch.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MerchUpdate {

    @Getter
    @Setter
    public static class MerchRemove {
        private List<UUID> ids;
    }
}
