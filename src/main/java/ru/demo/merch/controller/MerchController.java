package ru.demo.merch.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.demo.merch.MerchService;
import ru.demo.merch.model.MerchCreate;
import ru.demo.merch.model.MerchDetail;
import ru.demo.merch.model.MerchModify.MerchUpdate;
import ru.demo.merch.model.MerchShort;
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
    void removeMerch(@RequestParam List<UUID> ids) {merchService.deleteById(ids);}

    @ApiOperation(
            path = "/{merchId}",
            method = RequestMethod.GET,
            authorize = "permitAll()"
    )
    MerchDetail getById(@PathVariable UUID merchId) {return merchService.findById(merchId);}

    @ApiOperation(
            path = "/{merchId}",
            method = RequestMethod.PUT,
            authorize = "hasAuthority('ADMIN')"
    )
    void updateMerch(@PathVariable UUID merchId, @RequestBody @Valid MerchUpdate request) {
        merchService.update(merchId, request);
    }

    @ApiOperation(
            path = "/{merchId}/upload",
            method = RequestMethod.PUT,
            authorize = "hasAuthority('ADMIN')"
    )
    void uploadImage(@PathVariable UUID merchId, @RequestPart @NonNull List<MultipartFile> file) {
        var merchUpdate = new MerchUpdate();
        merchUpdate.setPhoto(file);
        merchService.update(merchId, merchUpdate);
    }

    @ApiOperation(
            method = RequestMethod.PUT,
            path = "/{merchId}/delete",
            authorize = "hasAuthority('ADMIN')"
    )
    void deletePhoto(@PathVariable UUID merchId, @RequestPart List<String> photoUrl) {
        merchService.deletePhoto(merchId, photoUrl);
    }
}
