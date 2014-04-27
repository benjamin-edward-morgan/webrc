
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

//diff_axle1();
diff_side_gears();
diff_center_gears();
diff_casing();
//diff_axles();



module diff_casing() {
//diff_side1();

rotate(90)
diff_side2();
}

module diff_axles() {
rotate([0,90,0])
diff_axle2();
}

module diff_side_gears() {
rotate([180/15,0,0])
translate([4,0,0])
rotate([0,90,0])
diff_side_gear();

rotate([0,0,0])
translate([-4,0,0])
rotate([0,-90,0])
diff_side_gear();
}

module diff_center_gears() {
translate([0,-4,0])
rotate([90,0,0])
diff_center_gear();

rotate([0,90,0])
translate([0,4,0])
rotate([-90,0,0])
diff_center_gear();
}

