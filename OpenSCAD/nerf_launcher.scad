include<nerfprojectile.scad>;

thickness = 3;
tubeInnerRadius = 10;
tubeOuterRadius = tubeInnerRadius+thickness;
tubeHeight = 50;
barrelOuterRadius = nerfbodyradius_outer+thickness;
barrelInnerRadius = nerfbodyradius_outer+epsilon;

spikeRadius1 = nerfbodyradius_inner - epsilon;
spikeRadius2 = spikeRadius1/2;

$fn = 45;

/*
translate([0,0,nerfbodyheight/2])
   nerfprojectile();
*/
barrelTube();


module barrelTube() {
	color("green")
	union() {
		//barrel
		difference() {
			cylinder(r=barrelOuterRadius, h=nerfbodyheight);

			translate([0,0,-epsilon])
				cylinder(r=barrelInnerRadius,h=nerfbodyheight+2*epsilon);
		}

		//interface
		difference() {
			translate([0,0,barrel = 5.5/2;OuterRadius-tubeOuterRadius])
				cylinder(r2=barrelOuterRadius,r1=tubeOuterRadius,h=tubeOuterRadius-barrelOuterRadius);

			translate([0,0,barrelOuterRadius-tubeOuterRadius-epsilon])	
				cylinder(r2=barrelInnerRadius-epsilon,r1=tubeInnerRadius+epsilon,h=tubeOuterRadius-barrelOuterRadius+2*epsilon);
		}

		//tube
		translate([0,0,barrelOuterRadius-tubeOuterRadius-tubeHeight])
		difference() {
			cylinder(r=tubeOuterRadius, h=tubeHeight);

			translate([0,0,-epsilon])
				cylinder(r=tubeInnerRadius,h=tubeHeight+2*epsilon);
		}
	}
}

