package com.dsltyyz.xuper.xuperchain.component;

import com.baidu.xuper.api.XuperClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 超级链客户端配置
 * @author dsltyyz
 * @date 2022-5-18
 */
@Component
public class XuperComponent {

    @Value("${xuper.client}")
    private String target;

    private XuperClient xuperClient;

    @PostConstruct
    public void init(){
        xuperClient = new XuperClient(target);
    }

    public XuperClient getClient(){
        return xuperClient;
    }
}
