package com.aita.gitanalytics.service;

import com.aita.gitanalytics.dto.ContributionMetricDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreCalculatorService {

    /**
     * Tính toán chỉ số đóng góp cá nhân (ICI - Individual Contribution Index) 
     * và phát hiện Free-rider cho từng thành viên trong nhóm.
     *
     * @param metricsList Danh sách chỉ số đóng góp của các thành viên trong nhóm
     * @return Map với Key là userId và Value là ContributionMetricDTO đã được cập nhật kết quả
     */
    public Map<Integer, ContributionMetricDTO> calculateGroupICI(List<ContributionMetricDTO> metricsList) {
        Map<Integer, ContributionMetricDTO> resultMap = new HashMap<>();

        if (metricsList == null || metricsList.isEmpty()) {
            return resultMap;
        }

        // 1. Tính tổng Churn (Additions + Deletions) của toàn bộ nhóm
        double totalGroupChurn = 0.0;
        for (ContributionMetricDTO metric : metricsList) {
            totalGroupChurn += (metric.getTotalAdditions() + metric.getTotalDeletions());
        }

        // 2. Tính tỉ lệ % ICI và gắn cờ Free-rider cho từng thành viên
        for (ContributionMetricDTO metric : metricsList) {
            double individualChurn = metric.getTotalAdditions() + metric.getTotalDeletions();
            double iciScore = 0.0;

            if (totalGroupChurn > 0) {
                iciScore = (individualChurn / totalGroupChurn) * 100.0;
            }

            // Cập nhật giá trị điểm ICI
            metric.setIciScore(iciScore);

            // Nếu điểm ICI < 15% thì đánh dấu là Free-rider
            metric.setFreeriderFlagged(iciScore < 15.0);

            // BẮT BUỘC: Đưa vào Map với Key là userId để resultMap.get(userId) không bị null
            resultMap.put(metric.getUserId(), metric);
        }

        return resultMap;
    }
}
