[#import "/spring.ftl" as spring /]
[#--
    Template Type:   Main Layout
    Desription:         This template servers as the master/main layout for GAM
        @bodyContent    Content and HTML provided by all individual template/ftl files
--]
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css">
    <link rel="stylesheet" href="/css/theme.css">
    <title>Git Archive Manager (GAM)</title>
</head>

<body role="document">
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <a class="navbar-brand" href="/">Git Archive Manager (GAM)</a>
            </div>
            <ul class="nav navbar-nav">
                <li><a href="/projects">Projects</a></li>
            </ul>
        </div>
    </nav>

    <div class="container theme-showcase" role="main">
    [@bodyContent/]
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</body>
</html>