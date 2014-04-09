<?
include_once("Config.php");


class Log
{
  public static function writeLog($pLogLevel, $pFileName, $pMsg)
  {
    if (Config::$LOGLEVEL >= $pLogLevel)
    {
      if ($lFH = fopen(Config::$LOGFILEPATH, "a"))
      {
        if (flock($lFH, LOCK_EX)) 
        {
          // Get timestamp
          $lTimeStamp = date("Y-m-d H:i:s", time());
 
          // Write log message
          fputs($lFH, "$lTimeStamp : " . basename($pFileName) . " : $pMsg\n");
          flock($lFH, LOCK_UN);
        }
        fclose($lFH);
      }
    }
  }



  public static function writeRemoteError($gSessionUID, $gErrorMsg, $gStackTrace)
  {
    if ($lFH = fopen(Config::$REMOTELOGFILEPATH, "a"))
    {
      if (flock($lFH, LOCK_EX)) 
      {
        // Get timestamp
        $lTimeStamp = date("Y-m-d H:i:s", time());
 
        // Write log message
        fputs($lFH, "$lTimeStamp : $gSessionUID, $gErrorMsg, $gStackTrace\n");
        flock($lFH, LOCK_UN);
      }
      fclose($lFH);
    }
  }
}



?>
