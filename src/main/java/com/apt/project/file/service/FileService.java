package com.apt.project.file.service;

import com.apt.project.auth.models.User;
import com.apt.project.auth.repository.UserRepository;
import com.apt.project.auth.service.AuthService;
import com.apt.project.file.dto.FileContentDto;
import com.apt.project.file.dto.FileDto;
import com.apt.project.file.dto.NewFileContentDto;
import com.apt.project.file.model.File;
import com.apt.project.file.repository.FileRepository;
import com.apt.project.permission.dto.NewPermission;
import com.apt.project.permission.dto.NewPermissionDto;
import com.apt.project.permission.model.Permission;
import com.apt.project.permission.model.Role;
import com.apt.project.permission.service.PermissionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {
    final PermissionService permissionService;
    final AuthService authService;
    final FileRepository fileRepository;
    final UserRepository userRepository;

    public FileService(PermissionService permissionService, AuthService authService, FileRepository repository, UserRepository userRepository) {
        this.permissionService = permissionService;
        this.authService = authService;
        this.fileRepository = repository;
        this.userRepository = userRepository;
    }

    public List<FileDto> getAllFiles() {
        List<Permission> permissions = permissionService.findAllByUserId(authService.getUserId());
        return permissions
                .stream()
                .map(permission ->FileDto.from(fileRepository.findById(permission.getFileId()).get(),permission.getRole()))
                .toList();
    }



    public List<FileDto> getMyFiles() {
        return fileRepository.findAllByOwnerId(authService.getUserId())
                .stream()
                .map(file ->FileDto.from(file,Role.Owner))
                .toList();
    }

    public List<FileDto> getSharedFiles() {
        List<Permission> permissions = permissionService.findAllByUserId(authService.getUserId());
        return permissions
                .stream()
                .filter(permission -> permission.getRole() != Role.Owner)
                .map(permission ->FileDto.from(fileRepository.findById(permission.getFileId()).get(),permission.getRole()))
                .toList();
    }

    public ResponseEntity<?> getFileContent(String fileId) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if(optionalFile.isEmpty()) {
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(FileContentDto.from(optionalFile.get()));
    }


    public ResponseEntity<?> saveFileContent(String fileId, NewFileContentDto newFileContentDto) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if(optionalFile.isEmpty()) {
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        String userId = authService.getUserId();
        Optional<Permission> optionalPermission = permissionService.findByFileIdAndUserId(fileId, userId);

        if (optionalPermission.isEmpty()) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }

        Permission permission = optionalPermission.get();

        if(permission.getRole() == Role.Viewer) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }

        File file = optionalFile.get();
        file.setContent(newFileContentDto.content());
        fileRepository.save(file);
        return ResponseEntity.ok(FileDto.from(file,permission.getRole()));
    }


    public ResponseEntity<?> createFile(String filename) {
        boolean fileExists = fileRepository.findByName(filename).isPresent();
        if (fileExists) {
            return new ResponseEntity<>("file already exists", HttpStatus.BAD_REQUEST);
        }
        File newFile = new File(authService.getUserId(), filename);
        File file =  fileRepository.insert(newFile);
        String userId = authService.getUserId();

        NewPermission permission = new NewPermission(userId, file.getId(), Role.Owner);
        permissionService.newPermission(permission);
        return new ResponseEntity<>(FileDto.from(file,permission.role()), HttpStatus.CREATED);
    }

    public ResponseEntity<?> renameFile(String fileId, String name) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if(optionalFile.isEmpty()) {
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        String userId = authService.getUserId();
        Optional<Permission> optionalPermission = permissionService.findByFileIdAndUserId(fileId, userId);

        if (optionalPermission.isEmpty()) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }

        Permission permission = optionalPermission.get();

        if(permission.getRole() == Role.Viewer) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }

        File file = optionalFile.get();
        file.setName(name);
        fileRepository.save(file);
        return ResponseEntity.ok(FileDto.from(file,permission.getRole()));
    }

    public ResponseEntity<?> deleteFile(String fileId) {
        Optional<File> optionalFile = fileRepository.findById(fileId);
        if (optionalFile.isEmpty()) {
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        String userId = authService.getUserId();
        Optional<Permission> optionalPermission = permissionService.findByFileIdAndUserId(fileId, userId);

        if (optionalPermission.isEmpty()) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }

        Permission permission = optionalPermission.get();

        if (permission.getRole() != Role.Owner) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }

        fileRepository.deleteById(fileId);
        permissionService.deleteByFileId(fileId);
        return ResponseEntity.ok(" File Deleted Successfully!");
    }


    public ResponseEntity<?> share(NewPermissionDto request) {
        Optional<File> optionalFile = fileRepository.findById(request.fileId());
        if(optionalFile.isEmpty()) {
            return new ResponseEntity<>("file not found", HttpStatus.NOT_FOUND);
        }
        String userId = authService.getUserId();
        Optional<Permission> optionalPermission = permissionService.findByFileIdAndUserId(request.fileId(), userId);

        if (optionalPermission.isEmpty()) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }

        Permission permission = optionalPermission.get();

        if (permission.getRole() == Role.Viewer) {
            return new ResponseEntity<>("You don't have permission", HttpStatus.UNAUTHORIZED);
        }
        Optional<User> optionalUser = userRepository.findByUsername(request.username());

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();
        NewPermission newPermission=new NewPermission(user.getId(),request.fileId(),request.role());
        permissionService.newPermission(newPermission);
        return ResponseEntity.ok("file has been shared successfully ");
    }
}
