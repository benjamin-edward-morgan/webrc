$(document).ready(
		function() {

			var driveMax = 175;
			var camMax = 175;

			var driveX = 0;
			var driveY = 0;
			var camX = 0;
			var camY = 0;

			var driveX_0 = 0;
			var driveY_0 = 0;
			var camX_0 = 0;
			var camY_0 = 0;

			var driveKeyLeft = 65;
			var driveKeyUp = 83;
			var driveKeyDown = 87;
			var driveKeyRight = 68;

			var camKeyLeft = 37;
			var camKeyDown = 40;
			var camKeyUp = 38;
			var camKeyRight = 39;
			
			var joystickUpdateTime = 100;

            var cometd = $.cometd;

            var subscription = null;

			$(document).keydown(function(key) {
				switch (parseInt(key.which, 10)) {
				case driveKeyLeft:
					$('.button.a').css('background-color', 'red');
					driveX = -driveMax;
					update();
					break;
				case driveKeyUp:
					$('.button.s').css('background-color', 'red');
					driveY = driveMax;
					update();
					break;
				case driveKeyDown:
					$('.button.w').css('background-color', 'red');
					driveY = -driveMax;
					update();
					break;
				case driveKeyRight:
					$('.button.d').css('background-color', 'red');
					driveX = driveMax;
					update();
					break;

				case camKeyLeft:
					$('.button.left').css('background-color', 'red');
					camX = -camMax;
					update();
					break;
				case camKeyUp:
					$('.button.up').css('background-color', 'red');
					camY = -camMax;
					update();
					break;
				case camKeyRight:
					$('.button.right').css('background-color', 'red');
					camX = camMax;
					update();
					break;
				case camKeyDown:
					$('.button.down').css('background-color', 'red');
					camY = camMax;
					update();
					break;
				}
			});

			$(document).keyup(function(key) {
				switch (parseInt(key.which, 10)) {
				case driveKeyLeft:
					$('.button.a').css('background-color', 'black');
					driveX = 0;
					update();
					break;
				case driveKeyUp:
					$('.button.s').css('background-color', 'black');
					driveY = 0;
					update();
					break;
				case driveKeyDown:
					$('.button.w').css('background-color', 'black');
					driveY = 0;
					update();
					break;
				case driveKeyRight:
					$('.button.d').css('background-color', 'black');
					driveX = 0;
					update();
					break;

				case camKeyLeft:
					$('.button.left').css('background-color', 'black');
					camX = 0;
					update();
					break;
				case camKeyUp:
					$('.button.up').css('background-color', 'black');
					camY = 0;
					update();
					break;
				case camKeyRight:
					$('.button.right').css('background-color', 'black');
					camX = 0;
					update();
					break;
				case camKeyDown:
					$('.button.down').css('background-color', 'black');
					camY = 0;
					update();
					break;

				}
			});

			// call server with updated control state
			// use cometd to push commands to vehicle
			function update() {
				
				if (driveX != driveX_0 || driveY != driveY_0
						|| camX != camX_0 || camY != camY_0) {
					
					driveX_0=driveX;
					driveY_0=driveY;
					camX_0=camX;
					camY_0=camY;
					
					var dat = {};
					dat['steering'] = driveX_0;
					dat['speed'] = driveY_0;
					dat['pan'] = camX_0;
					dat['tilt'] = camY_0;

                    cometd.publish('/data/up', dat);

                }
			}

			// one on the left of the screen
			var joystick_left = new VirtualJoystick({
				container : document.getElementById("container"),
				strokeStyle : 'orange',
				mouseSupport : true
			});
			
			joystick_left.addEventListener('touchStartValidation', function(
					event) {
				var touch = event.changedTouches[0];
				if (touch.pageX >= window.innerWidth / 2)
					return false;
				
				return true
			});
			
			setInterval(function() {

			if (VirtualJoystick.touchScreenAvailable()) {
				driveX = joystick_left.deltaX();
				driveY = joystick_left.deltaY();

				update();
			}

			},  joystickUpdateTime);

			// one on the right of the screen
			var joystick_right = new VirtualJoystick({
				container : document.getElementById("container"),
				strokeStyle : 'cyan',
				mouseSupport : true
			});

			joystick_right.addEventListener('touchStartValidation', function(
					event) {
				var touch = event.changedTouches[0];
				if (touch.pageX < window.innerWidth / 2)
					return false;
				return true
			});

			setInterval(function() {

			 if (VirtualJoystick.touchScreenAvailable()) {
				camX = joystick_right.deltaX();
				camY = joystick_right.deltaY();

				update();

			 }

			}, joystickUpdateTime);


                function _connectionEstablished()
                {
                    console.log("connection established");
                    updateConnectionStatus("Connected");
                }

                function _connectionBroken()
                {
                    console.log("connection broken");
                    updateConnectionStatus("Disconnected");
                    updateWifiStatus(-1);
                    updateBatteryStatus(-1);
                    updateBatteryVoltage(null);
                }

                function _connectionClosed()
                {
                    console.log("connection closed");
                    updateConnectionStatus("Disconnected");
                    updateWifiStatus(-1);
                    updateBatteryStatus(-1);
                    updateBatteryVoltage(null);
                }

                function _handlemessage(message)
                {
                    console.log("recieved: " + JSON.stringify(message));

                    if(message.data.signal) {
                        var signal = message.data.signal;
                        updateWifiStatus(signal);
                    }

                    if(message.data.battery_voltage) {
                        var voltage = message.data.battery_voltage;
                        updateBatteryVoltage(voltage);
                    }

                    if(message.data.battery_percentage) {
                        var status = message.data.battery_percentage;
                        updateBatteryStatus(status);
                    }
                }

                function updateBatteryVoltage(voltage) {
                    var text = document.getElementById("batteryVoltage");

                    if(voltage != null) {
                        text.innerHTML=Math.round(voltage*100)/100 + " V";
                    } else {
                        text.innerHTML="--V";
                    }

                }

                function updateConnectionStatus(status) {
                    var text = document.getElementById("connectionStatus");

                    text.innerHTML=status;
                }

                function updateWifiStatus(signal) {
                    var icon = document.getElementById("wifiIcon");

                    if(signal >= 80) {
                        icon.src="icons/wifi5.svg";
                    } else if(signal >= 64) {
                        icon.src="icons/wifi4.svg";
                    } else if(signal >= 48) {
                        icon.src="icons/wifi3.svg";
                    } else if(signal >= 32) {
                        icon.src="icons/wifi2.svg";
                    } else if(signal >= 16) {
                        icon.src="icons/wifi1.svg";
                    } else if(signal >= 0) {
                        icon.src="icons/wifi0.svg";
                    } else {
                        icon.src="icons/wifi-wtf.svg";
                    }
                }

                function updateBatteryStatus(status) {
                    var icon = document.getElementById("batteryIcon");

                    if(status >= 80) {
                        icon.src="icons/batt5.svg";
                    } else if(status >= 64) {
                        icon.src="icons/batt4.svg";
                    } else if(status >= 48) {
                        icon.src="icons/batt3.svg";
                    } else if(status >= 32) {
                        icon.src="icons/batt2.svg";
                    } else if(status >= 16) {
                        icon.src="icons/batt1.svg";
                    } else if(status >= 0) {
                        icon.src="icons/batt0.svg";
                    } else {
                        icon.src="icons/batt-wtf.svg";
                    }

                }

                // Function that manages the connection status with the Bayeux server
                var _connected = false;
                function _metaConnect(message)
                {
                    if (cometd.isDisconnected())
                    {
                        _connected = false;
                        _connectionClosed();
                        return;
                    }

                    var wasConnected = _connected;
                    _connected = message.successful === true;
                    if (!wasConnected && _connected)
                    {
                        _connectionEstablished();
                    }
                    else if (wasConnected && !_connected)
                    {
                        _connectionBroken();
                    }
                }

                // Function invoked when first contacting the server and
                // when the server has lost the state of this client
                function _metaHandshake(handshake)
                {
                    if (handshake.successful === true)
                    {
                        console.log("handshake successful...");

                        console.log("subscribing to server updates...");
                        subscription = cometd.subscribe('/data/down', _handlemessage);
                    }
                }

                // Disconnect when the page unloads
                $(window).unload(function()
                {
                    cometd.disconnect(true);
                });

                var cometURL = location.protocol + "//" + location.host + "/webrc/bayeux";
                cometd.configure({
                    url: cometURL,
                    logLevel: 'info'
                });

                cometd.addListener('/meta/handshake', _metaHandshake);
                cometd.addListener('/meta/connect', _metaConnect);

                cometd.handshake();


		});
