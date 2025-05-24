package com.nikoletta.pricecomparator.tasks;

import com.nikoletta.pricecomparator.service.PriceAlertService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceAlertScheduler {
    private final PriceAlertService priceAlertService;

    public PriceAlertScheduler(PriceAlertService priceAlertService) {
        this.priceAlertService = priceAlertService;
    }

    @Scheduled(fixedRate = 300000) // Check every 5 minutes
    public void checkPriceAlerts() {
        priceAlertService.checkPriceAlerts();
    }
} 