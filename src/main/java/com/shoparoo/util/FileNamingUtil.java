package com.shoparoo.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

public class FileNamingUtil {
    public static String nameFile(MultipartFile multipartFile) {
        String originalFileName =
                StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        Integer fileDotIndex = originalFileName.lastIndexOf('.');
        String fileExtension = originalFileName.substring(fileDotIndex);
        return UUID.randomUUID().toString() + fileExtension;
    }
}
