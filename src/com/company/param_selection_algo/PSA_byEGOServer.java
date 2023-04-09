package com.company.param_selection_algo;

import com.company.control_param.ControlParameters;
import com.company.arrangement_algo.Algo_WithCPAndFuture;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;

public class PSA_byEGOServer implements IOptimizationAlgo {
    private String scriptLocation;
    private int numberIterations;

    private Duration bestSolution;
    private ControlParameters bestControlParameters;
    private Duration bestDurationParameter;

    private String pathStartFile;
    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    // todo - добавить интерфейс на условия остановки алгаритма
    public PSA_byEGOServer(int numberIterations, String pathStartFile) {
        this.scriptLocation = new File("").getAbsolutePath() + "\\EGO_SERVER\\main.py";
        this.numberIterations = numberIterations;
        this.pathStartFile = new File("").getAbsolutePath() + "\\EGO_SERVER\\" + pathStartFile;
        bestSolution = Duration.ZERO;
    }

    @Override
    public void calculateBestSolution(Algo_WithCPAndFuture algoOfOperationPlacement) {

        try {
            Process process = new ProcessBuilder()
                    .command("python", scriptLocation)
                    .start();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        LocalDateTime timeEarliestOperation = PSA_byEGO.findEarliestStartTime(algoOfOperationPlacement.seriesForWork);
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

                    ControlParameters currentControlParameters = new ControlParameters(strategyParam, 1, 0);
                    Duration currentDurationParameter = Duration.ofSeconds(durationParam);

                    algoOfOperationPlacement.controlParameters = currentControlParameters;
                    algoOfOperationPlacement.selectedDuration = currentDurationParameter;
                    algoOfOperationPlacement.takeSeriesToWork();

                    Duration resDuration = Duration.between(timeEarliestOperation,
                            PSA_byEGO.findTimeOfLatestOperation(algoOfOperationPlacement.seriesForWork));

                    out.write(resDuration.getSeconds() + "\n");
                    out.flush();

                    if(bestSolution.isZero() || bestSolution.compareTo(resDuration) == 1) {
                        bestSolution = resDuration;
                        bestControlParameters = currentControlParameters;
                        bestDurationParameter = currentDurationParameter;
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

    public Duration getBestDurationParameter() { return bestDurationParameter; }

    public ControlParameters getBestControlParameters() { return bestControlParameters; }

    public void resetResult() {
        return;
    }
}
