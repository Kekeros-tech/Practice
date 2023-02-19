package com.company;

import java.time.LocalDateTime;
import java.util.*;

public class MaximumFlowSolution {

    public static ArrayList<ResultOfMaximumFlowSolution> solveMaximumFlowProblem(Collection<IOperation> frontOfWorkSortedByPriority) {

        ArrayList<IStructuralUnitOfResource> workResources = new ArrayList<>(formResourcesThatCanBeBorrowed(frontOfWorkSortedByPriority));
        if(workResources.isEmpty()) {
            return null;
        }

        int[][] adjacencyMatrix = formAdjacencyMatrix(frontOfWorkSortedByPriority, workResources);

        findMaxFlow(adjacencyMatrix);

        ArrayList<ResultOfMaximumFlowSolution> resultOfWork =
                formResultOfAlgorithm(adjacencyMatrix, frontOfWorkSortedByPriority, workResources);

        return resultOfWork;
    }

    public static HashSet<IStructuralUnitOfResource> formResourcesThatCanBeBorrowed(Collection<IOperation> frontOfWorkSortedByPriority) {
        HashSet<IStructuralUnitOfResource> workResources = new HashSet<>();

        for(IOperation operationInFrontOfWork : frontOfWorkSortedByPriority) {
            //ArrayList<IResource> buffer = operationInFrontOfWork.getResourcesToBorrow();
            workResources.addAll(operationInFrontOfWork.getResourcesToBorrow());
        }

        return workResources;
    }

    //delete
    public static void printMatrix(int[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix.length; j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println();
        }
    }

    public static ArrayList<ResultOfMaximumFlowSolution> formResultOfAlgorithm(int[][] adjacencyMatrix, Collection<IOperation> frontOfWorkSortedByPriority,ArrayList<IStructuralUnitOfResource> workResources ) {
        HashMap<IOperation, IStructuralUnitOfResource> resultOfWork = new HashMap<>();
        ArrayList<ResultOfMaximumFlowSolution> resultOfMaximumFlowSolutions = new ArrayList<>();
        ArrayList<IOperation> frontOfWork = new ArrayList<>(frontOfWorkSortedByPriority);

        for(int i = 1 + frontOfWork.size(); i < adjacencyMatrix.length - 1; i++) {
            for(int j = 1; j < 1 + frontOfWork.size(); j++) {

                if(adjacencyMatrix[i][j] > 0) {
                    ResultOfMaximumFlowSolution result = new ResultOfMaximumFlowSolution(frontOfWork.get(j - 1),
                            workResources.get(i - 1 - frontOfWork.size()).getCoreResource(), adjacencyMatrix[i][j]);
                    resultOfMaximumFlowSolutions.add(result);
                    //resultOfWork.put(frontOfWork.get(j - 1), workResources.get(i - 1 - frontOfWorkSortedByPriority.size()));
                }
            }
        }
        return resultOfMaximumFlowSolutions;
    }

    public static int[][] formAdjacencyMatrix(Collection<IOperation> frontOfWorkSortedByPriority, ArrayList<IStructuralUnitOfResource> workResources) {
        int matrixSize = frontOfWorkSortedByPriority.size() + workResources.size() + 2;
        int[][] adjacencyMatrix = new int[matrixSize][matrixSize];
        ArrayList<IOperation> frontOfWork = new ArrayList<>(frontOfWorkSortedByPriority);

        for(int i = 0; i < matrixSize; i++) {
            for(int j = 0; j < matrixSize; j ++) {
                adjacencyMatrix[i][j] = 0;
            }
        }

        for(int i = 0; i < matrixSize; i++) {
            if(1 <= i && i <= frontOfWorkSortedByPriority.size()) {
                adjacencyMatrix[0][i] = frontOfWork.get(i-1).getCountOfOperations();
            }
        }
        LocalDateTime currentTactTime = LocalDateTime.MAX;
        int difference = frontOfWork.size() + 1;
        for(int i = 0; i < frontOfWork.size(); i++) {
            ArrayList<IStructuralUnitOfResource> resourcesOfCurrentOperation = frontOfWork.get(i).getResourcesToBorrow();
            int countOfOperation = frontOfWork.get(i).getCountOfOperations();
            if(frontOfWork.get(i).getTactTime().isBefore(currentTactTime)) {
                currentTactTime = frontOfWork.get(i).getTactTime();
            }
            for(int j = 0; j < resourcesOfCurrentOperation.size(); j++) {
                int indexOfCurrentResource = workResources.indexOf(resourcesOfCurrentOperation.get(j)) + difference;
                int currentCount = resourcesOfCurrentOperation.get(j).getResourceAmount(frontOfWork.get(i).getTactTime());
                adjacencyMatrix[i + 1][indexOfCurrentResource] = countOfOperation;
                if(adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] != 0 &&
                        adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] < currentCount) {
                    continue;
                }
                else {
                    adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] = currentCount;
                }
            }
        }

        /*for(int j = 0; j < frontOfWork.size(); j++) {
            ArrayList<IStructuralUnitOfResource> resourcesOfCurrentOperation = frontOfWork.get(j).getResourcesToBorrow();
            for(int k = 0; k < resourcesOfCurrentOperation.size(); k ++) {
                int indexOfCurrentResource = workResources.indexOf(resourcesOfCurrentOperation.get(k)) + difference;
                int currentCount = resourcesOfCurrentOperation.get(k).getResourceAmount(frontOfWork.get(j).getTactTime());
                *//*if(adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] == 0) {
                    adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] = currentCount;
                }
                else {
                    if(currentCount < adjacencyMatrix[indexOfCurrentResource][matrixSize - 1]) {
                        adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] = currentCount;
                    }
                }*//*
                if(adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] != 0 &&
                        adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] < currentCount) {
                    continue;
                }
                else {
                    adjacencyMatrix[indexOfCurrentResource][matrixSize - 1] = currentCount;
                }
            }
        }*/

        return adjacencyMatrix;
    }

    public static int findMaxFlow(int[][] adjacencyMatrix) {
        for (int flow = 0;;) {
            int df = findPath(adjacencyMatrix, new boolean[adjacencyMatrix.length], 0, adjacencyMatrix.length - 1, Integer.MAX_VALUE);
            if (df == 0)
                return flow;
            flow += df;
        }
    }

    static int findPath(int[][] adjacencyMatrix, boolean[] visited, int u, int t, int f) {
        if (u == t)
            return f;
        visited[u] = true;
        for (int v = 0; v < visited.length; v++)
            if (!visited[v] && adjacencyMatrix[u][v] > 0) {
                int df = findPath(adjacencyMatrix, visited, v, t, Math.min(f, adjacencyMatrix[u][v]));
                if (df > 0) {
                    adjacencyMatrix[u][v] -= df;
                    adjacencyMatrix[v][u] += df;
                    return df;
                }
            }
        return 0;
    }
}
