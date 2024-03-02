package com.quiz.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UploadRequest {
	
//	private Long userId;
	private Long quizId;
	private String filePath;

}
