<?
include_once("Database.php");
include_once("Config.php");


class Files
{

  /*
   *
   *
   */
  public function removeFile($pOwner, $pFilename)
  {
    $lFilePath = Config::$FilesRootDir . "/$pOwner/$pFilename";
    if(file_exists($lFilePath) &&  is_link($lFilePath)) 
    {
      Log::writeLog(1, $_SERVER['SCRIPT_NAME'], "User $pOwner removed file \"$lFilePath\".");
      unlink($lFilePath);
    }
    else
    {
      Log::writeLog(1, $_SERVER['SCRIPT_NAME'], "User $pOwner: Can't remove file \"$lFilePath\".");
    }
  }



  /*
   *
   *
   */
  public function downloadFile($pOwner, $pFilename)
  {
    Log::writeLog(1, $_SERVER['SCRIPT_NAME'], "User $pOwner is downloading file \"$pFilename\".");

    $lFilePath = Config::$FilesRootDir . "/$pOwner/$pFilename";

    header("Content-Type: application/force-download");
    header("Content-Length: " . filesize($lFilePath));
    header('Content-Disposition: attachment; filename="' . basename($lFilePath) . '"');
    header("Content-Transfer-Encoding: binary\n");
    header("Full-Path: |$lFilePath|\n");

    readfile($lFilePath);
  }

}

?>
