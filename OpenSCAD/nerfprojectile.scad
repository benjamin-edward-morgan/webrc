/*rccannon to scale*/

epsilon = 0.01;
nerfbodyheight = 58.5;
nerfbodyradius_outer = 12/2;
nerfbodyradius_inner = 5.5/2;
nerftipheight = nerfbodyheight/2;
nerftipguideheight = 5.0;
nerftipguideradius_outer = 5.5/2;

module nerfbody() {

	color("blue")
	translate([0,0,0])
	difference() {
		cylinder(r=nerfbodyradius_outer, h=nerfbodyheight, $fs=epsilon, center=true);
		cylinder(r=nerfbodyradius_inner, h=nerfbodyheight+epsilon, $fs=epsilon, center=true);
 
				}
}

module nerftip() {
	color("orange")
	union() {
	translate([0,0,nerftipheight])
	cylinder(r1=6, r2=4.5, h=7.5, $fs=0.1);
	translate([0,0,nerftipheight-5])
	difference() {
		cylinder(r=nerftipguideradius_outer, h=nerftipguideheight, $fs=epsilon);
		cylinder(r=1.0, h=nerftipguideheight+epsilon, $fs=epsilon);

				}
			}
}

module nerfprojectile() {
	nerfbody();
	nerftip();

}

nerfprojectile();