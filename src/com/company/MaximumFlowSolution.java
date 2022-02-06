package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MaximumFlowSolution {

    public static HashMap<Operation, Recourse> solveMaximumFlowProblem(ArrayList<Operation> frontOfWorkSortedByPriority) {

        ArrayList<Recourse> workResources = new ArrayList<>(formResourcesThatCanBeBorrowed(frontOfWorkSortedByPriority));
        if(workResources.isEmpty()) {
            return null;
        }

        int[][] adjacencyMatrix = formAdjacencyMatrix(frontOfWorkSortedByPriority, workResources);

        findMaxFlow(adjacencyMatrix);

        HashMap<Operation, Recourse> resultOfWork = formResultOfAlgorithm(adjacencyMatrix, frontOfWorkSortedByPriority, workResources);

        return resultOfWork;
    }

    public static HashSet<Recourse> formResourcesThatCanBeBorrowed(ArrayList<Operation> frontOfWorkSortedByPriority) {
        HashSet<Recourse> workResources = new HashSet<>();

        for(Operation operationInFrontOfWork : frontOfWorkSortedByPriority) {
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

    public static HashMap<Operation, Recourse> formResultOfAlgorithm(int[][] adjacencyMatrix, ArrayList<Operation> frontOfWorkSortedByPriority,ArrayList<Recourse> workResources ) {
        HashMap<Operation, Recourse> resultOfWork = new HashMap<>();

        for(int i = 1 + frontOfWorkSortedByPriority.size(); i < adjacencyMatrix.length - 1; i++) {
            for(int j = 1; j < 1 + frontOfWorkSortedByPriority.size(); j++) {

                if(adjacencyMatrix[i][j] == 1) {
                    resultOfWork.put(frontOfWorkSortedByPriority.get(j - 1), workResources.get(i - 1 - frontOfWorkSortedByPriority.size()));
                }
            }
        }
        return resultOfWork;
    }

    public static int[][] formAdjacencyMatrix(ArrayList<Operation> frontOfWorkSortedByPriority, ArrayList<Recourse> workResources) {
        int matrixSize = frontOfWorkSortedByPriority.size() + workResources.size() + 2;
        int[][] adjacencyMatrix = new int[matrixSize][matrixSize];

        for(int i = 0; i < matrixSize; i++) {
            if(1 <= i && i <= frontOfWorkSortedByPriority.size()) {
                adjacencyMatrix[0][i] = 1;
            }
        }

        for(int i = 0; i < frontOfWorkSortedByPriority.size(); i++) {
            ArrayList<Recourse> resourcesOfCurrentOperation = frontOfWorkSortedByPriority.get(i).getResourcesToBorrow();
            for(int j = 0, difference = frontOfWorkSortedByPriority.size() + 1; j < resourcesOfCurrentOperation.size(); j++){
                adjacencyMatrix[i + 1][workResources.indexOf(resourcesOfCurrentOperation.get(j)) + difference] = 1;
            }
        }

        for(int i = frontOfWorkSortedByPriority.size() + 1; i <= matrixSize - 2; i++){
            adjacencyMatrix[i][matrixSize - 1] = 1;
        }

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
