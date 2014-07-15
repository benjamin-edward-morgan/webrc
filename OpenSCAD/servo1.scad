
servo_width = 20.3;
servo_height = 38;
servo_length = 40.1;

servo_bracket_height=2.5;
servo_bracket_distance=10;
servo_bracket_length=55;

screw_r = 2.5;
screw_a = 50;
screw_b = 10;

horn_height=8;
horn_thickness=2;
horn_radius=15;
horn_inner_radius=6;
horn_hole_radius=0.8;
horn_hole_separation=4;
horn_hole_position=10;
horn_center_hole_radius = 4;

horn_offset = 9;

//servo();

module servo() {
	color("grey")
	servo_body();

	color("white")
	translate([0,servo_length/2-horn_offset,servo_bracket_distance])
		servo_horn();

}

module servo_body() {
color("DarkGrey")
translate([0,0,-servo_height+servo_bracket_distance]) {

	difference() {
	union() {
	translate([0,0,servo_height/2])
		cube(size=[servo_width,servo_length,servo_height],center=true);

	translate([0,0,servo_height+servo_bracket_height/2-servo_bracket_distance])
		cube(size=[servo_width,servo_bracket_length,servo_bracket_height],center=true);
	}

	for(a = [screw_a/2,-screw_a/2])
	for(b = [screw_b/2,-screw_b/2])
	translate([b,a,0])
	cylinder(h=servo_bracket_height*30,r=screw_r,$fn=16);
	}
}
}

module servo_horn() {

	color("White")
	difference() {
		union() {
			//wide cylinder
			translate([0,0,horn_height-horn_thickness])
				cylinder(r=horn_radius, h=horn_thickness,$fn=32);
			
			//tall cylinder
			cylinder(r=horn_inner_radius,h=horn_height);
		}

		//small holes
		for(j=[0,90,180,270])
		rotate(j)
		translate([0,horn_hole_position,0])
		for(i=[-horn_hole_separation,0,horn_hole_separation])
		translate([i,0,0])
			cylinder(r=horn_hole_radius,h=2*horn_height,$fn=8);

		//center hole
		cylinder(r=horn_center_hole_radius,h=horn_height*3,center=true,$fn=16);

	}

}