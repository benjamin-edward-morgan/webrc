
include<motor.scad>;
include<dev_boards.scad>;
include<rcbattery.scad>;

translate([20+26/2,150,50])
rotate([0,90,90])
rcbattery();

wheelW = 150;

module wheel() {
	import("OpenRC_Truggy_Rim.A.1.stl");
}


module 4wheels() {
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

4wheels();

translate([-50,50,50])
rotate([0,90,0])
dcmotor();


translate([-20,175,50])
rotate([0,90,180])
raspberry_pi();