#webrc
An awesome java platform for raspberry pi to create autonomous and remote controlled robots.
****

##TODOs:
* replace Spring Boot with something more efficient for the raspberry pi, speed up start up time of robot application
* enable running the server and robot as one application (or separate) so the server can run on the pi
* replace messaging system with open source package
* add support for INA219 current and voltage sensor
* add filtering for sensor values communicated to the server
* integrate live streaming, photo/video capture
* implement a more robust robot to server communication
* add command line options to change configuration files

##building and running the project
* run `git clone https://github.com/benjamin-edward-morgan/webrc.git`
* make sure you are running Java 7, and that the JAVA_HOME environment variable points to the jdk7 home directory _You Must Use an Oracle JVM_
* run 
```
cd webrc
mvn clean install
```

The common module compiles a jar that the other projects depend on.

The robot runnable jar is built as `robot/target/robot-0.1.0-SNAPSHOT-jar-with-dependencies.jar` and can be invoked with `sudo java -jar robot/target/robot-0.1.0-SNAPSHOT-jar-with-dependencies.jar` The `robot.xml` file must be in the directory where you invoke the robot application from.

The server can be started from the command line like this:
````
cd server
mvn spring-boot:run
````
- NOTE: you may need to install maven by running 
```
sudo apt-get update
sudo apt-get install maven
````

To use the UI, visit `<server’s ip address>:8080/rc.html` from a phone/tablet/pc.

##robot
This application runs on the raspberry pi and communicates with the server. It is configured with robot.xml (must be in the directory that robot.jar is invoked from). This application relays information from the server, transforms it and outputs to physical hardware. This application can also read data from sensors, transform and send back to the server. Controllers read sensor values and output actuator commands to perform automatic functions.

####Setting up the Image

#####First Steps
* Use Pi Filler or other application to image an SD card with Raspbian. (Last tested with Raspbian version 2014-01-07)
* Insert SD card, monitor connected via HDMI, a usb keyboard and a usb wifi dongle (tested with Edimax EW-7811Un) to Raspberry Pi and boot up.
* In the “Raspberry Pi Software Configuration Tool” choose option 1: Expand Filesystem and choose ok.
* Choose 2: Change User Password and change the password.
* Choose 4: Internationalisation Options, then choose L3 Change Keyboard Layout. Wait a moment for the list of options to populate. Choose “Generic 105-key,” then “Other” for keyboard layout, then “English (US)” then “English (US)” again, then “Default for Keyboard Layout” 
* Choose 5: to Enable Camera, then choose “Enable.”
* Choose 8: Advanced Options, then choose A2 Hostname. Enter a new hostname for the Pi.
* Choose Advanced Options again, the choose A4 SSH. Choose Enable.
* Select Finish. Choose Yes to reboot.

This menu can be visited again later by running `sudo raspi-config`

#####Enable Auto-Login:
* run `sudo nano /etc/inittab`
* comment out the following line:
```
1:2345:respawn:/sbin/getty --noclear 38400 tty1
```
* and below it add:
```
1:2345:respawn:/bin/login -f pi ttyl </dev/tty1 >/dev/tty1 2>&1
```

#####Set up WiFi:
* run `sudo nano /etc/wpa_supplicant/wpa_supplicant.conf`
* add:
```
network={
    ssid="yourNetworkSSID"
    psk="yourNetworkPassword"
    }
```

#####Enable I2C
* type `sudo nano /etc/modules`
* append following lines
```
i2c-bcm2708 
i2c-dev 
```
* run `sudo nano /etc/modprobe.d/raspi-blacklist.conf`
* comment out `blacklist i2c-bcm2708` with #
* optional: run `sudo apt-get install i2c-tools`

#####Disable Camera LED
`disable_camera_led=1` to `/boot/config.txt`

#####Change Java Versions
* use `update-java-alternatives -l` to list available java vms
* use `sudo update-java-alternatives -s jdk-7-oracle-armhf` to enable oracle java 7

#####Install and run mjpg_streamer
```
sudo apt-get update
sudo apt-get install libjpeg8-dev imagemagick libv4l-dev
sudo ln -s /usr/include/linux/videodev2.h /usr/include/linux/videodev.h
wget http://sourceforge.net/code-snapshots/svn/m/mj/mjpg-streamer/code/mjpg-streamer-code-182.zip
unzip mjpg-streamer-code-182.zip
cd mjpg-streamer-code-182/mjpg-streamer
make mjpg_streamer input_file.so output_http.so
sudo cp mjpg_streamer /usr/local/bin
sudo cp output_http.so input_file.so /usr/local/lib/
mkdir /tmp/stream
raspistill --nopreview -w 640 -h 480 -q 5 -o /tmp/stream/pic.jpg -tl 100 -t 9999999 -th 0:0:0 &sudo cp -R www /usr/local/www
LD_LIBRARY_PATH=/usr/local/lib mjpg_streamer -i "input_file.so -f /tmp/stream -n pic.jpg" -o "output_http.so -w /usr/local/www"
```
visit <ip address of pi>:8080/ to view stream and sample code

#####Nifty command line functions
* `ifconfig` and `iwconfig` tell you about the overall network and wifi status
* `raspistill` and `raspivid` to take pictures and video, respectively
* i2c-tools gives you `i2cdetect` This command allows you to view the addresses of attached i2c devices. Run: `sudo i2cdetect -y 1`


