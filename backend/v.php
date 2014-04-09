<html>
  <body>
    <iframe class="youtube-player" type="text/html" width="300" height="225" src="http://www.youtube.com/embed/
<? 
  include_once("core/Log.php");
  include_once("core/Security.php");

  $gVID = isset($_GET['vid'])?$_GET['vid']:'';

  if (Security::containsIllegalChars($gVID))
  {
    Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "Video ID \"$gVID\" contains illegal characters.");
  }
  else
  {
    echo $gVID; 
  }



?>" frameborder="0">
  </body>
</html>
