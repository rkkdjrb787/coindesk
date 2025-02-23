package com.coindesk.dto;

import lombok.Data;
import java.util.List;

@Data
public class CoindeskTransformedDto {
    private String updateTime;
    private List<CurrencyInfo> currencies;

    @Data
    public static class CurrencyInfo {
        private String code;
        private String chineseName;
        private String rate;
    }
}