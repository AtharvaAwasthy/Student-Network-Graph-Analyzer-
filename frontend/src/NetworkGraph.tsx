
import { useEffect, useMemo, useState } from "react";
import {
  Background,
  Controls,
  ReactFlow,
  Handle,
  Position,
  type Node,
  type Edge,
  type NodeProps,
} from "@xyflow/react";
import "@xyflow/react/dist/style.css";
import "./NetworkGraph.css";

type Student = {
  name: string;
  group: string;
  connections: number;
};

type ApiStudent = {
  id: string;
  label: string;
};

type ApiConnection = {
  source: string;
  target: string;
  weight: number;
};

type NetworkResponse = {
  students: ApiStudent[];
  connections: ApiConnection[];
  studentCount: number;
  connectionCount: number;
  groups: number;
};

const positions = [
  { x: 280, y: 40 },
  { x: 100, y: 140 },
  { x: 30, y: 300 },
  { x: 270, y: 250 },
  { x: 470, y: 140 },
  { x: 430, y: 330 },
  { x: 600, y: 260 },
  { x: 740, y: 350 },
  { x: 850, y: 100 },
  { x: 1020, y: 210 },
  { x: 1160, y: 340 },
];

function StudentNode({ data, selected }: NodeProps) {
  const student = data as Student;

  return (
    <div className={`graph-student ${selected ? "selected" : ""}`}>
      <Handle type="target" position={Position.Top} />
      <div className="graph-student-avatar">{student.name[0]}</div>
      <strong>{student.name}</strong>
      <span>{student.group}</span>
      <Handle type="source" position={Position.Bottom} />
    </div>
  );
}

const nodeTypes = { student: StudentNode };

export default function NetworkGraph() {
  const [network, setNetwork] = useState<NetworkResponse | null>(null);
  const [selected, setSelected] = useState("Aarav");
  const [error, setError] = useState("");

  useEffect(() => {
    const controller = new AbortController();

    async function loadNetwork() {
      try {
        setError("");

        const response = await fetch(
          "http://localhost:8080/api/network",
          { signal: controller.signal }
        );

        if (!response.ok) {
          throw new Error(`API request failed (${response.status})`);
        }

        const data: NetworkResponse = await response.json();
        setNetwork(data);

        if (!data.students.some((student) => student.id === selected)) {
          setSelected(data.students[0]?.id ?? "");
        }
      } catch (err) {
        if (err instanceof Error && err.name === "AbortError") return;

        setError(
          "Could not load the student network. Make sure the Java API is running on port 8080."
        );
      }
    }

    void loadNetwork();

    return () => controller.abort();
  }, []);

  const students = useMemo<Student[]>(() => {
    if (!network) return [];

    return network.students.map((student) => ({
      name: student.id,
      group: "Student Network",
      connections: network.connections.filter(
        (edge) => edge.source === student.id || edge.target === student.id
      ).length,
    }));
  }, [network]);

  const nodes = useMemo<Node[]>(() => {
    return students.map((student, index) => ({
      id: student.name,
      type: "student",
      position: positions[index] ?? {
        x: 100 + (index % 4) * 240,
        y: 100 + Math.floor(index / 4) * 180,
      },
      data: student,
    }));
  }, [students]);

  const edges = useMemo<Edge[]>(() => {
    if (!network) return [];

    return network.connections.map((connection, index) => ({
      id: `edge-${index}`,
      source: connection.source,
      target: connection.target,
      type: "smoothstep",
      label: String(connection.weight),
      style: { stroke: "#8f7af5", strokeWidth: 2 },
      labelStyle: { fill: "#c8c2ff", fontSize: 12 },
      labelBgStyle: { fill: "#202133" },
    }));
  }, [network]);

  const student = students.find((item) => item.name === selected);

  const neighbors = useMemo(() => {
    if (!network) return [];

    return network.connections
      .filter(
        (edge) => edge.source === selected || edge.target === selected
      )
      .map((edge) =>
        edge.source === selected ? edge.target : edge.source
      );
  }, [network, selected]);

  if (error) {
    return (
      <div className="network-page">
        <div className="network-intro">
          <div>
            <p className="eyebrow">GRAPH EXPLORER</p>
            <h2>Student Network</h2>
          </div>
        </div>
        <p role="alert">{error}</p>
        <button onClick={() => window.location.reload()}>
          Retry connection
        </button>
      </div>
    );
  }

  if (!network || !student) {
    return (
      <div className="network-page">
        <p className="eyebrow">GRAPH EXPLORER</p>
        <h2>Student Network</h2>
        <p>Loading student network from Java backend...</p>
      </div>
    );
  }

  return (
    <div className="network-page">
      <div className="network-intro">
        <div>
          <p className="eyebrow">GRAPH EXPLORER</p>
          <h2>Student Network</h2>
          <p>Select a student node to explore their direct connections.</p>
        </div>

        <div className="network-count">
          <strong>{network.studentCount}</strong>
          <span>Students</span>
          <strong>{network.connectionCount}</strong>
          <span>Connections</span>
        </div>
      </div>

      <div className="network-layout">
        <div className="graph-canvas">
          <ReactFlow
            nodes={nodes}
            edges={edges}
            nodeTypes={nodeTypes}
            fitView
            fitViewOptions={{ padding: 0.2 }}
            minZoom={0.3}
            maxZoom={1.5}
            onNodeClick={(_, node) => setSelected(node.id)}
          >
            <Background color="#323448" gap={24} />
            <Controls />
          </ReactFlow>
        </div>

        <aside className="student-details">
          <p className="eyebrow">SELECTED STUDENT</p>
          <div className="detail-avatar">{student.name[0]}</div>
          <h3>{student.name}</h3>
          <p className="detail-group">{student.group}</p>

          <div className="detail-stat">
            <span>Direct connections</span>
            <strong>{neighbors.length}</strong>
          </div>

          <h4>Connected students</h4>

          {neighbors.length ? (
            neighbors.map((name) => (
              <button
                className="neighbor"
                key={name}
                onClick={() => setSelected(name)}
              >
                <span className="neighbor-avatar">{name[0]}</span>
                <span>{name}</span>
                <span className="neighbor-arrow">↗</span>
              </button>
            ))
          ) : (
            <p>No direct connections.</p>
          )}
        </aside>
      </div>

      <p className="demo-note">
        Live graph · Data supplied by the Java backend
      </p>
    </div>
  );
}
