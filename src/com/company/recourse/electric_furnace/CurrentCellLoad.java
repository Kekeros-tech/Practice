package com.company.recourse.electric_furnace;

import com.company.results_of_algos.ResultOfCurrentCellLoad;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class CurrentCellLoad {
    private ArrayList<ResultOfCurrentCellLoad> currentCellLoads;

    CurrentCellLoad() {
        currentCellLoads = new ArrayList<>();
    }

    CurrentCellLoad(ResultOfCurrentCellLoad resultOfCurrentCellLoad) {
        currentCellLoads = new ArrayList<>();
        currentCellLoads.add(resultOfCurrentCellLoad);
    }

    public void addCurrentCellLoads(ResultOfCurrentCellLoad resultOfCurrentCellLoad) {
        currentCellLoads.add(resultOfCurrentCellLoad);
    }

    public int getCurrentCellLoadsAtPointInTime(LocalDateTime tactTime) {
        int count = 0;
        for(ResultOfCurrentCellLoad currentResult: currentCellLoads) {
            if(currentResult.getReleaseTime().isWorkingTime(tactTime)) {
                count += currentResult.getNumberOfDetails();
                //System.out.println("Текущая загруженность = " + count);
            }
        }
        return count;
    }

    public LocalDateTime getCurrentReleaseTimeAtPointInTime(LocalDateTime tactTime) {
        LocalDateTime result = LocalDateTime.MIN;
        for(ResultOfCurrentCellLoad currentResult: currentCellLoads) {
            if(currentResult.getReleaseTime().isWorkingTime(tactTime)) {
                return tactTime;
            }
        }
        return result;
    }

    public LocalDateTime getLastReleaseDate() {
        LocalDateTime result = LocalDateTime.MIN;
        for(ResultOfCurrentCellLoad currentResult: currentCellLoads) {
            if(currentResult.getReleaseTime().getEndTime().isAfter(result)) {
                result = currentResult.getReleaseTime().getEndTime();
            }
        }
        return result;
    }
}
