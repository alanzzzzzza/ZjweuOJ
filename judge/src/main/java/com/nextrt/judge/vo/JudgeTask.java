package com.nextrt.judge.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeTask {
	private Integer subId;
	private Integer proId;
	private List<String> input;
	private List<String> output;
	private List<Integer> score;
	private int type; //0 acm 1 oi
	private Long timeLimit;
	private Long memoryLimit;
	private Integer judgeId;
	private String src;
}
