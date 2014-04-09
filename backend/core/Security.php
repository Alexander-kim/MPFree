<?
include_once('Config.php');
include_once('Log.php');
include_once('Database.php');
include_once('Friends.php');



class Security
{


  /*
   *
   *
   */
  public static function removeDangerousChars($pInputStr)
  {
    $lRetVal = $pInputStr;

    if (preg_match('/[^a-z0-9\s]/i', $pInputStr))
      $lRetVal = preg_replace('/[^a-z0-9\s\-\_]/i', ' ', $pInputStr);

    return($lRetVal);
  }




  /*
   *
   *
   */
  public static function passwordIllegalChars($pInputStr)
  {
    $lRetVal = false;

    if (preg_match("/[^a-z0-9\.\-\_]/i", $pInputStr))
      $lRetVal = true;

    return($lRetVal);
  }






  /*
   *
   *
   */
  public static function mailaddrIllegalChars($pInputStr)
  {
    $lRetVal = false;

    if (preg_match("/[^a-z0-9\.\-\_\@]/i", $pInputStr))
      $lRetVal = true;

    return($lRetVal);
  }


  
  /*
   *
   *
   */
  public static function nameIllegalChars($pInputStr)
  {
    $lRetVal = false;

    if (preg_match("/[^a-z0-9\.\-\_\@]/i", $pInputStr))
      $lRetVal = true;

    return($lRetVal);
  }  
  
  



  /*
   *
   *
   */
  public static function isValidMailAddress($pInputStr)
  {
    $lRetVal = false;

    if (preg_match('/^[\.\-_a-zA-Z0-9]+@[a-zA-Z0-9_\-\.]+\.[a-zA-Z0-9_\-]+/', $pInputStr))
      $lRetVal = true;

    return($lRetVal);
  }





  /*
   *
   *
   */
  public static function UIDIllegalChars($pInputStr)
  {
    $lRetVal = false;

    if (preg_match("/[^0-9]+/i", $pInputStr))
      $lRetVal = true;

    return($lRetVal);
  }


  
  
  

  /*
   *
   *
   */
  public static function registerIllegalChars($pInputStr)
  {
    $lRetVal = false;

    if (preg_match("/[^a-z0-9\.\-\_]/i", $pInputStr))
      $lRetVal = true;

    return($lRetVal);
  }


  
  
  



  /*
   *
   *
   */
  public static function createSession($pEmail, $pUID)
  {
      session_start();
	  
	  $lFriends = new Friends();
	  $lDetails = $lFriends->getFriendDetails($pUID);
	  
      $_SESSION['authenticated'] = true;
      $_SESSION['email'] = $pEmail;
      $_SESSION['uid'] = $pUID;
	  $_SESSION['vorname'] = $lDetails[Vorname];
	  $_SESSION['nachname'] = $lDetails[Nachname];
	  $_SESSION['stadt'] = $lDetails[Stadt];
	  $_SESSION['land'] = $lDetails[Land];
	  $_SESSION['role'] = $lDetails[Role];
      $_SESSION['lastactivity'] = time();
  }



  /*
   *
   *
   */
  public static function destroySession()
  {
  // Only attempt to end the session if there 
  // is a $PHPSESSID set by the request.
    if(isset($PHPSESSID)) 
    {
      session_start();
      session_destroy();
    }    
  }



  /*
   *
   *
   */
  public static function isSessionValid()
  {
    $lRetVal = false;
    session_start();

    if (isset($_SESSION) && $_SESSION['authenticated'] == true)
    {
      if ($_SESSION['lastactivity'] + 60 * Config::$SessionTimeout > time())
      {
        $_SESSION['lastactivity'] = time();
        $lRetVal = true;          
      }
    }

    
    return($lRetVal);
  }






  /*
   *
   *
   */
  public static function generateRandomKey ($pLength)
  {
    $lValidChars = "0123456789abcdefghijklmnopqrstuvwxyz";
    $lRetVal = "";

    for ($p = 0; $p < $pLength; $p++)
        $lRetVal .= $lValidChars[mt_rand(0, strlen($lValidChars))];

    return($lRetVal);
  }








  /*
   *
   *
   */
  public static function getUnlockKey($pEmail)
  {
      $lRetVal = '';

      $lQuery = "SELECT UnlockKey FROM Users Where Email='$pEmail'";
      $pDB = new Database();
      $pDB->connect();
      $pDB->select($lQuery);
      $lResult = $pDB->getResult();
      $pDB->disconnect();

      $lRetVal = $lResult['UnlockKey'];

      return($lRetVal);
  }




  /*
   *
   *
   */
  public static function register($pRegKey)
  {
    $lRetVal = false;

    if (strlen($pRegKey) > 60)
    {
    
      /*
       * Check if we have a user entry with such a registration key.
       *
       */
      $lQuery = "SELECT Email FROM Users Where UnlockKey='$pRegKey'";
      $pDB = new Database();
      $pDB->connect();
      $pDB->select($lQuery);
      $lResult = $pDB->getResult();
      $pDB->disconnect();


      if ($lResult == null)
        return(false);


      /*
       * Update the activation status of this user.
       *
       */
      $lQuery = "UPDATE Users SET Active=1 WHERE UnlockKey='$pRegKey'";
      $pDB = new Database();
      $pDB->connect();
      $pDB->update($lQuery);
      $lResult = $pDB->getResult();
      $pDB->disconnect();

      $lRetVal = true;
    }

    return($lRetVal);
  }
}


?>
