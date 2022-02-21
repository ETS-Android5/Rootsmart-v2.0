#!/system/xbin/smart/sh
mount -o remount system /system
cat $1 > $2
chown 0.0 $2
chmod 4755 $2
sync
mount -o remount,ro system /system