LOGDIR="logs"
ERRORLOG="$LOGDIR/error.log"
INFOLOG="$LOGDIR/info.log"
FILESDIR="files"
TMPDIR="$FILESDIR/temp"
BACKUPDIR="$FILESDIR/bak"
YOUTUBE_URL="http://www.youtube.com/watch?v="
USERNAME="mpfree"
PASSWORD="mpfree"
DATABASE="mpfree"
MAXDOWNLOADS=5
PWD=`pwd`






###
#
###
function errorLog
{
  TIMESTAMP=`date +"%Y-%m-%d %H:%M:%S"`
  MSG=$1

  (
    flock 201
    echo -e "$TIMESTAMP - $MSG" >>$ERRORLOG
  ) 201>/tmp/woozabi.err
}



###
#
###
function infoLog
{
  TIMESTAMP=`date +"%Y-%m-%d %H:%M:%S"`
  INFO=$1

  (
    flock 201
    echo "$TIMESTAMP - $INFO" >>$INFOLOG
  ) 201>/tmp/woozabi.log
}




###
#
###
function setMySQLStatus
{
  REQID=$1
  STATUS=$2

#  mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="UPDATE Requests SET Status=$STATUS WHERE ID=$REQID"
  mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="UPDATE Conversions SET Status=$STATUS WHERE ID=$REQID"
}

function setFileName
{
  REQID=$1
  DATACONTAINER=$2

#  mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="UPDATE Requests SET DataContainer='$DATACONTAINER' WHERE ID=$REQID"
  mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="UPDATE Conversions SET DataContainer='$DATACONTAINER' WHERE ID=$REQID"
}


function setFileSize
{
  REQID=$1
  FILESIZE=$2

#  mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="UPDATE Requests SET FileSize='$FILESIZE' WHERE ID=$REQID"
  mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="UPDATE Conversions SET FileSize='$FILESIZE' WHERE ID=$REQID"
}

