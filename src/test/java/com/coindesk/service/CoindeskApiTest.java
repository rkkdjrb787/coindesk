package com.coindesk.service;

import com.coindesk.dto.CoindeskApiDto;
import com.coindesk.dto.CoindeskTransformedDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CoindeskApiTest {

    @Autowired
    private CurrencyService currencyService;

    @Test
    public void testGetCoindeskApi() {
        // 測試原始API調用
        CoindeskApiDto apiResult = currencyService.getCoindeskApi();
        assertNotNull(apiResult);
        assertNotNull(apiResult.getTime());
        assertNotNull(apiResult.getBpi());
        assertTrue(apiResult.getBpi().containsKey("USD"));
    }

    @Test
    public void testTransformedData() {
        // 測試轉換後的數據
        CoindeskTransformedDto transformedResult = currencyService.getTransformedCoindesk();
        assertNotNull(transformedResult);
        assertNotNull(transformedResult.getUpdateTime());
        assertNotNull(transformedResult.getCurrencies());
        assertFalse(transformedResult.getCurrencies().isEmpty());

        // 驗證時間格式
        assertTrue(transformedResult.getUpdateTime().matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}"));

        // 驗證幣別資訊
        transformedResult.getCurrencies().forEach(currency -> {
            assertNotNull(currency.getCode());
            assertNotNull(currency.getRate());
            assertNotNull(currency.getChineseName());
        });
    }
}