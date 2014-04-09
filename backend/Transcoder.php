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
  $gVID = isset($_POST['vid'])?$_POST['vid']:$_GET['vid'];
  $gUID = isset($_POST['uid'])?$_POST['uid']:$_GET['uid'];


  if (Security::containsIllegalChars($gUID))
  {
    Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "User ID \"$gUID\" contains illegal characters.");
  }
  elseif (Security::containsIllegalChars($gVID))
  {
    Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "Video ID \"$gVID\" contains illegal characters.");
  }
  elseif (strlen($gUID) > 0 && strlen($gVID) > 0 && Youtube::validYoutubeID($gVID))
  {
    /*
     * Find video entry.
     */
    $gLocalSrch = new Videos();
    $gLocalSrchResult = $gLocalSrch->getSongByID($gVID);
    $lVideoTitle = (strlen($gLocalSrchResult[VideoTitle])>64)?substr($gLocalSrchResult[VideoTitle], 0, 64)."...":$gLocalSrchResult[VideoTitle];
	
    /*
     * Insert transcode request into db.
     */
    $lClientIP = isset($_SERVER['HTTP_X_FORWARDED_FOR'])?$_SERVER['HTTP_X_FORWARDED_FOR']:$_SERVER['REMOTE_ADDR'];
    $lStatement = "INSERT INTO Conversions (Timestamp, VideoID, RequestingUID, ClientIP, Description) Values(NOW(), '$gVID', '$gUID', '$lClientIP', '$lVideoTitle')";
  
    $lDB = new Database();
    $lDB->connect();  
        
    $lDB->select($lStatement);  
    $lResult = $lDB->getResult();
    $lDB->disconnect(); 	
  }
  
?>
