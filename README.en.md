# 🌐 LinkedIn Analyzer - Graph Engine

*Leia em outros idiomas: [Português](README.md)*

An analysis and recommendation engine for professional networks, using Graph Theory to solve connection, optimization, and proximity problems.

This practical project was developed for the **Graph Theory** course, taught by professor **Douglas Meneses**, in the Computer Science program at **Unipê**. The structural foundation of the graph and the base of some algorithms were inherited from the official course materials, which were subsequently refactored, optimized, and expanded by the team to build the current analysis engine.

---

## 🎥 Project Presentation

> 📺 **Click here to watch the explanatory video on YouTube** > *(LINK TO BE ADDED.)*

---

## 🚀 Features and Algorithms

The analysis engine, encapsulated in the `LinkedInAnalyzer` class, solves various professional social network problems using classic Graph Theory algorithms.

### Sub-network Mapping (DFS — *Depth-First Search*)
Performs a complete network scan to identify groups of users connected to each other but isolated from the rest of the network components.

### Degree of Separation (BFS — *Breadth-First Search*)
Calculates the shortest distance between two profiles considering only the number of connections (hops), ignoring edge weights.

### Highest Affinity Route (Dijkstra's Algorithm)
Determines the lowest-cost path between two users, prioritizing connections with higher affinity (lower weight), ensuring the optimal route between origin and destination.

### Connection Suggestion Engine
Identifies second-degree connections ("friends of friends"), suggesting new contacts ordered by the number of mutual friends.

---

## 🏗️ Project Architecture

The project was developed using **Object-Oriented Programming** principles, **Clean Code**, and software organization best practices.

### Main Components
- `GraphAlgorithm` — Common interface for all implemented graph algorithms.
- `DFS` — Depth-first search traversal.
- `BFS` — Breadth-first search traversal.
- `Dijkstra` — Weighted shortest path algorithm.
- `LinkedInAnalyzer` — Engine responsible for network analyses.
- `ConnectionSuggestion` — Class focused exclusively on contact recommendation logic.
- `PathResult` and `SuggestionResult` — Records and immutable models used to encapsulate analysis outputs.

### Data Structures Used
- `PriorityQueue` for Dijkstra's algorithm;
- `ArrayDeque` (as the main `Queue` implementation) for BFS;
- `LinkedHashSet`, `HashSet`, and `HashMap` to optimize searches, track visited vertices, and reconstruct paths.

---

## 📁 Project Structure

```text
src
├── graph
│   ├── Graph.java
│   ├── Vertex.java
│   └── Edge.java
│
├── algorithm
│   ├── GraphAlgorithm.java
│   ├── DFS.java
│   ├── BFS.java
│   ├── Dijkstra.java
│   └── PathResult.java
│
├── analyzer
│   ├── LinkedInAnalyzer.java
│   └── ConnectionSuggestion.java
│
└── LinkedInApp.java
```

---

## ⚙️ How to Run

The project already has a demonstration scenario configured to validate all implemented features.

1. Clone the repository:

```bash
git clone https://github.com/GuilhermeParnaibaNunes/LinkedInAnalyzer
```

2. Open the project in your preferred IDE (IntelliJ IDEA, Eclipse, VS Code, etc.).

3. Execute the main class:

```text
br.com.unipe.linkedingraph.LinkedInApp
```

4. Follow the analysis results displayed in the console.

---

## 🛠️ Technologies Used

- Java 21+
- Gradle
- Git
- GitHub
- IntelliJ IDEA

---

## 👥 Team

- **Guilherme Parnaíba Nunes** — RGM 38887479
- **Ikharo Cardoso Queiroz da Silva** — RGM 38421313
- **Sebastião Ricardo Felix Dos Santos Silva** — RGM 38966875
- **Vinicius Emanuel de Souza Nascimento** — RGM 38339188

---

## 🙏 Acknowledgements

The base graph structure used in this project was adapted from the educational material provided by professor **Douglas Meneses** during the **Graph Theory** course, serving as a starting point for the development of this application. We thank him for the classes and teachings both within the scope of the course and beyond.

---

## 📄 License

This project is strictly for **academic** purposes and was developed for the **Graph Theory** course of the Computer Science program at **Unipê**. Distributed under the MIT License.