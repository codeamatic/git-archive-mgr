[#--
    Variables:
        project
            - id
            - name
            - owner
            - buildDirectory
            - repoUrl
            - siteUrl
            - active
            - created

--]
[#if (project.id)??]
    [#assign formAction = "/projects/${project.id}/edit"]
    [#assign formMethod = "PUT"]
[#else]
    [#assign formAction = "/projects/add"]
    [#assign formMethod = "POST"]
[/#if]

[#-- Include master layout --]
[#include "/_layouts/layouts-main.ftl"]

[#-- Add bodyContent macro --]
[#macro bodyContent]

<h1 class="page-header">Add Project<a href="/projects" class="btn btn-link" role="button">Back to Projects</a></h1>

<form role="form" action="${formAction}" method="${formMethod}">
    [@spring.bind "projectForm" /]

    <div class="input-group">
        [@spring.formInput "projectForm.projectName" "class='form-control' placeholder='Project Name'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "projectForm.projectOwner" "class='form-control' placeholder='Project Owner'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "projectForm.projectRepoUrl" "class='form-control' placeholder='Repository URL'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "projectForm.projectSiteUrl" "class='form-control' placeholder='Site URL'" /]
    </div>

    <button type="submit" class="btn btn-default">Submit</button>
</form>

[/#macro]