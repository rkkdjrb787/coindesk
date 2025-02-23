package com.coindesk.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.coindesk.dto.CoindeskApiDto;
import com.coindesk.dto.CoindeskTransformedDto;

@SpringBootTest
public class CurrencyTransformTest {

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testTransformCoindesk() {
        // 準備簡單的模擬數據
        CoindeskApiDto mockResponse = new CoindeskApiDto();
        mockResponse.setTime(new CoindeskApiDto.Time());
        mockResponse.getTime().setUpdated("Sep 2, 2024 07:07:20 UTC");
        mockResponse.setBpi(new HashMap<>());

        // 模擬 API 調用
        when(restTemplate.getForEntity(anyString(), eq(CoindeskApiDto.class)))
            .thenReturn(ResponseEntity.ok(mockResponse));

        // 執行並驗證基本結果
        CoindeskTransformedDto result = currencyService.getTransformedCoindesk();
        assertNotNull(result);
        assertNotNull(result.getUpdateTime());
    }
}