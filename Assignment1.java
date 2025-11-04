//Shravani Vinod Dham
//123B1F023
import java.io.*;
import java.util.*;

public class Assignment1 {

    // Student class
    static class Student {
        String name;
        int marks;

        Student(String name, int marks) {
            this.name = name;
            this.marks = marks;
        }

        @Override
        public String toString() {
            return name + " - " + marks;
        }
    }

    // QuickSort
    static void quickSort(Student[] arr, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    static int partition(Student[] arr, int low, int high) {
        int pivot = arr[high].marks;
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (arr[j].marks < pivot) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    static void swap(Student[] arr, int i, int j) {
        Student temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\shrav\\Desktop\\DAA\\students.csv";
        List<Student> students = new ArrayList<>();

        // Read CSV
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine(); // To Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    students.add(new Student(parts[0].trim(), Integer.parseInt(parts[1].trim())));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading file: " + e.getMessage());
            return;
        }

        Student[] arr = students.toArray(new Student[0]);

        System.out.println("Original Marks:");
        for (Student s : arr) System.out.println(s);

        quickSort(arr, 0, arr.length - 1);

        System.out.println("\nSorted Marks (Ascending):");
        for (Student s : arr) System.out.println(s);
    }
}
