package com.whitec.agentmcp.agentmcpserver.mcp;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PexelsImageToolsTest {

    @Resource
    private PexcelSearchMcpTool pexelsMcpTool;

    @Test
    void searchPexels() {
        String content = pexelsMcpTool.searchPexels("帮我查询彭于晏的图片", 10);
        System.out.println(content);
        Assertions.assertNotNull(content);
    }
}