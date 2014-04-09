<?
#include_once('Config.php');
#include_once('Log.php');
#include_once('Database.php');
#include_once('Friends.php');



class Security
{


  /*
   *
   *
   */
  public static function containsIllegalChars($pInputStr)
  {
    $lRetVal = false;

    if (preg_match('/[^a-z0-9\-\_]/i', $pInputStr))
      $lRetVal = true;

    return($lRetVal);
  }


}


?>
