[#-- Include master layout --]
[#include "/_layouts/layouts-main.ftl"]

[#-- Add bodyContent macro --]
[#macro bodyContent]

<h1 class="page-header">Archives <a href="/projects/add" class="btn btn-primary" role="button">Add Archive</a></h1>

<div class="archives-list">
    <div class="list-group">
        <a href="#" class="list-group-item">
            <span class="badge">31</span>
            2015-08-23 Package 15
        </a>
        <a href="#" class="list-group-item">
            <span class="badge">2</span>
            2015-08-23 Hotfix - Lucky Orange
        </a>
    </div>
</div>

<h2 class="page-header">Add Archive</h2>

<form role="form" method="post">
    [@spring.bind "archive" /]

    <div class="input-group">
        [@spring.formInput "archive.webPrefix" "class='form-control' placeholder='Static Prefix'" "text" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archive.appPrefix" "class='form-control' placeholder='Dynamic Prefix'" "text" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archive.diffBranch" "class='form-control' placeholder='Diff Branch'" "text" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archive.diffParam1" "class='form-control' placeholder='Commit Parameter 1'" "text" /]
    </div>

    <div class="input-group">
        [@spring.formInput "archive.diffParam2" "class='form-control' placeholder='Commit Parameter 2'" "text" /]
    </div>

    <button type="submit" class="btn btn-default">Submit</button>
</form>

[/#macro]