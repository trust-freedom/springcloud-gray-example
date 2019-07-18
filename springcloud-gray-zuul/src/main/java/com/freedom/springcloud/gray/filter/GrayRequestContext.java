package com.freedom.springcloud.gray.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 灰度请求上下文
 * 可以在线程隔离模式下跨线程设置、获取请求中的数据
 */
public class GrayRequestContext {

    // HystrixRequestVariable接口实现类，是放在HystrixRequestContext.state这个ConcurrentHashMap中的默认变量
    private static final HystrixRequestVariableDefault<GrayRequestContext> CURRENT_CONTEXT = new HystrixRequestVariableDefault<>();

    private Map<String, String> data = new ConcurrentHashMap<>();

    /**
     * 在当前线程的HystrixRequestContext.state中设置 HystrixRequestVariableDefault : new GrayRequestContext()
     */
    public static void initCurrentRequestContext(){
        CURRENT_CONTEXT.set(new GrayRequestContext());
    }

    public static GrayRequestContext currentRequestContext(){
        return CURRENT_CONTEXT.get();
    }

    public Map<String, String> getData() {
        return data;
    }

    public void addData(String name, String value){
        getData().put(name, value);
    }

    public String getDataValue(String name){
        return getData().get(name);
    }
}
