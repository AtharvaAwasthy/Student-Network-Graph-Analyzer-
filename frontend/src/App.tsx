
import { useState } from "react";
import "./App.css";
import NetworkGraph from "./NetworkGraph";
import FindConnections from "./FindConnections";
import Algorithms from "./Algorithms";

const students = [
  { name: "Aarav", connections: 4, group: "Computer Science" },
  { name: "Diya", connections: 3, group: "Computer Science" },
  { name: "Kabir", connections: 2, group: "Information Technology" },
  { name: "Meera", connections: 3, group: "Computer Science" },
  { name: "Rohan", connections: 4, group: "Information Technology" },
  { name: "Ishita", connections: 2, group: "Computer Science" },
];

function App() {
  const [search, setSearch] = useState("");
  const [activeTab, setActiveTab] = useState("Overview");

  const filteredStudents = students.filter((student) =>
    student.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon">C</div>
          <div>
            <h2>CampusConnect</h2>
            <p>Student Network Analyzer</p>
          </div>
        </div>

        <p className="nav-label">WORKSPACE</p>

        {["Overview", "Student Network", "Find Connections", "Algorithms"].map(
          (tab) => (
            <button
              key={tab}
              className={`nav-item ${activeTab === tab ? "active" : ""}`}
              onClick={() => setActiveTab(tab)}
            >
              <span>
                {{
                  Overview: "◫",
                  "Student Network": "◎",
                  "Find Connections": "⌕",
                  Algorithms: "⌘",
                }[tab]}
              </span>
              {tab}
            </button>
          )
        )}

        <div className="sidebar-bottom">
          <div className="status-dot" />
          <span>Java graph engine · Connected</span>
        </div>
      </aside>

      <main className="main-content">
      {activeTab === "Student Network" ? (
        <NetworkGraph />
      ) : activeTab === "Find Connections" ? (
        <FindConnections />
      ) : activeTab === "Algorithms" ? (
        <Algorithms />
      ) : (
          <>
            <header className="topbar">
              <div>
                <p className="eyebrow">STUDENT NETWORK / ANALYTICS</p>
                <h1>{activeTab}</h1>
              </div>

              <div className="profile">
                <div className="avatar">CC</div>
                <div>
                  <strong>Campus Admin</strong>
                  <span>Network overview</span>
                </div>
              </div>
            </header>

            {activeTab === "Overview" ? (
              <>
                <section className="welcome">
                  <div>
                    <p className="eyebrow">YOUR CAMPUS, CONNECTED</p>
                    <h2>Understand your student network.</h2>
                    <p>
                      Explore connections, discover communities, and analyze
                      paths between students using graph algorithms.
                    </p>
                  </div>

                  <div className="welcome-art" aria-hidden="true">
                    <span className="node node-a">A</span>
                    <span className="node node-b">D</span>
                    <span className="node node-c">R</span>
                    <span className="node node-d">I</span>
                    <span className="node node-e">K</span>
                    <span className="edge edge-a" />
                    <span className="edge edge-b" />
                    <span className="edge edge-c" />
                    <span className="edge edge-d" />
                    <span className="edge edge-e" />
                  </div>
                </section>

                <section className="stats-grid">
                  <article className="stat-card">
                    <span>Total students</span>
                    <strong>11</strong>
                    <small>Registered graph vertices</small>
                  </article>

                  <article className="stat-card">
                    <span>Connections</span>
                    <strong>10</strong>
                    <small>Relationships between students</small>
                  </article>

                  <article className="stat-card">
                    <span>Communities</span>
                    <strong>3</strong>
                    <small>Connected components</small>
                  </article>

                  <article className="stat-card">
                    <span>Algorithms</span>
                    <strong>3</strong>
                    <small>BFS · DFS · Dijkstra</small>
                  </article>
                </section>

                <section className="content-grid">
                  <article className="panel student-panel">
                    <div className="panel-heading">
                      <div>
                        <h3>Student directory</h3>
                        <p>Explore students in the network</p>
                      </div>
                      <span className="pill">Demo data</span>
                    </div>

                    <input
                      className="search-input"
                      placeholder="Search students by name..."
                      value={search}
                      onChange={(event) => setSearch(event.target.value)}
                    />

                    <div className="student-list">
                      {filteredStudents.map((student) => (
                        <div className="student-row" key={student.name}>
                          <div className="student-avatar">
                            {student.name.charAt(0)}
                          </div>

                          <div className="student-info">
                            <strong>{student.name}</strong>
                            <span>{student.group}</span>
                          </div>

                          <span className="connection-count">
                            {student.connections} links
                          </span>
                        </div>
                      ))}

                      {filteredStudents.length === 0 && (
                        <p className="empty-state">No students found.</p>
                      )}
                    </div>
                  </article>

                  <article className="panel">
                    <div className="panel-heading">
                      <div>
                        <h3>Graph algorithms</h3>
                        <p>Core analysis capabilities</p>
                      </div>
                    </div>

                    <div className="algorithm-row">
                      <div className="algorithm-icon purple">B</div>
                      <div>
                        <strong>Breadth-First Search</strong>
                        <p>Explore connections level by level</p>
                      </div>
                      <span className="algorithm-tag">BFS</span>
                    </div>

                    <div className="algorithm-row">
                      <div className="algorithm-icon blue">D</div>
                      <div>
                        <strong>Depth-First Search</strong>
                        <p>Traverse deeper into the network</p>
                      </div>
                      <span className="algorithm-tag">DFS</span>
                    </div>

                    <div className="algorithm-row">
                      <div className="algorithm-icon green">↗</div>
                      <div>
                        <strong>Dijkstra's Algorithm</strong>
                        <p>Find minimum-cost paths</p>
                      </div>
                      <span className="algorithm-tag">PATH</span>
                    </div>

                    <div className="note">
                      <strong>Next step</strong>
                      <p>
                        Connect this interface to your Java graph engine to
                        run algorithms on live project data.
                      </p>
                    </div>
                  </article>
                </section>

                <footer>CampusConnect · Student Network Graph Analyzer</footer>
              </>
            ) : (
              <section className="panel">
                <h3>{activeTab}</h3>
                <p>This section is coming next.</p>
              </section>
            )}
          </>
        )}
      </main>
    </div>
  );
}

export default App;
