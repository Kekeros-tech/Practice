package com.company;

import java.util.*;

public class MaximumFlowSolution {

    public static HashMap<IOperation, IResource> solveMaximumFlowProblem(Collection<IOperation> frontOfWorkSortedByPriority) {

        ArrayList<IResource> workResources = new ArrayList<>(formResourcesThatCanBeBorrowed(frontOfWorkSortedByPriority));
        if(workResources.isEmpty()) {
            return null;
        }

        int[][] adjacencyMatrix = formAdjacencyMatrix(frontOfWorkSortedByPriority, workResources);

        findMaxFlow(adjacencyMatrix);

        HashMap<IOperation, IResource> resultOfWork = formResultOfAlgorithm(adjacencyMatrix, frontOfWorkSortedByPriority, workResources);

        return resultOfWork;
    }

    public static HashSet<IResource> formResourcesThatCanBeBorrowed(Collection<IOperation> frontOfWorkSortedByPriority) {
        HashSet<IResource> workResources = new HashSet<>();

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

    public static HashMap<IOperation, IResource> formResultOfAlgorithm(int[][] adjacencyMatrix, Collection<IOperation> frontOfWorkSortedByPriority,ArrayList<IResource> workResources ) {
        HashMap<IOperation, IResource> resultOfWork = new HashMap<>();
        ArrayList<IOperation> frontOfWork = new ArrayList<>(frontOfWorkSortedByPriority);

        for(int i = 1 + frontOfWork.size(); i < adjacencyMatrix.length - 1; i++) {
            for(int j = 1; j < 1 + frontOfWork.size(); j++) {

                if(adjacencyMatrix[i][j] == 1) {
                    resultOfWork.put(frontOfWork.get(j - 1), workResources.get(i - 1 - frontOfWorkSortedByPriority.size()));
                }
            }
        }
        return resultOfWork;
    }

    public static int[][] formAdjacencyMatrix(Collection<IOperation> frontOfWorkSortedByPriority, ArrayList<IResource> workResources) {
        int matrixSize = frontOfWorkSortedByPriority.size() + workResources.size() + 2;
        int[][] adjacencyMatrix = new int[matrixSize][matrixSize];
        ArrayList<IOperation> frontOfWork = new ArrayList<>(frontOfWorkSortedByPriority);

        for(int i = 0; i < matrixSize; i++) {
            if(1 <= i && i <= frontOfWorkSortedByPriority.size()) {
                adjacencyMatrix[0][i] = 1;
            }
        }

        for(int i = 0; i < frontOfWork.size(); i++) {
            ArrayList<IResource> resourcesOfCurrentOperation = frontOfWork.get(i).getResourcesToBorrow();
            for(int j = 0, difference = frontOfWork.size() + 1; j < resourcesOfCurrentOperation.size(); j++){
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
