package com.nextrt.core.vo.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {
	private Integer subId;
	private Integer status;
	private Integer gotScore;
	private Integer score;
	private Integer type;
	private Long timeUsed;
	private Long memoryUsed;
	private long judgeTime;
	private String errorMessage;
	private String sn;
}
