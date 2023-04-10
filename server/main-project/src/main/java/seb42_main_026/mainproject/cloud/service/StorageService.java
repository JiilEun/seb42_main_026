package seb42_main_026.mainproject.cloud.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void imageStore(MultipartFile file, String encodedFileName);

    void voiceStore(MultipartFile file, String encodedFileName);
}
