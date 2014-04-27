/* rcbattery */

//rcbattery();

module rcbattery() {
	
	color([0.2,0.2,0.2])
	hull() {

		cube([4.32, 2.6, 14.0], center=true);
		rotate([0,90,0]) {
			translate([7,0,0])
				cylinder(r=1.3, h=4.3, $fn=30, center=true);
		}
	}
}

