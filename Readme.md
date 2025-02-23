# Coindesk API Project


## 功能
1. 幣別資料表 CRUD API
2. Coindesk API 調用
3. 資料轉換 API

- Java 8
- Spring Boot 2.7.18
- H2 Database
- Spring Data JPA
- Maven

## API 文檔
- GET /api/currency/coindesk/original - 獲取原始 Coindesk 數據
- GET /api/currency/coindesk/transformed - 獲取轉換後的數據
- GET /api/currency - 獲取所有幣別
- POST /api/currency - 創建新幣別
- PUT /api/currency/{id} - 更新幣別
- DELETE /api/currency/{id} - 刪除幣別

## 測試
詳細測試報告請查看 [TestReport.md](TestReport.md)