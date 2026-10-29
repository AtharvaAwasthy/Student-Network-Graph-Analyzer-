# 🌐 LinkedIn Analyzer - Graph Engine

*Read this in other languages: [English](README.en.md)*

Um motor de análises e recomendações para redes profissionais, utilizando a Teoria dos Grafos para resolver problemas de conexão, otimização e proximidade.

Este projeto prático foi desenvolvido para a disciplina de **Teoria dos Grafos**, ministrada pelo professor **Douglas Meneses**, no curso de Ciência da Computação do **Unipê**. A fundação estrutural do grafo e a base de alguns algoritmos foram herdadas do material didático oficial da disciplina, sendo posteriormente refatoradas, otimizadas e expandidas pela equipe para construir o motor de análise atual.

---

## 🎥 Apresentação do Projeto

> 📺 **Clique aqui para assistir ao vídeo explicativo no YouTube**  
> *[(Projeto LinkedIn Analyzer: Motor de Recomendação com Grafos | Unipê)](https://youtu.be/ILRbmSqzFgk)*

---

## 🚀 Funcionalidades e Algoritmos

O motor de análise, encapsulado na classe `LinkedInAnalyzer`, resolve diferentes problemas de redes sociais profissionais utilizando algoritmos clássicos da Teoria dos Grafos.

### Mapeamento de Sub-redes (DFS — *Depth-First Search*)
Realiza uma varredura completa da rede para identificar grupos de usuários conectados entre si, mas isolados dos demais componentes da rede.

### Grau de Separação (BFS — *Breadth-First Search*)
Calcula a menor distância entre dois perfis considerando apenas o número de conexões (saltos), desconsiderando os pesos das arestas.

### Rota de Maior Afinidade (Algoritmo de Dijkstra)
Determina o caminho de menor custo entre dois usuários, priorizando conexões com maior afinidade (menor peso), garantindo a rota ótima entre origem e destino.

### Motor de Sugestões de Conexão
Identifica conexões de segundo grau ("amigos de amigos"), sugerindo novos contatos ordenados pela quantidade de amigos em comum.

---

## 🏗️ Arquitetura do Projeto

O projeto foi desenvolvido utilizando princípios de **Programação Orientada a Objetos**, **Clean Code** e boas práticas de organização de software.

### Componentes principais
- `GraphAlgorithm` — Interface comum para todos os algoritmos de grafos implementados.
- `DFS` — Busca em profundidade.
- `BFS` — Busca em largura.
- `Dijkstra` — Algoritmo de menor caminho ponderado.
- `LinkedInAnalyzer` — Motor responsável pelas análises da rede.
- `ConnectionSuggestion` — Classe focada exclusivamente na lógica de recomendação de contatos.
- `PathResult` e `SuggestionResult` — Records e modelos imutáveis utilizados para encapsular os retornos das análises.

### Estruturas de dados utilizadas
- `PriorityQueue` para o algoritmo de Dijkstra;
- `ArrayDeque` (como implementação principal de `Queue`) para o BFS;
- `LinkedHashSet`, `HashSet` e `HashMap` para otimização das buscas, controle de vértices visitados e reconstrução de caminhos.

---

## 📁 Estrutura do Projeto

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

## ⚙️ Como Executar

O projeto já possui um cenário de demonstração configurado para validar todas as funcionalidades implementadas.

1. Clone o repositório:

```bash
git clone https://github.com/GuilhermeParnaibaNunes/LinkedInAnalyzer
```

2. Abra o projeto em sua IDE de preferência (IntelliJ IDEA, Eclipse, VS Code etc.).

3. Execute a classe principal:

```text
br.com.unipe.linkedingraph.LinkedInApp
```

4. Acompanhe os resultados das análises sendo exibidos no console.

---

## 🛠️ Tecnologias Utilizadas

- Java 21+
- Gradle
- Git
- GitHub
- IntelliJ IDEA

---

## 👥 Equipe

- **Guilherme Parnaíba Nunes** — RGM 38887479
- **Ikharo Cardoso Queiroz da Silva** — RGM 38421313
- **Sebastião Ricardo Felix Dos Santos Silva** — RGM 38966875
- **Vinicius Emanuel de Souza Nascimento** — RGM 38339188

---

## 🙏 Agradecimentos

A estrutura base do grafo utilizada neste projeto foi adaptada do material didático disponibilizado pelo professor **Douglas Meneses** durante a disciplina de **Teoria dos Grafos**, servindo como ponto de partida para o desenvolvimento desta aplicação. Agradecemos pelas aulas e pelos ensinamentos transmitidos tanto nos termos da disciplina quanto fora dela.

---

## 📄 Licença

Este projeto possui finalidade exclusivamente **acadêmica** e foi desenvolvido para a disciplina de **Teoria dos Grafos** do curso de Ciência da Computação do **Unipê**. Distribuído sob a licença MIT.
