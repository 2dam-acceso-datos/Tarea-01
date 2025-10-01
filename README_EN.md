# Airline Reservation Manager

This repository hosts a console-based Java application that creates and manages a lightweight registry of airline reservations. The main workflow generates a CSV file (`reservas.txt`), gathers passenger data through dialog windows, and prints a formatted summary with helpful statistics.

## Key Features

- Reservation file creation with name validation and permission handling.
- Configurable field structure via enumerations (`Reservation.ReservationFields`).
- Assisted data capture using Swing dialog boxes (`JOptionPane`).
- Interactive prompt to decide how many reservations to capture per session.
- Console log formatted with headers and emojis for readability.
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
3. Ensure the JDK is installed and that the `JAVA_HOME` environment variable is configured.

## Running the project

From the `Tarea-01/` folder that hosts the source code (i.e., `Tarea-01/Tarea-01` relative to the repository):

```bash
javac -d out src/*.java
java -cp out Reservation.Main
```

When launched, the application will ask for:

1. The number of reservation records to create.
2. Seat number, passenger name, and class for each reservation.
3. Optionally, the destination if `Reservation.ReservationAll` is initialized with the four-field mode.

Results are stored in `reservas.txt`, and a formatted log is printed to the console.

## Repository structure

```text
Tarea-01/
├── LICENSE
├── README.md
├── README_EN.md
├── CHANGELOG.md
├── CHANGELOG_EN.md
└── Tarea-01/
    └── src/
        ├── Reservation.Main.java
        ├── Reservation.ReservationAll.java
        ├── Reservation.ReservationClass.java
        └── Reservation.ReservationFields.java
```

## Recommended workflow

- **Branches**: use feature (`feature/`), hotfix (`hotfix/`), and integration (`develop`, `main`) branches.
- **Commits**: keep action-oriented messages such as `Added: Support for destinations`.
- **Pull Requests**: document scope, local testing, and any noteworthy considerations.

## Contributing

1. Fork the repository.
2. Create a descriptive working branch.
3. Implement the changes and run local validations.
4. Open a pull request detailing the motivation and impact.

## License

This project is distributed under the MIT License. Refer to the [`LICENSE`](LICENSE) file for details.
