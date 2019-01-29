package com.rocketsoftware.facein.core.webservices;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rocketsoftware.facein.core.Core;
import com.rocketsoftware.facein.core.webservices.service.PassportsService;
import com.rocketsoftware.facein.core.webservices.storage.StorageFileNotFoundException;
import com.rocketsoftware.facein.core.webservices.storage.StorageService;
import com.rocketsoftware.facein.ocrsdk.OCR;


@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class FileUploadController {

	private Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
    private final StorageService storageService;

    @Autowired
    private PassportsService passportsService;
    @Autowired
    private Core core;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/files")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
    	InputStream is;
		try {
			is = file.getInputStream();
			OCR ocrsdk = new OCR();
	    	return ocrsdk.passportNo(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
    }
    
    @PostMapping("/files/{passportid}")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,@PathVariable String passportid,
            RedirectAttributes redirectAttributes) {
        storageService.store(file);
        String fileName = storageService.load(file.getOriginalFilename()).getFileName().toString();
        fileName = "images/"+fileName;
        logger.info("You've successfully saved file("+fileName+")");
        
        Optional<String> passportPhotoPath = Optional.ofNullable(passportsService.getPassport(passportid)).flatMap(passport->Optional.ofNullable(passport.getPhotoPath()));
        logger.info("You've retrieved file path("+passportPhotoPath.get()+")");
        
        logger.info("Start comparing two faces  ("+passportPhotoPath.get()+", "+fileName+")...");
        String result = core.compareFaces(passportPhotoPath.get(), fileName);
        logger.info("Result:"+result);
        redirectAttributes.addFlashAttribute("result", result);
        return result;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
