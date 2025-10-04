# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/) and the project aims to follow [Semantic Versioning](https://semver.org/).

## [Unreleased]
### Changed
- Improved code readability with comprehensive JavaDoc and reorganized usage examples.
- Updated public documentation (README files and full guide) to reflect the current workflows.

## [3.0.0] - 2025-10-04
### Added
- If the reservations are invalid, no destination files are created and they are reported in the logs, in addition to displaying the number of valid records.

## [2.3.0] - 2025-10-04
### Added
- Create destination files only if there are valid records; otherwise, report them in the logs.

## [2.2.0] - 2025-10-04
### Added
- Records with errors in the file `reserves_maestro_con_errores.txt`.

## [2.1.0] - 2025-10-04
### Added
- Implemented the creation of `reserves_maestro_con_errores.txt` file.

## [2.0.0] - 2025-10-04
### Added
- Display the reservations for a specific destination file.

## [1.5.0] - 2025-10-01
### Changed
- Code refactorization to improve readability and maintenance.
- Use of functions to modulate the logic of creation and writing of files.
- Improvement in error management and input validations.

## [1.4.0] - 2025-10-01
### Added
- Creates files based on the destinations of each reservation `reserves_<destination>.txt`.
- Copies reservation records to the corresponding file according to the destination.

## [1.3.0] - 2025-09-30
### Added
- Implementation of records in the file `reserves_maestro.txt`.
- Maximum characters in the fields.
- Better error control.

## [1.2.0]
### Added
- Headers on `reserves_maestro.txt` file.

## [1.1.0]
### Added
- Implemented the creation of `reserves_maestro.txt` file.

## [1.0.0] - 2025-09-29
### Added
- Rich console log with aggregated statistics for every reservation.
- Optional class-based filtering when rendering the log.
### Fixed
- Adjusted the reservation output to avoid misalignment in the console view.

## [0.2.0] - 2025-09-29
### Added
- Numeric spinner dialog to define how many reservations to capture per session.
- Clarifying comments in the codebase to improve maintainability.

### Removed
- Unused imports discovered during review.

## [0.1.0] - 2025-09-28
### Added
- Field and class enumerations to standardize the reservation file structure.
- Validated creation of output files with configurable headers.
- Assisted data capture for passengers and persistence to `reservas.txt`.
- Initial documentation and `.gitignore` setup.

> Note: Create Git tags in the repository to enable comparison links between versions.
