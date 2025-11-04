//NAME:Vaishnavi Dada Bhange
//PRN:123B1F007
//DATE:
import java.util.*;

class Graph {
    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    int V;
    List<List<Edge>> adj;

    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++)
            adj.add(new ArrayList<>());
    }

    void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(v, w));
        adj.get(v).add(new Edge(u, w)); // undirected graph
    }

    int[] dijkstra(int source) {
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, source}); // {distance, node}

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int d = top[0];
            int u = top[1];

            if (d > dist[u]) continue;

            for (Edge edge : adj.get(u)) {
                int v = edge.to;
                int w = edge.weight;

                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{dist[v], v});
                }
            }
        }
        return dist;
    }
}

public class Assignment4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of intersections (vertices): ");
        int V = sc.nextInt();
        System.out.print("Enter number of roads (edges): ");
        int E = sc.nextInt();

        Graph graph = new Graph(V);

        System.out.println("Enter edges (u v w):");
        for (int i = 0; i < E; i++) {
            int u = sc.nextInt() - 1; // convert 1-based to 0-based
            int v = sc.nextInt() - 1;
            int w = sc.nextInt();
            graph.addEdge(u, v, w);
        }

        System.out.print("Enter ambulance start location (source): ");
        int source = sc.nextInt() - 1;

        System.out.print("Enter number of hospitals: ");
        int H = sc.nextInt();
        int[] hospitals = new int[H];
        System.out.println("Enter hospital nodes:");
        for (int i = 0; i < H; i++)
            hospitals[i] = sc.nextInt() - 1;

        int[] dist = graph.dijkstra(source);

        int minTime = Integer.MAX_VALUE;
        int nearestHospital = -1;

        for (int h : hospitals) {
            if (dist[h] < minTime) {
                minTime = dist[h];
                nearestHospital = h;
            }
        }

        if (nearestHospital == -1)
            System.out.println("No hospital reachable.");
        else
            System.out.println("Nearest hospital is at node " + (nearestHospital + 1)
                    + " with travel time " + minTime + " minutes.");
    }
}

