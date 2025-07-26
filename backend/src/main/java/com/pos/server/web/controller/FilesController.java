package com.pos.server.web.controller;
import com.pos.server.infrastructure.config.FileUploadConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
@RestController
@RequestMapping("/files")
public class FilesController {
    private static final String UPLOAD_PRODUCT_IMG_DIR = "src/main/resources/static/images/products";
    private static final String UPLOAD_CATEGORY_IMG_DIR = "src/main/resources/static/images/categories";
    
    @Autowired
    private FileUploadConfig fileUploadConfig;


    @PostMapping("/upload/image/product/{id}")
    public ResponseEntity<String> uploadImage(@PathVariable("id") int productId, @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (!fileUploadConfig.isFileTypeAllowed(originalFilename)) {
            return ResponseEntity.badRequest().body("File type not allowed. Allowed types: " + String.join(", ", fileUploadConfig.getAllowedFileTypes()));
        }

        try {
            String fileExtension = "";
            String fileNameWithoutExtension = originalFilename;

            int dotIndex = originalFilename.lastIndexOf(".");

            if (dotIndex > 0) {
                fileNameWithoutExtension = originalFilename.substring(0, dotIndex);
                fileExtension = originalFilename.substring(dotIndex);
            }

            // Concatenate the ID to the filename
            String newFilename = fileNameWithoutExtension + productId + fileExtension;


            // Obtener la ruta absoluta del directorio de destino
            String absolutePath = new File(UPLOAD_PRODUCT_IMG_DIR).getAbsolutePath();

            // Crear el directorio si no existe
            Path uploadDirPath = Paths.get(absolutePath);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            // Guardar el archivo
            String filePath = Paths.get(absolutePath, newFilename).toString();

            file.transferTo(new File(filePath));



            return ResponseEntity.ok("File uploaded successfully:");

        } catch (IOException e) {
            e.printStackTrace();
            return (ResponseEntity<String>) ResponseEntity.notFound();
        }
    }

    @PostMapping("/upload/image/category/{id}")
    public ResponseEntity<String> uploadCategoryImg(@PathVariable("id") int categoryproductId, @RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file selected");
        }
        
        String originalFilename = file.getOriginalFilename();
        if (!fileUploadConfig.isFileTypeAllowed(originalFilename)) {
            return ResponseEntity.badRequest().body("File type not allowed. Allowed types: " + String.join(", ", fileUploadConfig.getAllowedFileTypes()));
        }

        try {
            String fileExtension = "";
            String fileNameWithoutExtension = originalFilename;

            int dotIndex = originalFilename.lastIndexOf(".");

            if (dotIndex > 0) {
                fileNameWithoutExtension = originalFilename.substring(0, dotIndex);
                fileExtension = originalFilename.substring(dotIndex);
            }

            // Concatenate the ID to the filename
            String newFilename = fileNameWithoutExtension + categoryproductId + fileExtension;


            // Obtener la ruta absoluta del directorio de destino
            String absolutePath = new File(UPLOAD_CATEGORY_IMG_DIR).getAbsolutePath();

            // Crear el directorio si no existe
            Path uploadDirPath = Paths.get(absolutePath);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }

            // Guardar el archivo
            String filePath = Paths.get(absolutePath, newFilename).toString();

            file.transferTo(new File(filePath));



            return ResponseEntity.ok("File uploaded successfully:");

        } catch (IOException e) {
            e.printStackTrace();
            return (ResponseEntity<String>) ResponseEntity.notFound();
        }
    }
}

