# 🎓 Student Network Graph Analyzer

A graph-based application for analyzing student connections, exploring network structures, finding paths between students, and generating connection recommendations using fundamental Data Structures and Algorithms.

Built with **Java, Gradle, React, and TypeScript**, the project demonstrates how graph theory can be applied to student social networks through an interactive frontend and a Java-based backend.

---

## 🚀 Features

### 🔍 Graph Traversal — BFS and DFS

- **Breadth-First Search (BFS):** Explores the network level by level and helps identify the shortest path in terms of number of connections in an unweighted graph.
- **Depth-First Search (DFS):** Explores connected vertices by following paths as deeply as possible before backtracking.
- Displays traversal results to help users understand how the network is explored.

### 🌐 Connected Components

Identifies disconnected groups of students within the network, helping reveal separate communities and isolated portions of the graph.

### 🛣️ Shortest-Path Analysis

Uses BFS to calculate paths based on the number of connections and Dijkstra's algorithm to find minimum-cost routes in a weighted network.

### 🤝 Connection Recommendations

Suggests potential student connections based on the relationships represented in the network.

### 🖥️ Interactive Frontend

The React and TypeScript frontend communicates with the Java backend through REST API endpoints to retrieve network data and display analysis results.

---

## 🏗️ System Architecture

The application is organized into a Java backend and a React frontend.

### Backend — Java

The backend contains the graph data structures, algorithms, network-analysis logic, and API endpoints.

**Core components:**

- `Graph` — Represents the student network.
- `Vertex` — Represents a student in the graph.
- `Edge` — Represents a connection between students.
- `GraphAlgorithm` — Common abstraction for graph algorithms.
- `BFS` — Implements breadth-first traversal.
- `DFS` — Implements depth-first traversal.
- `Dijkstra` — Calculates minimum-cost paths in a weighted graph.
- `PathResult` — Encapsulates path-analysis results.
- `LinkedInAnalyzer` — Provides network-analysis operations.
- `ConnectionSuggestion` — Handles connection recommendation logic.
- `LinkedInApp` — Demonstrates the graph-analysis functionality.
- `StudentNetworkServer` — Exposes network functionality through HTTP API endpoints.

### Frontend — React and TypeScript

The frontend provides an interface for interacting with the network-analysis functionality and viewing results returned by the backend.

### API Endpoints

The Java backend exposes the following endpoints:

| Endpoint | Purpose |
|---|---|
| `/api/network` | Retrieves network data |
| `/api/groups` | Retrieves connected-group information |
| `/api/path` | Performs path analysis |
| `/api/route` | Calculates a weighted route |
| `/api/recommendations` | Retrieves connection recommendations |
| `/api/traversal` | Runs graph traversal |

The API runs locally on port `8080` in the development configuration.

---

## 📁 Project Structure

```text
Student-Network-Graph-Analyzer/
├── src/
│   └── main/
│       └── java/
│           └── br/com/unipe/linkedingraph/
│               ├── graph/
│               ├── algorithm/
│               ├── analyzer/
│               ├── LinkedInApp.java
│               └── StudentNetworkServer.java
│
├── frontend/
│   ├── src/
│   ├── package.json
│   └── ...
│
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
├── .gitignore
└── README.md
```

*Note: The structure above is a logical overview. Adjust the nested paths to match the exact locations of your source files.*

---

## ⚙️ Getting Started

### Prerequisites

- Java Development Kit (JDK) 21 or compatible version
- Node.js and npm
- Git

### 1. Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/Student-Network-Graph-Analyzer.git
cd Student-Network-Graph-Analyzer
```

Replace `YOUR_USERNAME` with your GitHub username.

### 2. Build the Java backend

```bash
chmod +x gradlew
./gradlew build
```

On Windows, use:

```bat
gradlew.bat build
```

### 3. Start the Java API server

If `StudentNetworkServer` is the configured server entry point, run:

```bash
java -cp build/classes/java/main br.com.unipe.linkedingraph.StudentNetworkServer
```

The API should be available at:

```text
http://localhost:8080/api
```

Keep the backend terminal running while using the frontend.

### 4. Start the frontend

Open a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Open the local URL printed by the frontend development server.

*The commands assume the frontend uses npm and a development script configured in `package.json`.*

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java 21 | Backend logic and graph algorithms |
| Gradle | Build automation |
| Java HTTP Server | REST API |
| React | Frontend interface |
| TypeScript | Frontend development |
| HTML and CSS | Interface structure and styling |
| Git and GitHub | Version control and project hosting |

---

## 🎯 Learning Outcomes

This project demonstrates practical application of:

- Graph representation using vertices and edges
- Breadth-First Search and Depth-First Search
- Connected-component analysis
- Shortest-path algorithms and weighted graphs
- Java object-oriented programming
- REST API development and frontend-backend communication
- Modular software design and build automation

---

## 🔮 Future Improvements

- Add interactive graph visualization.
- Support dynamic creation and editing of student profiles and connections.
- Add persistent database storage.
- Improve recommendation ranking with explainable graph-based metrics.
- Add automated unit and integration tests.
- Evaluate algorithm performance on larger networks.
- Improve API validation and error handling.

---

## 👨‍💻 Author

**Atharva Awasthy**

B.Tech — Computer Science and Engineering  
Manipal University Jaipur

---

## 📄 License

See the `LICENSE` file in this repository for the applicable license terms.