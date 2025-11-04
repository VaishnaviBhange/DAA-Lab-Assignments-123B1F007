/*
ASSIGNMENT 6:
PROBLEM STATEMENT:
Disaster Relief Resource Allocation Optimization

Scenario:
After a major earthquake, a relief organization must select
supplies to maximize total utility within a limited truck capacity.

Approach:
Use the 0/1 Knapsack Model to decide which items to include
such that the total weight ≤ capacity and utility is maximized.
Compare Brute Force, Dynamic Programming, and Greedy strategies.
*/

import java.util.*;

class SupplyItem {
    int id;
    String name;
    int weight;
    int utility;
    boolean isPerishable;

    public SupplyItem(int id, String name, int weight, int utility, boolean isPerishable) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.utility = utility;
        this.isPerishable = isPerishable;
    }
}

public class Assignment6 {

    // Brute Force recursive solution
    public static int bruteForceKnapsack(List<SupplyItem> list, int capacity, int n) {
        if (n == 0 || capacity == 0)
            return 0;

        SupplyItem current = list.get(n - 1);

        if (current.weight > capacity)
            return bruteForceKnapsack(list, capacity, n - 1);

        return Math.max(
                current.utility + bruteForceKnapsack(list, capacity - current.weight, n - 1),
                bruteForceKnapsack(list, capacity, n - 1)
        );
    }

    // Dynamic Programming solution
    public static int dpKnapsack(List<SupplyItem> list, int capacity) {
        int n = list.size();
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                SupplyItem item = list.get(i - 1);
                if (item.weight <= w)
                    dp[i][w] = Math.max(item.utility + dp[i - 1][w - item.weight], dp[i - 1][w]);
                else
                    dp[i][w] = dp[i - 1][w];
            }
        }

        // Backtrack to show chosen items
        System.out.println("\nItems Selected (DP):");
        int res = dp[n][capacity];
        int w = capacity;
        for (int i = n; i > 0 && res > 0; i--) {
            if (res != dp[i - 1][w]) {
                System.out.println("- " + list.get(i - 1).name);
                res -= list.get(i - 1).utility;
                w -= list.get(i - 1).weight;
            }
        }

        return dp[n][capacity];
    }

    // Greedy approximation using utility-to-weight ratio and perishable bonus
    public static double greedyKnapsack(List<SupplyItem> list, int capacity) {
        list.sort((a, b) -> {
            double ratioA = (double) a.utility / a.weight + (a.isPerishable ? 1.0 : 0);
            double ratioB = (double) b.utility / b.weight + (b.isPerishable ? 1.0 : 0);
            return Double.compare(ratioB, ratioA);
        });

        int totalWeight = 0;
        double totalUtility = 0.0;

        System.out.println("\nItems Selected (Greedy Approximation):");
        for (SupplyItem item : list) {
            if (totalWeight + item.weight <= capacity) {
                totalWeight += item.weight;
                totalUtility += item.utility;
                System.out.println("- " + item.name);
            }
        }

        return totalUtility;
    }

    // Multiple truck allocation using DP for each truck
    public static void multiTruckKnapsack(List<SupplyItem> list, int[] capacities) {
        System.out.println("\n--- Multi-Truck Allocation Report ---");
        int truckNum = 1;
        for (int cap : capacities) {
            System.out.println("\nTruck " + truckNum + " (Capacity: " + cap + " kg)");
            int totalUtility = dpKnapsack(list, cap);
            System.out.println("→ Total Utility Loaded: " + totalUtility);
            truckNum++;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<SupplyItem> supplies = new ArrayList<>();

        // Relief items (some perishable)
        supplies.add(new SupplyItem(1, "Medical Kits", 10, 60, true));
        supplies.add(new SupplyItem(2, "Food Supplies", 20, 100, true));
        supplies.add(new SupplyItem(3, "Water Bottles", 30, 120, false));
        supplies.add(new SupplyItem(4, "Blankets", 25, 80, false));
        supplies.add(new SupplyItem(5, "Tents", 40, 150, false));
        supplies.add(new SupplyItem(6, "First Aid Kits", 8, 70, true));
        supplies.add(new SupplyItem(7, "Fuel Cans", 35, 130, false));
        supplies.add(new SupplyItem(8, "Batteries", 12, 90, false));
        supplies.add(new SupplyItem(9, "Clothes", 18, 60, false));
        supplies.add(new SupplyItem(10, "Flashlights", 5, 40, false));
        supplies.add(new SupplyItem(11, "Satellite Phone", 6, 110, false));
        supplies.add(new SupplyItem(12, "Dry Fruits", 10, 85, true));
        supplies.add(new SupplyItem(13, "Power Banks", 7, 75, false));
        supplies.add(new SupplyItem(14, "Oxygen Cylinders", 32, 200, false));
        supplies.add(new SupplyItem(15, "Instant Meals", 15, 95, true));

        System.out.print("Enter truck capacity (in kg): ");
        int capacity = sc.nextInt();

        // --- Brute Force ---
        long startBF = System.nanoTime();
        int bfResult = bruteForceKnapsack(supplies, capacity, supplies.size());
        long endBF = System.nanoTime();
        double bfTime = (endBF - startBF) / 1e6;
        System.out.println("\n--- Brute Force Result ---");
        System.out.println("Max Utility: " + bfResult);
        System.out.printf("Time Taken: %.3f ms%n", bfTime);

        // --- Dynamic Programming ---
        long startDP = System.nanoTime();
        int dpResult = dpKnapsack(supplies, capacity);
        long endDP = System.nanoTime();
        double dpTime = (endDP - startDP) / 1e6;
        System.out.println("\n--- Dynamic Programming Result ---");
        System.out.println("Max Utility: " + dpResult);
        System.out.printf("Time Taken: %.3f ms%n", dpTime);

        // --- Greedy Approximation ---
        long startGR = System.nanoTime();
        double grResult = greedyKnapsack(supplies, capacity);
        long endGR = System.nanoTime();
        double grTime = (endGR - startGR) / 1e6;
        System.out.println("\n--- Greedy Result ---");
        System.out.println("Approx Utility: " + grResult);
        System.out.printf("Time Taken: %.3f ms%n", grTime);

        // --- Multi-truck case ---
        int[] capacities = {capacity, capacity + 20, capacity + 40};
        multiTruckKnapsack(supplies, capacities);

        // --- Performance Summary ---
        System.out.println("\n--- Performance Summary ---");
        System.out.printf("Brute Force Time: %.3f ms%n", bfTime);
        System.out.printf("Dynamic Programming Time: %.3f ms%n", dpTime);
        System.out.printf("Greedy Time: %.3f ms%n", grTime);

        sc.close();
    }
}
