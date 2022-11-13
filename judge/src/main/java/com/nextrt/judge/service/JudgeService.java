package com.nextrt.judge.service;

import com.nextrt.judge.handler.*;
import com.nextrt.judge.service.mq.RabbitMqSend;
import com.nextrt.judge.vo.JudgeResult;
import com.nextrt.judge.vo.JudgeTask;
import org.springframework.stereotype.Service;

import static com.nextrt.judge.config.SystemConfig.DealNum;
import static com.nextrt.judge.util.SystemUtil.getSN;

@Service
public class JudgeService {

	private final GNUCHandler gnucHandler;
	private final GNUCPPHandler gnucppHandler;
	private final Py2Handler py2Handler;
	private final Py3Handler py3Handler;
	private final JavaHandler javaHandler;
	private final RubyHandler rubyHandler;
	private final GoHandler goHandler;
	private final RabbitMqSend rabbitMqSend;
	private final MonoHandler monoHandler;

	public JudgeService(GNUCHandler gnucHandler, GNUCPPHandler gnucppHandler, Py2Handler py2Handler, Py3Handler py3Handler, JavaHandler javaHandler, RubyHandler rubyHandler, GoHandler goHandler, RabbitMqSend rabbitMqSend, MonoHandler monoHandler) {
		this.gnucHandler = gnucHandler;
		this.gnucppHandler = gnucppHandler;
		this.py2Handler = py2Handler;
		this.py3Handler = py3Handler;
		this.javaHandler = javaHandler;
		this.rubyHandler = rubyHandler;
		this.goHandler = goHandler;
		this.rabbitMqSend = rabbitMqSend;
		this.monoHandler = monoHandler;
	}

	public void judge(JudgeTask task) {
		long start = System.currentTimeMillis();
		JudgeResult result;
		if (task.getJudgeId() == null || task.getJudgeId() < 1 || task.getJudgeId() > 8) {
			result = new JudgeResult(task.getSubId(),7,0L,0L,"编译选项有误!");
		} else {
			Handler handler;
			switch (task.getJudgeId()) {
				case 2:
					handler = gnucppHandler;
					break;
				case 3:
					handler = javaHandler;
					break;
				case 4:
					handler = py2Handler;
					break;
				case 5:
					handler = py3Handler;
					break;
				case 6:
					handler = rubyHandler;
					break;
				case 7:
					handler = goHandler;
					break;
				case 8:
					handler = monoHandler;
					break;
				default:
					handler = gnucHandler;
			}
			result = handler.judge(task);
		}
		result.setSubId(task.getSubId());
		result.setSn(getSN());
		result.setJudgeTime((System.currentTimeMillis() - start));
		rabbitMqSend.sendResult(result);
		++DealNum;
	}
}
