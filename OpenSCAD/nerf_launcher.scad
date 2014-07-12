include<nerfprojectile.scad>;

spikePlateThickness = 1.5;
thickness = 2;
oringThickness = 1;
oringRadius = 13;
oringPosition = 5;
hookWidth=8;
hookThickness=3;
hookHeight=2;
spikeHeight = nerfbodyheight*0.75;
tubeInnerRadius = oringRadius;
tubeOuterRadius = tubeInnerRadius+thickness;
tubeHeight = 40;
barrelOuterRadius = nerfbodyradius_outer+thickness;
barrelInnerRadius = nerfbodyradius_outer+epsilon;

spikeRadius1 = nerfbodyradius_inner - epsilon;
spikeRadius2 = spikeRadius1/2;

stopPlateThickness = hookWidth+hookHeight;

tol = 0.1;

$fn = 45;


module cutCube() { 
rotate(-45)
translate([0,0,-100])
cube(size=[100,100,200]);
}

/*
difference() 
{
	translate([0,0,nerfbodyheight/2])
	 nerfprojectile();

	color("red")
	cutCube();
}


color("Chartreuse") {
	difference() {
		barrelTube();
		cutCube();
	}
	spike();
}
*/

//translate([0,0,-tubeHeight+barrelOuterRadius-tubeOuterRadius])
//plungerAssembly();

printPlate();

module printPlate() {
	
	translate([0,0,stopPlateThickness])
	plunger();

	translate([30, 30,-(barrelOuterRadius-tubeOuterRadius-tubeHeight)])
	barrelTube();
}


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


module plungerAssembly() {
	color("red")
	plunger();

	color("black")
	translate([0,0,tubeHeight-oringPosition])
	oring();
}
	

module plunger() {

	difference() {
		union() {
		cylinder(r=tubeInnerRadius-tol*2, h=tubeHeight);
		
		translate([0,0,-stopPlateThickness])
			cylinder(r=tubeOuterRadius, h=stopPlateThickness);

		for(i=[0,90,180,270])
		rotate([0,0,i])
		translate([tubeOuterRadius,0,0])
		rotate([180,0,0])
		hook();
		}



		translate([0,0,tubeHeight-oringPosition])
		oring();
	}

}

module oring() {
	
	rotate_extrude() 
	translate([oringRadius,0])
		circle(r=oringThickness/2);
}


module barrelTube() {
	union() {
		//barrel
		difference() {
			cylinder(r=barrelOuterRadius, h=nerfbodyheight);

			translate([0,0,-epsilon])
				cylinder(r=barrelInnerRadius,h=nerfbodyheight+2*epsilon);
		}

		//interface
		difference() {
			translate([0,0,barrelOuterRadius-tubeOuterRadius])
				cylinder(r2=barrelOuterRadius,r1=tubeOuterRadius,h=tubeOuterRadius-barrelOuterRadius);

			translate([0,0,barrelOuterRadius-tubeOuterRadius-epsilon])	
				cylinder(r2=barrelInnerRadius-epsilon,r1=tubeInnerRadius+epsilon,h=tubeOuterRadius-barrelOuterRadius+2*epsilon);
		}

		//tube
		translate([0,0,barrelOuterRadius-tubeOuterRadius-tubeHeight])
		difference() {

			union() {
			cylinder(r=tubeOuterRadius, h=tubeHeight);

			for(i=[0,90,180,270])
				rotate([0,0,i])
				translate([tubeOuterRadius,0,-hookWidth+tubeHeight])
				hook();
			}

			translate([0,0,-epsilon])
				cylinder(r=tubeInnerRadius,h=tubeHeight+2*epsilon);
		}

		

		//spike
		spike();
	}
}

module spike() {
	union() {
	cylinder(r1=nerfbodyradius_inner-tol, r2=(nerfbodyradius_inner-tol)*0.6, h=spikeHeight);

	intersection() 
	{
	translate([0,0,-spikePlateThickness/2])
	cube(size=[(nerfbodyradius_inner-tol)*2,2*tubeOuterRadius,spikePlateThickness],center=true);

	translate([0,0,barrelOuterRadius-tubeOuterRadius])
	cylinder(r2=barrelOuterRadius,r1=tubeOuterRadius,h=tubeOuterRadius-barrelOuterRadius);
	}
	}
}
