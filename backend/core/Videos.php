<?
#include_once("Security.php");
include_once("Config.php");
include_once("Log.php");
include_once("Database.php");




class Videos
{

  public static $Status = array('pending', 'downloading', 'transcoding', 'ready');



  /*
   *
   *
   */
  public function getSongByID($pVideoID)
  {
    if (strlen($pVideoID) > 0)
    {
      $lStatement = "SELECT VideoID, VideoTitle, Duration FROM VideoDB WHERE VideoID='$pVideoID' ";
  
      $lDB = new Database();
      $lDB->connect();  
	  
      $lDB->select($lStatement);  
      $lResult = $lDB->getResult();
      $lDB->disconnect(); 
    }
	
    return($lResult);	
  }
  
  
  /*
   *
   *
   */
  public function searchVideos($pSrchString)
  {
    if (strlen($pSrchString) > 0)
    {
      $lSrchString = trim($pSrchString);
      $lSplitter = preg_split('/\s+/', $lSrchString);
	  
      if (count($lSplitter) > 0)
      {
        $lStatement = "SELECT ID, VideoTitle, Duration FROM VideoDB WHERE 1=1 ";
		
         foreach ($lSplitter as $lKey => $lValue)
           $lStatement .= " AND VideoTitle like '%$lValue%'";
	  
         $lStatement .= " LIMIT 0," . Config::$YoutubeMaxLocalResults;
	  
        $lDB = new Database();
        $lDB->connect();  
	  
        $lDB->select($lStatement);  
        $lResult = $lDB->getResult();
        $lDB->disconnect(); 
      }
    }
	
    return($lResult);	
  }
  
  
  
  /*
   *
   *
   */  
  public function addVideo($pVideoID, $pVideoTitle, $pDuration = 0, $pViewCounter = 0)
  {
    $lDB = new Database();
    $lDB->connect(); 
	
    $lStatement = "INSERT INTO VideoDB (VideoID, VideoTitle, Duration) VALUES('$pVideoID', '$pVideoTitle', '$pDuration')";
    $lDB->insert($lStatement);  
    $lDB->disconnect();    
  }



  /*
   *
   *
   */
  public function removeVideo($pVideoID)
  {
    $lDB = new Database();
    $lDB->connect(); 
	
    Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "Removing video with ID $pVideoID");

    $lStatement = "DELETE FROM Conversions WHERE ID = $pVideoID";
    $lDB->delete($lStatement);  
    $lDB->disconnect();    
  }
   
  
  
  /*
   *
   *
   */
   public function getMP3($pVideoID, $pVideoTitle)
   {
     $this->addVideo($pVideoID, $pVideoTitle);
   }
   
   
}


?>
