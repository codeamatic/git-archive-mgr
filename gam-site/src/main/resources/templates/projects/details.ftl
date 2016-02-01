[#--
    Variables:
        project
            - id
            - name
            - owner
            - projectRepoUrl
            - projectSiteUrl

[#-- Include master layout --]
[#include "/_layouts/layouts-main.ftl"]

[#-- Add bodyContent macro --]
[#macro bodyContent]

<h1 class="page-header">${archiveForm.project.name} <a href="/projects" class="btn btn-link"
                                                       role="button">Back
    to Projects</a></h1>

[#--<div class="archives-list">--]
[#--<div class="list-group">--]
[#--<a href="#" class="list-group-item">--]
[#--2015-08-23 Package 15--]
[#--</a>--]
[#--<a href="#" class="list-group-item">--]
[#--2015-08-23 Hotfix - Lucky Orange--]
[#--</a>--]
[#--</div>--]
[#--</div>--]

<h2 class="page-header">Add Archive</h2>

<form role="form" action="/projects/${archiveForm.project.id}/archives" method="POST">
    [@spring.bind "archiveForm" /]

    [@spring.formHiddenInput "archiveForm.project" /]

    <div class="input-group">
        [@spring.formInput "archiveForm.webPrefix" "class='form-control' placeholder='Static Prefix'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archiveForm.appPrefix" "class='form-control' placeholder='Dynamic Prefix'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archiveForm.deployDate" "class='form-control' placeholder='Deploy Date'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archiveForm.diffBranch" "class='form-control' placeholder='Diff Branch'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archiveForm.diffParam1" "class='form-control' placeholder='Commit Parameter 1'" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archiveForm.diffParam2" "class='form-control' placeholder='Commit Parameter 2'" /]
    </div>

    <div class="input-group">
        [@spring.formTextarea "archiveForm.readmeTxt" "class='form-control' placeholder='Readme.Txt'" /]
    </div>

    <button type="submit" class="btn btn-default">Submit</button>
</form>

[/#macro]