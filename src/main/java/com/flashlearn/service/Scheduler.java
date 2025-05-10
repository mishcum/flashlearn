package com.flashlearn.service;

import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    public Double next(double easeRate, int quality) {
        double newEaseRate = Math.max(1.3, easeRate + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)));
        return newEaseRate;
    }
}
