package com.whitec.agentmcp.agentmcpserver;

import com.whitec.agentmcp.agentmcpserver.mcp.PexcelSearchMcpTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgentMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgentMcpServerApplication.class, args);
    }

//    @Bean
//    public ToolCallbackProvider weatherTools(PexcelSearchMcpTool pexcelSearchMcpTool) {
//        return MethodToolCallbackProvider.builder().toolObjects(pexcelSearchMcpTool).build();
//    }

}
