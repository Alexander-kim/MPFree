<?

class Config
{
  // Woozabi settings
  public static $FilesRootDir = '/var/www/mpfree/transcoder/files/';
  
  // Logging and debugging
  public static $LOGLEVEL = 1;
  public static $LOGFILEPATH = '/tmp/mpfree.log';
  public static $REMOTELOGFILEPATH = '/tmp/remote.log';

  // DB settings
  public static $DBHost = 'localhost';
  public static $DBUser = 'mpfree';
  public static $DBPass = 'mpfree';
  public static $DBName = 'mpfree';

  
  // Youtube settings
  public static $YoutubeMaxResults = 50; 
  public static $YoutubeMaxLocalResults = 10;
  public static $YoutubeWatchVideoURL = "https://www.youtube.com/watch?v=";
  public static $YoutubeSearchVideoURL = "https://www.youtube.com/results?search_query=";
  public static $YoutubeIDLength = 11;
  public static $YoutubeIgnoreResultRegex = '/(\s|\(|\)|\"|\')(live|interview|trailer|teaser|parody|parodie)/i';

  
}

?>
