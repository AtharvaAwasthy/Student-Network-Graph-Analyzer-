
import { useEffect, useState } from "react";
import "./Algorithms.css";

const API = "http://localhost:8080/api";

type Network = {
  students: { id: string; label: string }[];
};

type TraversalResult = {
  algorithm: string;
  start: string;
  order: string[];
  visitedCount: number;
};

type RouteResult = {
  path: string[];
  cost: number;
  hasPath: boolean;
};

type AlgorithmType = "BFS" | "DFS" | "Dijkstra";

export default function Algorithms() {
  const [students, setStudents] = useState<string[]>([]);
  const [algorithm, setAlgorithm] = useState<AlgorithmType>("BFS");
  const [start, setStart] = useState("Aarav");
  const [end, setEnd] = useState("Ishita");
  const [traversal, setTraversal] = useState<string[]>([]);
  const [route, setRoute] = useState<string[]>([]);
  const [cost, setCost] = useState<number | null>(null);
  const [visitedCount, setVisitedCount] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    fetch(`${API}/network`)
      .then((response) => {
        if (!response.ok) {
          throw new Error("Could not load students.");
        }
        return response.json() as Promise<Network>;
      })
      .then((data) => {
        const names = data.students.map((student) => student.label);
        setStudents(names);

        if (names.length > 0) {
          setStart(names[0]);
          setEnd(names[Math.min(1, names.length - 1)]);
        }
      })
      .catch(() => {
        setError(
          "Cannot connect to Java backend. Is it running on port 8080?"
        );
      });
  }, []);

  async function runAlgorithm() {
    if (!start || (algorithm === "Dijkstra" && !end)) return;

    setLoading(true);
    setError("");
    setTraversal([]);
    setRoute([]);
    setCost(null);
    setVisitedCount(0);

    try {
      if (algorithm === "BFS" || algorithm === "DFS") {
        const response = await fetch(
          `${API}/traversal?algorithm=${algorithm}&start=${encodeURIComponent(start)}`
        );

        const data = (await response.json()) as TraversalResult & {
          error?: string;
        };

        if (!response.ok) {
          throw new Error(data.error || "Traversal failed.");
        }

        setTraversal(data.order);
        setVisitedCount(data.visitedCount);
      } else {
        const response = await fetch(
          `${API}/route?from=${encodeURIComponent(start)}&to=${encodeURIComponent(end)}`
        );

        const data = (await response.json()) as RouteResult & {
          error?: string;
        };

        if (!response.ok) {
          throw new Error(data.error || "Route search failed.");
        }

        if (!data.hasPath) {
          setError(`No route exists between ${start} and ${end}.`);
        } else {
          setRoute(data.path);
          setCost(data.cost);
          setVisitedCount(data.path.length);
        }
      }
    } catch (err) {
      setError(
        err instanceof Error ? err.message : "Something went wrong."
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="algorithms-page">
      <div className="algorithms-header">
        <p className="section-eyebrow">GRAPH ALGORITHMS</p>
        <h1>Explore the Network</h1>
        <p>Run graph algorithms on the live Java student network.</p>
      </div>

      <div className="algorithm-controls">
        {/* LEFT: Student selection */}
        <div className="student-selectors">
          <label>
            Starting student
            <select
              value={start}
              onChange={(event) => setStart(event.target.value)}
              disabled={students.length === 0}
            >
              {students.map((student) => (
                <option key={student} value={student}>
                  {student}
                </option>
              ))}
            </select>
          </label>

          {algorithm === "Dijkstra" && (
            <label>
              Destination student
              <select
                value={end}
                onChange={(event) => setEnd(event.target.value)}
                disabled={students.length === 0}
              >
                {students.map((student) => (
                  <option key={student} value={student}>
                    {student}
                  </option>
                ))}
              </select>
            </label>
          )}

          <p className="selection-hint">
            {algorithm === "Dijkstra"
              ? "Choose the source and destination for the lowest-cost route."
              : "Choose a starting student to explore their connected network."}
          </p>
        </div>

        {/* RIGHT: Algorithm selection */}
        <div className="algorithm-selector-panel">
          <label>
            Algorithm
            <select
              value={algorithm}
              onChange={(event) => {
                setAlgorithm(event.target.value as AlgorithmType);
                setTraversal([]);
                setRoute([]);
                setCost(null);
                setVisitedCount(0);
                setError("");
              }}
            >
              <option value="BFS">Breadth-First Search (BFS)</option>
              <option value="DFS">Depth-First Search (DFS)</option>
              <option value="Dijkstra">Dijkstra's Shortest Path</option>
            </select>
          </label>

          <div className="algorithm-info">
            <span className="algorithm-info-symbol">
              {algorithm === "BFS" ? "↔" : algorithm === "DFS" ? "↧" : "⌁"}
            </span>
            <div>
              <strong>
                {algorithm === "BFS"
                  ? "Breadth-First Search"
                  : algorithm === "DFS"
                    ? "Depth-First Search"
                    : "Shortest Path"}
              </strong>
              <p>
                {algorithm === "BFS"
                  ? "Explore neighbors level by level."
                  : algorithm === "DFS"
                    ? "Explore each branch before backtracking."
                    : "Find the minimum total-weight route."}
              </p>
            </div>
          </div>

          <button
            type="button"
            className="run-algorithm"
            onClick={runAlgorithm}
            disabled={loading || students.length === 0}
          >
            {loading ? "Running..." : "Run Algorithm"}
          </button>
        </div>
      </div>

      {error && (
        <p role="alert" className="algorithm-error">
          {error}
        </p>
      )}

      <div className="algorithm-results">
        <article className="algorithm-result-card">
          <div className="result-card-heading">
            <div>
              <p className="result-eyebrow">EXECUTION RESULTS</p>
              <h2>
                {algorithm === "Dijkstra"
                  ? "Shortest Route"
                  : `${algorithm} Traversal`}
              </h2>
            </div>
            <span className="live-tag">Java Backend</span>
          </div>

          {algorithm !== "Dijkstra" ? (
            traversal.length > 0 ? (
              <>
                <p className="result-summary">
                  Reachable students visited:{" "}
                  <strong>{visitedCount}</strong>
                </p>

                <ol className="traversal-list">
                  {traversal.map((student, index) => (
                    <li key={`${student}-${index}`}>
                      <span>{index + 1}</span>
                      {student}
                    </li>
                  ))}
                </ol>
              </>
            ) : (
              <div className="algorithm-placeholder">
                <span className="placeholder-symbol">⌘</span>
                <p>
                  Select an algorithm and click{" "}
                  <strong>Run Algorithm</strong> to view the traversal.
                </p>
              </div>
            )
          ) : route.length > 0 ? (
            <>
              <div className="route-metrics">
                <div>
                  <span>Total route cost</span>
                  <strong>{cost}</strong>
                </div>
                <div>
                  <span>Students on route</span>
                  <strong>{visitedCount}</strong>
                </div>
              </div>

              <p className="route-label">
                Route from {start} to {end}
              </p>
              <p className="route-display">{route.join(" → ")}</p>
            </>
          ) : (
            <div className="algorithm-placeholder">
              <span className="placeholder-symbol">⌁</span>
              <p>
                Choose a source and destination, then run Dijkstra to find the
                lowest-cost route.
              </p>
            </div>
          )}
        </article>
      </div>

      <p className="algorithm-demo-note">
        Live mode · Results are calculated by the Java graph algorithms.
      </p>
    </section>
  );
}
