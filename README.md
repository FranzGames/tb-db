# README #

### What is tb-db? ###

* tb-db is a text-based database. That uses CSV files to store each row of the database
* Current Version: 0.5

### Why was tb-db created? ###

* To avoid data format locking.
  * Since it is CSV format. It is pure text.
  * Most databases store their data in a specialized format, so:
    * Assume that the project exists in the future.
    * Require that you are able to install and run the software
* To open the data to searching outside of the database
  * Unix was built on little utilities to search and process text files, so saving as text files allows:
    * the use of external tools to query the data.
    * does not require a running database process to search the data. 

### How do I get set up? ###

* Java Setup:
   * Grab the project and build it
   * Unit Test that are included can be run
   * **Note:** This will be made available via Maven in the future
* Perl Setup:
   * Grab the Perl Module
   * Add it to the Perl include path
   * **Note:** This will be made available via CPAN in the future

### Contribution guidelines ###

* Write tests for:
  * Any bugs you find (this will help make sure they don't get broken again later)
  * Any new feature that is added
* Code review will be done by the project maintainer.

### Who do I talk to? ###

* This repo is currently owned and maintained by Paul Franz of Franz Games, LLC. If you want to contribute please feel free to contact me.

### What kind of license? ###

* Haven't decided but leaning towards BSD or Apache.