<?
  include_once('core/Config.php');
  include_once('core/Security.php');
  include_once('core/Videos.php');  
  include_once('core/Youtube.php');
  include_once('core/HTTP.php');
  include_once('core/Database.php');

  
  
  /*
   * Initialize parameter variables.
   */
  $gVideoID = isset($_POST['vid'])?$_POST['vid']:$_GET['vid'];
  $gRequestingUID = isset($_POST['uid'])?$_POST['uid']:$_GET['uid'];
  
  
  if (strlen($gRequestingUID) > 0 && strlen($gVideoID) > 0 && Youtube::validYoutubeID($gVideoID))
  {
    /*
     * Find video entry.
     */
    $gLocalSrch = new Videos();
    $gLocalSrchResult = $gLocalSrch->getSongByID($gVideoID);
    $lVideoTitle = (strlen($gLocalSrchResult[VideoTitle])>64)?substr($gLocalSrchResult[VideoTitle], 0, 64)."...":$gLocalSrchResult[VideoTitle];
	
    /*
     * Insert transcode request into db.
     */
    $lClientIP = isset($_SERVER['HTTP_X_FORWARDED_FOR'])?$_SERVER['HTTP_X_FORWARDED_FOR']:$_SERVER['REMOTE_ADDR'];
    $lStatement = "INSERT INTO Conversions (Timestamp, VideoID, RequestingUID, ClientIP, Description) Values(NOW(), '$gVideoID', '$gRequestingUID', '$lClientIP', '$lVideoTitle')";
  
    $lDB = new Database();
    $lDB->connect();  
        
    $lDB->select($lStatement);  
    $lResult = $lDB->getResult();
    $lDB->disconnect(); 	
  }
  
?>
