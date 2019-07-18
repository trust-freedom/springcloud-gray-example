package com.freedom.springcloud.gray.ribbon;

import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 继承ZoneAvoidanceRule，只是添加一个基于Tag的谓词TagMetadataPredicate
 */
public class TagMetadataRule2 extends ZoneAvoidanceRule {
    private static final Logger logger = LoggerFactory.getLogger(TagMetadataRule2.class);

    protected CompositePredicate tagCompositePredicate;

    public TagMetadataRule2(){
        // 初始化ZoneAvoidanceRule
        super();

        // 初始化，添加TagMetadataPredicate到CompositePredicate
        init();
    }

    protected void init() {
        TagMetadataPredicate grayPredicate = new TagMetadataPredicate(this);
        tagCompositePredicate = CompositePredicate.withPredicates(super.getPredicate(),
                grayPredicate).build();
    }

    /**
     * 重写ZoneAvoidanceRule的getPredicate()，返回添加了tag谓词策略的tagCompositePredicate
     * @return
     */
    @Override
    public AbstractServerPredicate getPredicate() {
        return tagCompositePredicate;
    }

}
