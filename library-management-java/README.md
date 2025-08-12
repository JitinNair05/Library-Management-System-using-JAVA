Library Management System (Java console app)
==============================================

What this is
-    A console-based Java application (no frameworks) that models a small library system.
-    Features: add/list/search books, borrow/return books, persistent storage to CSV files, basic validation, and sorting/searching operations.
-    Organized in packages: `app`, `model`, `service`, `util`

How to build & run (Linux / macOS)
-----------------------------------
```bash
# compile
javac -d out $(find src -name "*.java")

# run
java -cp out app.Main
```

How to build & run (Windows PowerShell)
---------------------------------------
```powershell
# compile
javac -d out (Get-ChildItem -Recurse -Filter *.java).FullName

# run
java -cp out app.Main
``

Data files
- `data/books.csv` and `data/members.csv` are used for persistence. The app will create them if missing.

Notes
- Plain Java (compatible with Java 11+). No external libraries.
- The project includes helper scripts `run.sh` and `run.bat` to compile+run quickly.
