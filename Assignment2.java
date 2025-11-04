/*
ASSIGNMENT 2:
PROBLEM STATEMENT:
Movie Recommendation System Optimization

Implement QuickSort to efficiently sort movie data based on
user-selected parameters such as rating, release year, or views.
*/

import java.util.Scanner;

class Movie {
    String title;
    double rating;
    int year;
    int views;

    Movie(String title, double rating, int year, int views) {
        this.title = title;
        this.rating = rating;
        this.year = year;
        this.views = views;
    }
}

public class Assignment2 {

    // QuickSort algorithm
    public static void quickSort(Movie[] arr, int low, int high, String sortBy) {
        if (low < high) {
            int pivotIndex = partition(arr, low, high, sortBy);
            quickSort(arr, low, pivotIndex - 1, sortBy);
            quickSort(arr, pivotIndex + 1, high, sortBy);
        }
    }

    // Partition logic for QuickSort
    private static int partition(Movie[] arr, int low, int high, String sortBy) {
        Movie pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            boolean condition = false;

            switch (sortBy) {
                case "rating":
                    condition = arr[j].rating < pivot.rating;
                    break;
                case "year":
                    condition = arr[j].year < pivot.year;
                    break;
                case "views":
                    condition = arr[j].views < pivot.views;
                    break;
            }

            if (condition) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }

    // Helper function to swap movies
    private static void swap(Movie[] arr, int i, int j) {
        Movie temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Display movies in tabular format
    private static void displayMovies(Movie[] arr) {
        System.out.printf("%-18s %-8s %-6s %-8s%n", "Title", "Rating", "Year", "Views");
        System.out.println("----------------------------------------------------");
        for (Movie m : arr) {
            System.out.printf("%-18s %-8.1f %-6d %-8d%n", m.title, m.rating, m.year, m.views);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Movie[] movies = {
                new Movie("Avengers", 8.5, 2012, 9500),
                new Movie("Inception", 8.8, 2010, 8700),
                new Movie("Titanic", 7.8, 1997, 12000),
                new Movie("Matrix", 8.7, 1999, 9800),
                new Movie("Interstellar", 8.6, 2014, 7300),
                new Movie("Joker", 8.4, 2019, 8200),
                new Movie("Avatar", 7.9, 2009, 15000),
                new Movie("Gladiator", 8.5, 2000, 6600),
                new Movie("Up", 8.2, 2009, 5400),
                new Movie("Coco", 8.4, 2017, 4700)
        };

        System.out.print("Enter sorting criterion (rating/year/views): ");
        String sortBy = sc.next().toLowerCase();

        if (!sortBy.equals("rating") && !sortBy.equals("year") && !sortBy.equals("views")) {
            System.out.println("Invalid criterion. Program exiting...");
            sc.close();
            return;
        }

        long start = System.nanoTime();
        quickSort(movies, 0, movies.length - 1, sortBy);
        long end = System.nanoTime();

        System.out.println("\nMovies sorted by " + sortBy + ":");
        displayMovies(movies);

        double timeTaken = (end - start) / 1_000_000.0;
        System.out.printf("%nSorting completed in %.3f ms%n", timeTaken);

        sc.close();
    }
}
