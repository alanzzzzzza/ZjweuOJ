package com.nextrt.judge.config.thread;

/**
 * 当前类（子系统中定义的类）继承 ThreadPoolManager 类，设置相关参数
 */
public class ViThreadPoolManager extends ThreadPoolManager{
    private static ThreadPoolManager threadPool  = null;

    public synchronized static ThreadPoolManager getInstance() {
        if(threadPool == null) {
            threadPool = new ViThreadPoolManager();
        }
        return threadPool;
    }

    @Override
    protected String getThreadPoolName() {
        return "GeekTheread";
    }

    @Override
    protected int corePoolSize() {
        return 10;
    }

    @Override
    protected int maximumPoolSize() {
        return 20;
    }
}
