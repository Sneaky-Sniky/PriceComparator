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

    @Scheduled(fixedRateString = "${price.alert.check.rate}")
    public void checkPriceAlerts() {
        priceAlertService.checkPriceAlerts();
    }
} 