
//dcmotor();


epsilon = 0.1;
module dcmotor(
	motorRadius=18,
	motorHeight=50,
	motorScrewHeight=10,
	motorScrewN=6,
	motorScrewRadius=1.5,
	motorScrewDist=25/2,
	motorShaftHeight=14.5,
	motorShaftRadius=1.5) {

//motor body
color("red")
difference() 
{
	cylinder(h=motorHeight,r=motorRadius);
	
	//screw holes
	for(i=[1:motorScrewN])
		rotate(360/motorScrewN*i)
			translate([motorScrewDist,0,motorHeight-motorScrewHeight])
				cylinder(h=motorScrewHeight+epsilon,r=motorScrewRadius,$fn=8);
}

//shaft
color("grey")
translate([0,0,motorHeight])
	cylinder(h=motorShaftHeight, r=motorShaftRadius,$fn=8);
}
