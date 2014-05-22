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

			$("#camera-options").click(function(){
				$("#camera-options-menu").toggle();
			});

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
			
			function log(msg) {
			    setTimeout(function() {
			        throw new Error("LOG: " + msg);
			    }, 0);
			}

			// call server with updated control state
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

					$.ajax({
						type : "POST",
						headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
						url : "/webrc",
                        dataType : "application/json",
						data : JSON.stringify(dat)
					});
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
				var outputE2 = document.getElementById('result2');
				outputE2.innerHTML = '<b>Result:</b> ' + ' dx:'
						+ joystick_left.deltaX() + ' dy:'
						+ joystick_left.deltaY()
						+ (joystick_left.right() ? ' right' : '')
						+ (joystick_left.up() ? ' up' : '')
						+ (joystick_left.left() ? ' left' : '')
						+ (joystick_left.down() ? ' down' : '');

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
				 var outputEl = document.getElementById('result1');
				 outputEl.innerHTML = '<b>Result:</b> ' + ' dx:'
				 + joystick_right.deltaX() + ' dy:'
				 + joystick_right.deltaY()
				 + (joystick_right.right() ? ' right' : '')
				 + (joystick_right.up() ? ' up' : '')
				 + (joystick_right.left() ? ' left' : '')
				 + (joystick_right.down() ? ' down' : '');

			 if (VirtualJoystick.touchScreenAvailable()) {
				camX = joystick_right.deltaX();
				camY = joystick_right.deltaY();

				update();

			 }

			}, joystickUpdateTime);
			

		});
