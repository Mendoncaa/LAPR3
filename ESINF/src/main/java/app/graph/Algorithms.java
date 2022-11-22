package app.graph;

import app.graph.matrix.MatrixGraph;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BinaryOperator;

/**
 *
 * @author DEI-ISEP
 *
 */
public class Algorithms {
    /** Performs breadth-first search of a Graph starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex that will be the source of the search
     * @return a LinkedList with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vert) {
        if (!g.validVertex(vert))
            return null;

        Queue<V> queueBFS = new LinkedList<>();
        Queue<V> queueAux = new LinkedList<>();
        boolean[] visitedVec = new boolean[g.numVertices()];

        queueBFS.add(vert);
        queueAux.add(vert);
        visitedVec[g.key(vert)] = true;

        while (!queueAux.isEmpty()) {
            vert = queueAux.remove();

            for (V adjVert : g.adjVertices(vert)) {
                if (!visitedVec[g.key(adjVert)]) {
                    queueBFS.add(adjVert);
                    queueAux.add(adjVert);
                    visitedVec[g.key(adjVert)] = true;
                }
            }
        }

        return (LinkedList<V>) queueBFS;
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vOrig vertex of graph g that will be the source of the search
     * @param visited set of previously visited vertices
     * @param qdfs return LinkedList with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        if (visited[g.key(vOrig)])
            return;

        qdfs.push(vOrig);
        visited[g.key(vOrig)] = true;

        for (V adjVert : g.adjVertices(vOrig))
            DepthFirstSearch(g, adjVert, visited, qdfs);
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex of graph g that will be the source of the search

     * @return a LinkedList with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        if (!g.validVertex(vert))
            return null;

        Queue<V> queueDFS = new LinkedList<>();
        DepthFirstSearch(g, vert, new boolean[g.numVertices()], (LinkedList<V>) queueDFS);

        return (LinkedList<V>) queueDFS;
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g       Graph instance
     * @param vOrig   Vertex that will be the source of the path
     * @param vDest   Vertex that will be the end of the path
     * @param visited set of discovered vertices
     * @param path    stack with vertices of the current path (the path is in reverse order)
     * @param paths   ArrayList with all the paths (in correct order)
     */
    private static <V, E> void allPaths(Graph<V, E> g, V vOrig, V vDest, boolean[] visited,
                                        LinkedList<V> path, ArrayList<LinkedList<V>> paths) {
        path.push(vOrig);
        visited[g.key(vOrig)] = true;

        for (V adjVert : g.adjVertices(vOrig)) {
            if (adjVert == vDest) {
                path.push(vDest);
                paths.add(path);
                path.pop();
            } else {
                if (!visited[g.key(adjVert)])
                    allPaths(g, adjVert, vDest, visited, path, paths);
            }
        }

        path.pop();
    }

    /** Returns all paths from vOrig to vDest
     *
     * @param g     Graph instance
     * @param vOrig information of the Vertex origin
     * @param vDest information of the Vertex destination
     * @return paths ArrayList with all paths from vOrig to vDest
     */
    public static <V, E> ArrayList<LinkedList<V>> allPaths(Graph<V, E> g, V vOrig, V vDest) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest))
            return null;

        List<LinkedList<V>> pathsList = new ArrayList<>();
        allPaths(g, vOrig, vDest, new boolean[g.numVertices()], new LinkedList<>(), (ArrayList<LinkedList<V>>) pathsList);

        return (ArrayList<LinkedList<V>>) pathsList;
    }

    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V [] pathKeys, E [] dist) {
        int vkey = g.key(vOrig);
        dist[vkey] = zero;
        pathKeys[vkey] = vOrig;
        while (vOrig != null) {
            vkey = g.key(vOrig);
            visited[vkey] = true;
            for (Edge<V, E> edge : g.outgoingEdges(vOrig)) {
                int vkeyAdj = g.key(edge.getVDest());
                if (!visited[vkeyAdj]) {
                    E s = sum.apply(dist[vkey], edge.getWeight());
                    if (dist[vkeyAdj] == null || ce.compare(dist[vkeyAdj], s) > 0) {
                        dist[vkeyAdj] = s;
                        pathKeys[vkeyAdj] = vOrig;
                    }
                }
            }
            E minDist = null;  //next vertice, that has minimum dist
            vOrig = null;
            for (V vert : g.vertices()) {
                int i = g.key(vert);
                if (!visited[i] && (dist[i] != null) && ((minDist == null) || ce.compare(dist[i], minDist) < 0)) {
                    minDist = dist[i];
                    vOrig = vert;
                }
            }
        }
    }

    private static <V, E> void initializePathDist(int nVerts, V [] pathKeys, E[] dist) {
        for (int i = 0; i < nVerts; i++) {
            dist[i] = null;
            pathKeys[i] = null;
        }
    }



    /** Shortest-path between two vertices
     *
     * @param g graph
     * @param vOrig origin vertex
     * @param vDest destination vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {
        if (!g.validVertex(vOrig) || !g.validVertex(vDest))
            return null;

        boolean[] visited = new boolean[g.numVertices()];
        @SuppressWarnings("unchecked") V[] pathKeys = (V[]) Array.newInstance(vOrig.getClass(), g.numVertices());
        @SuppressWarnings("unchecked") E[] dist = (E[]) Array.newInstance(zero.getClass(), g.numVertices());

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        Iterator<V> it = shortPath.iterator();
        while (it.hasNext()) {
            V vert = it.next();
            System.out.println(vert);
        }
        System.out.println();

        if (dist[g.key(vDest)] != null) {
            getPath(g, vOrig, vDest, pathKeys, shortPath);

            return dist[g.key(vDest)];
        }


        return null;
    }

    /** Shortest-path between a vertex and all other vertices
     *
     * @param g graph
     * @param vOrig start vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig,
                                               Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {
        if (!g.validVertex(vOrig))
            return false;

        boolean[] visited = new boolean[g.numVertices()];
        @SuppressWarnings("unchecked") V[] pathKeys = (V[]) Array.newInstance(vOrig.getClass(), g.numVertices());
        @SuppressWarnings("unchecked") E[] dist = (E[]) Array.newInstance(zero.getClass(), g.numVertices());

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);

        for (int cnt = 0; cnt < g.numVertices(); cnt++) {
            paths.add(null);
            dists.add(null);
        }

        for (int cnt = 0; cnt < g.numVertices(); cnt++) {
            Queue<V> shortPath = new LinkedList<>();

            if (dist[cnt] != null)
                getPath(g, vOrig, g.vertex(cnt), pathKeys, (LinkedList<V>) shortPath);

            paths.set(cnt, (LinkedList<V>) shortPath);
            dists.set(cnt, dist[cnt]);
        }

        return true;
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    protected static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                         V [] pathKeys, LinkedList<V> path) {
        if (!vOrig.equals(vDest)) {
            path.push(vDest);
            getPath(g, vOrig, pathKeys[g.key(vDest)], pathKeys, path);
        } else
            path.push(vOrig);
    }

    /** Calculates the minimum distance graph using Floyd-Warshall
     *
     * @param g initial graph
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @return the minimum distance graph
     */
    public static <V,E> MatrixGraph<V,E> minDistGraph(Graph <V,E> g, Comparator<E> ce, BinaryOperator<E> sum) {
        int numVerts = g.numVertices();
        if (numVerts == 0) return null;

        @SuppressWarnings("unchecked")
        E[][] m = (E[][]) new Object[numVerts][numVerts];

        for (int i = 0; i < numVerts; i++) {
            for (int j = 0; j < numVerts; j++) {
                Edge <V,E> e = g.edge(i,j);
                if (e != null) m[i][j] = e.getWeight();
            }
        }

        for (int k = 0; k < numVerts; k++) {
            for (int i = 0; i < numVerts; i++) {
                if (i !=k && m[i][k] != null) {
                    for (int j = 0; j < numVerts; j++) {
                        if (i != j && k != j && m[k][j] != null) {
                            E s = sum.apply(m[i][k], m[k][j]);
                            if (m[i][j] == null || ce.compare(s, m[i][j]) < 0) {
                                m[i][j] = s;
                            }
                        }
                    }
                }
            }
        }
        return new MatrixGraph<>(g.isDirected(),g.vertices(),m);
    }

}