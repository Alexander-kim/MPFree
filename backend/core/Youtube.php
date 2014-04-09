<?

require_once('Config.php');



class Youtube
{


  /*
   *
   *
   */
  public static function extractYTID($pYTID)
  {
    $lRetVal = "";
	
    if (strlen($pYTID) > 0)
    {
      if (preg_match('/v=([a-z0-9\_\-]{10,12})&?/i', $pYTID, $lOutput))
      {
        $lRetVal = $lOutput[1];
      } 
      else if (preg_match('/([a-z0-9\_\-]{10,12})/i', $pYTID, $lOutput)) 
      {
        $lRetVal = $lOutput[1];
      }
    }

    return($lRetVal);
  }



  /*
   * 
   *
   */   
  public static function validYoutubeID($pYTID)
  {
    $lRetVal = false;
    $lLength = strlen($pYTID);
  
    if (preg_match('/^[a-z0-9_-]{10,12}$/i', $pYTID))
      $lRetVal = true;
  
    return($lRetVal);
  }  
 


  /*
   * 
   *
   */   
  public static function validYoutubeURL($pYTID)
  {
    $lRetVal = false;
    $lLength = strlen($pYTID);
  
    if (preg_match('/www\.youtube\.com\/.*(\&|\?)v\=[a-z0-9_-]{10,12}(&?|\s*$)/i', $pYTID))
      $lRetVal = true;
  
    return($lRetVal);
  }   

}

?>
