#!/bin/bash

. downloadConfig.sh



function cleanUp
{
  errorLog "$SCRIPTNAME ($REQID, $REQUESTINGUID, $VIDEOID) TRAPPED"
  setMySQLStatus $REQID 4
}

trap cleanUP HUP INT TERM EXIT QUIT ERR




###
# Download flv file from Youtube
# $REQID $VIDEOID $REQUESTINGUID $DESCRIPTION
###
SCRIPTNAME=$0
REQID=$1
VIDEOID=$2
REQUESTINGUID=$3
DESCRIPTION=$4
DOWNLOADURL=${YOUTUBE_URL}$VIDEOID

infoLog "Start downloading $DOWNLOADURL"
setMySQLStatus $REQID 1
setFileName $REQID "$DESCRIPTION.mp3"


echo "REQID=$REQID=$1/VIDEOID=$VIDEOID=$2/REQUESTINGUID=/$REQUESTINGUID=$3/DESCRIPTION=$DESCRIPTION=$4"


if [ -f "$PWD/$BACKUPDIR/${VIDEOID}_${DESCRIPTION}.mp3" ]
then
  if [ ! -f "$PWD/$FILESDIR/$REQUESTINGUID/$DESCRIPTION.mp3" ]
  then
    echo "File does not exist: $PWD/$FILESDIR/$REQUESTINGUID/$DESCRIPTION.mp3"
    ln -s "$PWD/$BACKUPDIR/${VIDEOID}_${DESCRIPTION}.mp3" "$PWD/$FILESDIR/$REQUESTINGUID/$DESCRIPTION.mp3"
    infoLog "$DOWNLOADURL was already in the backup directory. created a link."
    setMySQLStatus $REQID 2
    exit
  else
    echo "File exists: $PWD/$FILESDIR/$REQUESTINGUID/$DESCRIPTION.mp3"
    infoLog "$DOWNLOADURL/${VIDEOID}_${DESCRIPTION}.mp3 was already in the backup and in the user directory."
    setMySQLStatus $REQID 2
    exit
  fi
else
  echo "File does not exist: $PWD/$BACKUPDIR/${VIDEOID}_${DESCRIPTION}.mp3"
fi





FILENAME1="$PWD/$TMPDIR/$VIDEOID.ytb"
echo "Downloading file: $FILENAME1"
echo "Downloading URL: $DOWNLOADURL"
if [ -f $FILENAME1 ]; then rm $FILENAME1; fi
COMMAND1="./youtube-dl $DOWNLOADURL -o $FILENAME1"
echo "Command: $COMMAND1"
ERROR=$($COMMAND1 2>&1)
RETVAL=$?

echo "RETVAL: $RETVAL"

# Check if we got any error
if [ $RETVAL != 0 ];
then
  errorLog "$COMMAND1 - $ERROR"
  exit 2
fi

# Check if file exists
if [ ! -f $FILENAME1 ];
then
  errorLog "$FILENAME1 does not exist"
  exit 3
fi

# Check if file has a reasonable size
FILESIZE1=`stat -c %s $FILENAME1`

if [ $FILESIZE1 -lt 1000 ];
then
  errorLog "$FILENAME1 file size is too small ($FILESIZE1)"
  exit 4
fi




###
# convert it from FLV/MP3 to WAV
###
FILENAME2="$PWD/$TMPDIR/$VIDEOID.wav"
if [ -f $FILENAME2 ]; then rm $FILENAME2; fi
COMMAND2="ffmpeg -v 0 -y -i $FILENAME1 $FILENAME2"
ERROR=$($COMMAND2 2>&1)
RETVAL=$?


# Check if we got any error
if [ $RETVAL != 0 ];
then
  errorLog "$COMMAND2 - $ERROR"
  exit 5
fi

# Check if file exists
if [ ! -f $FILENAME2 ];
then
  errorLog "$COMMAND2 - $FILENAME2 does not exist"
  exit 6
fi

# Check if file has a reasonable size
FILESIZE2=`stat -c %s $FILENAME2`

if [ $FILESIZE2 -lt 1000 ];
then
  errorLog "$COMMAND2 - $FILENAME2 file size is too small ($FILESIZE2)"
  exit 7
fi



###
# convert it from WAV to MP3
###
FILENAME3="$PWD/$TMPDIR/$VIDEOID.mp3"
#  if [ -f $FILENAME3 ]; then rm $FILENAME3; fi
COMMAND3="lame --quiet --preset 128 $FILENAME2 $FILENAME3"
ERROR=$($COMMAND3 2>&1)
RETVAL=$?


# Check if we got any error
if [ $RETVAL != 0 ];
then
  errorLog "$COMMAND3 -$ERROR"
  exit 8
fi

# Check if file exists
if [ ! -f $FILENAME3 ];
then
  errorLog "$FILENAME3 does not exist"
  exit 9
fi

# Check if file has a reasonable size
FILESIZE3=`stat -c %s $FILENAME3`

if [ $FILESIZE3 -lt 1000 ];
then
  errorLog "$FILENAME3 file size is too small ($FILESIZE3)"
  exit 10
fi





# Move file to the appropriate place
if [ ! -e "$FILESDIR/$REQUESTINGUID" ]; then mkdir -p "$FILESDIR/$REQUESTINGUID"; fi
mv $FILENAME3 "$PWD/$BACKUPDIR/${VIDEOID}_${DESCRIPTION}.mp3"
echo "mv $FILENAME3 \"$PWD/$BACKUPDIR/${VIDEOID}_${DESCRIPTION}.mp3\""
ln -s "$PWD/$BACKUPDIR/${VIDEOID}_${DESCRIPTION}.mp3" "$PWD/$FILESDIR/$REQUESTINGUID/$DESCRIPTION.mp3"

#
if [ -f $FILENAME1 ]; then rm $FILENAME1; fi
if [ -f $FILENAME2 ]; then rm $FILENAME2; fi
if [ -f $FILENAME3 ]; then rm $FILENAME3; fi
infoLog "Stop downloading $DOWNLOADURL"
setMySQLStatus $REQID 2
setFileSize $REQID $FILESIZE3

