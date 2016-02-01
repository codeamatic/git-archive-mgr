[#--
    Variables:
        projects
            []
                - id
                - name
                - owner
                - projectRepoUrl
                - projectSiteUrl

[#-- Include master layout --]
[#include "/_layouts/layouts-main.ftl"]

[#-- Add bodyContent macro --]
[#macro bodyContent]

<h1 class="page-header">Projects <a href="/projects/add" class="btn btn-primary" role="button">Add Project</a></h1>

[#list projects as project ]
<div class="list-group">
    <a href="/projects/${project.id}" class="list-group-item">
        <span class="badge">0</span>
    ${project.name}
    </a>
</div>
[/#list]

[/#macro]