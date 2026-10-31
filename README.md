# Tron 2D Arcade Game

A two-player arcade desktop game built in **Java** utilizing **Swing** for graphics and an embedded **Apache Derby** relational database via **JDBC** for persistent leaderboard tracking.

## Overview

Inspired by the classic arcade game *Tron*, two players navigate the arena leaving light trails behind their light cycles. The objective is to outmaneuver the opponent: colliding with the arena boundaries or any light trail results in defeat. The application tracks match outcomes and persists high scores in an embedded SQL database.

## Features

- **2-Player Local Multiplayer:** Real-time movement and collision detection using custom keyboard controls.
- **Embedded Database (Apache Derby):** Standalone relational database integration via JDBC requiring no external database server setup.
- **Leaderboard / Toplist:** Tracks player match statistics, win counts, and scores with interactive GUI display.
- **Custom Java Swing Engine:** Dynamic 2D rendering loop powered by `javax.swing.Timer` and double-buffered graphics.

## Project Structure

```text
├── src/                    # Java source code
│   ├── Direction.java      # Enum for player movement vectors
│   ├── GamePanel.java      # Main game canvas and rendering logic
│   ├── Main.java           # Application entry point
│   ├── Motor.java          # Vehicle coordinates and trail state
│   ├── Player.java         # Player profile and score attributes
│   ├── TopListWindow.java  # Swing dialog for leaderboard display
│   ├── TronDB.java         # JDBC connection and SQL persistence layer
│   └── TronGUI.java        # Main application window and menu controls
├── lib/                    # External dependencies (Apache Derby embedded driver)
│   └── derby.jar
├── .gitignore              # Build and local database exclusions
└── README.md
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Git

### Build & Run via Command Line

1. **Clone the repository:**
   ```bash
   git clone https://github.com/kokenydani77-oss/Tron-Game-Java.git
   cd Tron-Game-Java
   ```

2. **Compile the source files:**
   ```bash
   javac -cp "lib/derby.jar" -d bin src/*.java
   ```

3. **Launch the game:**
   - **Windows (PowerShell / CMD):**
     ```bash
     java -cp "bin;lib/derby.jar" Main
     ```
   - **Linux / macOS / Git Bash:**
     ```bash
     java -cp "bin:lib/derby.jar" Main
     ```

## Controls

- **Player 1:** `W`, `A`, `S`, `D`
- **Player 2:** Arrow Keys (`↑`, `←`, `↓`, `→`)