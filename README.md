# ICT2207 - Rootsmart 2.0 w/ cve-2019-2215 + Ransomware

Attack Vector:

References:
- [Rootsmart analysis A](https://forensics.spreitzenbarth.de/2012/02/12/detailed-analysis-of-android-bmaster/)
- [Rootsmart analysis B](https://resources.infosecinstitute.com/topic/rootsmart-android-malware/)
- [Rootsmart analysis C](https://www.csc2.ncsu.edu/faculty/xjiang4/RootSmart/)
- [Git - Dropper PoC](https://github.com/agentzex/The-Nice-Dropper)
- [Git - cve-2019-2215](https://github.com/kangtastic/cve-2019-2215)


1. Install Malware
2. Run malware
3. Download shells.zip from C2 and unzip to /data/data/com.google.android.\<app package name\>/files
	- cve-2019-2215 binary
	- install.sh
	- ransomware.sh
4. chmod 775 cve-2019-2215 (our root shell binary)
5. exec install.sh to use cve-2019-2215 root shell and remount /system and then create other needed dir in filesystem
6. exec ransomeware.sh which will download dex file and execute dex file via dalvikvm command [ref](https://gist.github.com/lifuzu/9918513)