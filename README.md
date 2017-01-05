vigorBackup
===========
**Disclaimer: This program is not endorsed by Draytek. Use it at your own risk.**

Draytek has their own solution for orchestration wich includes backing up configuration files, the VigorACS. Since it's a tool that does many other things (and it's sold separately from the routers) I decided to build something that could do backups only, having in mind that the backups needed to be available to field technicians (so in our case we needed sharepoint support) but also with support to backing up to a folder, just in case. We also needed to monitor the backups, so the program has an e-mail report support. 
Since this tool is built to be used by administrators it has no GUI, and it's managed entirely by a config file (and the router list is stored in a csv).
The source code is documented with full javadocs. 

## How it works

The tool simply recreates the routines that a human would use to make a configuration backup. It executes the same functions already present in the routers, so there's no backup voodoo to cause potential restore problems.

## Supported routers
1. VIGOR 2925
2. VIGOR 2910
3. VIGOR 3300
4. VIGOR 2920
5. VIGOR 3200
6. VIGOR 3220

## Features

**Backup to Filesystem:** Backups each router to a different folder inside a defined path. The folders are separated by site name (defined in the router list).

**Backup to WebDAV server:** Backups can be saved to a webdav server. The program was tested on sharepoint 2010+ and have the same functions as the Filesystem backup.

**Backup history:** The system can be configured to save backups for a defined amount of runs and the older backups are deleted only if the last execution was successfully done to avoid cases like when a router is inacessible (and thus on a 'error' state) and because of that all backup copies are lost.

**Email reporting:** At each run the system can send an e-mail message with the router list and the backup status.

**Multiple WAN links per router supported:** If you have more than one internet link connected to a router the system can be configured to try to backup from any of them, ensuring that the backup is made if atleast one link is alive.

**Crossplataform support:** The project is made with Java 1.8+ and was run on Linux (Centos and Ubuntu distros) as well as Windows Desktops and Servers. It was originally developed to run on Linux, saving the backups to a local directory and a sharepoint library.

## How to install (binary)
1. Download the binary release (the .jar file) and place it in a folder;
2. Fix your java path to point it to a java 1.8+ installation (or use the java.exe path directly) and execute in a command line "java.exe" -jar vigorBackup.jar";
3. The first time the program is executed it creates a new configuration file in the same folder the .jar is. You need to edit it to suit your needs;
4. When you execute the program again it will look for the router list file (a csv). If it's not found it will try to create a sample file for you to customize. The router models are currently set by a index that need to be inserted into the csv file. For example, to define a router as Vigor 3300 we would place the number 2 in the csv file. The codes: VIGOR_2925=0 VIGOR_2910=1 VIGOR_3300=2 VIGOR_2920=3 VIGOR_3200=4 VIGOR_3220=5;
5. After the config file and the router list are in place, simply create a cron task or windows scheduler task to automate the program's execution.

## How to build (eclipse instructions)
1. Open eclipse and use the "Import from git" option;
2. Paste this repo in the fields;
3. Import the project. Maven will start to download the project's dependencies, this can take some time;
4. Create a new runtime configuration (RUN -> Run configurations -> Maven build or use whatever way you like) with the goal 'package' and then run it;
5. A shaded jar will be built, with all the dependencies inside it.

## How to add new routers in the code
1. Create a new Router Class that will extend the BaseDownloader class. If you're using the IDE it will ask you to implement the downloadBackupFromUrl abstract method, and it's in this method that you need to put the logic that will login to the router and download the backup file. The method receives a download addresses and returns true if the backup is successfull. At the end when the file has been downloaded you need to setDownloadedBackup(backupFileThatHasBeenDownloaded) to allow the rest of the program to manipulate the file;
2. Edit the ERouterModels class and add a new model and index number in the enum. In the bottom of the same file there's a factory to return the correct classes based on the model indexes, you need to add the relevant lines to it (just add a new case pointing to the new objects);
3. Build and test!
Note: Various routers use the same method to autenticate and download firmwares. Sometimes it's needed to only copy a routers code to a new router class and edit the FILE_DOWNLOAD_STRING to make it work (don't forget to add the objects to ERouterModels!).

### TODO
1. Create both the configuration files and the csv file in the first run;
2. Add SSL validation;
3. Create a local GUI or something web based. This will problably require some major overhaul in the read router list routines if a DB is used;
4. Add a way to send the backups by e-mail (the files are usually 10Kb each).

Enjoy!
