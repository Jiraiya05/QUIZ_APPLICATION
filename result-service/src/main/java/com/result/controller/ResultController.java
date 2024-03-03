package com.result.controller;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.result.entities.ResultData;
import com.result.genericresponse.GenericResponse;
import com.result.genericresponse.GenericResponseBody;
import com.result.response.ResultResponse;
import com.result.service.ResultService;

@RestController
@RequestMapping("/result")
public class ResultController {
	
	@Autowired
	private ResultService service;
	
	@Value("${result.download.path}")
	private String downloadFilePath;
	
	@GetMapping("/{quizId}")
	public GenericResponse<ResultResponse> getResult(@RequestHeader("X-userId") String userId, @RequestHeader("X-userName") String userName, @PathVariable Long quizId, @RequestParam Integer cutOff, @RequestParam Integer markPerQuestion) throws Exception {
		
		ResultResponse processResponse = service.processResponse(Long.parseLong(userId), userName, quizId, cutOff, markPerQuestion);
		
		return new GenericResponse<>(GenericResponseBody.successBody("Result Fetched successfully"), processResponse);
	}
	
	@GetMapping("/download/{quizId}")
	public ResponseEntity<Resource> downloadResultFile(@RequestHeader("X-userId") String userId, @RequestHeader("X-userName") String userName, @PathVariable Long quizId, @RequestParam Integer cutOff, @RequestParam Integer markPerQuestion) throws Exception {
		
		String filePath = downloadFilePath+"RESULT_"+userId+"_"+quizId+".txt";
		
		service.downloadResult(Long.parseLong(userId), userName, quizId, cutOff, markPerQuestion, filePath);
		
		if (Files.exists(Paths.get(filePath)) && Files.isRegularFile(Paths.get(filePath))) {
            byte[] data = Files.readAllBytes(Paths.get(filePath));
            ByteArrayResource resource = new ByteArrayResource(data);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=textfile.txt");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(data.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/mongo/{quizId}")
	public GenericResponse<ResultData> getDataFromMongo(@RequestHeader("X-userId") String userId, @PathVariable Long quizId) {
		ResultData dataFromMongo = null;
		try {
		dataFromMongo = service.getDataFromMongo(Long.parseLong(userId), quizId);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return new GenericResponse<ResultData>(GenericResponseBody.successBody("Data from mongo fetched successfully"), dataFromMongo);
	}

}
