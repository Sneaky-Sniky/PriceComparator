package com.nikoletta.pricecomparator.controllers;

import com.nikoletta.pricecomparator.dtos.PriceAlertCreateDTO;
import com.nikoletta.pricecomparator.models.PriceAlert;
import com.nikoletta.pricecomparator.service.PriceAlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class PriceAlertController {
    private final PriceAlertService priceAlertService;

    public PriceAlertController(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @PostMapping
    public ResponseEntity<PriceAlert> createPriceAlert(
            @RequestBody PriceAlertCreateDTO priceAlertCreateDTO) {
        try {
            PriceAlert alert = priceAlertService.createPriceAlert(
                    priceAlertCreateDTO.getProductId(), priceAlertCreateDTO.getTargetPrice());
            return ResponseEntity.ok(alert);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PriceAlert>> getActiveAlerts() {
        return ResponseEntity.ok(priceAlertService.getActiveAlerts());
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<Void> deleteAlert(@PathVariable Long alertId) {
        priceAlertService.deleteAlert(alertId);
        return ResponseEntity.ok().build();
    }
} 