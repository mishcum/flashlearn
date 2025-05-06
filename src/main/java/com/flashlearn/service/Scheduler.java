package com.flashlearn.service;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    public Pair<Double, Integer> next(double easeRate, int intervalDays, int quality) {
        double newEaseRate = Math.max(1.3, easeRate + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)));
        int interval = switch (intervalDays) {
            case 0 -> 1;
            case 1 -> 6;
            default -> (int) Math.round(intervalDays * newEaseRate);
        };

        return Pair.of(newEaseRate, interval);
    }
}
