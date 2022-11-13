package com.nextrt.judge.handler;

import com.nextrt.judge.util.ExecutorUtil;
import com.nextrt.judge.util.FileUtils;
import com.nextrt.judge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class Py3Handler extends Handler {
	@Value("${judge.Python3word}")
	private String compilerWord;

	@Value("${judge.Python3run}")
	private String runWord;

	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "main.py");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = compilerWord.replace("PATH",path.getPath());
		return ExecutorUtil.exec(cmd, 15000);
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH",path.getPath());
	}
}
