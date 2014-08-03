include<nerfprojectile.scad>;
include<global_parameters.scad>;

$fn=64;

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
tubeHeight = 55;

//plug
plugRadius = oRingMajorRadius+oRingMinorRadius/2-xy_fudge;
plugHeight = 6*oRingMinorRadius;

//interface
interfaceHeight=tubeOuterRadius-barrelOuterRadius;

//shaft clip
shaftClipSize = 2;

plugClipRadius = 6.5;
plugClipHeight = shaftClipSize+thickness;

//spring
springInnerR=13/2;
springOuterR=16/2;
springH1 = 51;
springH2 = 20; //compressed height

//spring wall
springWallHeight = 1;
springWallThickness = 1;

//handle
handleRadius=10;

//bolts
nBolts = 4;
boltRadius = 1.5;

//stop plate
stopPlateThickness = 3;
stopPlateRadius=tubeOuterRadius+5*boltRadius;

//shaft
shaftLength = tubeHeight+5;
shaftWidth = 4;
shaftNotchSize = 2;
shaftNotchPosition = 20+stopPlateThickness;

module assembly() {


translate([0,0,springH1-shaftLength])
shaft();


color([0.5,0.5,0.5,0.5])
spring();


translate([0,0,springH1+plugHeight/2]) {
rotate([180,0,0])
plug_plug_clip();
color("black")
oring();
}


translate([0,0,-stopPlateThickness])
stop_plate();

difference() {
tube_interface_barrel();
cut_cube();
}

translate([0,0,nerfbodyheight/2+spikePlateThickness+tubeHeight+interfaceHeight])
nerfprojectile();


}

assembly();

//stop_plate();


//spring();
module spring() {
	difference() {
		cylinder(r=springOuterR,h=springH1);

		translate([0,0,-epsilon])
			cylinder(r=springInnerR,h=springH1+2*epsilon);
	}
}

//spring_wall();
module spring_wall() {
	translate([0,0,-epsilon])
	difference() {
		cylinder(r=springOuterR+springWallThickness+xy_fudge,h=springWallHeight+epsilon);

		translate([0,0,-epsilon])
		cylinder(r=springOuterR+xy_fudge,h=springWallHeight+3*epsilon);
	}
}
	
//plug_plug_clipi();
module plug_plug_clip() {
	union() {
		translate([0,0,plugHeight/2])
		plug_clip();

		plug();
		
		translate([0,0,plugHeight/2])
		difference() {
		spring_wall();
		
		translate([0,-shaftWidth/2-xy_fudge,0])
		rotate([-90,0,0])
		plug_clip_cut();
		}
	}
}

//plug_clip();
module plug_clip() {
difference() {
rotate(45)
cylinder(r1=plugClipRadius,r2=shaftWidth+thickness,h=plugClipHeight+epsilon);

translate([0,-shaftWidth/2-xy_fudge,0])
rotate([-90,0,0])
plug_clip_cut();
}
}


//plug_clip_cut();
module plug_clip_cut() {
	linear_extrude(height=shaftWidth*4)
	polygon(points = [
					[shaftWidth/2+xy_fudge,-shaftClipSize],
					[shaftWidth/2+shaftClipSize+xy_fudge+epsilon,epsilon],
					[-shaftWidth/2-shaftClipSize-xy_fudge-epsilon,epsilon],
					[-shaftWidth/2-xy_fudge,-shaftClipSize],
					[-shaftWidth/2-xy_fudge,-shaftLength],
					[shaftWidth/2+xy_fudge,-shaftLength]]);

}



//shaft();
module shaft() {

	union() {
	translate([-shaftWidth/2,-shaftWidth/2,0])
	cube(size=[shaftWidth,shaftWidth,shaftLength]);

	translate([-shaftWidth/2,0,shaftLength-shaftNotchPosition])
	rotate([90,90,180])
	shaft_notch();

	translate([0,0,-handleRadius-shaftWidth/4])
	rotate([90,0,0])
	handle();

	translate([0,0,shaftLength])
	rotate([90,0,0])
	shaft_clip();
	}
}

//shaft_clip();
module shaft_clip() {
	linear_extrude(height=shaftWidth,center=true)
	polygon(points = [[shaftWidth/2,-shaftClipSize],[shaftWidth/2+shaftClipSize,0],[-shaftWidth/2-shaftClipSize,0],[-shaftWidth/2,-shaftClipSize]]);

}


//handle();
module handle() {
	
	rotate_extrude() 
	translate([handleRadius,0,0])
	intersection() {
		circle(r=shaftWidth*0.6);
		square(size=[3*shaftWidth,shaftWidth],center=true);
	}
}	




//shaft_notch()
module shaft_notch() {
	linear_extrude(height=shaftWidth+2*epsilon,center=true)
	polygon(points=[[0,-epsilon],[0,shaftNotchSize],[shaftNotchSize,0],[shaftNotchSize,-epsilon]]);	
}


//stop_plate();
module stop_plate() {

	difference() {
	union() {
	cylinder(r=stopPlateRadius,h=stopPlateThickness);

	translate([0,0,stopPlateThickness])
	spring_wall();
	}
	cube(size=[shaftWidth+shaftClipSize*2+2*xy_fudge,shaftWidth+2*xy_fudge,stopPlateThickness*3],center=true);

	for(i = [1 : nBolts])
		rotate(i*(360/nBolts))
		translate([tubeOuterRadius+3*boltRadius,0,0])
			cylinder(r=boltRadius+xy_fudge,h=3*stopPlateThickness,center=true);	
	}
}

//stop_plate_bracket();
module stop_plate_bracket() {

	difference() {
	cylinder(r=stopPlateRadius,h=stopPlateThickness);

	for(i = [1 : nBolts])
		rotate(i*(360/nBolts))
		translate([tubeOuterRadius+3*boltRadius,0,0])
			cylinder(r=boltRadius+xy_fudge,h=3*stopPlateThickness,center=true);	

	cylinder(r=tubeOuterRadius-epsilon,h=stopPlateThickness*3,center=true);
	}
}

//oring();
module oring(minorRadius=oRingMinorRadius, majorRadius=oRingMajorRadius) {
	rotate_extrude() 
	translate([majorRadius,0])
		circle(r=minorRadius);
}


//plug();
module plug() {
	difference() {
		cylinder(r=plugRadius, h=plugHeight, center=true);
		oring(minorRadius=oRingMinorRadius+xy_fudge, majorRadius=oRingMajorRadius);
	}
}

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

//tube();
module tube() {
	difference() {
		cylinder(r=tubeOuterRadius, h=tubeHeight);

		translate([0,0,-epsilon])
			cylinder(r=tubeInnerRadius,h=tubeHeight+2*epsilon);
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

//tube_interface_barrel();
module tube_interface_barrel() {
	union() {
		tube();
	
		translate([0,0,tubeHeight])
			interface();

		translate([0,0,tubeHeight+interfaceHeight])
			spike_barrel();

		stop_plate_bracket();
	}
}

//cut_cube();
module cut_cube() {
	cube(200,200,200);
}
