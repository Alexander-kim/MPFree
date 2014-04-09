<?
  include_once('core/Videos.php');
  include_once('core/Files.php');
  include_once('core/Database.php');

  $gUID = isset($_GET['uid'])?$_GET['uid']:'';
  $gVID = isset($_GET['vid'])?$_GET['vid']:'';


  /*
   * Download the file to the users system.
   */
  if (strlen($gUID) > 0 && strlen($gVID) > 0)
  {
    $lDB = new Database();
    $lDB->connect();
    $lStatement = "SELECT ID,VideoID,Description,DataContainer,Status " .
                  "FROM Conversions " .
                  "WHERE Status = 2 " .
                  "AND VideoID = '$gVID' " .
                  "AND RequestingUID = '$gUID' " . 
                  "LIMIT 1 ";
    $lDB->select($lStatement);
    $lResult = $lDB->getResult();
    $lDB->disconnect();


    if (array_key_exists('ID', $lResult) && array_key_exists('DataContainer', $lResult))
    {
      $lID = $lResult['ID'];
      $lVideoID = $lResult['VideoID'];
      $lStatus = $lResult['Status'];
      $lStatusStr = Videos::$Status[$lStatus];
      $lDescr = $lResult['Description'];
      $lDataContainer = $lResult['DataContainer'];


      $lFile = new Files();
      $lFile->downloadFile($gUID, $lDataContainer);

      # Remove file and DB entry
      $lFile->removeFile($gUID, $lDataContainer);

      $lVideos = new Videos();
      $lVideos->removeVideo($lID);
    }
    else
    {
      Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "User with ID $gUID can't download $gVID");
    }
  }

?>
