include<differential_assembly.scad>
include<motor.scad>;
include<dev_boards.scad>;
include<rcbattery.scad>;
include<pantilt1.scad>;

translate([0,250,50])
rotate(90)
pan_tilt_assembly();

translate([20+26/2,150,50])
rotate([0,90,90])
rcbattery();

wheelW = 150;

module wheel() {
	import("OpenRC_Truggy_Rim.A.1.stl");
}

rotate(90)
differential();


module 4wheels() {
color("silver") {
	translate([wheelW/2,0,0])
	rotate(90)
		wheel();

	translate([wheelW/2,250,0])
	rotate(90)
		wheel();

	translate([-wheelW/2,0,0])
	rotate(-90)
		wheel();

	translate([-wheelW/2,250,0])
	rotate(-90)
		wheel();
}
}

4wheels();

translate([-50,50,50])
rotate([0,90,0])
dcmotor();


translate([-20,175,50])
rotate([0,90,180])
raspberry_pi();