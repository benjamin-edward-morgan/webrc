/*
 ___   ___        ___       __  ___   ___                            
|   |=|_.'   .'|=|_.'  .'|=|  ||   | |   |  .'|=|`.                  
`.  |      .'  |  ___.'  | |  ||   | |   |.'  | |  `.                
  `.|=|`.  |   |=|_.'|   |=|.' |   | |   ||   | |   |                
 ___  |  `.|   |  ___|   |  |`.`.  | |  .'`.  | |  .'                
 `._|=|___||___|=|_.'|___|  |_|  `.|=|.'    `.|=|.'                  
                                                                     
       _        ___  ___   ___     ___                 ___ ___   ___ 
  .'|=| `.  .'|`._|=|   |=|_.'.'|=|_.'  .'| |`.   .'|=|_.'|   |=|_.' 
.'  | | .'.'  |     |   |   .'  |     .'  | |  `.'  |  ___`.  |      
|   |=|'. |   |     |   |   |   |     |   |=|   |   |=|_.'  `.|=|`.  
|   | |  ||   |     `.  |   `.  |  ___|   | |   |   |  ___ ___  |  `.
|___|=|_.'|___|       `.|     `.|=|_.'|___| |___|___|=|_.' `._|=|___|
                                                                     
*/

/*~*~*~*~*~*~*~*~*~*~*~*~Servo Design~*~*~*~*~*~*~*~*~*~*~*~*/

/* Description*/
//This code adapted from work protected by the Collective Commons Attribution-ShareAlike 3.0 Unported license http://creativecommons.org/licenses/by-sa/3.0/legalcode. 
//I made changes to this work in order to accurately reflect the servo motors used in the RaspberrySpy project.
//by Melanie Allen


module miniservo(base_x, base_y, base_z, 
	fix_offset, fix_y, fix_z,
	cyl_height, cyl_radius, cyl_offset,
	axe_height, axe_radius) {
	color("LightBlue") cube([base_x, base_y, base_z]);
	color("LightBlue") translate([0, -fix_y, base_z - fix_offset]) 
		cube([base_x, fix_y, fix_z]);
	color("LightBlue") translate([0, base_y, base_z - fix_offset]) 	
difference(){
	cube([base_x, fix_y, fix_z]);
	translate([4,2,0]) cylinder(r=2.2,h=fix_z+1, $fn=100);
	translate([8,2,0]) cylinder(r=2.2,h=fix_z+1, $fn=100);
}

	color("grey") translate([base_x/2, base_y-cyl_offset, base_z]) 
		cylinder(cyl_height, cyl_radius, cyl_radius);
	color("White") translate([base_x/2, base_y-cyl_offset, base_z+cyl_height]) 
		cylinder(axe_height, axe_radius, axe_radius);
}

module TG9() {
	base_x = 12; base_y = 32.5; base_z = 29;
	fix_offset = 5; fix_y = 4; fix_z = 2;
	cyl_height = 5; cyl_radius = 6;	cyl_offset = 6;
	smallcyl_radius = 3; smallcyl_offset = 15;
	axe_height = 3; axe_radius = 2.5;
	union() {
		miniservo(base_x, base_y, base_z,
		 fix_offset, fix_y, fix_z,
		 cyl_height, cyl_radius, cyl_offset,
		 axe_height, axe_radius);
		
		translate([base_x/2, base_y-smallcyl_offset,base_z]) color("Blue") cylinder(cyl_height, smallcyl_radius, smallcyl_radius);
	}
}

//TG9(); //Generate servo