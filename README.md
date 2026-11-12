# CSA09-192511053-Assignment4

**Name:** B. Santhosh Kumar  
**Register Number:** 192511053  
**Course:** Programming in Java (CSA09)  
**Assignment:** 4 - CO4, Event-Driven GUI Design  
**Java:** OpenJDK 21 (source uses standard Java AWT, Swing and JDBC APIs)  
**GitHub Repository:** ______________________________________________

## Folder layout
Each `TaskNN` folder contains the source files, `input.txt`, `output.txt`, `notes.md`, a task `.gitignore` and `evidence/`. Task 6 also contains `schema.sql`.

## Compile and run
Run each program from its task folder:

```bash
javac *.java
java FeeWindow
```

Use the matching main class for each task:
- Task01: `java FeeWindow`
- Task03: `java LibrarySearch`
- Task04: `java DrawPad`
- Task05: `java Converter` (also `ConverterFrameListener`, `ConverterNamedInner`, `ConverterAnonymous`)
- Task06: `java EventRegistrationApp`

Task02 is a design task and is answered in `Task02/notes.md`.

## Task 6 database setup
1. Run `Task06/schema.sql` in MySQL.
2. Install MySQL Connector/J.
3. Set `CAMPUS_DB_URL`, `CAMPUS_DB_USER` and `CAMPUS_DB_PASSWORD`.
4. Run `javac -cp .:mysql-connector-j.jar *.java` and `java -cp .:mysql-connector-j.jar EventRegistrationApp` (Windows uses `;` instead of `:`).

The Task 6 Java source compiles against the standard JDBC API. Live database insert, duplicate, filter and export tests require a configured MySQL server and Connector/J.

## Task summary
| Task | Work |
|---|---|
| Task01 | Diagnosed event handling and separated FeeWindow from FeeService |
| Task02 | Event table, component tree, pseudocode, event trace and complexity |
| Task03 | Swing library search screen with nested layout managers |
| Task04 | Mouse listener defect, repaint-safe drawing and completed TODO program |
| Task05 | Frame, named inner, anonymous and lambda event handling versions |
| Task06 | Layered Swing/JDBC registration app, MySQL schema, paging and CSV export |

## Commit history
Commit progressively, one task at a time. Add the actual GitHub repository URL to this README and the report cover page after creating the repository.
