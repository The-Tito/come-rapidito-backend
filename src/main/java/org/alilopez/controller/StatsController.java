package org.alilopez.controller;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.alilopez.DTO.Stat.StatsDTO;
import org.alilopez.model.Stat;
import org.alilopez.service.StatsService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    public void createStatsSum(Context ctx) {
        try {
            List<Stat> stats = statsService.createStatsSum();
            List<String> tempLabels = new ArrayList<>();
            List<Integer> tempData = new ArrayList<>();
            for (Stat stat : stats) {
                tempLabels.add(stat.getLabel());
                tempData.add(stat.getValue());
            }
            StatsDTO statsDTO = new StatsDTO(tempLabels, tempData);
            ctx.status(HttpStatus.OK).json(statsDTO);
        } catch (SQLException e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
