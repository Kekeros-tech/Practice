package com.company;

import com.company.operation.IOperation;

import java.io.*;
import java.net.Socket;
import java.util.Collection;

public class Visualization {
    private String scriptLocation;
    private String operationDistributionName;

    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;

    public Visualization(String operationDistributionName) {
        this.scriptLocation = new File("").getAbsolutePath() + "\\GANTT_VISUALIZATION\\main.py";
        this.operationDistributionName = operationDistributionName;
    }

    public void visualizeSolution(Collection<Series> seriesVisualization) {
//        Process process;
//        try {
//            process = new ProcessBuilder()
//                    .command("python", scriptLocation)
//                    .start();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }

        try {
            try {
                clientSocket = new Socket("localhost", 5050);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                out.write(operationDistributionName + "\n");
                out.flush();
                for(Series series : seriesVisualization) {
                    for(IOperation oper : series.getOperationsToCreate()) {
                        out.write(oper.formResultOfOperation() + "\n");
                        out.flush();
                    }
                }
                out.write("End");
                out.flush();
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
}
