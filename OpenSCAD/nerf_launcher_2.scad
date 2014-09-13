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
handleRadius=7;

//bolts
nBolts = 4;
boltRadius = 1.5;

//stop plate
stopPlateThickness = 3;
stopPlateRadius=tubeOuterRadius+5*boltRadius;

//shaft
shaftLength = tubeHeight+10;
shaftWidth = 4;
shaftNotchSize = 2;
shaftNotchPosition = 19+stopPlateThickness;

//latch
latchLength=23;
latchWidth=8;
latchHoleWidth=9;
latchOffset=10;
latchLipLength=2;
latchLipHeight=5;

//solenoid
solenoidHeight=12;
solenoidWidth=11;
solenoidLength=20.5;
solenoidShaftLength=29;
solenoidR1=7/2;
solenoidR2=2.5/2;
solenoidR3=3/2;
solenoidCapHeight=1;
solenoidA=0.5;
solenoidB=3.5;
solenoidPosition=solenoidA;

//brace
braceThickness = 2;
braceHeight=2;

//solenoid backet
solenoidTranslation=latchOffset;
solenoidBracketClipSize=1;

//latch clip
latchClipX=12;
latchClipR=1.2;

//brace();
module brace() {
linear_extrude(height=braceHeight) {
union() {
for(i = [[0,0],[1,0]]) {
mirror(i)
polygon(points = [
		[-epsilon,shaftNotchSize+braceThickness],
		[latchWidth/2+xy_fudge,shaftNotchSize+braceThickness],
		[latchWidth/2+xy_fudge+shaftNotchSize+braceThickness+epsilon,-epsilon],
		[latchWidth/2+xy_fudge+epsilon,-epsilon],
		[latchWidth/2+xy_fudge,shaftNotchSize+xy_fudge],
		[-epsilon,shaftNotchSize+xy_fudge]
		]);
}}}}



//latch_clip();
module latch_clip() {

	translate([-latchClipX,0,0])
	rotate([0,180,0])
	translate([0,0,-epsilon])
	cylinder(r1=latchClipR-epsilon, r2=latchClipR+shaftClipSize,h=shaftClipSize+epsilon);
}


//latch_clip_cut();
module latch_clip_cut() {

	translate([-latchClipX,0,0])
	rotate([0,180,0])
	hull() {
	for(i=[0,2*latchLength])
	translate([i,0,-epsilon])
	cylinder(r1=latchClipR-epsilon+xy_fudge, r2=latchClipR+2*shaftClipSize+2*xy_fudge,h=2*shaftClipSize+epsilon+xy_fudge);
	}
}


module solenoid_bracket() {
union() {
translate([solenoidTranslation,-solenoidHeight/2,0])
cube(size=[solenoidLength+solenoidBracketClipSize+xy_fudge,solenoidHeight,stopPlateThickness]);

translate([solenoidTranslation+solenoidLength+xy_fudge,0,0])
rotate([-90,0,0])
linear_extrude(height=solenoidHeight,center=true)
polygon(points = [
			[0,solenoidBracketClipSize],
			[solenoidBracketClipSize,0],
			[solenoidBracketClipSize,-epsilon],
			[0,-epsilon]
		]);

translate([solenoidTranslation-xy_fudge,0,0])
difference() {
rotate([-90,0,180])
linear_extrude(height=solenoidHeight,center=true)
polygon(points = [
			[0,solenoidBracketClipSize],
			[solenoidBracketClipSize,0],
			[solenoidBracketClipSize,-epsilon],
			[0,-epsilon]
		]);

cube(size=[5*solenoidBracketClipSize,latchWidth+2*xy_fudge,5*solenoidBracketClipSize],center=true);
}}
}

//latch();
//shaft();
stop_plate_brace();


//latch_assembly();
module latch_assembly() {


translate([solenoidTranslation,0,0])
rotate([0,180,0])
	solenoid();


color([1.0,0.0,0.0,0.5])
translate([solenoidA-solenoidPosition,0,0])
latch();



translate([0,0,-shaftLength+shaftNotchPosition-shaftNotchSize-xy_fudge])
  shaft();

color([0.5,0.5,0.5,0.5])
stop_plate_brace();

}



//solenoid();
module solenoid() {

	translate([-solenoidLength,-solenoidHeight/2,0]) {
	color([0.1,0.1,0.8])
	cube(size=[solenoidLength,solenoidHeight,solenoidWidth]);

	translate([(latchLipLength-solenoidCapHeight)/2,0,0])
	translate([solenoidPosition-solenoidShaftLength+solenoidCapHeight+solenoidLength,solenoidHeight/2,solenoidWidth/2])
	rotate([0,90,0]) {
	color([0.9,0.9,0.9]) {
	translate([0,0,epsilon])
		cylinder(r=solenoidR2,h=solenoidShaftLength-2*epsilon);
	
	translate([0,0,solenoidShaftLength-solenoidCapHeight])
		cylinder(r=solenoidR3,h=solenoidCapHeight);	
	}

	color([0.05,0.05,0.05])
	cylinder(r=solenoidR1,h=solenoidShaftLength/3);
	}
	}

}


//solenoid_lip_cut();
module solenoid_lip_cut() {

	translate([solenoidTranslation,0,0])
rotate([0,180,0])
	translate([-solenoidLength,-solenoidHeight/2,0]) {
	translate([(latchLipLength-solenoidCapHeight)/2,0,0])
	translate([solenoidA-solenoidShaftLength+solenoidCapHeight+solenoidLength,solenoidHeight/2,solenoidWidth/2])
	rotate([0,90,0]) {

	union () {
	hull() {
	translate([0,0,epsilon])
		cylinder(r=solenoidR2+xy_fudge,h=solenoidShaftLength-2*epsilon);

	translate([-30,0,epsilon])
		cylinder(r=solenoidR2+xy_fudge,h=solenoidShaftLength-2*epsilon);
	}	

	hull() {
	translate([0,0,solenoidShaftLength-solenoidCapHeight-xy_fudge/2])
		cylinder(r=solenoidR3+xy_fudge,h=solenoidCapHeight+xy_fudge);	

	
	translate([-30,0,solenoidShaftLength-solenoidCapHeight-xy_fudge/2])
		cylinder(r=solenoidR3+xy_fudge,h=solenoidCapHeight+xy_fudge);	

	}}}}

}

//solenoid_lip_cut();
//latch();
module latch() {

	difference() {

	union() {
	translate([latchOffset-latchLength/2-xy_fudge,0,-shaftNotchSize/2])
	cube(size=[latchLength,latchWidth,shaftNotchSize],center=true);

	translate([latchOffset-latchLipLength-xy_fudge,-latchWidth/2,-latchLipHeight-shaftNotchSize])
	cube(size=[latchLipLength,latchWidth,latchLipHeight+epsilon]);
	}


	latch_cut();

	solenoid_lip_cut();

	translate([solenoidB-solenoidA,0,0])
	latch_clip_cut();


	}
}


//latch_cut();
module latch_cut() {

	translate([-shaftNotchSize,0,0])
	rotate([-90,0,180])	
	linear_extrude(height=shaftWidth+epsilon*2,center=true)
	polygon(points = [
			[-latchHoleWidth-xy_fudge,-epsilon],
			[-latchHoleWidth-xy_fudge,shaftNotchSize+epsilon],
			[xy_fudge-epsilon,shaftNotchSize+epsilon],
			[xy_fudge+shaftNotchSize+epsilon,-epsilon]
	]);

}


//assembly();
module assembly() {

/*
rotate(-45)
translate([0,0,springH1-shaftLength])
shaft();
*/

//color([0.5,0.5,0.5,0.5])
//spring();


translate([0,0,shaftNotchPosition-plugHeight/2]) {
rotate([180,0,-45])
plug_plug_clip();
color("black")
oring();
}


rotate(-45)
translate([0,0,-stopPlateThickness])
latch_assembly();

difference() {
tube_interface_barrel();
cut_cube();
}

translate([0,0,nerfbodyheight/2+spikePlateThickness+tubeHeight+interfaceHeight])
nerfprojectile();


}

//assembly();

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

//spring_trench();
module spring_trench() {

	translate([0,0,-springWallHeight])
	difference() {
		cylinder(r=springOuterR+xy_fudge,h=springWallHeight+epsilon);

		translate([0,0,-epsilon])
			cylinder(r=springInnerR-xy_fudge,h=springWallHeight+3*epsilon);
	}

}
	
//plug_plug_clip();
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


//translate([0,0,-shaftLength])
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

module stop_plate_brace() {
union() {
stop_plate();

translate([shaftWidth/2+braceHeight+xy_fudge,0,0])
rotate([0,270,0])
rotate([0,0,90])
brace();

latch_clip();

solenoid_bracket();
}
}

//stop_plate();
module stop_plate() {

	difference() {
	cylinder(r=stopPlateRadius,h=stopPlateThickness);

	translate([0,0,stopPlateThickness])
	spring_trench();
	
	cube(size=[shaftWidth+shaftClipSize*2+2*xy_fudge,shaftWidth+2*xy_fudge,stopPlateThickness*3],center=true);

	for(i = [1 : nBolts])
		rotate(i*(360/nBolts)+45)
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
