package com.sean.netty.rpc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AAA {
    private static final Logger logger = LogManager.getLogger(AAA.class);

    public static void main(String[] args) {


        // 记录debug级别的信息
        logger.debug("This is debug message.");
        // 记录info级别的信息
        logger.info("This is info message.");
        // 记录error级别的信息
        //logger.error("This is error message.");
    }
}
