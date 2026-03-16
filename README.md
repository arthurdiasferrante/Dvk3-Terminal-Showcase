# DVK-3 Bunker

<<<<<<< HEAD
A Java terminal simulator inspired by the historic Soviet **DVK-3** computer (Диалоговый Вычислительный Комплекс). Demo environment with a virtual file system, boot sequence, and file “decryption.”

=======

A Java terminal simulator inspired by the historic Soviet **DVK-3** computer (Диалоговый Вычислительный Комплекс). Demo environment with a virtual file system, boot sequence, and file “decryption.”


>>>>>>> 18faa1a68dc3fd25f13ee5b9d609717ceba5315a
> **Note:** This is a creative reinterpretation inspired by the DVK-3. It is not an accurate hardware or software emulation.


---


## Features


- **Virtual file system** — Navigate directories (ROOT, BIN, HOME, SYS, PRIVATE) with styled listing
- **Terminal UI** — Built with [Lanterna](https://github.com/mabe02/lanterna) and a retro font (Glass TTY VT220)
- **Boot sequence** — ROM BIOS–style startup with Russian messages and memory tests
- **Encryption simulation** — “Protected” files with layers (SAFE) and a `DECRYPT` command to “stabilize” and read them
- **Classic commands** — `LS`, `CD`, `CAT`, `CLEAR`, `SHUTDOWN`, `HELP`, plus shortcuts like `ROOT` and `CHECK`


---


## Requirements


- **Java 8+** (Java 11 or later recommended)
- Dependency: **Lanterna** (via your IDE or project dependency manager)


---


## How to run


1. Clone the repo and open the `Dvk-3 snippet` folder as a Java project (e.g. in IntelliJ IDEA).
2. Add the Lanterna library to the classpath (JAR or Maven/Gradle, depending on your setup).
3. Run the main class: `core_logic.Main`.


The terminal window will open with the boot sequence; then you can type commands.


---


## Available commands


<<<<<<< HEAD
| Command         | Description                                       |
|-----------------|---------------------------------------------------|
| `LS` / `DIR`    | List files and folders in the current directory   |
| `CD [DIR]`      | Enter a directory (use `CD ..` to go back)        |
| `ROOT` / `/`    | Go to the root directory                          |
| `CAT [FILE]`    | Read a text file’s contents                       |
| `DECRYPT ...`   | Decrypt protected files (see `HELP` for syntax)   |
| `STABILIZE ...` | Stabilize protected files (see `HELP` for syntax) |
| `CHECK [FILE]`  | Check a file’s “frequency” (used with DECRYPT)    |
| `CLEAR`         | Clear the screen                                  |
| `HELP` / `?`    | List commands                                     |
| `SHUTDOWN`      | End the session                                   |
=======
| Command        | Description |
|----------------|-------------|
| `LS` / `DIR`   | List files and folders in the current directory |
| `CD [DIR]`     | Enter a directory (use `CD ..` to go back) |
| `ROOT` / `/`   | Go to the root directory |
| `CAT [FILE]`   | Read a text file’s contents |
| `DECRYPT ...`  | Decrypt protected files (see `HELP` for syntax) |
| `STABILIZE ...`| Stabilize protected files (see `HELP` for syntax) |
| `CHECK [FILE]` | Check a file’s “frequency” (used with DECRYPT) |
| `CLEAR`        | Clear the screen |
| `HELP` / `?`   | List commands |
| `SHUTDOWN`     | End the session |
>>>>>>> 18faa1a68dc3fd25f13ee5b9d609717ceba5315a


**Tip:** Go to the `HOME` folder for contacts and projects; in `HOME/PRIVATE` there are files that require `DECRYPT`.


---


## Project structure (overview)


```
Dvk-3 snippet/
├── src/core_logic/
│   ├── Main.java                 # Entry point, Lanterna terminal and font
│   ├── controller/               # GameController, ProcessCommands
│   ├── models/                   # System, filesystem, crypto, rules
│   │   ├── filesystem/           # VirtualFile, VirtualFolder, Dvk3FileManager
│   │   ├── system/               # Dvk3System, Logger, TaskManager, DocReader
│   │   ├── physical/             # Dvk3Core
│   │   └── utils/                # CryptoUtils
│   └── views/                    # BootSequence, TerminalViewer, DocumentWindowViewer
└── src/assets/data/              # readme.txt, contacts, secret.txt, etc.
```


---


## License and credits


<<<<<<< HEAD
© 2026 Arthur Dias Ferrante. All rights reserved.
=======
© 2026 Arthur Dias Ferrante. All rights reserved. 
>>>>>>> 18faa1a68dc3fd25f13ee5b9d609717ceba5315a
Unauthorized distribution or reverse engineering of the DVK-3 kernel simulator logic is strictly prohibited.


---


*DVK-3 Bunker — a trip to a terminal inspired by the DVK-3.*



