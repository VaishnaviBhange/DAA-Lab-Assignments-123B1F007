/**
NAME:Vaishnavi Dada Bhange
PRN:123B1F007
DATE:31-10-25
 ASSIGNMENT 8:
 PROBLEM STATEMENT:
 Optimizing Delivery Routes for a Logistics Company
 A logistics company, SwiftShip, delivers packages across multiple cities.
 To minimize travel cost and time, the company must find the shortest route
 visiting every city exactly once and returning to the start.
 Requirements:
 ● Varying distances between cities.
 ● Fuel cost depends on road conditions.
 ● Must complete within delivery time limit.
 A brute-force solution is inefficient for large N.
 Hence, use Least Cost (LC) Branch and Bound approach
 to determine the minimum cost path efficiently.
 **/

import java.util.*;

public class Assignment8 {

    static class SwiftShipTSP {

        private int cities;
        private int[][] distance;
        private int[][] fuelCost;
        private int maxTime;

        private int bestCost = Integer.MAX_VALUE;
        private List<Integer> bestPath;

        public SwiftShipTSP(int cities, int[][] distance, int[][] fuelCost, int maxTime) {
            this.cities = cities;
            this.distance = distance;
            this.fuelCost = fuelCost;
            this.maxTime = maxTime;
            bestPath = new ArrayList<>();
        }

        // Lower bound calculation for pruning
        private int getLowerBound(boolean[] visited, int current) {
            int bound = 0;

            for (int city = 0; city < cities; city++) {
                if (!visited[city]) {
                    int minEdge = Integer.MAX_VALUE;
                    for (int next = 0; next < cities; next++) {
                        if (city != next && !visited[next]) {
                            minEdge = Math.min(minEdge, fuelCost[city][next]);
                        }
                    }
                    bound += (minEdge == Integer.MAX_VALUE) ? 0 : minEdge;
                }
            }

            int minReturn = Integer.MAX_VALUE;
            for (int city = 0; city < cities; city++) {
                if (!visited[city]) minReturn = Math.min(minReturn, fuelCost[city][0]);
            }
            if (minReturn != Integer.MAX_VALUE) bound += minReturn;

            return bound;
        }

        // Recursive Least Cost Branch and Bound
        private void branchAndBound(List<Integer> path, boolean[] visited, int currentCity, int currentCost) {
            if (path.size() == cities) {
                int totalCost = currentCost + fuelCost[currentCity][0];
                int totalTime = calculateTime(path) + distance[currentCity][0];

                if (totalCost < bestCost && totalTime <= maxTime) {
                    bestCost = totalCost;
                    bestPath = new ArrayList<>(path);
                    bestPath.add(0);
                }
                return;
            }

            for (int nextCity = 0; nextCity < cities; nextCity++) {
                if (!visited[nextCity]) {
                    int nextCost = currentCost + fuelCost[currentCity][nextCity];
                    int lowerBound = nextCost + getLowerBound(visited, nextCity);

                    if (lowerBound < bestCost) {
                        visited[nextCity] = true;
                        path.add(nextCity);

                        branchAndBound(path, visited, nextCity, nextCost);

                        visited[nextCity] = false;
                        path.remove(path.size() - 1);
                    }
                }
            }
        }

        // Calculate total distance/time for a route
        private int calculateTime(List<Integer> path) {
            int total = 0;
            for (int i = 0; i < path.size() - 1; i++) {
                total += distance[path.get(i)][path.get(i + 1)];
            }
            if (!path.isEmpty()) total += distance[path.get(path.size() - 1)][0];
            return total;
        }

        // Find optimal route
        public void findOptimalRoute() {
            long start = System.currentTimeMillis();

            boolean[] visited = new boolean[cities];
            List<Integer> path = new ArrayList<>();
            visited[0] = true;
            path.add(0);

            branchAndBound(path, visited, 0, 0);

            long end = System.currentTimeMillis();

            System.out.println("\n--- OPTIMAL DELIVERY ROUTE ---");
            System.out.println("City Order: " + bestPath);
            System.out.println("Minimum Fuel Cost: " + bestCost);
            System.out.println("Total Estimated Time: " + calculateTime(bestPath));
            System.out.println("Execution Time: " + (end - start) + " ms");
        }
    }

    public static void main(String[] args) {
        int cities = 4;

        int[][] distance = {
                {0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}
        };

        int[][] fuelCost = {
                {0, 5, 8, 7},
                {5, 0, 12, 10},
                {8, 12, 0, 6},
                {7, 10, 6, 0}
        };

        int maxTime = 100;

        SwiftShipTSP routePlanner = new SwiftShipTSP(cities, distance, fuelCost, maxTime);
        routePlanner.findOptimalRoute();
    }
}


