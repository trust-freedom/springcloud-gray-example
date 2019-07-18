package com.freedom.springcloud.gray.ribbon;

import com.freedom.springcloud.gray.filter.GrayRequestContext;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * （弃用）仍使用ZoneAvoidanceRule中的逻辑，只是增加自定义TagMetadataPredicate谓词
 */
public class TagMetadataRule extends ZoneAvoidanceRule {
    private static final Logger logger = LoggerFactory.getLogger(TagMetadataRule.class);

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
    public Server choose(Object key) {
        // LoadBalancer获取AllServers，再通过Predicate谓词过滤（ZoneAvoidanceRule中的逻辑）
        List<Server> serverList = getPredicate().getEligibleServers(getLoadBalancer().getAllServers(), key);
        if (CollectionUtils.isEmpty(serverList)) {
            return null;
        }

        // 获取请求中的tag
        String tagHeader = getTagHeader();
        logger.info("tagHeader {}", tagHeader);

        // 自定义choose server逻辑，但只能返回带tag标签的第一个server，不能轮询
        List<Server> noMetaServerList = new ArrayList<>(); // 维护没有tag元数据的实例
        for (Server server : serverList) {
            Map<String, String> metadata = ((DiscoveryEnabledServer) server).getInstanceInfo().getMetadata();

            // tag策略
            String metaTag = metadata.get("tag");
            // 找到与tagHeader相同元数据的第一个Instance就返回，否则添加到noMetaServerList
            if (!StringUtils.isEmpty(metaTag)) {
                if (metaTag.equals(tagHeader)) {
                    return server;
                }
            }
            else {
                noMetaServerList.add(server);
            }
        }

        // 如果请求头中没有tag，且也有备选的无标签server
        // 仍使用ZoneAvoidanceRule#chooseRoundRobinAfterFiltering()逻辑，即再已经通过谓词过滤后，再轮询选择server
        if (StringUtils.isEmpty(tagHeader) && !noMetaServerList.isEmpty()) {
            logger.info("====> 无请求tagHeader...");
            return originChoose(noMetaServerList, key);
        }

        return null;
    }

    private Server originChoose(List<Server> noMetaServerList, Object key) {
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(noMetaServerList, key);
        if (server.isPresent()) {
            return server.get();
        } else {
            return null;
        }
    }

}
