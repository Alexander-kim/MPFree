{
  "records": [
<?
  include_once('core/Config.php');
  include_once('core/Youtube.php');
  include_once('core/HTTP.php');
  include_once('core/Videos.php');
  include_once('core/Security.php');
  include_once('SimpleDom/simple_html_dom.php');

  

  $gYTSearch = isset($_GET['srch'])?$_GET['srch']:'';    

  if (strlen($gYTSearch) <= 0)
    $gYTSearch = isset($_POST['srch'])?$_POST['srch']:'';


  $gOutRecords = array();


  if (strlen($gYTSearch) >= 3)
  {
    $lHTML = file_get_html(Config::$YoutubeSearchVideoURL . urlencode($gYTSearch));
    $lVideo = new Videos();

    foreach($lHTML->find('div[class=yt-lockup-content] h3 a') as $lElement) 
    {
      if (strlen($lElement->href) > 0 && strlen($lElement->title) > 0)
      {
        if (!preg_match(Config::$YoutubeIgnoreResultRegex, $lElement->title))
        {
          $lYTID = Youtube::extractYTID($lElement->href);
          $lYTTitle = preg_replace('/[\'\"\`\=\&\%\'\+]+/', ' ', $lElement->title);
          $lYTTitle = preg_replace('/\s{2,}/', ' ', $lYTTitle);

          if (Youtube::validYoutubeID($lYTID))
          {
            $lRec =  " {\n";
            $lRec .= "    \"ytid\": \"$lYTID\",\n";
            $lRec .= "    \"href\": \"$lElement->href\",\n";
            $lRec .= "    \"title\": \"$lElement->title\"\n";
            $lRec .= "  }";
 
            array_push($gOutRecords, $lRec);

            $lVideo->addVideo($lYTID, $lYTTitle);	
          }
          else
          {
            Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "Invalid Youtube ID \"$lYTID\"");
          }
        }
        else
        {
          Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "Ignoring title \"$lElement->title\"");
        }
      } 
      else
      {
        Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "Something is wrong with the title or href");
      }
    }
  }
  else
  {
    Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "Search string to short!");
  }


  echo implode ("," , $gOutRecords);

?>
  ]
}
