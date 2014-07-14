include<servo1.scad>
include<RaspberryPiCam.scad>
include<global_parameters.scad>

enclosure_width=29.8;
enclosure_front_z=10;

thickness=2;

/*

translate([-servo_width/2-thickness,20,-5])
rotate([0,90,180])
camera_assembly();
*/

servo_mount_height = 10;
servo_mount_hole_r = 0.9;

support_thickness = 1.0;

arm_a = 20;
arm_b = 47;
arm_width = horn_radius*2+2;
arm_thickness=3;

clear_blue=[0.5,0.5,1.0,0.75];

phi = 0;
theta = 180;


pan_tilt_assembly();

module pan_tilt_assembly() {

	rotate(180)
	translate([0,-servo_length/2+horn_offset,-servo_bracket_distance-horn_height])
		servo_body();

	rotate(theta) {
		translate([0,0,-horn_height])
			servo_horn();

		translate([0,-arm_a,arm_b])
		rotate([0,phi,0]){
		rotate([90,0,0]) 
		translate([0,-servo_length/2+horn_offset,-servo_bracket_distance-horn_height]) {
				servo_body();

				color(clear_blue)
					pantiltbracket1();

				translate([-servo_width/2-2*thickness,20,-5])
				rotate([0,90,180])
				camera_assembly();
		}

		rotate([90,0,0])
		translate([0,0,-horn_height])
		servo_horn();
		}

		color(clear_blue)
			arm();
	}
}


//pantiltbracket1();
//arm();


module arm() {
difference() {
union() {
difference() {
hull() {
cylinder(h=arm_thickness,r=arm_width/2,$fn=16);

translate([-arm_width/2,-arm_a-arm_thickness,0])
	cube(size=[arm_width,arm_thickness,arm_thickness]);
}
	//small holes
		for(j=[0,90,180,270])
		rotate(j)
		translate([0,horn_hole_position,0])
		for(i=[-horn_hole_separation,0,horn_hole_separation])
		translate([i,0,0])
			cylinder(r=horn_hole_radius,h=2*horn_height,$fn=8,center=true);

			//center hole
		cylinder(r=horn_center_hole_radius,h=horn_height*3,center=true,$fn=16);

}

translate([0,-arm_a,0])
rotate([90,0,0])
translate([0,arm_b,0])
difference() {
hull() {
cylinder(h=arm_thickness,r=arm_width/2,$fn=16);

translate([-arm_width/2,-arm_b-epsilon,0])
	cube(size=[arm_width,arm_thickness,arm_thickness]);
}
	//small holes
		for(j=[0,90,180,270])
		rotate(j)
		translate([0,horn_hole_position,0])
		for(i=[-horn_hole_separation,0,horn_hole_separation])
		translate([i,0,0])
			cylinder(r=horn_hole_radius,h=2*horn_height,$fn=8,center=true);

			//center hole
		cylinder(r=horn_center_hole_radius,h=horn_height*3,center=true,$fn=16);

}

translate([0,-arm_a,arm_thickness])
cube(size=[arm_width,arm_thickness*2,arm_thickness*2],center=true);
}
translate([0,-arm_a+arm_thickness,2*arm_thickness])
rotate([0,90,0])
cylinder(r=arm_thickness,h=arm_width*2,center=true);
}
}
	

//servo();
//pantiltbracket1();

module pantiltbracket1() {
difference() {
union() {
translate([-servo_width/2-thickness,-servo_length/2-xy_fudge-thickness,-enclosure_width+servo_bracket_distance])
cube(size=[thickness+servo_width,servo_length+2*xy_fudge+2*thickness,enclosure_width]);

translate([-thickness/2,0,-servo_mount_height/2-xy_fudge])
cube(size=[servo_width+thickness,servo_bracket_length,servo_mount_height],center=true);

}

translate([epsilon,0,0])
cube(size=[servo_width+epsilon,servo_length+2*xy_fudge,3*enclosure_width],center=true);

translate([0,0,servo_bracket_height/2])
cube(size=[servo_width+2*epsilon,2*servo_bracket_length,servo_bracket_height+2*xy_fudge],center=true);

for(a = [screw_a/2,-screw_a/2])
for(b = [screw_b/2,-screw_b/2])
translate([b,a,0])
cylinder(r=servo_mount_hole_r,h=2*servo_height,center=true,$fn=32);

}
}



module camera_assembly() {

color([0.5,0.5,0.5,0.5]) {
import("Raspberry_pi_camera_caseenclosure/enclosure_back_hack.stl");
import("Raspberry_pi_camera_caseenclosure/enclosure_front_hack.stl");
}

translate([-12.6,2.5,3])
rasPiCam();

}




//hacked a piece of troublesom plastic off of the part and exported as 
enclosure_back_z=-2;
enclosure_hack_w=1.8;
enclosure_hack_x=1.9;

//back_hack();
module back_hack() {
difference() {
translate([-enclosure_width/2,0,enclosure_back_z])
import("Raspberry_pi_camera_caseenclosure/raspberri_pi_camera_case_back_v0.4.STL");

translate([-enclosure_width/2,enclosure_hack_x,0])
cube(size=[enclosure_width,enclosure_hack_w,10]);
}
}

//front_hack();
module front_hack() {
translate([enclosure_width/2,0,enclosure_front_z])
rotate([0,180,0])
import("Raspberry_pi_camera_caseenclosure/raspberri_pi_camera_case_front_v0.4.2.STL");
}

