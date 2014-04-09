#!/bin/bash

. downloadConfig.sh




###
# create file/directory structure
###

if [ ! -e $FILESDIR ]; then mkdir $FILESDIR; fi
if [ ! -e $TMPDIR ]; then mkdir -p $TMPDIR; fi
if [ ! -e $LOGDIR ]; then mkdir -p $LOGDIR; fi
if [ ! -e $BACKUPDIR ]; then mkdir -p $BACKUPDIR; fi



###
# 
###

while [ 1 == 1 ]
do

#  while read REQID REQUESTINGUID VIDEOID
  while read REQID REQUESTINGUID VIDEOID DESCRIPTION
  do

    NUMDOWNLOADS=`ps ax|grep -i "downloader.sh"|wc -l`

    if [ $NUMDOWNLOADS -lt $MAXDOWNLOADS ];
    then
      DESCRIPTION=$(echo $DESCRIPTION | tr ":,.![]{}()\\\|\$\*\+/\?\&\"\' " _)
      ./downloader.sh $REQID $VIDEOID $REQUESTINGUID $DESCRIPTION&
#      ./downloader.sh $REQID $VIDEOID $REQUESTINGUID 'a_description'&

    else
      echo -e "WAITING! Too many downloads in progress ($NUMDOWNLOADS/$MAXDOWNLOADS)"
      sleep 2
    fi
  done < <(mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="SELECT ID,RequestingUID,VideoID,Description FROM Conversions WHERE Status=0")
#  done < <(mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="SELECT ID,RequestingUID,VideoID FROM Conversions WHERE Status = 0")
##  done < <(mysql -N -p$PASSWORD -u $USERNAME -D $DATABASE --execute="SELECT ID,RequestingUID,YoutubeID,Description FROM Requests WHERE Source=1 AND Status=0")

  sleep 2
  echo "."
done




