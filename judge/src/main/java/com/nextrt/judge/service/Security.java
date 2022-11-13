package com.nextrt.judge.service;

import com.nextrt.judge.vo.JudgeResult;
import com.nextrt.judge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Security {

	@Value("${judge.sensitive-key}")
	private String sensitive;

	public boolean checkSecurity(JudgeTask task, JudgeResult result) {
		for (String key: sensitive.split(",")) {
			if (task.getSrc().contains(key)) {
				result.setMemoryUsed(0L);
				result.setTimeUsed(0L);
				result.setStatus(9);
				result.setErrorMessage("the src is't allowed to contains sensitive key : " + key);
				return false;
			}
		}
		return true;
	}
}
