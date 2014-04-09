<?
  include_once("core/Database.php");
  include_once("core/Security.php");
  include_once("core/Videos.php");


  $gUID = isset($_GET['uid'])?$_GET['uid']:'';

  if (strlen($gUID) <= 0)
    $gUID = isset($_POST['uid'])?$_POST['uid']:'';




  echo "{\n  \"records\": [\n";

                  
  if (Security::containsIllegalChars($gUID))
  {
    Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "User ID \"$gUID\" contains illegal characters.");
  }
  elseif (strlen($gUID) >= 0)
  {
    $lDB = new Database();
    $lDB->connect();
    $lStatement = "SELECT VideoID,Description,Status " .
                  "FROM Conversions " .
                  "WHERE Status in (1, 2) " .
                  "AND RequestingUID = '$gUID'";
    $lDB->select($lStatement);
    $lResult = $lDB->getResult();


    if (array_key_exists('0', $lResult))
    {
      foreach (array_values($lResult) as $i => $lEntry) 
      {
        $lVideoID = $lEntry['VideoID'];
        $lStatus = $lEntry['Status'];
        $lStatusStr = Videos::$Status[$lStatus];
        $lDescr = $lEntry['Description'];


        echo "  {\n";
        echo "    \"ytid\": \"$lVideoID\",\n";
        echo "    \"stat\": \"$lStatus\",\n";
        echo "    \"statstr\": \"$lStatusStr\",\n";
        echo "    \"desc\": \"$lDescr\"\n";

        if ($i+1 < count($lResult))
          echo "  },\n";
        else
          echo "  }\n";
        
      }
    }
    elseif (array_key_exists('VideoID', $lResult) && array_key_exists('Status', $lResult) && array_key_exists('Description', $lResult))
    {
      $lVideoID = $lResult['VideoID'];
      $lStatus = $lResult['Status'];
      $lStatusStr = Videos::$Status[$lStatus];
      $lDescr = $lResult['Description'];

      echo "  {\n";
      echo "    \"ytid\": \"$lVideoID\",\n";
      echo "    \"stat\": \"$lStatus\",\n";
      echo "    \"statstr\": \"$lStatusStr\",\n";
      echo "    \"desc\": \"$lDescr\"\n";
      echo "  }\n";
    }

    $lDB->disconnect(); 
  }
  else
  {
    Log::writeLog(1, $_SERVER["SCRIPT_NAME"], "User ID \"$gUID\" is wrong.");
  }

  echo "]}\n";

?>
