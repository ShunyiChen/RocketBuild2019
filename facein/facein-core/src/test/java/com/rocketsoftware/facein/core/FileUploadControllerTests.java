package com.rocketsoftware.facein.core;

import static org.springframework.test.web.servlet.ResultMatcher.matchAll;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class FileUploadControllerTests {

	@Autowired
    private MockMvc mockMvc;
	
	private Logger logger = LoggerFactory.getLogger(FileUploadControllerTests.class);
	
	/**
	 * Upload file with PassportNo
	 * 
	 * @throws Exception
	 */
//	@Test
	public void handleFileUpload() throws Exception {
		logger.info("Run handleFileUpload()");
		FileInputStream fi2 = new FileInputStream(new File("images/Barack_Obama.jpg"));
		MockMultipartFile secmp = new MockMultipartFile("file", "aa.jpg","multipart/form-data",fi2); 
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files/E61169299").file(secmp))
//        .andDo(print())
        .andExpect(matchAll(
    	      status().isOk(),
    	      content().string("not same person"))
    	 );
	}
	
	/**
	 * Upload file without PassportNo
	 * 
	 * @throws Exception
	 */
	@Test
	public void handleFileUpload2() throws Exception {
		logger.info("Run handleFileUpload2()");
		FileInputStream fi2 = new FileInputStream(new File("C:\\images\\amitab_young.jpg"));
		MockMultipartFile secmp = new MockMultipartFile("file", "IMG_7234.JPG","multipart/form-data",fi2); 
        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/files").file(secmp))
        .andExpect(matchAll(
    	      status().isOk(),
    	      content().string("E61169299"))
    	 );
	}
}
