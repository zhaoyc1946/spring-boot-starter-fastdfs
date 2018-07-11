package com.zhaoyc.config;


import com.zhaoyc.common.MyException;
import com.zhaoyc.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * Spring boot auto configuration for fastdfs.
 *
 * @author zhaoyc
 */
@Configuration
@EnableConfigurationProperties(FastDfsProperties.class)
public class FastDfsConfiguration {

    @Autowired
    private FastDfsProperties fastDfsProperties;

    /**
     * If the fastdfs connection connInitialized
     */
    private boolean connInitialized;


    /**
     * Set the scope of this bean to 'prototype' to avoid concurrency usage issue of StorageClient instance.
     *
     * @return
     * @throws IOException
     * @throws MyException
     */
    @Scope("prototype")
    @Bean
    public FastDfsManager fastDfsManager() throws IOException, MyException {
        if (!connInitialized) {
            ClientGlobal.initByProperties(fastDfsProperties.getProperties());
            this.connInitialized = true;
        }

        final TrackerClient trackerClient = new TrackerClient();

        return new FastDfsManager(trackerClient);
    }

    /**
     * Do the initialization
     *
     * @throws IOException
     * @throws MyException
     */
    @PostConstruct
    private void initFastDfs() throws IOException, MyException {
        if (fastDfsProperties.getInitConnOnLoad() != null && fastDfsProperties.getInitConnOnLoad()) {
            ClientGlobal.initByProperties(fastDfsProperties.getProperties());

            this.connInitialized = true;
        }
    }
}
