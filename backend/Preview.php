<?
  include_once('core/Youtube.php');
  include_once('core/Files.php');



  $lYoutube = new Youtube();

  /*
   * Initialize parameter variables.
   */
  $gSrc = isset($_GET['src'])?$_GET['src']:'';
  $gID = isset($_GET['id'])?$_GET['id']:'';
  $gYTID = isset($_GET['ytid'])?$_GET['ytid']:'';
  
  
  /*
   * Get Youtube Video id if it's defined.
   */
  if (strlen($gYTID) > 0)
  {
    $gSrc = 1;
    $gID = Youtube::extractYTID(urldecode($gYTID));
  }

  /*
   * Get Youtube video details.
   */
  if ($gSrc == 1 && strlen($gID) >= Config::$YoutubeIDLen)
  {
    $lFile = new Files();
    $lFilePath = $lFile->getFilePath($gID);


    $lYT = new Zend_Gdata_YouTube();
    $lQuery = $lYT->newVideoQuery();
    $lQuery->setQuery($gID);


    $lFeed = $lYT->getVideoFeed($lQuery);
    $lCounter = 0;


    foreach ($lFeed as $lEntry)
    {		
      $lThumbnailURL = $lEntry->mediaGroup->thumbnail[0]->url;
      $lVideoTitle = $lEntry->mediaGroup->title;
      $lVideoURL = $lYoutube->findFlashUrl($lEntry);
      $lVideoID = $lEntry->getVideoId();
      $lVideoDuration = $lEntry->mediaGroup->duration->seconds;
      $lYTWatchURL = $lEntry->getVideoWatchPageUrl();
      $lVideoEmbeddable = $lEntry->isVideoEmbeddable();

      if ($lVideoDuration > 0) 
        $lVideoDuration = sprintf("%d:%02d", (floor($lVideoDuration / 60)), ($lVideoDuration % 60));
      else
        $lVideoDuration = "0:00";
    } // foreach ($lFeed...

	
    /*
     * Build download link variable.
     */
    $gDownlodURL = "?src=$gSrc&id=$gID&action=download";
    $gFullURL = "http://".$_SERVER['HTTP_HOST'].$_SERVER['REQUEST_URI'];


?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
  <head>

    <title>Woozabi - Convert YouTube to MP3 - YouTube zu MP3 konvertieren - Convertir YouTube en MP3 - Convertire YouTube in MP3</title>

<meta property="og:title" content="<? echo $lVideoTitle; ?>" />
<meta property="og:type" content="song" />
<meta property="og:image" content="<? echo $lThumbnailURL; ?>" />
<meta property="og:url" content="<? echo $gFullURL; ?>" />
<meta property="og:site_name" content="woozabi" />
<meta property="fb:admins" content="535719402" />

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />


    <link href="global.css" media="all" rel="stylesheet" type="text/css" />
    <link href="default.css" media="all" rel="stylesheet" type="text/css" />
    <style type="text/css">
    </style>

    <script type="text/javascript">
      function getYoutubeVideo(pID)
      {
        window.open("ManageFiles.php?action=download&src=1&id=" + pID);
      }
    </script>

  </head>
  <body>
    <div id="wrapper">
      <div id="banners_top">
      </div>
      <? require_once('Header.php'); ?>
      <div id="content">

        <? require_once('SearchBar.php'); ?>
        <div id="bottom">
          <div id="sidebar">
            <div class="box">
              <? require_once('LatestDownloads.php'); ?>
              <p class="more_link">&nbsp;</p>
            </div>
            
            <div class="box banners">
            </div>
          </div>
          
          <div id="body">
            <br/>
            <br/>

    <b><? echo "$lVideoTitle ($lVideoDuration)"; ?></b><br><br>
<?


    if ($lVideoEmbeddable)
      $lYoutube->printEmbeddedObject($gID);
    else
      $lYoutube->printEmbeddedObject($gID);




?>
    <br/>
    <br/> 

    <div id="preview_buttons">
    <table>
      <tr>
        <td>
          <a class="medium lightgreen button"  onClick="getYoutubeVideo('<? echo $gID; ?>');">
          Get MP3 &#8609;
          </a>
        </td>
        <td>
         <div id="fb-root"></div><script src="http://connect.facebook.net/en_US/all.js#appId=190341787682755&amp;xfbml=1"></script><fb:like href="http://www.woozabi.com/run/Preview.php?src=1&amp;id=<? echo $gID; ?>" send="true" width="450" show_faces="true" action="recommend" font="arial"></fb:like>
        </td>
      </tr>
    </table>
    </div>

<!--
<iframe src="http://www.facebook.com/plugins/like.php?href=<? echo urlencode($gFullURL); ?>" scrolling="no" frameborder="0" style="height: 62px; width: 100%" allowTransparency="no"></iframe>
-->

<?
  }
?>

          </div>
        </div>
      </div>

      <? require_once('Footer.php'); ?>

    </div>

  </body>
</html>
