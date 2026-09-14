package com.aita.gitanalytics.service;

import com.aita.gitanalytics.dao.AuthorMetricDAO;
import com.aita.gitanalytics.dao.GitCommitDAO;
import com.aita.gitanalytics.dao.UserDAO;
import com.aita.gitanalytics.dto.CommitLogDTO;
import com.aita.gitanalytics.dto.ContributionMetricDTO;
import com.aita.gitanalytics.dto.UserDTO;

import java.sql.SQLException;
import java.util.*;

public class GitParserService {

    private final GitCommitDAO gitCommitDAO = new GitCommitDAO();
    private final AuthorMetricDAO authorMetricDAO = new AuthorMetricDAO();
    private final UserDAO userDAO = new UserDAO();
    private final ScoreCalculatorService scoreCalculatorService = new ScoreCalculatorService();

    /**
     * Synchronizes and calculates ICI metrics for a given group.
     */
    public List<ContributionMetricDTO> syncAndCalculateGroupMetrics(int groupId) throws SQLException {
        // 1. Fetch group members
        List<UserDTO> groupMembers = userDAO.getUsersByGroup(groupId);
        List<CommitLogDTO> commits = gitCommitDAO.getCommitsByGroup(groupId);

        Map<Integer, ContributionMetricDTO> metricMap = new HashMap<>();

        // Initialize metric records for all group members
        for (UserDTO user : groupMembers) {
            ContributionMetricDTO metric = new ContributionMetricDTO();
            metric.setGroupId(groupId);
            metric.setUserId(user.getUserId());
            metric.setFullName(user.getFullName());
            metric.setGithubUsername(user.getGithubUsername());
            metric.setTotalCommits(0);
            metric.setTotalAdditions(0);
            metric.setTotalDeletions(0);
            metric.setActiveDays(0);
            metricMap.put(user.getUserId(), metric);
        }

        // Aggregate commit data per user
        Map<Integer, Set<String>> activeDaysMap = new HashMap<>();

        for (CommitLogDTO commit : commits) {
            int userId = commit.getUserId();
            if (metricMap.containsKey(userId)) {
                ContributionMetricDTO metric = metricMap.get(userId);
                metric.setTotalCommits(metric.getTotalCommits() + 1);
                metric.setTotalAdditions(metric.getTotalAdditions() + commit.getAdditions());
                metric.setTotalDeletions(metric.getTotalDeletions() + commit.getDeletions());

                // Track active date (YYYY-MM-DD)
                if (commit.getCommitDate() != null) {
                    activeDaysMap.computeIfAbsent(userId, k -> new HashSet<>())
                            .add(new java.text.SimpleDateFormat("yyyy-MM-dd").format(commit.getCommitDate()));
                }
            }
        }

        for (Map.Entry<Integer, ContributionMetricDTO> entry : metricMap.entrySet()) {
            int uId = entry.getKey();
            Set<String> days = activeDaysMap.getOrDefault(uId, Collections.emptySet());
            entry.getValue().setActiveDays(Math.max(1, days.size()));
        }

        // 2. Calculate ICI scores using algorithm service
        List<ContributionMetricDTO> rawList = new ArrayList<>(metricMap.values());
        Map<Integer, ContributionMetricDTO> calculatedMap = scoreCalculatorService.calculateGroupICI(rawList);

        // 3. Persist metrics into DB
        for (ContributionMetricDTO metric : calculatedMap.values()) {
            authorMetricDAO.saveOrUpdateMetrics(metric);
        }

        return authorMetricDAO.getMetricsByGroup(groupId);
    }
}
