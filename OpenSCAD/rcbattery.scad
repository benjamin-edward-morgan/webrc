/* rcbattery */

//rcbattery();

module rcbattery() {
	
	color([0.2,0.2,0.2])
	hull() {

		cube([43.2, 26, 140], center=true);
		rotate([0,90,0]) {
			translate([70,0,0])
				cylinder(r=13, h=43, $fn=30, center=true);
		}
	}
}

