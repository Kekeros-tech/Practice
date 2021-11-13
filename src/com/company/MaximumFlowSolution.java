package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MaximumFlowSolution {

    public static HashMap<Operation, Recourse> solveMaximumFlowProblem(ArrayList<Operation> frontOfWorkSortedByPriority) {
        HashMap<Operation, Recourse> resultOfWork = new HashMap<>();
        HashSet<Recourse> workResources = new HashSet<>();

        for(Operation operationInFrontOfWork : frontOfWorkSortedByPriority) {
            workResources.addAll(operationInFrontOfWork.getResourcesToBorrow());
        }

        int matrixSize = frontOfWorkSortedByPriority.size() + workResources.size() + 2;
        int[][] adjacencyMatrix = new int[matrixSize][matrixSize];

        for(int i = 0; i < matrixSize; i++) {
            if(1 <= i && i <= frontOfWorkSortedByPriority.size()) {
                adjacencyMatrix[0][i] = 1;
            }
            else {
                adjacencyMatrix[0][i] = 0;
            }
        }
    }

    public static int[][] formAdjacencyMatrix(ArrayList<Operation> frontOfWorkSortedByPriority, HashSet<Recourse> workResources) {
        int matrixSize = frontOfWorkSortedByPriority.size() + workResources.size() + 2;
        int[][] adjacencyMatrix = new int[matrixSize][matrixSize];

        for(int i = 0; i < matrixSize; i++) {
            if(1 <= i && i <= frontOfWorkSortedByPriority.size()) {
                adjacencyMatrix[0][i] = 1;
            }
        }
        for(int i = 0; i < frontOfWorkSortedByPriority.size(); i++) {
            ArrayList<Recourse> resourcesOfCurrentOperation = frontOfWorkSortedByPriority.get(i).getResourcesToBorrow();
            for(int j = 0; j < resourcesOfCurrentOperation.size(); j++){
                adjacencyMatrix[i + 1][frontOfWorkSortedByPriority.indexOf(resourcesOfCurrentOperation.get(j))] = 1;
            }
        }

        for(int i = frontOfWorkSortedByPriority.size() + 1; i <= matrixSize - 2; i++){
            adjacencyMatrix[i][matrixSize - 1] = 1;
        }

        return adjacencyMatrix;
    }
}
