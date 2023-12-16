package com.seyrek.customerarchiving.controllers;

import com.seyrek.customerarchiving.entities.File;
import com.seyrek.customerarchiving.services.FileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/files")
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<File>> getAllFiles() {
        List<File> files = fileService.getAllFiles().map(db -> {
            return new File(db.getId(), db.getCode(), db.getName(), db.getPath(), db.getCustomerId(), db.getCreatedId(), db.getCreatedDate(), db.getUpdatedId(), db.getUpdatedDate());
        }).collect(Collectors.toList());
        return ResponseEntity.status(OK).body(files);
    }

    @PostMapping
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestBody File bodyFile) {
        try {
            fileService.createFile(file, bodyFile);
            return new ResponseEntity<>("File uploaded successfully!", OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

    @GetMapping("/download/{code}")
    public ResponseEntity<?> getFileByCode(@PathVariable String code) {
        Resource resource = null;
        try {
            resource = fileService.getFileAsResourceByCode(code);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource == null) {
            return new ResponseEntity<>("File not found!", NOT_FOUND);
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteByCode(@PathVariable String code) throws IOException {
        Resource resource = null;
        try {
            resource = fileService.getFileAsResourceByCode(code);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        if (resource == null) {
            return new ResponseEntity<>(NOT_FOUND);
        } else {
            fileService.deleteByCode(code);
            resource.getFile().delete();
            return new ResponseEntity<>(OK);
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<String> updateFileByCode(@PathVariable String code, @RequestParam("file") MultipartFile file, @RequestBody File bodyFile) {
        try {
            File f = fileService.updateFileByCode(code, file, bodyFile);
            if (f != null) {
                return new ResponseEntity<>("File updated successfully!", OK);
            } else {
                return new ResponseEntity<>("File not updated!", OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.EXPECTATION_FAILED);
        }
    }

}
