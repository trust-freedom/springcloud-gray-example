package com.freedom.springcloud.gray.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 提取请求中的标签
 */
public class ExtractTagFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 2000;
    }

    /**
     * 提取请求中的tag标签
     * 目前是从请求头中获取
     * @return
     */
    private String extractTagHeader(){
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        return request.getHeader("tag");
    }

    /**
     * 如果请求头中有tag标签
     * @return
     */
    @Override
    public boolean shouldFilter() {
        String tagHeader = extractTagHeader();
        if(StringUtils.hasText(tagHeader)){
            return true;
        }
        return false;
    }

    @Override
    public Object run() {
        // 提取tag请求头
        String tagHeader = extractTagHeader();

        // （废弃）不支持线程隔离模式
        //RequestContext context = RequestContext.getCurrentContext();
        //context.addZuulRequestHeader("tag", tagHeader);

        //直接用报错
        //HystrixRequestVariableDefault<String> test = new HystrixRequestVariableDefault<>();
        //test.set("test");

        // 向灰度请求上下文中添加请求头中的tag
        // 实际维护的是 threadlocal -> HystrixRequestContext -> state(Map) -> HystrixRequestVariableDefault : GrayRequestContext -> GrayRequestContext.data（tag:gray）
        GrayRequestContext.currentRequestContext().addData("tag", tagHeader);

        return null;
    }




}
