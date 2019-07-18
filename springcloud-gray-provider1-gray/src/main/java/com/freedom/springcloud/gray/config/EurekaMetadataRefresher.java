package com.freedom.springcloud.gray.config;

import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EurekaMetadataRefresher {

    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    @ApolloConfigChangeListener
    public void onChange(ConfigChangeEvent changeEvent) {
        Map<String, String> appMetadata = new HashMap<>();

        //判断发送变更的配置中是否有eureka metadata相关的
        for (String changedKey : changeEvent.changedKeys()) {
            if (changedKey.startsWith("eureka.instance.metadata-map.")) {
                ConfigChange change = changeEvent.getChange(changedKey);

                String changeType = change.getChangeType().toString();
                String key = changedKey.substring(new String("eureka.instance.metadata-map.").length());
                String value = "";
                // 预防空指针
                if(!"DELETED".equals(changeType)){
                    value = change.getNewValue();
                }
                appMetadata.put(key, value);
            }
        }

        // 修改元数据
        applicationInfoManager.registerAppMetadata(appMetadata);

    }

}
