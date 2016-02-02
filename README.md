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

## Run Locally

Using Gradle you can run the application by running the following command from the command line at the root of the project.

`gradlew gam-site:bootRun`

This will create a Tomcat instance with GAM available at <http://localhost:8888>.

## Deploy

Using Gradle and Docker you can deploy the application to an image and run it locally or add it to your continuous integration configuration.


