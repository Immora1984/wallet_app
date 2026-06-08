package ru.demo.merch.impl;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.demo.config.minio.MinioUtils;
import ru.demo.merch.MerchMapper;
import ru.demo.merch.MerchRepository;
import ru.demo.merch.MerchService;
import ru.demo.merch.impl.jpa.Merch_;
import ru.demo.merch.model.*;
import ru.demo.merch.model.MerchModify.MerchUpdate;

import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MerchServiceImpl implements MerchService {

    @Value("${spring.minio.backet-name}")
    private String backetName;

    private final MerchRepository merchRepository;
    private final MinioUtils minioUtils;
    private final MinioClient minioClient;
    private final MerchMapper merchMapper;

    @Override
    public MerchShort createMerch(MerchCreate request) {
        return merchMapper.toShort(merchRepository.save(merchMapper.fromCreate(request)));
    }

    @Override
    public Page<MerchShort> search(Pageable pageable) {
        return merchRepository.findAll(pageable).map(merch -> {
            var shortDto = merchMapper.toShort(merch);

            if (merch.getPhotos() != null) {

                var photoUrls = minioUtils.generatePresignedUrls(merch.getPhotos());
                shortDto.setPhotoUrls(photoUrls);

                if (!photoUrls.isEmpty()) {
                    shortDto.setFirstPhotoUrl(photoUrls.getFirst());
                }
            }
            return shortDto;
        });
    }

    @Override
    public void deleteByListId(List<UUID> request) {
        if (request != null && !request.isEmpty()) merchRepository.deleteAllById(request);
    }

    @Override
    public MerchDetail findById(UUID merchId) {
        var merchDetail = merchMapper.toDetail(merchRepository.findById(merchId).orElseThrow(MerchException.NotFound::new));

        if (merchDetail.getPhotoUrls() != null)
            merchDetail.setPhotoUrls(minioUtils.generatePresignedUrls(merchDetail.getPhotoUrls()));

        return merchDetail;
    }

    @Override
    public void update(UUID merchId, MerchUpdate request) {
        merchRepository.update((rt, cu, cb) -> {

            if (request.getBand() != null) cu.set(rt.get(Merch_.BAND), request.getBand());
            if (request.getSize() != null) cu.set(rt.get(Merch_.SIZE), request.getSize());
            if (request.getPrice() != null) cu.set(rt.get(Merch_.PRICE), request.getPrice());
            if (request.getColor() != null) cu.set(rt.get(Merch_.COLOR), request.getColor());
            if (request.getPhoto() != null) cu.set(rt.get(Merch_.PHOTOS), upload(merchId, request));
            if (request.getCompound() != null) cu.set(rt.get(Merch_.COMPOUND), request.getCompound());
            if (request.getDescription() != null) cu.set(rt.get(Merch_.DESCRIPTION), request.getDescription());

            return cb.equal(rt.get(Merch_.ID), merchId);
        });
    }

    @Override
    public void deletePhoto(UUID merchId, List<String> photoUrl) {
        merchRepository.findById(merchId).ifPresent(result -> {
            var photo = result.getPhotos();

            var keys = photoUrl.stream().map(this::extractKeyFromUrl).toList();
            var isRemove = keys.stream().allMatch(photo::remove);

            if (!isRemove) throw new MerchException.RemovePhotoException();

            merchRepository.save(result);
        });
    }

    private String extractKeyFromUrl(String photoUrl) {
        try {
            var uri = new URI(photoUrl);
            var path = uri.getPath();
            var key = path.startsWith("/") ? path.substring(1) : path;

            return key.substring( key.indexOf('/') + 1);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> upload(UUID merchId, MerchUpdate request) {
        var files = request.getPhoto();
        var photos = merchRepository.findById(merchId).orElseThrow(MerchException.NotFound::new).getPhotos();

        if (photos.size() + files.size() > 3) throw new MerchException.PhotosLimit();

        return files.stream().map(file -> {
            try {
                var key = merchId + "-" + file.getOriginalFilename();
                minioClient.putObject(PutObjectArgs.builder()
                        .bucket(backetName)
                        .object(key)
                        .stream(new ByteArrayInputStream(file.getBytes()), file.getBytes().length, -1)
                        .build());
                return key;
            } catch (Exception e) {
                throw new MerchException.UploadImageException();
            }
        }).toList();
    }
}
