[#-- Include master layout --]
[#include "/_layouts/layouts-main.ftl"]

[#-- Add bodyContent macro --]
[#macro bodyContent]

<div class="jumbotron">

    <p>GAM is a custom tool used for automating the creation of an archive of (1) deltas between
        two different branches OR (2) the deltas between two different commits within the same
        branch. After the deltas have been generated they are packaged up into a zip file with a folder
        structure that matches the following:</p>

    <ul>
        <li>From_Author_ProjectName.zip [Root Zip file]
            <ul>
                <li>From_Author [Folder]
                    <ul>
                        <li>ProjectName [Folder]
                            <ul>
                                <li>YYYYMMDD [Folder]
                                    <ul>
                                        <li>app.zip [Zip file that holds all dynamic files]</li>
                                        <li>web.zip [Zip file that holds all static asset files]
                                        </li>
                                        <li>README.txt [Provides a listing of deltas within the zip
                                            files]
                                        </li>
                                        <li>XYZ_Update.sql [If applicable, holds query updates]</li>
                                    </ul>
                                </li>
                            </ul>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
    </ul>

    <p>The XYZ_Update.sql file is only provided if updates to the database need to be made.</p>

    <p><a class="btn btn-primary btn-lg" href="/projects" role="button">View Projects</a></p>

</div>

[/#macro]