# Airline Reservation Manager

This repository hosts a Java application that creates and manages a lightweight registry of airline reservations. The main workflow generates CSV files, gathers passenger data through dialog windows (`JOptionPane`), and prints a formatted summary with helpful statistics.

## Key Features

- Reservation file creation with name, permission, and structure validation.
- Configurable field structure via enumerations (`Reservation.ReservationFields`).
- Assisted data capture using Swing dialog boxes (`JOptionPane`).
- Interactive prompt to decide how many reservations to capture per session.
- Console log formatted with headers, emojis, and optional filtered statistics.
- Optional headless execution when the `--interactive` argument is omitted.
- Summary statistics with optional filtering by reservation class (`Reservation.ReservationClass`).

## Prerequisites

- Java Development Kit (JDK) 17 or later.
- Operating system capable of running Java Swing interfaces.

## Project Setup

1. Clone the repository and enter the root folder:
   ```bash
   git clone <repository-url>
   cd Tarea-01
   ```
2. Move into the module that contains the source code:
   ```bash
   cd Tarea-01
   ```
3. Create the output directory and compile the Java sources:
   ```bash
   mkdir -p out
   javac -d out $(find src -name "*.java")
   ```
4. Ensure the JDK is installed and that the `JAVA_HOME` environment variable is configured.

## Running the project

From the `Tarea-01/` folder that hosts the source code (i.e., `Tarea-01/Tarea-01` relative to the repository):

```bash
java -cp out Reservation.Main
```

To enable interactive data capture via Swing dialogs add the `--interactive` flag:

```bash
java -cp out Reservation.Main --interactive
```

When launched, the application will ask for:

1. The number of reservation records to create.
2. Seat number, passenger name, and class for each reservation.
3. Optionally, the destination if `Reservation.ReservationAll` is initialized with the four-field mode.

Results are stored in `reservas_maestro.txt`, destination files are generated, and a formatted log is printed to the console. When `reservas_maestro_con_errores.txt` is present, the batch-validation flow is also showcased.

## Repository structure

```text
Tarea-01/
├── LICENSE
├── README.md
├── README_EN.md
├── CHANGELOG.md
├── CHANGELOG_EN.md
├── docs/
│   └── DOCUMENTACION_PROYECTO.md
└── Tarea-01/
    └── src/
        ├── Reservation/
        │   ├── Destinations.java
        │   ├── Main.java
        │   ├── ReservationAll.java
        │   ├── ReservationClass.java
        │   └── ReservationFields.java
        └── Utils/
            └── Utils.java
```

## Recommended workflow

- **Branches**: use feature (`feature/`), hotfix (`hotfix/`), and integration (`develop`, `main`) branches.
- **Commits**: keep action-oriented messages such as `Added: Support for destinations`.
- **Pull Requests**: document scope, local testing, and any noteworthy considerations.

## Extended documentation

The complete project guide with detailed flows, validations, and examples is available at [`docs/DOCUMENTACION_PROYECTO.md`](docs/DOCUMENTACION_PROYECTO.md).

## License

This project is distributed under the MIT License. Refer to the [`LICENSE`](LICENSE) file for details.
