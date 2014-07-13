include<nerfprojectile.scad>;
include<global_parameters.scad>;



//wall thickness for barrel and tube
thickness = 2;

//spike
spike_taper = 0.6;
spikeR1=nerfbodyradius_inner-xy_fudge;
spikeR2=spikeR1*spike_taper;
spikeHeight=nerfbodyheight*0.85;
spikePlateThickness = 1.5;

//barrel
barrelInnerRadius = nerfbodyradius_outer+0.3+xy_fudge;
barrelOuterRadius = barrelInnerRadius+thickness;
barrelHeight = nerfbodyheight*0.9;

//oRing
oRingMinorRadius = 1.4/2;
oRingMajorRadius = 10;

//tube
tubeInnerRadius = oRingMajorRadius+oRingMinorRadius;
tubeOuterRadius = tubeInnerRadius+thickness;
tubeHeight = 40;

//plug
plugRadius = oRingMajorRadius+oRingMinorRadius/2-xy_fudge;
plugHeight = 6*oRingMinorRadius;

//interface
interfaceHeight=tubeOuterRadius-barrelOuterRadius;

//pusher
pusherRadius = plugRadius;
pusherHeight = tubeHeight-plugHeight;

tol = 0.1;

$fn = 45;

//hook
hookWidth=8;
hookThickness=3;
hookHeight=2;

//stop plate
stopPlateHeight = hookWidth+hookHeight;


//translate([0,0,nerfbodyheight/2+spikePlateThickness])
//	nerfprojectile();

//spike();
module spike() {
	cylinder(r1=spikeR1, r2=spikeR2, h=spikeHeight);
}

//spike_plate();
module spike_plate() {
	translate([0,0,spikePlateThickness/2])
		cube(size=[barrelInnerRadius*2,spikeR1*2,spikePlateThickness], center=true);
}

//barrel();
module barrel() {
	difference() {
		cylinder(r=barrelOuterRadius, h=nerfbodyheight);

		translate([0,0,-epsilon])
			cylinder(r=barrelInnerRadius,h=nerfbodyheight+2*epsilon);
	}
}

//spike_barrel();
module spike_barrel() {
	union() {
		barrel();
		spike_plate();
		translate([0,0,spikePlateThickness])
			spike();
	}
}

//oring();
module oring(minorRadius=oRingMinorRadius, majorRadius=oRingMajorRadius) {
	rotate_extrude() 
	translate([majorRadius,0])
		circle(r=minorRadius);
}

//tube();
module tube() {
	difference() {
		cylinder(r=tubeOuterRadius, h=tubeHeight);

		translate([0,0,-epsilon])
			cylinder(r=tubeInnerRadius,h=tubeHeight+2*epsilon);
	}
}

//translate([0,0,plugHeight/2+pusherHeight])
//	plug();
module plug() {
	difference() {
		cylinder(r=plugRadius, h=plugHeight, center=true);
		oring(minorRadius=oRingMinorRadius+xy_fudge, majorRadius=oRingMajorRadius);
	}
}

//pusher();
module pusher() {
	translate([0,0,-epsilon])
		cylinder(r=pusherRadius, h=pusherHeight+2*epsilon);
}

//stop_plate();
module stop_plate() {
	cylinder(r=tubeOuterRadius,h=stopPlateHeight);
}

//plunger();
module plunger() {
	union() {
		stop_plate();

		translate([0,0,stopPlateHeight])
			pusher();

		translate([0,0,stopPlateHeight+pusherHeight+plugHeight/2])
			plug();		
	}
}

//plunger_hooks();
module plunger_hooks() {
	union() {
		plunger();

		for(i=[0,90,180,270])
			rotate([0,0,i])
			translate([tubeOuterRadius,0,stopPlateHeight])
			rotate([180,0,0])
				hook();
	}
}
		

//interface();
module interface() {
	difference() {
		cylinder(r2=barrelOuterRadius,r1=tubeOuterRadius,h=interfaceHeight);

		translate([0,0,-epsilon])	
			cylinder(r2=barrelInnerRadius-epsilon,r1=tubeInnerRadius+epsilon,h=interfaceHeight+2*epsilon);
	}
}

//translate([0,0,stopPlateHeight])
//color([0.5,0.5,0.5,0.5])
//tube_interface_barrel();
module tube_interface_barrel() {
	union() {
		tube();
	
		translate([0,0,tubeHeight])
			interface();

		translate([0,0,tubeHeight+interfaceHeight])
			spike_barrel();
	}
}

//tube_interface_barrel_hooks();
module tube_interface_barrel_hooks() {
	union() {
		
		tube_interface_barrel();
		
		difference() {
		for(i=[0,90,180,270])
				rotate([0,0,i])
				translate([tubeOuterRadius,0,-hookWidth+tubeHeight])
				hook();

		translate([0,0,-epsilon])
			cylinder(r=tubeInnerRadius,h=tubeHeight+2*epsilon);
		}

	}
}



//hook();
module hook() {
	rotate([90,0,0])
	linear_extrude(height=hookThickness,center=true)
	polygon(points=[
		[hookWidth,hookWidth],
		[hookWidth,hookWidth+hookHeight],
		[hookWidth/2,hookWidth+hookHeight],
		[hookWidth/2,hookWidth],
		[hookWidth/4,3*hookWidth/4],
		[0,hookWidth],
		[-hookThickness,hookWidth],
		[-hookThickness,0],
		[0,0]]);
}

//plunger_assembly();
module plunger_assembly() {
	color("red")
	plunger_hooks();

	color("black")
	translate([0,0,tubeHeight+stopPlateHeight-plugHeight/2])
	oring();
}

assembly();
module assembly() {
	translate([0,0,stopPlateHeight])
	color([0.5,0.5,0.5])
		difference() {
			tube_interface_barrel_hooks();
			cut_cube();
		}

	plunger_assembly();
	
	translate([0,0,nerfbodyheight/2+spikePlateThickness+stopPlateHeight+tubeHeight+interfaceHeight])
		nerfprojectile();
}
	
//cut_cube();
module cut_cube() {
	cube(200,200,200);
}



