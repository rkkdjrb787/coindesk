package com.coindesk.controller;

import com.coindesk.dto.CurrencyDto;
import com.coindesk.dto.CoindeskApiDto;
import com.coindesk.dto.CoindeskTransformedDto;
import com.coindesk.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<List<CurrencyDto>> getAllCurrencies() {
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyDto> getCurrency(@PathVariable String id) {
        try {
            return ResponseEntity.ok(currencyService.getCurrency(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // create
    @PostMapping
    public ResponseEntity<CurrencyDto> createCurrency(@RequestBody CurrencyDto currencyDto) {
        return ResponseEntity.ok(currencyService.createCurrency(currencyDto));
    }

    // update
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyDto> updateCurrency(
            @PathVariable String id,
            @RequestBody CurrencyDto currencyDto) {
        return ResponseEntity.ok(currencyService.updateCurrency(id, currencyDto));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCurrency(@PathVariable String id) {
        try {
            currencyService.deleteCurrency(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // get original Api data
    @GetMapping("/coindesk/original")
    public ResponseEntity<CoindeskApiDto> getCoindeskApi() {
        return ResponseEntity.ok(currencyService.getCoindeskApi());
    }

    // trans Api data
    @GetMapping("/coindesk/transformed")
    public ResponseEntity<CoindeskTransformedDto> getTransformedCoindesk() {
        return ResponseEntity.ok(currencyService.getTransformedCoindesk());
    }
}