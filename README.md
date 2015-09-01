Git Archive Manager
========================

GAM is a custom tool used for automating the creation of an archive of (1) deltas between
two difference branches OR (2) the deltas between two different commits within the same
branch.  The deltas are then packaged up into a zip file with a folder structure that
matches the following.

```
From_Author_ProjectName.zip [Root Zip file]
    - From_Author [Folder]
        - ProjectName [Folder]
            - YYYYMMDD  [Folder]
                - app.zip [Zip file that holds all dynamic files] 
                - web.zip [Zip file that holds all static asset files]
                - README.txt [Provides a listing of deltas within the zip files]
                - XYZ_Update.sql  [If applicable, holds query updates]
```           
 
## Requirements

* JDK 7
* Git

## Configuration

In order to successfully generate a zip package, you must create a `gam.properties`  file and place it in the direct.  The following properties should
be added to the properties file.

```
# Authoring Team/Agency  - Used in the name of the two top level directories within the package
gam.projectAuthor=CuddlefishCo
# Project Name - Used in the name of the top level and 3rd level directory within the package
gam.projectName=CuddleProject
# The directory where static files are located (directory will be omitted from final package)
gam.webPrefix=cuddle-web/web/
# The directory where Java related app files are located (directory will be omitted from final package)
gam.appPrefix=cuddle-web/app/
# The branch to be used when doing a diff between two commits.  Will not be used when doing a diff between different branches
gam.diffBranch=
# The 1st parameter used when completing a git diff.  This parameter can be either a branch or a commit
gam.diffParam1=develop
# The 2nd parameter used when completing a git diff.  This parameter can be either a branch or a commit
gam.diffParam2=origin/master
# The location of the project files
gam.repoDir=E:\\Dev\\_projects\\Cuddlefish\\cuddleproj\\projdir
```

__NOTE:__ This configuration assumes that your `gam.repoDir` is already connected to your git server.

## Run

Using Gradle you can run the application by running the following command from the command line at the root of the project.

`gradlew gam:run`
