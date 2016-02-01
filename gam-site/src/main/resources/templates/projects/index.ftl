[#--
    Variables:
        projects
            []
                - id
                - name
                - owner
                - projectRepoUrl
                - project

[#-- Include master layout --]
[#include "/_layouts/layouts-main.ftl"]

[#-- Add bodyContent macro --]
[#macro bodyContent]

<h1 class="page-header">Projects <a href="/projects/add" class="btn btn-primary" role="button">Add Project</a></h1>

[#list projects as project ]
<div class="list-group">
    <a href="/projects/${project.id}" class="list-group-item">
        <span class="badge">14</span>
        Ready Set Eat
    </a>
    <a href="/projects/kid-cuisine" class="list-group-item">
        <span class="badge">122</span>
        Kid Cuisine
    </a>
    <a href="/projects/alexia" class="list-group-item">
        <span class="badge">8</span>
        Alexia
    </a>
</div>
[/#list]

[/#macro]