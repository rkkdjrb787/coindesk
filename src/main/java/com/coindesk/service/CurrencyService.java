package com.coindesk.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.coindesk.dto.CoindeskApiDto;
import com.coindesk.dto.CoindeskTransformedDto;
import com.coindesk.dto.CurrencyDto;
import com.coindesk.entity.Currency;
import com.coindesk.repository.CurrencyRepository;

@Service
@Transactional
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RestTemplate restTemplate;

    private static final String COINDESK_API_URL = "https://kengp3.github.io/blog/coindesk.json";

    public CurrencyDto createCurrency(CurrencyDto currencyDto) {
        Currency currency = new Currency();
        BeanUtils.copyProperties(currencyDto, currency);
        currency = currencyRepository.save(currency);
        BeanUtils.copyProperties(currency, currencyDto);
        return currencyDto;
    }

    public CurrencyDto getCurrency(String id) {
        Currency currency = currencyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Currency not found with id: " + id));
        return convertToDto(currency);
    }

    public List<CurrencyDto> getAllCurrencies() {
        return currencyRepository.findAll().stream()
                .map(currency -> {
                    CurrencyDto dto = new CurrencyDto();
                    BeanUtils.copyProperties(currency, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public CurrencyDto updateCurrency(String id, CurrencyDto currencyDto) {
        Currency currency = currencyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Update, Currency not found with id: " + id));
        currency.setChineseName(currencyDto.getChineseName());
        currency = currencyRepository.save(currency);
        BeanUtils.copyProperties(currency, currencyDto);
        return currencyDto;
    }

    public void deleteCurrency(String id) {
        Currency currency = currencyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Delete, Currency not found with id: " + id));
        currencyRepository.delete(currency);
    }

    // 取得API資料
    public CoindeskApiDto getCoindeskApi() {
        ResponseEntity<CoindeskApiDto> response = restTemplate.getForEntity(COINDESK_API_URL, CoindeskApiDto.class);
        return response.getBody();
    }

    public CoindeskTransformedDto getTransformedCoindesk() {
        CoindeskApiDto coindeskData = getCoindeskApi();
        CoindeskTransformedDto result = new CoindeskTransformedDto();
        
        String originalTime = coindeskData.getTime().getUpdated(); // "Sep 2, 2024 07:07:20 UTC"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm:ss z", Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(originalTime, formatter);
        
        result.setUpdateTime(dateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        
        // 轉幣別
        List<CoindeskTransformedDto.CurrencyInfo> currencyInfoList = new ArrayList<>();
        coindeskData.getBpi().forEach((key, value) -> {
            CoindeskTransformedDto.CurrencyInfo info = new CoindeskTransformedDto.CurrencyInfo();
            info.setCode(value.getCode());
            info.setRate(value.getRate());
            
            // 取中文
            currencyRepository.findById(value.getCode())
                    .ifPresent(currency -> info.setChineseName(currency.getChineseName()));
            
            currencyInfoList.add(info);
        });
        
        result.setCurrencies(currencyInfoList);
        return result;
    }

	private CurrencyDto convertToDto(Currency currency) {
		CurrencyDto dto = new CurrencyDto();
		BeanUtils.copyProperties(currency, dto);
		return dto;
	}
}