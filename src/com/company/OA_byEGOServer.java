package com.company;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class OA_byEGOServer implements IOptimizationAlgo{
    private String scriptLocation;
    private int numberIterations;
    private Duration bestSolution;
    private String pathStartFile;
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    // todo - добавить интерфейс на условия остановки алгаритма
    OA_byEGOServer(String pathToPythonEGOScript, int numberIterations, String pathStartFile) {
        this.scriptLocation = pathToPythonEGOScript;
        this.numberIterations = numberIterations;
        this.pathStartFile = pathStartFile;
        bestSolution = Duration.ZERO;
    }

    @Override
    public void calculateBestSolution(OperationsArrangementAlgorithmWithCPAndFutureFrontNew algoOfOperationPlacement) {

        try {
            Process process = new ProcessBuilder()
                    .command("python", scriptLocation)
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        LocalDateTime timeEarliestOperation = OA_byEGO.findEarliestStartTime(algoOfOperationPlacement.seriesForWork);
        try {
            try {
                clientSocket = new Socket("localhost", 500);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                out.write(pathStartFile + "\n");
                out.flush();
                out.write(numberIterations + "\n");
                out.flush();

                for(int i = 0; i < numberIterations; i ++) {
                    int durationParam = Integer.parseInt(in.readLine());
                    int strategyParam = Integer.parseInt(in.readLine());

                    algoOfOperationPlacement.controlParameters = new ControlParameters(strategyParam, 1, 0);
                    algoOfOperationPlacement.selectedDuration = Duration.ofSeconds(durationParam);
                    algoOfOperationPlacement.takeSeriesToWork();

                    Duration resDuration = Duration.between(timeEarliestOperation,
                            OA_byEGO.findTimeOfLatestOperation(algoOfOperationPlacement.seriesForWork));

                    out.write(resDuration.getSeconds() + "\n");
                    out.flush();

                    if(bestSolution.isZero() || bestSolution.compareTo(resDuration) == 1) {
                        bestSolution = resDuration;
                    }
                }
            } finally {
                System.out.println("Клиент был закрыт...");
                clientSocket.close();
                in.close();
                out.close();
                return;
            }
        } catch (IOException e) {
            System.err.println(e);
            return;
        }
    }

    @Override
    public Duration getBestSolution() {
        return bestSolution;
    }

    public void resetResult() {
        return;
    }
}
