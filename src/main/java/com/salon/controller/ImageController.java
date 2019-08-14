package com.salon.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.salon.controller.error.ResponseError;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping(path="/api/image",
        produces="application/json",
        consumes = {"image/jpeg","multipart/form-data"})
@CrossOrigin(origins="*")
public class ImageController {

    //todo replace to properties
    private final String IMG_FOLDER =  "/home/anna/projects/imagesForSpringProject/";

    private final Cloudinary cloudinary;

    public ImageController(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ResponseBody
    public ResponseEntity saveImage(@RequestParam("file") MultipartFile image) throws IOException {
        File file = new File(IMG_FOLDER + "something.jpg");
        image.transferTo(file);
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        log.info("In saveImage() ----- url: {}",uploadResult.get("url"));
        return ResponseEntity.status(HttpStatus.CREATED).body(uploadResult);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IOException.class)
    @ResponseBody
    public ResponseError handleRuntimeException(IOException ex) {
        return new ResponseError(ex.getMessage());
    }
}
