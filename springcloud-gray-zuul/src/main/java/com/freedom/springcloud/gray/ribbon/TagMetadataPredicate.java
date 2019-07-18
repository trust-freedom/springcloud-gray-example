package com.freedom.springcloud.gray.ribbon;

import com.freedom.springcloud.gray.filter.GrayRequestContext;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 基于Tag元数据的谓词
 */
public class TagMetadataPredicate extends AbstractServerPredicate {
    private static final Logger logger = LoggerFactory.getLogger(TagMetadataPredicate.class);

    public TagMetadataPredicate(IRule rule) {
        super(rule);
    }

    /**
     * 获取请求头中的Tag
     * @return
     */
    private String getTagHeader(){
        // （弃用）不使用com.netflix.zuul.context.RequestContext维护请求数据
        //RequestContext context = RequestContext.getCurrentContext();
        //Map<String, String> zuulRequestHeaders = context.getZuulRequestHeaders();
        //
        //Set keySet = zuulRequestHeaders.keySet();
        //Iterator it = keySet.iterator();
        //while (it.hasNext()){
        //    String key = (String)it.next();
        //    if("tag".equals(key)){
        //        return zuulRequestHeaders.get(key);
        //    }
        //}

        // 从GrayRequestContext中获取tag
        String tag = GrayRequestContext.currentRequestContext().getDataValue("tag");
        if(StringUtils.hasText(tag)){
            return tag;
        }

        return null;
    }


    @Override
    public boolean apply(PredicateKey input) {
        // 请求头中的tag
        String tagHeader = getTagHeader();
        logger.info("tagHeader {}", tagHeader);

        // server元数据
        Map<String, String> metadata = ((DiscoveryEnabledServer) input.getServer()).getInstanceInfo().getMetadata();
        // tag元数据
        String metaTag = metadata.get("tag");

        // 逻辑：
        // 如果请求中有tag，只能选择server中有相同tag元数据的
        if(StringUtils.hasText(tagHeader)){
            if(tagHeader.equals(metaTag)){
                return true;
            }
        }
        // 如果请求中没有tag，只能选择server中没有tag元数据的
        else{
            if(StringUtils.isEmpty(metaTag)){
                return true;
            }
        }

        return false;
    }
}
