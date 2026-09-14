package com.aita.gitanalytics.service;

import com.aita.gitanalytics.dto.ContributionMetricDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreCalculatorService {

    // Weight parameters specified in PRJ301 Group 5 RBL Document (Section I.2)
    public static final double WEIGHT_COMMIT = 0.25;  // w1: Commit Ratio Score
    public static final double WEIGHT_CHURN = 0.45;   // w2: LOC Churn Score (Additions + 0.5 * Deletions)
    public static final double WEIGHT_TIME = 0.15;    // w3: Time Spread Active Days Score
    public static final double WEIGHT_FILE = 0.15;    // w4: File Ownership Score
    
    // Free-rider threshold: ICI < 15%
    public static final double FREERIDER_THRESHOLD_PERCENT = 15.0;

    /**
     * Calculates the Individual Contribution Index (ICI) for all group members.
     * Formula: ICI_i = w1 * C_i + w2 * L_i + w3 * T_i + w4 * F_i (expressed as percentage 0..100)
     * Automatically sets isFreeriderFlagged = true if ICI_i < 15%.
     * 
     * @param metricsList List of raw contribution metrics per author
     * @return Map of userId -> updated ContributionMetricDTO
     */
    public Map<Integer, ContributionMetricDTO> calculateGroupICI(List<ContributionMetricDTO> metricsList) {
        Map<Integer, ContributionMetricDTO> resultMap = new HashMap<>();

        if (metricsList == null || metricsList.isEmpty()) {
            return resultMap;
        }

        // 1. Calculate Group Aggregates
        int totalGroupCommits = metricsList.stream().mapToInt(ContributionMetricDTO::getTotalCommits).sum();
        
        // Calculate weighted LOC churn: ΔLOC = Additions + 0.5 * Deletions
        double totalGroupWeightedChurn = metricsList.stream()
                .mapToDouble(m -> m.getTotalAdditions() + 0.5 * m.getTotalDeletions())
                .sum();

        int maxActiveDaysInGroup = metricsList.stream()
                .mapToInt(ContributionMetricDTO::getActiveDays)
                .max().orElse(1);

        for (ContributionMetricDTO metric : metricsList) {
            // C_i: Commit Ratio
            double commitRatio = (totalGroupCommits > 0) ? (double) metric.getTotalCommits() / totalGroupCommits : 0.0;

            // L_i: LOC Churn Ratio
            double userWeightedChurn = metric.getTotalAdditions() + 0.5 * metric.getTotalDeletions();
            double churnRatio = (totalGroupWeightedChurn > 0) ? userWeightedChurn / totalGroupWeightedChurn : 0.0;

            // T_i: Time Spread Ratio (relative to max active days in project)
            double timeSpreadRatio = (maxActiveDaysInGroup > 0) ? (double) metric.getActiveDays() / maxActiveDaysInGroup : 0.0;

            // F_i: File Ownership / Module Responsibility Ratio (Estimated by commit ratio & active spread balance)
            double fileOwnershipRatio = (commitRatio * 0.6 + churnRatio * 0.4);

            // ICI_i Score calculation (Percentage 0.0 - 100.0)
            double rawIci = (WEIGHT_COMMIT * commitRatio + 
                             WEIGHT_CHURN * churnRatio + 
                             WEIGHT_TIME * timeSpreadRatio + 
                             WEIGHT_FILE * fileOwnershipRatio) * 100.0;

            // Round to 1 decimal place
            double iciScore = Math.round(rawIci * 10.0) / 10.0;

            metric.setIciScore(iciScore);
            metric.setFreeriderFlagged(iciScore < FREERIDER_THRESHOLD_PERCENT);

            resultMap.put(metric.getUserId(), metric);
        }

        return resultMap;
    }
}
