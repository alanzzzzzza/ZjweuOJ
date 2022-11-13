package com.nextrt.judge.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
	private Integer subId;
	private Integer status;
	private Integer type;
	private Long timeUsed = -1L;
	private Long memoryUsed = -1L;
	private int gotScore = 0;
	private int score = 0;
	private String errorMessage;
	private long judgeTime;
	private String sn;

	public JudgeResult(Integer subId, Integer status, Long timeUsed, Long memoryUsed, String errorMessage) {
		this.subId = subId;
		this.status = status;
		this.timeUsed = timeUsed;
		this.memoryUsed = memoryUsed;
		this.errorMessage = errorMessage;
		this.score = 0;
		this.gotScore = 0;
		this.type = 0;
	}
}
