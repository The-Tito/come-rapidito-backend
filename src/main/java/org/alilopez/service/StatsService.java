package org.alilopez.service;

import org.alilopez.model.Stat;
import org.alilopez.repository.StatsRepository;

import java.sql.SQLException;
import java.util.List;

public class StatsService {
    private final StatsRepository statsRepository;
    public StatsService(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    public List<Stat> createStatsSum() throws SQLException {
        return statsRepository.createStatsSum();
    }
}
