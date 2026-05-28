package ru.demo.merch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import ru.demo.merch.MerchService;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;
import ru.demo.merch.model.MerchUpdate;
import ru.demo.merch.model.MerchUpdate.MerchRemove;
import ru.demo.util.ApiOperation;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merch")
public class MerchController {

    private final MerchService merchService;

    @ApiOperation(
            method = RequestMethod.POST,
            tags = "Мерч",
            authorize = "hasAuthority('ADMIN')"
    )
    MerchShort createMerch(@RequestBody @Valid MerchCreate request) {
        return merchService.createMerch(request);
    }

    @ApiOperation(
            method = RequestMethod.GET,
            tags = "Мерч",
            authorize = "permitAll()"
    )
    PagedModel<MerchShort> findAll(Pageable pageable) {
        return new PagedModel<>(merchService.search(pageable));
    }

    @ApiOperation(
            method = RequestMethod.DELETE,
            tags = "Мерч",
            authorize = "hasAuthority('ADMIN')"
    )
    void removeMerch(@RequestParam List<UUID> ids) {
        merchService.deleteByListId(ids);
    }
}
