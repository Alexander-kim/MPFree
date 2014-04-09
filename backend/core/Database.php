<?
include_once("Config.php");
include_once("Log.php");



class Database  
{  
  private $mConnected = false;
  private $mResult = array();
  private $mConn = 0;


  public function Connection($pValue = '')
  {
    if(empty($pValue))
      return($this->mConn);
    else
      $this->mConn = $pValue;
  }


  /*
   *
   *
   */
  public function connect()
  {
    if(!$this->mConnected)  
    {  
      if($this->mConn = mysql_connect(Config::$DBHost, Config::$DBUser, Config::$DBPass))  
      {  
        if($seldb = @mysql_select_db(Config::$DBName, $this->mConn))  
        {  
          $this->mConnected = true;  
          return(true);  
        } else  {  
          return(false); 
        }  
      } else {  
        return(false); 
      }  
    } else {  
        return(true);  
    } 
  }  


  

  /*
   *
   *
   */
  public function disconnect()
  {
    if($this->mConnected)  
    {  
      if(@mysql_close($this->mConn))  
      {  
        $this->mConnected = false;  
        return(true);
      } else {  
        return(false);
      }  
    } 
  }  


  

  /*
   *
   *
   */
  public function select($pQuery)
  {
    Log::writeLog(3, $_SERVER["SCRIPT_NAME"], "(0) : \"$pQuery\"");

    if($query = mysql_query($pQuery))  
    {  
      $this->numResults = @mysql_num_rows($query);	  
	  
      for($i = 0; $i < $this->numResults; $i++)  
      {  
        $r = mysql_fetch_array($query);  
        $key = array_keys($r);  

        for($x = 0; $x < count($key); $x++)  
        {  
          // Sanitizes keys so only alphavalues are allowed  
          if(!is_int($key[$x]))  
          {  
            if(mysql_num_rows($query) > 1)  
              $this->mResult[$i][$key[$x]] = $r[$key[$x]];  
            else if(mysql_num_rows($query) < 1)  
              $this->mResult = null;  
            else  
              $this->mResult[$key[$x]] = $r[$key[$x]];  
          }  
        }  
      }  
      return(true);  
    } else {  
      return(false);
    }  
  }  


  

  /*
   *
   *
   */
  public function insert($pStatement)
  {
    Log::writeLog(3, $_SERVER["SCRIPT_NAME"], "(0) : \"$pStatement\"");

    if($ins = @mysql_query($pStatement))
      return true;  
    else  
      return false;  
  }  


  

  /*
   *
   *
   */
  public function delete($pStatement)  
  {  
    Log::writeLog(3, $_SERVER["SCRIPT_NAME"], "(0) : \"$pStatement\"");

    if($del = @mysql_query($pStatement))
      return(true);  
    else  
     return(false);  
  }  


  

  /*
   *
   *
   */
  public function update($pStatement)
  {
    Log::writeLog(3, $_SERVER["SCRIPT_NAME"], "(0) : \"$pStatement\"");

    if ($query = @mysql_query($pStatement))
        return true;
    else
        return false;
  }  



  /*
   * Returns the result set
   *
   */
  public function getResult()
  {
    return($this->mResult);
  }






  /*
   * 
   *
   */
  public static function getEntry($pUsername, $pUniqueElement)
  {
    return($this->mResult);
  }

} 

?>
