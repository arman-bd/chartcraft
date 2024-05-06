# ChartCraft
ChartCraft is a Java and Scala-based Integrated Development Environment (IDE) designed for creating graphical representations such as charts with embedded text. Developed as part of the Software Correctness course at Aarhus University, this application provides a platform for users to input graphical commands and view their output dynamically on a Euclidean plane.

## Features
**Euclidean Graphics Viewer:** Features a viewer that displays graphical objects with a background grid.

**Graphics Language Editor:** An editor that allows users to input and modify graphical commands which directly influence the viewer without intermediate processing.

**Dynamic Drawing with Bounding Box:** Implements a bounding box within which all drawings are clipped, enhancing the control over visible graphics.

**Support for Multiple Shapes and Colors:** Users can draw lines, rectangles, circles, and embed text, with support for multiple colors and fill options.

**Real-time Highlighting:** Highlights the currently drawn object, providing immediate visual feedback. ( in progress )

## Prerequisites
To run ChartCraft, you must have the following installed:

* Java JDK 11 or higher
* Scala SDK 2.13 or higher
* Simple Build Tool (SBT) for compiling Scala code

## Installation
To get started with ChartCraft, clone the repository and compile the source code:
```bash
git clone https://github.com/arman-bd/chartcraft.git
cd chartcraft
sbt compile
```

## Running the Application
To launch the ChartCraft IDE, execute the following command:

```bash
sbt run
```

The application window should open, divided into a graphical viewer on the left and a graphics command editor on the right.

## Usage
Input graphical commands in the editor on the right side of the application. Example commands to draw a basic bar chart and pie chart are as follows:

```text
(BOUNDING-BOX (1 1) (500 500))
(LINE (150 100) (150 200))
(LINE (150 100) (100 150))
(LINE (150 100) (200 150))
(LINE (230 120) (200 150))
(DRAW red (TEXT-AT (230 120) "@"))
(CIRCLE (150 75) 30)
(FILL red (CIRCLE (135 75) 10))
(FILL red (CIRCLE (165 75) 10))
(FILL black (CIRCLE (148 90) 3))
(FILL black (CIRCLE (152 90) 3))
(LINE (150 200) (200 250))
(LINE (150 200) (100 250))
(TEXT-AT (70 290) "ChartCraft is Awesome!!")
(DRAW red (TEXT-AT (110 320) "YAY!!"))
(DRAW green (TEXT-AT (160 320) "YAY!!"))
```
## Contributing
Contributions are welcome. Please fork the repository, create a feature branch, and submit a pull request for review.

## Testing
Testing methodologies include unit tests for Scala components and integration tests for the Java GUI. Test cases focus on graphical accuracy, command parsing, and user interaction.

## Documentation
A detailed report is available that describes the architecture, usage, and testing strategies employed during development.

## Authors
[Arman](https://github.com/arman-bd) and Team

## License
This project is licensed under the MIT License.

## Acknowledgments
[Aarhus University, Denmark](https://au.dk) - For providing the academic environment and guidance necessary for this project.