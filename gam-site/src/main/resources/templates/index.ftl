[#-- Include master layout --]
[#include "/_layouts/layouts-main.ftl"]

[#-- Add bodyContent macro --]
[#macro bodyContent]

<div class="jumbotron">

    <p>GAM is a custom tool used for automating the creation of an archive of (1) deltas between
        two difference branches OR (2) the deltas between two different commits within the same
        branch. After the deltas have been generated they are packaged up into a zip file with a folder
        structure that matches the following:</p>

    <p><a class="btn btn-primary btn-lg" href="/projects" role="button">View Projects &raquo;</a></p>

</div>

[/#macro]