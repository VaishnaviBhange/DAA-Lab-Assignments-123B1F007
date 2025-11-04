//NAME:Vaihsnavi Dada Bhange
//PRN:123B1F007
//DATE:
import java.util.*;

class Edge {
    int to;
    double baseCost; // static base cost
    double curCost;  // current cost (can be updated)

    Edge(int to, double baseCost) {
        this.to = to;
        this.baseCost = baseCost;
        this.curCost = baseCost;
    }
}

public class Assignment5 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input stages
        System.out.print("Enter number of stages: ");
        int S = sc.nextInt();
        int[] stageCount = new int[S];
        int N = 0;
        System.out.print("Enter number of nodes in each stage (" + S + " values): ");
        for (int i = 0; i < S; i++) {
            stageCount[i] = sc.nextInt();
            N += stageCount[i];
        }

        int[] stageStart = new int[S];
        int idx = 0;
        for (int i = 0; i < S; i++) {
            stageStart[i] = idx;
            idx += stageCount[i];
        }

        // Input edges
        System.out.print("Enter number of edges: ");
        int M = sc.nextInt();
        List<List<Edge>> adj = new ArrayList<>();
        List<List<Integer>> revAdj = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            adj.add(new ArrayList<>());
            revAdj.add(new ArrayList<>());
        }

        System.out.println("Enter each edge as: u v cost");
        for (int i = 0; i < M; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            double cost = sc.nextDouble();
            if (u < 0 || u >= N || v < 0 || v >= N) {
                System.err.println("Invalid edge node id");
                return;
            }
            adj.get(u).add(new Edge(v, cost));
            revAdj.get(v).add(u);
        }

        final double INF = 1e30;
        double[] bestCost = new double[N];
        int[] nextNode = new int[N];
        Arrays.fill(bestCost, INF);
        Arrays.fill(nextNode, -1);

        // Initialize sink stage (last stage)
        int lastStage = S - 1;
        for (int k = 0; k < stageCount[lastStage]; k++) {
            int node = stageStart[lastStage] + k;
            bestCost[node] = 0.0;
            nextNode[node] = -1;
        }

        // DP: compute best cost from last stage backward
        for (int st = S - 2; st >= 0; st--) {
            for (int k = 0; k < stageCount[st]; k++) {
                int u = stageStart[st] + k;
                double best = INF;
                int bestv = -1;
                for (Edge e : adj.get(u)) {
                    int v = e.to;
                    double cost = e.curCost;
                    if (bestCost[v] + cost < best) {
                        best = bestCost[v] + cost;
                        bestv = v;
                    }
                }
                bestCost[u] = best;
                nextNode[u] = bestv;
            }
        }

        // Output best costs from Stage-0 nodes
        System.out.println("\nBest costs from Stage-0 nodes:");
        for (int k = 0; k < stageCount[0]; k++) {
            int u = stageStart[0] + k;
            if (bestCost[u] >= INF / 2)
                System.out.println("Node " + u + ": unreachable");
            else
                System.out.printf("Node %d: cost = %.6f%n", u, bestCost[u]);
        }

        // Print path from a source node
        System.out.print("\nEnter a source node id (in stage 0) to print path, or -1 to skip: ");
        int src = sc.nextInt();
        if (src >= 0 && src < N) {
            if (bestCost[src] >= INF / 2) {
                System.out.println("No route from " + src);
            } else {
                System.out.print("Path from " + src + " : ");
                int cur = src;
                double total = 0.0;
                while (cur != -1) {
                    System.out.print(cur);
                    int nxt = nextNode[cur];
                    if (nxt != -1) {
                        double ecost = 0;
                        boolean found = false;
                        for (Edge e : adj.get(cur)) {
                            if (e.to == nxt) {
                                ecost = e.curCost;
                                found = true;
                                break;
                            }
                        }
                        if (!found) ecost = 0;
                        total += ecost;
                        System.out.print(" -> ");
                    } else break;
                    cur = nxt;
                }
                System.out.printf("%nTotal route cost (sum edges): %.6f%n", total);
            }
        }

        // Real-time updates
        System.out.print("\nEnter number of live updates to edge costs (0 to finish): ");
        int Q = sc.nextInt();
        while (Q-- > 0) {
            System.out.print("Enter edge update (u v multiplier): ");
            int u = sc.nextInt();
            int v = sc.nextInt();
            double multiplier = sc.nextDouble();

            for (Edge e : adj.get(u)) {
                if (e.to == v) {
                    e.curCost = e.baseCost * multiplier;
                }
            }

            // Incremental DP update
            Queue<Integer> queue = new LinkedList<>();
            double newCostU = recomputeNode(u, adj, bestCost, nextNode, INF);
            if (Math.abs(newCostU - bestCost[u]) > 1e-9) {
                bestCost[u] = newCostU;
                queue.add(u);
            }
            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (int pred : revAdj.get(node)) {
                    double newC = recomputeNode(pred, adj, bestCost, nextNode, INF);
                    if (Math.abs(newC - bestCost[pred]) > 1e-9) {
                        bestCost[pred] = newC;
                        queue.add(pred);
                    }
                }
            }
        }

        System.out.println("\nAfter updates, best costs from Stage-0 nodes:");
        for (int k = 0; k < stageCount[0]; k++) {
            int u = stageStart[0] + k;
            if (bestCost[u] >= INF / 2)
                System.out.println("Node " + u + ": unreachable");
            else
                System.out.printf("Node %d: cost = %.6f%n", u, bestCost[u]);
        }
    }

    // Helper function for incremental DP update
    static double recomputeNode(int u, List<List<Edge>> adj, double[] bestCost, int[] nextNode, double INF) {
        double best = INF;
        int bestv = -1;
        for (Edge e : adj.get(u)) {
            if (bestCost[e.to] >= INF / 2) continue;
            double cand = e.curCost + bestCost[e.to];
            if (cand < best) {
                best = cand;
                bestv = e.to;
            }
        }
        nextNode[u] = bestv;
        return best;
    }
}

