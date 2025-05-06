package com.flashlearn.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class SchedulerTest {

    private final Scheduler scheduler = new Scheduler();
    
    @Test
    void first_answer_returns_min_interval() {
        var result = scheduler.next(2.5, 0, 5);
        assertEquals(1, result.getRight());
        assertTrue(result.getLeft() >= 2.5);
    }
}
