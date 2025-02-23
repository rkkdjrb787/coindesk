package com.coindesk.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.coindesk.dto.CurrencyDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCurrencyCRUD() throws Exception {
        // 測試創建幣別
        CurrencyDto newCurrency = new CurrencyDto();
        newCurrency.setId("JPY");
        newCurrency.setChineseName("日圓");

        // 創建幣別
        mockMvc.perform(post("/api/currency")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCurrency)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("JPY"))
                .andExpect(jsonPath("$.chineseName").value("日圓"));

        // 測試查詢幣別
        mockMvc.perform(get("/api/currency/JPY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("JPY"))
                .andExpect(jsonPath("$.chineseName").value("日圓"));

        // 測試更新幣別
        CurrencyDto updateCurrency = new CurrencyDto();
        updateCurrency.setChineseName("日元");

        mockMvc.perform(put("/api/currency/JPY")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCurrency)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chineseName").value("日元"));

        // 確認更新成功
        mockMvc.perform(get("/api/currency/JPY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chineseName").value("日元"));

        // 測試刪除幣別
        mockMvc.perform(delete("/api/currency/JPY"))
                .andExpect(status().isOk());

        // 確認刪除後查詢不到
        mockMvc.perform(get("/api/currency/JPY"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCurrencyDataTransform() throws Exception {
        // 1. 先取得原始 API 數據
        String originalResponse = mockMvc.perform(get("/api/currency/coindesk/original"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 確認原始數據包含必要欄位
        assertTrue(originalResponse.contains("time"));
        assertTrue(originalResponse.contains("bpi"));
        assertTrue(originalResponse.contains("USD"));

        // 2. 取得轉換後的數據
        String transformedResponse = mockMvc.perform(get("/api/currency/coindesk/transformed"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 驗證轉換後的數據格式
        assertTrue(transformedResponse.contains("updateTime"));
        assertTrue(transformedResponse.contains("currencies"));
        
        // 驗證時間格式是否正確 (yyyy/MM/dd HH:mm:ss)
        assertTrue(transformedResponse.matches(".*\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}.*"));
        
        // 驗證幣別中文名稱是否正確
        assertTrue(transformedResponse.contains("美元"));
        assertTrue(transformedResponse.contains("英鎊"));
        assertTrue(transformedResponse.contains("歐元"));
    }
}