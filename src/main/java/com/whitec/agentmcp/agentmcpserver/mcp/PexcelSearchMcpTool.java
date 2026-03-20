package com.whitec.agentmcp.agentmcpserver.mcp;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.StringJoiner;

@Component
public class PexcelSearchMcpTool {

    @Value("${pexels.api.key}")
    private String apiKey;

    private static final String PEXELS_SEARCH_URL = "https://api.pexels.com/v1/search";

    /**
     * 定义一个 MCP 工具。
     * Spring AI 会自动将此方法包装并发送给 LLM。
     * * @param query 搜索关键词（由 LLM 根据用户意图生成）
     * @param count 搜索数量（由 LLM 决定或默认）
     */
//    @Tool(description = "从 Pexels 图库搜索图片。返回图片的描述、作者和中等尺寸链接。")
//    public String searchPexels(@ToolParam(description = "Search query key") String query,
//                               @ToolParam(description = "Search count") Integer count) {
    /** mcp注解方式可不用手动注册Tools的bean */
    @McpTool(description = "从 Pexels 图库搜索图片。返回图片的描述、作者和中等尺寸链接。")
    public String searchPexels(@McpToolParam(description = "Search query key") String query,
                               @McpToolParam(description = "Search count") Integer count) {
        // 1. 参数校验
        if (query == null || query.isBlank()) {
            return "请输入搜索关键词。";
        }
        int perPage = (count == null || count <= 0) ? 3 : count;

        try {
            String key = System.getenv("api-key");
            // 2. 使用 Hutool 发起 HTTP 请求
            String body = HttpRequest.get(PEXELS_SEARCH_URL)
//                    .header("Authorization", apiKey)
                    .header("Authorization", key)
                    .form("query", query)
                    .form("per_page", perPage)
                    .timeout(10000) // 10秒超时
                    .execute()
                    .body();

            // 3. 使用 Hutool 解析结果
            JSONObject json = JSONUtil.parseObj(body);
            JSONArray photos = json.getJSONArray("photos");

            if (photos == null || photos.isEmpty()) {
                return "未找到关于 '" + query + "' 的相关图片。";
            }

            // 4. 格式化返回结果给 LLM
            StringJoiner sj = new StringJoiner("\n---\n");
            for (int i = 0; i < photos.size(); i++) {
                JSONObject photo = photos.getJSONObject(i);
                String alt = photo.getStr("alt", "无描述");
                String photographer = photo.getStr("photographer");
                String url = photo.getJSONObject("src").getStr("medium");

                sj.add(String.format("图片[%d]: %s\n摄影师: %s\n查看链接: %s",
                        i + 1, alt, photographer, url));
            }

            return "为你找到以下图片：\n\n" + sj.toString();

        } catch (Exception e) {
            return "调用 Pexels API 时发生错误: " + e.getMessage();
        }
    }

}
