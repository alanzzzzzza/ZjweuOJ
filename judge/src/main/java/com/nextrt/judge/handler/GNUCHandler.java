package com.nextrt.judge.handler;

import com.nextrt.judge.util.ExecutorUtil;
import com.nextrt.judge.util.FileUtils;
import com.nextrt.judge.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class GNUCHandler extends Handler {

	@Value("${judge.GNUC}")
	private String compilerWord;
	@Value("${judge.Crun}")
	private String runWord;

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = compilerWord.replace("PATH",path.getPath());
		return ExecutorUtil.exec(cmd, 15000);
	}
	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "main.c");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH", path.getPath());
	}
}
