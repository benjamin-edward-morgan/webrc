#webrc
=====
An awesome java platform for raspberry pi to create autonomous and remote controlled robots.

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

* comment out the following line with #
```
1:2345:respawn:/sbin/getty —nuclear 38400 ttyl1
```
* and below it add:
```
1:2345:respawn:/bin/login -f pi tty1 </dev/tty1 >/dev/tty1 2>&1
```

######Set up WiFi:
* run `sudo nano /etc/wpa_supplicant/wpa_supplicant.conf`

* add:
```
network={
    ssid="yourNetworkSSID"
    psk="yourNetworkPassword"
    }
```

######Enable I2C
* type `sudo nano /etc/modules`
* append following lines
```
i2c-bcm2708 
i2c-dev 
```
