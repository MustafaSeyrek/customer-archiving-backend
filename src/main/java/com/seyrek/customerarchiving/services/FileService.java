package com.seyrek.customerarchiving.services;

import com.seyrek.customerarchiving.entities.File;
import com.seyrek.customerarchiving.repositories.FileRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Service
public class FileService {
    private FileRepository fileRepository;
    private Path fileStorageLocation;

    @Autowired
    public FileService(FileRepository fileRepository, Environment env) {
        this.fileRepository = fileRepository;

        this.fileStorageLocation = Paths.get(env.getProperty("app.file.upload-dir", "./uploads/files"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Stream<File> getAllFiles() {
        return fileRepository.findAll().stream();
    }

    public Resource getFileAsResourceByCode(String code) throws IOException {
        Path dirPath = this.fileStorageLocation;
        AtomicReference<Path> foundFile = new AtomicReference<>();
        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(code)) {
                foundFile.set(file);
                return;
            }
        });
        if (foundFile.get() != null) {
            return new UrlResource(foundFile.get().toUri());
        }
        return null;
    }

    public void deleteByCode(String code) {
        fileRepository.deleteByCode(code);
    }

    public File findFileByCode(String code) {
        return fileRepository.findByCode(code);
    }

    public File createFile(MultipartFile file, File bodyFile) throws IOException {
        String name = StringUtils.cleanPath(file.getOriginalFilename());
        String code = RandomStringUtils.randomAlphanumeric(8);
        Path targetLocation = this.fileStorageLocation.resolve(code + "-" + name);
        File f = File.builder().name(name).path("/uploads/files/" + code + "-" + name).createdDate(new Date()).customerId(bodyFile.getCustomerId()).createdId(bodyFile.getCreatedId())
                .code(code).build();
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileRepository.save(f);
    }

    public File updateFileByCode(String code, MultipartFile file, File bodyFile) throws IOException {
        Optional<File> oldFile = Optional.ofNullable(fileRepository.findByCode(code));
        if (oldFile.isPresent()) {
            File foundFile = oldFile.get();
            String newCode = RandomStringUtils.randomAlphanumeric(8);
            String name = StringUtils.cleanPath(file.getOriginalFilename());
            foundFile.setCode(newCode);
            foundFile.setUpdatedId(bodyFile.getUpdatedId());
            foundFile.setName(name);
            foundFile.setPath("/uploads/files/" + code + "-" + name);
            foundFile.setUpdatedDate(new Date());
            fileRepository.save(foundFile);
            Path targetLocation = this.fileStorageLocation.resolve(newCode + "-" + name);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            Resource resource = getFileAsResourceByCode(code);
            resource.getFile().delete();

            return foundFile;
        } else {
            return null;
        }
    }
}
