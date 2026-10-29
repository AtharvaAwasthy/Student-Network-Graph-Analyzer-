
import { useEffect, useState } from "react";
import "./FindConnections.css";

const API = "http://localhost:8080/api";

type PathResponse = {
  from: string;
  to: string;
  hops: number;
};

type NetworkResponse = {
  students: { id: string; label: string }[];
};

export default function FindConnections() {
  const [students, setStudents] = useState<string[]>([]);
  const [start, setStart] = useState("Aarav");
  const [end, setEnd] = useState("Arjun");
  const [result, setResult] = useState<string[] | null>(null);
  const [searched, setSearched] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadStudents() {
      try {
        const response = await fetch(`${API}/network`);
        if (!response.ok) throw new Error("Could not load students.");

        const data: NetworkResponse = await response.json();
        const names = data.students.map((student) => student.id);
        setStudents(names);

        if (!names.includes(start) && names.length > 0) setStart(names[0]);
        if (!names.includes(end) && names.length > 0) {
          setEnd(names[names.length - 1]);
        }
      } catch {
        setError("Cannot connect to the Java API. Start the backend on port 8080.");
      }
    }

    void loadStudents();
  }, []);

  async function handleSearch() {
    setLoading(true);
    setError("");
    setSearched(false);
    setResult(null);

    try {
      const query = new URLSearchParams({ from: start, to: end });
      const response = await fetch(`${API}/path?${query.toString()}`);

      if (!response.ok) {
        const body = await response.json().catch(() => null);
        throw new Error(body?.error ?? "The path request failed.");
      }

      const data: PathResponse = await response.json();

      if (data.hops < 0) {
        setResult(null);
      } else if (start === end) {
        setResult([start]);
      } else {
        // The backend returns the BFS hop count. Recover a shortest path
        // by querying the Java graph's available connections.
        const networkResponse = await fetch(`${API}/network`);
        if (!networkResponse.ok) throw new Error("Could not load graph data.");

        const network: NetworkResponse & {
          connections: { source: string; target: string; weight: number }[];
        } = await networkResponse.json();

        const adjacency = new Map<string, string[]>();
        network.students.forEach(({ id }) => adjacency.set(id, []));

        network.connections.forEach(({ source, target }) => {
          adjacency.get(source)?.push(target);
          adjacency.get(target)?.push(source);
        });

        const queue: string[][] = [[start]];
        const visited = new Set([start]);
        let path: string[] | null = null;

        while (queue.length > 0) {
          const currentPath = queue.shift()!;
          const current = currentPath[currentPath.length - 1];

          if (current === end) {
            path = currentPath;
            break;
          }

          for (const neighbor of adjacency.get(current) ?? []) {
            if (!visited.has(neighbor)) {
              visited.add(neighbor);
              queue.push([...currentPath, neighbor]);
            }
          }
        }

        setResult(path);
      }

      setSearched(true);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Unexpected error.");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="find-page">
      <div className="find-heading">
        <p className="eyebrow">GRAPH ALGORITHMS / BFS</p>
        <h2>Find Connections</h2>
        <p>
          Discover the shortest chain of direct connections between two students.
        </p>
      </div>

      <section className="find-card">
        {error && <p role="alert">{error}</p>}

        <div className="find-form">
          <label>
            Starting student
            <select
              value={start}
              onChange={(event) => setStart(event.target.value)}
              disabled={students.length === 0}
            >
              {students.map((student) => (
                <option key={student} value={student}>{student}</option>
              ))}
            </select>
          </label>

          <div className="route-symbol">→</div>

          <label>
            Destination student
            <select
              value={end}
              onChange={(event) => setEnd(event.target.value)}
              disabled={students.length === 0}
            >
              {students.map((student) => (
                <option key={student} value={student}>{student}</option>
              ))}
            </select>
          </label>
        </div>

        <button
          className="find-button"
          onClick={handleSearch}
          disabled={loading || students.length === 0}
        >
          {loading ? "Searching..." : "Find shortest path"}
        </button>

        {searched && (
          <div className="path-result">
            {result ? (
              <>
                <div className="result-top">
                  <div>
                    <p className="eyebrow">SHORTEST PATH FOUND</p>
                    <h3>
                      {result.length - 1}{" "}
                      {result.length - 1 === 1 ? "hop" : "hops"}
                    </h3>
                  </div>
                  <span className="result-badge">Java BFS</span>
                </div>

                <div className="path-visual">
                  {result.map((student, index) => (
                    <div className="path-step" key={`${student}-${index}`}>
                      <div className="path-node">
                        <span>{student[0]}</span>
                        <strong>{student}</strong>
                      </div>
                      {index < result.length - 1 && (
                        <div className="path-arrow">→</div>
                      )}
                    </div>
                  ))}
                </div>

                <p className="path-explanation">
                  {start === end
                    ? "You selected the same student. No connections need to be traversed."
                    : `The path from ${start} to ${end} uses ${result.length - 1} direct connection(s).`}
                </p>
              </>
            ) : (
              <div className="no-path">
                <h3>No path exists</h3>
                <p>
                  These students belong to disconnected parts of the network.
                  Try another destination.
                </p>
              </div>
            )}
          </div>
        )}
      </section>

      <div className="find-info">
        <strong>How it works</strong>
        <p>
          Breadth-First Search explores the network one level at a time.
          In an unweighted graph, this finds a path with the fewest edges.
        </p>
        <span>Time complexity: O(V + E)</span>
      </div>

      <p className="demo-note">
        Live mode · Hop count calculated by the Java backend
      </p>
    </div>
  );
}
