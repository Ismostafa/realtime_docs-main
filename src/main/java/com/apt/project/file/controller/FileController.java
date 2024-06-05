package com.apt.project.file.controller;

import com.apt.project.file.dto.FileDto;
import com.apt.project.file.dto.NewFileContentDto;
import com.apt.project.file.dto.NewFileDto;
import com.apt.project.file.service.FileService;
import com.apt.project.permission.dto.NewPermissionDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController()
@RequestMapping("api/files")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("")
    public ResponseEntity<List<FileDto>> getAllFiles() {
        List<FileDto> files = fileService.getAllFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("my")
    public ResponseEntity<List<FileDto>> getMyFiles() {
        List<FileDto> files = fileService.getMyFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("shared")
    public ResponseEntity<List<FileDto>> getSharedFiles() {
        List<FileDto> files = fileService.getSharedFiles();
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<?> getFileContent(@PathVariable String fileId) {
        return fileService.getFileContent(fileId);
    }

    @PostMapping("/{fileId}")
    public ResponseEntity<?> saveFileContent(@PathVariable String fileId,
                                             @RequestBody NewFileContentDto newFileContentDto) {
        return fileService.saveFileContent(fileId, newFileContentDto);
    }

    @PostMapping("")
    public ResponseEntity<?> createFile(@Valid @RequestBody NewFileDto newFile) {
        return fileService.createFile(newFile.fileName());
    }

    @PutMapping("/rename/{id}")
    public ResponseEntity<?> renameFile(@PathVariable String id, @Valid @RequestBody NewFileDto request) {
        return fileService.renameFile(id, request.fileName());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable String id) {
        return fileService.deleteFile(id);
    }

    @PostMapping("/share")
    public ResponseEntity<?> ShareFile(@Valid @RequestBody NewPermissionDto request) {
        return fileService.share(request);
    }
}
