# 測試報告

## 測試環境
- JDK: 8
- Spring Boot: 2.7.18
- 資料庫: H2
- 測試框架: JUnit 5

## 測試項目與結果

### 1. 幣別資料 CRUD API 測試 (CurrencyControllerTest)
- [✓] 新增幣別 (JPY/日圓)
- [✓] 查詢幣別
- [✓] 更新幣別 (日圓→日元)
- [✓] 刪除幣別
- [✓] 驗證刪除後無法查詢

### 2. Coindesk API 測試 (CoindeskApiTest)
- [✓] 成功調用原始 API
- [✓] 驗證回傳資料結構
  - 包含時間資訊
  - 包含幣別資訊 (USD, GBP, EUR)
  - 包含匯率資訊

### 3. 資料轉換測試 (CurrencyTransformTest)
- [✓] 時間格式轉換 (UTC → yyyy/MM/dd HH:mm:ss)
- [✓] 幣別中文名稱對應
  - USD → 美元
  - GBP → 英鎊
  - EUR → 歐元
- [✓] 匯率資訊保留