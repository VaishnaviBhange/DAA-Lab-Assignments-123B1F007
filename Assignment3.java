import java.util.*;

class Item {
    String name;
    double weight;
    double value;
    boolean divisible;
    int priority;

    Item(String name, double weight, double value, boolean divisible, int priority) {
        this.name = name;
        this.weight = weight;
        this.value = value;
        this.divisible = divisible;
        this.priority = priority;
    }

    double valuePerWeight() {
        return value / weight;
    }
}

public class Assignment3 {

    // Compare by priority first, then value/weight
    static class ItemComparator implements Comparator<Item> {
        @Override
        public int compare(Item a, Item b) {
            if (a.priority == b.priority) {
                return Double.compare(b.valuePerWeight(), a.valuePerWeight());
            }
            return Integer.compare(a.priority, b.priority);
        }
    }

    public static double fractionalKnapsack(List<Item> items, double capacity) {
        // Sort items
        items.sort(new ItemComparator());

        System.out.println("\nSorted Items (by Priority, then Value/Weight):");
        System.out.printf("%-20s %-10s %-10s %-12s %-15s %-15s%n",
                "Item", "Weight", "Value", "Priority", "Value/Weight", "Type");

        for (Item item : items) {
            System.out.printf("%-20s %-10.2f %-10.2f %-12d %-15.2f %-15s%n",
                    item.name, item.weight, item.value, item.priority,
                    item.valuePerWeight(), (item.divisible ? "Divisible" : "Indivisible"));
        }

        double totalValue = 0.0;
        double totalWeightCarried = 0.0;

        System.out.println("\nItems selected for transport:");
        for (Item item : items) {
            if (capacity <= 0) break;

            if (item.divisible) {
                double takenWeight = Math.min(item.weight, capacity);
                double takenValue = item.valuePerWeight() * takenWeight;
                totalValue += takenValue;
                capacity -= takenWeight;
                totalWeightCarried += takenWeight;

                System.out.printf(" - %s: %.2f kg, Utility = %.2f, Priority = %d, Type = Divisible%n",
                        item.name, takenWeight, takenValue, item.priority);
            } else {
                if (item.weight <= capacity) {
                    totalValue += item.value;
                    capacity -= item.weight;
                    totalWeightCarried += item.weight;

                    System.out.printf(" - %s: %.2f kg, Utility = %.2f, Priority = %d, Type = Indivisible%n",
                            item.name, item.weight, item.value, item.priority);
                }
            }
        }

        System.out.println("\n===== Final Report =====");
        System.out.printf("Total weight carried: %.2f kg%n", totalWeightCarried);
        System.out.printf("Total utility value carried: %.2f units%n", totalValue);

        return totalValue;
    }

    public static void main(String[] args) {
        List<Item> items = Arrays.asList(
                new Item("Medical Kits", 10, 100, false, 1),
                new Item("Food Packets", 20, 60, true, 3),
                new Item("Drinking Water", 30, 90, true, 2),
                new Item("Blankets", 15, 45, false, 3),
                new Item("Infant Formula", 5, 50, false, 1)
        );

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter maximum weight capacity of the boat (in kg): ");
        double capacity = sc.nextDouble();

        fractionalKnapsack(items, capacity);
    }
}
