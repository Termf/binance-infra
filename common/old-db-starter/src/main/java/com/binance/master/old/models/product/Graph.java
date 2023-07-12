package com.binance.master.old.models.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Graph {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Map<String, Vertex> vertexes = new HashMap<String, Vertex>();
    private Map<String, Boolean> edges = new HashMap<String, Boolean>();

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addEdge("123", "456", "123456");
        g.addEdge("BCC", "BTC", "BCCBTC");
        g.addEdge("ETH", "BTC", "ETHBTC");
        g.addEdge("GAS", "BTC", "GASBTC");
        g.addEdge("LTC", "BTC", "LTCBTC");
        g.addEdge("NEO", "BTC", "NEOBTC");
        g.addEdge("BNB", "BTC", "BNBBTC");
        g.addEdge("BNT", "ETH", "BNTETH");
        g.addEdge("EOS", "ETH", "EOSETH");
        g.addEdge("ICN", "ETH", "ICNETH");
        g.addEdge("MCO", "ETH", "MCOETH");
        g.addEdge("OAX", "ETH", "OAXETH");
        g.addEdge("OMG", "ETH", "OMGETH");
        g.addEdge("QTUM", "ETH", "QTUMETH");
        g.addEdge("WTC", "ETH", "WTCETH");
        g.addEdge("BNB", "ETH", "BNBETH");
        g.addEdge("BTM", "ETH", "BTMETH");
        g.addEdge("DNT", "ETH", "DNTETH");
        g.addEdge("LRC", "ETH", "LRCETH");
        g.addEdge("SNT", "ETH", "SNTETH");
        g.addEdge("HSR", "BTC", "HSRBTC");
        g.addEdge("MCO", "BTC", "MCOBTC");
        g.addEdge("OMG", "BTC", "OMGBTC");
        g.addEdge("QTUM", "BTC", "QTUMBTC");
        g.addEdge("WTC", "BTC", "WTCBTC");
        g.addEdge("BTC", "USDT", "BTCUSDT");
        g.addEdge("ETH", "USDT", "ETHUSDT");
        g.addEdge("ELC", "BTC", "ELCBTC");
        g.addEdge("HCC", "BTC", "HCCBTC");
        g.addEdge("LLT", "BTC", "LLTBTC");
        g.addEdge("LRC", "BTC", "LRCBTC");
        g.addEdge("YOYO", "BTC", "YOYOBTC");

        System.out.println("123:" + g.getShortestPathEdge("USDT", "BTC"));
        System.out.println("BCC:" + g.getShortestPathEdge("BCC", "USDT"));
        System.out.println("ETH:" + g.getShortestPathEdge("ETH", "BTC"));
        System.out.println("GAS:" + g.getShortestPathEdge("GAS", "BTC"));
        System.out.println("LTC:" + g.getShortestPathEdge("LTC", "BTC"));
        System.out.println("NEO:" + g.getShortestPathEdge("NEO", "USDT"));
        System.out.println("BNB:" + g.getShortestPathEdge("BNB", "BTC"));
        System.out.println("BNT:" + g.getShortestPathEdge("BNT", "BTC"));
        System.out.println("EOS:" + g.getShortestPathEdge("EOS", "BTC"));
        System.out.println("ICN:" + g.getShortestPathEdge("USDT", "BTC"));
        System.out.println("MCO:" + g.getShortestPathEdge("MCO", "BTC"));
        System.out.println("OAX:" + g.getShortestPathEdge("USDT", "BTC"));
        System.out.println("OMG:" + g.getShortestPathEdge("OMG", "BTC"));
        System.out.println("QTUM:" + g.getShortestPathEdge("QTUM", "BTC"));
        System.out.println("WTC:" + g.getShortestPathEdge("WTC", "BTC"));
        System.out.println("BNB:" + g.getShortestPathEdge("BNB", "BTC"));
        System.out.println("BTM:" + g.getShortestPathEdge("BTM", "BTC"));
        System.out.println("DNT:" + g.getShortestPathEdge("DNT", "BTC"));
        System.out.println("LRC:" + g.getShortestPathEdge("LRC", "BTC"));
        System.out.println("SNT:" + g.getShortestPathEdge("SNT", "BTC"));
        System.out.println("HSR:" + g.getShortestPathEdge("HSR", "BTC"));
        System.out.println("MCO:" + g.getShortestPathEdge("MCO", "BTC"));
        System.out.println("OMG:" + g.getShortestPathEdge("OMG", "BTC"));
        System.out.println("QTUM:" + g.getShortestPathEdge("QTUM", "BTC"));
        System.out.println("WTC:" + g.getShortestPathEdge("WTC", "BTC"));
        System.out.println("BTC:" + g.getShortestPathEdge("BTC", "BTC"));
        System.out.println("ETH:" + g.getShortestPathEdge("ETH", "BTC"));
        System.out.println("ELC:" + g.getShortestPathEdge("ELC", "BTC"));
        System.out.println("HCC:" + g.getShortestPathEdge("HCC", "BTC"));
        System.out.println("LLT:" + g.getShortestPathEdge("LLT", "BTC"));
        System.out.println("LRC:" + g.getShortestPathEdge("LRC", "BTC"));
        System.out.println("YOYO:" + g.getShortestPathEdge("YOYO", "BTC"));
    }

    public void clear() {
        vertexes.clear();
        edges.clear();
    }

    private void addVertex(String name) {
        if (vertexes.get(name) != null) {
            return;
        }
        Vertex vertext = new Vertex(name);
        vertexes.put(name, vertext);
    }

    public boolean addEdge(String from, String to, String name) {
        addVertex(from);
        addVertex(to);
        if (edges.get(name) != null) {
            return false;
        }
        vertexes.get(from).addEdge(vertexes.get(to), name);
        edges.put(name, true);
        return true;
    }

    private boolean computePaths(String strSource) {
        if (vertexes.get(strSource) == null) {
            return false;
        }
        Vertex source = vertexes.get(strSource);
        source.minDistance = 0.;
        PriorityQueue<Vertex> vertexQueue = new PriorityQueue<Vertex>();
        vertexQueue.add(source);

        while (!vertexQueue.isEmpty()) {
            Vertex u = vertexQueue.poll();

            // Visit each edge exiting u
            for (Edge e : u.adjacencies) {
                Vertex v = e.target;
                double weight = e.weight;
                double distanceThroughU = u.minDistance + weight;
                if (distanceThroughU < v.minDistance) {
                    vertexQueue.remove(v);

                    v.minDistance = distanceThroughU;
                    v.previous = u;
                    v.shortEdge = e;
                    vertexQueue.add(v);
                }
            }
        }
        return true;
    }

    private void refresh() {
        for (Vertex v : vertexes.values()) {
            v.minDistance = Double.POSITIVE_INFINITY;
            v.previous = null;
            v.shortEdge = null;
        }
    }

    public synchronized List<String> getShortestPathEdge(String from, String to) {
        if (vertexes.get(from) == null || vertexes.get(to) == null) {
            return null;
        }
        refresh();
        computePaths(from);
        List<String> path = new ArrayList<String>();
        for (Vertex vertex = vertexes.get(to); vertex != null; vertex = vertex.previous) {
            if (vertex.shortEdge != null) {
                path.add(vertex.shortEdge.toString());
                if (path.size() > 10) {
                    logger.error("path max exceeded:{} to {}", from, to);
                    return new ArrayList<String>();
                }
            }
        }
        Collections.reverse(path);
        return path;
    }
}


class Vertex implements Comparable<Vertex> {
    public final String name;
    public List<Edge> adjacencies = new ArrayList<Edge>();
    public double minDistance = Double.POSITIVE_INFINITY;
    public Vertex previous = null;
    public Edge shortEdge = null;

    public Vertex(String argName) {
        name = argName;
    }

    public String toString() {
        return name;
    }

    public int compareTo(Vertex other) {
        return Double.compare(minDistance, other.minDistance);
    }

    public Vertex addEdge(Vertex other, String name) {
        Edge out = new Edge(other, 1, name);
        Edge in = new Edge(this, 1, name);
        adjacencies.add(out);
        other.adjacencies.add(in);
        return this;
    }

}


class Edge {
    public final Vertex target;
    public final double weight;
    public final String name;

    public Edge(Vertex argTarget, double argWeight, String name) {
        target = argTarget;
        weight = argWeight;
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
