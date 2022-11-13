package com.nextrt.judge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResultCase {
	private Integer status;
	private Long timeUsed;
	private Long memoryUsed;
	private String errorMessage;

}
