
//differential();


module diff_axle1() {
import("OpenRC_110_RC_Differential/Axle_to_CVD.A.6.stl");
}

module diff_center_gear() {
import("OpenRC_110_RC_Differential/Center_Gear.A.3.stl");
}

module diff_side1() {
import("OpenRC_110_RC_Differential/Diff_Casing_Side_1.A.10.stl");
}

module diff_side2() {
import("OpenRC_110_RC_Differential/Diff_Casing_Side_2.A.9.stl");
}

module diff_axle2() {
import("OpenRC_110_RC_Differential/Side_Gear_Axle.A.1.stl");
}

module diff_side_gear() {
import("OpenRC_110_RC_Differential/Side_Gear.A.0.stl");
}



module differential() {
diff_side_gears();
diff_center_gears();
diff_casing();
diff_axles();
}



module diff_casing() {
color([0.5,0.5,0.5,0.5]) {
	diff_side1();

	rotate(90)
	diff_side2();
	}
}

module diff_axles() {
color("purple") {
rotate([0,90,0])
diff_axle2();

translate([0,-6,0])
rotate([90,0,0])
diff_axle1();

translate([0,6,0])
rotate([-90,0,0])
diff_axle1();
}
}

module diff_side_gears() {
color("green") {
rotate([180/15,0,0])
translate([4,0,0])
rotate([0,90,0])
diff_side_gear();

rotate([0,0,0])
translate([-4,0,0])
rotate([0,-90,0])
diff_side_gear();
}
}

module diff_center_gears() {
color("blue") {
translate([0,-4,0])
rotate([90,0,0])
diff_center_gear();

rotate([0,90,0])
translate([0,4,0])
rotate([-90,0,0])
diff_center_gear();
}
}

