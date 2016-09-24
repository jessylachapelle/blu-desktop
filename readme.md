# Banque de Livres Usagés (BLU)

## Introduction
This program allows to run a used books sell. It handles an inventory of books and other school items as well as a member system with money return, student-parent discounts and reservations.

## About the BLU
This project started to refresh the current, but outdated, system in place at my college for our used book sell. At the beginning of each semester students can come and drop their old books to sell them. All the money is given back to the students who sold the books. This is a great way to recycle books and to diminish the costs of higher education.

For more information on the BLU, please refer to the [Student Council (AÉCS) website](http://aecs.info)

## Stack

* Java 8 (Platform)
* JavaFx 2.0 (UI)
* Maven (Management)
* PHP >= 5.6 (API) ([API repo](https://github.com/katima-g33k/blu-api))
* MySQL (Database) ([Database repo](https://github.com/katima-g33k/blu-db))


## Getting Started
This guide assumes you have [Java 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html), [JavaFX](http://docs.oracle.com/javafx/2/installation/jfxpub-installation.htm), [Maven](https://maven.apache.org/) and [IntelliJ](https://www.jetbrains.com/idea/) installed and configured on your workstation

### Setup setup project
1. `git clone git@github.com:katima-g33k/blu-desktop.git`
2. Open Intellij, select **Import project** and choose repo folder
3. Choose to import a Maven project
4. Follow the steps in the import wizard
5. First run
	* In `src/main/java` directory right click on **BLU.java** and select **Run 'BLU.main()'**
6. To be able to use the program, you must also installed the [Database](https://github.com/katima-g33k/blu-db) and the [API](https://github.com/katima-g33k/blu-api)

## Documentation

No documentation is available at the moment, working on generating JavaDoc

## Contributing

**Never** commit directly on master, instead use branches and pull requests with contributors as reviewers.


## Contributors

* [Jessy Lachapelle](https://github.com/katima-g33k/)

### Special thanks
This started as a school project, so I would like to thank my teamates who aided in building the first iteration of the program from which this version is derived.

* [Alizée Fournier](https://github.com/Nightneko)
* [Dereck Pouliot](https://github.com/oXceeDo)
* [Marc Dupuis](https://github.com/Nathuu)
