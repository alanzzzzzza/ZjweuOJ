package com.nextrt.acm.config.aspect;

import com.nextrt.acm.biz.exercise.ProblemBiz;
import com.nextrt.acm.config.thread.ViThreadPoolManager;
import com.nextrt.core.entity.annotation.ProblemOPLog;
import com.nextrt.core.entity.exercise.ProblemOperationLog;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Objects;

import static com.nextrt.acm.util.NetUtil.getPublicIP;

@Aspect
@Component
public class ProblemOPLogAspect {
    private final ProblemBiz problemBiz;

    public ProblemOPLogAspect(ProblemBiz problemBiz) {
        this.problemBiz = problemBiz;
    }

    @Pointcut("@annotation(com.nextrt.core.entity.annotation.ProblemOPLog)")
    public void logAspect() {
    }

    @After(value = "logAspect()")
    public void recordLog(JoinPoint joinPoint) throws Throwable {
        Class targetClass = Class.forName(joinPoint.getTarget().getClass().getName());
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            if (!method.isAnnotationPresent(ProblemOPLog.class))
                return;
            // 开始时间
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            ViThreadPoolManager.getInstance().execute(() -> {
                ProblemOperationLog problemOPLog = new ProblemOperationLog();
                problemOPLog.setUserId(request.getIntHeader("userId"));
                problemOPLog.setOperationTime(new Date());
                problemOPLog.setOperationIp(getPublicIP(request));
                problemOPLog.setOperationType(method.getAnnotation(ProblemOPLog.class).Desc());// 获取标签注解字段信息
                problemBiz.insertOperationLog(problemOPLog);
            });
        }
    }
}
