package ru.demo.merch.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.demo.merch.MerchService;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchShort;
import ru.demo.util.ApiOperation;

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
}
