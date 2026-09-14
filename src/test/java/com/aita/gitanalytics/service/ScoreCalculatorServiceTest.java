package com.aita.gitanalytics.service;

import com.aita.gitanalytics.dto.ContributionMetricDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ScoreCalculatorServiceTest {

    private ScoreCalculatorService scoreCalculatorService;

    @BeforeEach
    public void setUp() {
        scoreCalculatorService = new ScoreCalculatorService();
    }

    @Test
    public void testCalculateGroupICI_DetectsFreeriderCorrectly() {
        List<ContributionMetricDTO> metricsList = new ArrayList<>();

        // Student 1 (Alice - Heavy contributor)
        ContributionMetricDTO alice = new ContributionMetricDTO();
        alice.setUserId(2);
        alice.setTotalCommits(4);
        alice.setTotalAdditions(3450);
        alice.setTotalDeletions(1200);
        alice.setActiveDays(4);
        metricsList.add(alice);

        // Student 2 (Bob - Normal contributor)
        ContributionMetricDTO bob = new ContributionMetricDTO();
        bob.setUserId(3);
        bob.setTotalCommits(3);
        bob.setTotalAdditions(2100);
        bob.setTotalDeletions(800);
        bob.setActiveDays(3);
        metricsList.add(bob);

        // Student 3 (Charlie - Free-rider)
        ContributionMetricDTO charlie = new ContributionMetricDTO();
        charlie.setUserId(4);
        charlie.setTotalCommits(1);
        charlie.setTotalAdditions(50);
        charlie.setTotalDeletions(10);
        charlie.setActiveDays(1);
        metricsList.add(charlie);

        Map<Integer, ContributionMetricDTO> resultMap = scoreCalculatorService.calculateGroupICI(metricsList);

        assertNotNull(resultMap);
        assertEquals(3, resultMap.size());

        ContributionMetricDTO aliceResult = resultMap.get(2);
        ContributionMetricDTO bobResult = resultMap.get(3);
        ContributionMetricDTO charlieResult = resultMap.get(4);

        assertTrue(aliceResult.getIciScore() > 40.0, "Alice should have high ICI score");
        assertFalse(aliceResult.isFreeriderFlagged(), "Alice should NOT be flagged as free-rider");

        assertTrue(bobResult.getIciScore() > 25.0, "Bob should have normal ICI score");
        assertFalse(bobResult.isFreeriderFlagged(), "Bob should NOT be flagged as free-rider");

        assertTrue(charlieResult.getIciScore() < 15.0, "Charlie should have ICI score < 15%");
        assertTrue(charlieResult.isFreeriderFlagged(), "Charlie MUST be flagged as free-rider");
    }
}
