import java.awt.*;
import java.util.*;
import javax.swing.*;

public class RouteOptimizerGUI extends JFrame {

    static class Route {
        String city1, city2;
        int time, cost;
        Route(String city1, String city2, int time, int cost) {
            this.city1 = city1; this.city2 = city2;
            this.time = time; this.cost = cost;
        }
        public String toString() {
            return city1 + " <-> " + city2 + " | Time: " + time + " | Cost: " + cost;
        }
    }

    // ---------- GUI Components ----------
    private JTextArea outputArea;
    private JTextField cityField;
    private JComboBox<String> routeCity1Combo, routeCity2Combo;
    private JSpinner timeSpinner, costSpinner;
    private JComboBox<String> startCombo, destCombo, choiceBox;
    private DefaultListModel<String> cityListModel, routeListModel;
    private JList<String> cityList, routeList;
    private JLabel statusBar;
    private GraphPanel graphPanel;

    private java.util.List<String> cities = new ArrayList<>();
    private java.util.List<Route> routes = new ArrayList<>();

    // For path highlighting
    private java.util.List<String> lastOptimalPath = new ArrayList<>();

    public RouteOptimizerGUI() {
        setTitle("Route Optimizer");
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // -------- North Panel (City & Route Adders) --------
        JPanel north = new JPanel();
        north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
        north.setBorder(BorderFactory.createTitledBorder("Add Cities and Routes"));

        JPanel cityAddPanel = new JPanel(new FlowLayout());
        cityField = new JTextField(10);
        JButton addCityBtn = new JButton("Add City");
        addCityBtn.addActionListener(e -> addCity());
        cityAddPanel.add(new JLabel("Add City Name:"));
        cityAddPanel.add(cityField);
        cityAddPanel.add(addCityBtn);
        north.add(cityAddPanel);

        JPanel routeAddPanel = new JPanel(new FlowLayout());
        routeCity1Combo = new JComboBox<>();
        routeCity2Combo = new JComboBox<>();
        timeSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        costSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
        JButton addRouteBtn = new JButton("Add Route");
        addRouteBtn.addActionListener(e -> addRoute());
        routeAddPanel.add(new JLabel("City 1:")); routeAddPanel.add(routeCity1Combo);
        routeAddPanel.add(new JLabel("City 2:")); routeAddPanel.add(routeCity2Combo);
        routeAddPanel.add(new JLabel("Time:")); routeAddPanel.add(timeSpinner);
        routeAddPanel.add(new JLabel("Cost:")); routeAddPanel.add(costSpinner);
        routeAddPanel.add(addRouteBtn);
        north.add(routeAddPanel);

        add(north, BorderLayout.NORTH);

        // ----- West Panel: City & Route Lists -----
        JPanel west = new JPanel(new GridLayout(2, 1, 4, 4));
        cityListModel = new DefaultListModel<>();
        routeListModel = new DefaultListModel<>();
        cityList = new JList<>(cityListModel);
        routeList = new JList<>(routeListModel);
        cityList.setBorder(BorderFactory.createTitledBorder("Cities"));
        routeList.setBorder(BorderFactory.createTitledBorder("Routes"));
        west.add(new JScrollPane(cityList));
        west.add(new JScrollPane(routeList));
        add(west, BorderLayout.WEST);

        // ----- Center Panel: Output & Controls -----
        JPanel center = new JPanel();
        center.setLayout(new BorderLayout(5,5));

        JPanel controls = new JPanel(new FlowLayout());
        startCombo = new JComboBox<>();
        destCombo = new JComboBox<>();
        choiceBox = new JComboBox<>(new String[]{"Minimum Time", "Minimum Cost", "Display Both"});
        JButton findBtn = new JButton("Find Optimal Route");
        findBtn.addActionListener(e -> runOptimization());
        JButton resetBtn = new JButton("Reset All");
        resetBtn.addActionListener(e -> resetAll());

        controls.add(new JLabel("Start City:")); controls.add(startCombo);
        controls.add(new JLabel("Destination City:")); controls.add(destCombo);
        controls.add(new JLabel("Criteria:")); controls.add(choiceBox);
        controls.add(findBtn); controls.add(resetBtn);

        center.add(controls, BorderLayout.NORTH);

        outputArea = new JTextArea(10, 50);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        center.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        // ----- Graph Panel -----
        graphPanel = new GraphPanel();
        add(graphPanel, BorderLayout.EAST);

        // ----------- South: Status Bar -------------
        statusBar = new JLabel("Ready.");
        statusBar.setForeground(Color.BLACK);
        statusBar.setBorder(BorderFactory.createBevelBorder(1));
        add(statusBar, BorderLayout.SOUTH);

        // ------ Load built-in sample data ---------
        loadSampleData();
    }

    private void loadSampleData() {
        String[] sampleCities = {"delhi", "mumbai", "chennai", "bangalore", "kolkata"};
        for (String city : sampleCities) {
            cities.add(city);
            cityListModel.addElement(city);
            startCombo.addItem(city);
            destCombo.addItem(city);
            routeCity1Combo.addItem(city);
            routeCity2Combo.addItem(city);
        }
        Route[] sampleRoutes = {
            new Route("delhi", "mumbai", 18, 900),
            new Route("delhi", "chennai", 24, 1500),
            new Route("delhi", "bangalore", 20, 1200),
            new Route("mumbai", "chennai", 12, 850),
            new Route("mumbai", "bangalore", 14, 1100),
            new Route("chennai", "bangalore", 6, 400),
            new Route("chennai", "kolkata", 20, 1300),
            new Route("bangalore", "kolkata", 28, 2000)
        };
        for (Route r : sampleRoutes) {
            routes.add(r);
            routeListModel.addElement(r.toString());
        }
        graphPanel.repaint();
    }

    private void addCity() {
        String city = cityField.getText().trim();
        if (city.isEmpty()) {
            showError("City name cannot be empty!");
            return;
        }
        if (cities.contains(city)) {
            showError("City already exists.");
            return;
        }
        cities.add(city);
        cityListModel.addElement(city);
        startCombo.addItem(city);
        destCombo.addItem(city);
        routeCity1Combo.addItem(city);
        routeCity2Combo.addItem(city);
        showStatus("City '" + city + "' added.");
        cityField.setText("");
        graphPanel.repaint();
    }

    private void addRoute() {
        String c1 = (String) routeCity1Combo.getSelectedItem();
        String c2 = (String) routeCity2Combo.getSelectedItem();
        int t = (Integer) timeSpinner.getValue();
        int c = (Integer) costSpinner.getValue();
        if (c1 == null || c2 == null || c1.equals(c2)) {
            showError("Choose two different cities for a route.");
            return;
        }
        for (Route r : routes)
            if ((r.city1.equals(c1) && r.city2.equals(c2)) || (r.city1.equals(c2) && r.city2.equals(c1))) {
                showError("Route already exists between these cities.");
                return;
            }
        Route r = new Route(c1, c2, t, c);
        routes.add(r);
        routeListModel.addElement(r.toString());
        showStatus("Route added.");
        graphPanel.repaint();
    }

    private void showError(String msg) {
        statusBar.setText("Error: " + msg);
        statusBar.setForeground(Color.RED);
    }

    private void showStatus(String msg) {
        statusBar.setText(msg);
        statusBar.setForeground(Color.BLACK);
    }

    private void runOptimization() {
        outputArea.setText("");
        lastOptimalPath.clear();
        String start = (String) startCombo.getSelectedItem();
        String dest = (String) destCombo.getSelectedItem();
        if (start == null || dest == null || start.equals(dest)) {
            showError("Select valid and different start and destination cities.");
            graphPanel.repaint();
            return;
        }
        if (routes.size() == 0) {
            showError("Add at least one route.");
            graphPanel.repaint();
            return;
        }
        Map<String, Map<String, Route>> graph = buildGraph(routes);

        int choice = choiceBox.getSelectedIndex();
        if (choice == 0)
            findOptimal(graph, start, dest, "time");
        else if (choice == 1)
            findOptimal(graph, start, dest, "cost");
        else
            findBoth(graph, start, dest);
        graphPanel.repaint();
    }

    private Map<String, Map<String, Route>> buildGraph(java.util.List<Route> routes) {
        Map<String, Map<String, Route>> graph = new HashMap<>();
        for (Route r : routes) {
            graph.putIfAbsent(r.city1, new HashMap<>());
            graph.putIfAbsent(r.city2, new HashMap<>());
            graph.get(r.city1).put(r.city2, r);
            graph.get(r.city2).put(r.city1, r);
        }
        return graph;
    }

    // --- Dijkstra with path reconstruction ---
    private void findOptimal(Map<String, Map<String, Route>> graph, String start, String dest, String mode) {
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        Set<String> visited = new HashSet<>();
        for (String city : graph.keySet()) dist.put(city, Integer.MAX_VALUE);

        dist.put(start, 0);
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);

        while (!pq.isEmpty()) {
            String curr = pq.poll();
            if (visited.contains(curr)) continue;
            visited.add(curr);
            if (!graph.containsKey(curr)) continue;
            for (String nei : graph.get(curr).keySet()) {
                if (visited.contains(nei)) continue;
                Route r = graph.get(curr).get(nei);
                int val = mode.equals("time") ? r.time : r.cost;
                int newDist = dist.get(curr) + val;
                if (newDist < dist.get(nei)) {
                    dist.put(nei, newDist);
                    prev.put(nei, curr);
                    pq.add(nei);
                }
            }
        }

        if (!dist.containsKey(dest) || dist.get(dest) == Integer.MAX_VALUE) {
            outputArea.append("No path found from '" + start + "' to '" + dest + "'.\n");
            showError("No route found.");
            lastOptimalPath.clear();
            graphPanel.repaint();
            return;
        }

        java.util.List<String> path = buildPathList(prev, start, dest);
        lastOptimalPath = path;
        outputArea.append("Optimal Route (" + mode + "): " + String.join(" -> ", path) + "\n");
        outputArea.append("Total " + (mode.equals("time") ? "Time" : "Cost") + ": " + dist.get(dest) + "\n");
        showStatus("Optimal route calculation done.");
        graphPanel.repaint();
    }

    private void findBoth(Map<String, Map<String, Route>> graph, String start, String dest) {
        Map<String, Integer> timeDist = new HashMap<>();
        Map<String, String> timePrev = new HashMap<>();
        findSingleOptimal(graph, start, dest, "time", timeDist, timePrev);

        Map<String, Integer> costDist = new HashMap<>();
        Map<String, String> costPrev = new HashMap<>();
        findSingleOptimal(graph, start, dest, "cost", costDist, costPrev);

        outputArea.append("=== MINIMUM TIME ROUTE ===\n");
        if (timeDist.get(dest) != null && timeDist.get(dest) != Integer.MAX_VALUE) {
            java.util.List<String> path1 = buildPathList(timePrev, start, dest);
            outputArea.append("Route: " + String.join(" -> ", path1) + "\n");
            outputArea.append("Total Time: " + timeDist.get(dest) + "\n");
            outputArea.append("Total Cost on this route: " + calculateCostForPath(graph, path1) + "\n");
            lastOptimalPath = path1;
        } else {
            outputArea.append("No path found for minimum time.\n");
            lastOptimalPath.clear();
        }

        outputArea.append("\n=== MINIMUM COST ROUTE ===\n");
        if (costDist.get(dest) != null && costDist.get(dest) != Integer.MAX_VALUE) {
            java.util.List<String> path2 = buildPathList(costPrev, start, dest);
            outputArea.append("Route: " + String.join(" -> ", path2) + "\n");
            outputArea.append("Total Cost: " + costDist.get(dest) + "\n");
            outputArea.append("Total Time on this route: " + calculateTimeForPath(graph, path2) + "\n");
            lastOptimalPath = path2;
        } else {
            outputArea.append("No path found for minimum cost.\n");
            lastOptimalPath.clear();
        }
        showStatus("Optimal routes for both criteria found.");
        graphPanel.repaint();
    }

    private void findSingleOptimal(Map<String, Map<String, Route>> graph, String start, String dest, String mode,
                                   Map<String, Integer> dist, Map<String, String> prev) {
        Set<String> visited = new HashSet<>();
        for (String city : graph.keySet()) dist.put(city, Integer.MAX_VALUE);
        dist.put(start, 0);
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(dist::get));
        pq.add(start);
        while (!pq.isEmpty()) {
            String curr = pq.poll();
            if (visited.contains(curr)) continue;
            visited.add(curr);
            if (!graph.containsKey(curr)) continue;
            for (String nei : graph.get(curr).keySet()) {
                if (visited.contains(nei)) continue;
                Route r = graph.get(curr).get(nei);
                int val = mode.equals("time") ? r.time : r.cost;
                int newDist = dist.get(curr) + val;
                if (newDist < dist.get(nei)) {
                    dist.put(nei, newDist);
                    prev.put(nei, curr);
                    pq.add(nei);
                }
            }
        }
    }

    private int calculateCostForPath(Map<String, Map<String, Route>> graph, java.util.List<String> path) {
        int totalCost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String city1 = path.get(i), city2 = path.get(i + 1);
            totalCost += graph.get(city1).get(city2).cost;
        }
        return totalCost;
    }

    private int calculateTimeForPath(Map<String, Map<String, Route>> graph, java.util.List<String> path) {
        int totalTime = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            String city1 = path.get(i), city2 = path.get(i + 1);
            totalTime += graph.get(city1).get(city2).time;
        }
        return totalTime;
    }

    private java.util.List<String> buildPathList(Map<String, String> prev, String start, String dest) {
        java.util.List<String> path = new ArrayList<>();
        for (String at = dest; at != null; at = prev.get(at)) path.add(at);
        Collections.reverse(path);
        return path;
    }

    private void resetAll() {
        cities.clear();
        routes.clear();
        cityListModel.clear();
        routeListModel.clear();
        startCombo.removeAllItems();
        destCombo.removeAllItems();
        routeCity1Combo.removeAllItems();
        routeCity2Combo.removeAllItems();
        outputArea.setText("");
        lastOptimalPath.clear();
        showStatus("Reset complete.");
        loadSampleData();
        graphPanel.repaint();
    }

    // ---- Inner Class: Graph visualization panel -----
    class GraphPanel extends JPanel {
        public GraphPanel() { setPreferredSize(new Dimension(420, 620)); }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (cities.isEmpty()) return;

            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int nodeRadius = 30;
            int margin = 70;
            int n = cities.size();

            // Place cities in a circle for visibility
            Map<String, Point> cityPos = new HashMap<>();
            double angleStep = 2 * Math.PI / n;
            int cx = getWidth() / 2, cy = getHeight() / 2, r = Math.min(cx, cy) - margin;
            for (int i = 0; i < n; i++) {
                double angle = i * angleStep - Math.PI/2;
                int x = cx + (int)(r * Math.cos(angle));
                int y = cy + (int)(r * Math.sin(angle));
                cityPos.put(cities.get(i), new Point(x, y));
            }

            // Draw edges
            for (Route route : routes) {
                Point p1 = cityPos.get(route.city1);
                Point p2 = cityPos.get(route.city2);
                if (p1 == null || p2 == null) continue;

                g2.setStroke(new BasicStroke(2.5f));
                g2.setColor(Color.LIGHT_GRAY);

                // Highlight edge if it's in the last optimal path
                boolean highlight = false;
                for (int i=0; i < lastOptimalPath.size()-1; i++) {
                    String from = lastOptimalPath.get(i), to = lastOptimalPath.get(i+1);
                    if ((from.equals(route.city1) && to.equals(route.city2)) ||
                        (from.equals(route.city2) && to.equals(route.city1))) {
                        highlight = true; break;
                    }
                }
                if (highlight) g2.setColor(Color.RED);

                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                // Draw weights (both time and cost)
                String label = "T:" + route.time + ", C:" + route.cost;
                int midX = (p1.x + p2.x) / 2, midY = (p1.y + p2.y) / 2;
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.drawString(label, midX, midY);
            }

            // Draw nodes
            for (String city : cities) {
                Point p = cityPos.get(city);
                g2.setColor(lastOptimalPath.contains(city) ? Color.GREEN : Color.CYAN);
                g2.fillOval(p.x - nodeRadius/2, p.y - nodeRadius/2, nodeRadius, nodeRadius);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(p.x - nodeRadius/2, p.y - nodeRadius/2, nodeRadius, nodeRadius);
                g2.setFont(new Font("SansSerif", Font.BOLD, 15));
                FontMetrics fm = g.getFontMetrics();
                int tx = p.x - fm.stringWidth(city)/2;
                int ty = p.y + fm.getHeight()/4;
                g2.drawString(city, tx, ty);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
            new RouteOptimizerGUI().setVisible(true);
        });
    }
}
