package com.company.param_selection_algo;

import com.company.*;
import com.company.arrangement_algo.Algo_WithCPAndFuture;
import com.company.control_param.ControlParameters;
import com.company.operation.IOperation;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class PSA_byEGO implements IOptimizationAlgo {
    //private String[] scriptLocation;
    private String scriptLocation;
    private int numberIterations;
    private String resultPath;

    private String fileContent;
    private Duration bestSolution;

    // todo - добавить интерфейс на условия остановки алгаритма
    public PSA_byEGO(String pathToPythonEGOScript, int numberIterations, String resultPath) {
        //scriptLocation = new String[]{"python", pathToPythonEGOScript};
        scriptLocation = pathToPythonEGOScript;
        this.numberIterations = numberIterations;
        this.resultPath = resultPath;
        bestSolution = Duration.ZERO;

        fileContent = new String();
        try(FileReader reader = new FileReader(resultPath))
        {
            int c;
            while((c=reader.read())!=-1){
                fileContent += (char)c;
            }
        }
        catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void calculateBestSolution(Algo_WithCPAndFuture algoOfOperationPlacement) {
        LocalDateTime timeEarliestOperation = findEarliestStartTime(algoOfOperationPlacement.seriesForWork);
        for(int i = 0; i < numberIterations; i++) {
            try {
                Process process = new ProcessBuilder()
                        .command("python", scriptLocation, resultPath)
                        .start();
                System.out.println(process.waitFor());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

            FileReader file = null;
            try {
                file = new FileReader(resultPath);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }

            Scanner scanner = new Scanner(file);
            String lastString = "";
            ArrayList<Integer> controlParameterValues = new ArrayList<>();
            while (scanner.hasNextLine()) {
                lastString = scanner.nextLine();
            }
            for (String current : lastString.split(" ")) {
                controlParameterValues.add(Integer.parseInt(current));
            }

            algoOfOperationPlacement.controlParameters = new ControlParameters(controlParameterValues.get(1), 1, 0);
            algoOfOperationPlacement.selectedDuration = Duration.ofSeconds(controlParameterValues.get(0));
            algoOfOperationPlacement.takeSeriesToWork();

            LocalDateTime latestTime = findTimeOfLatestOperation(algoOfOperationPlacement.seriesForWork);
            Duration resultDuration = Duration.between(timeEarliestOperation, latestTime);

            if(bestSolution.isZero() || bestSolution.compareTo(resultDuration) == 1) {
                bestSolution = resultDuration;
            }

            try(FileWriter writer2 = new FileWriter(resultPath, true))
            {
                writer2.append('\n');
                writer2.write(Long.toString(resultDuration.getSeconds()));

                writer2.flush();
            }
            catch(IOException ex){

                System.out.println(ex.getMessage());
            }
        }
    }

    static public LocalDateTime findEarliestStartTime(Collection<Series> seriesForWork) {
        LocalDateTime minTime = LocalDateTime.MAX;
        for(Series currentSeries: seriesForWork) {
            LocalDateTime currentSeriesTime = currentSeries.getArrivalTime();
            if(currentSeriesTime.isBefore(minTime)) {
                minTime = currentSeriesTime;
            }
        }
        return minTime;
    }

    @Override
    public Duration getBestSolution() {
        return bestSolution;
    }

    public void resetResult() {
        bestSolution = Duration.ZERO;
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(resultPath);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        writer.print(fileContent);
        writer.close();
    }

    static public LocalDateTime findTimeOfLatestOperation(Collection<Series> seriesForWork) {
        LocalDateTime maxTime = LocalDateTime.MIN;
        for(Series currentSeries: seriesForWork) {
            for (IOperation currentOperationOfCurrentSeries: currentSeries.getOperationsToCreate()){
                if(findTimeOfLatestOfWorkingInterval(currentOperationOfCurrentSeries).isAfter(maxTime)){
                    maxTime = findTimeOfLatestOfWorkingInterval(currentOperationOfCurrentSeries);
                }
            }
        }
        return maxTime;
    }


    //перенести куда-нибудь
    static public LocalDateTime findTimeOfLatestOfWorkingInterval( IOperation operation) {
        LocalDateTime maxTime = LocalDateTime.MIN;
        for(WorkingHours wh: operation.getCWorkingInterval()){
            if(wh.getEndTime().isAfter(maxTime)){
                maxTime = wh.getEndTime();
            }
        }
        return maxTime;
    }
}
