/*
   .___                                                       
 /   \   ___  , __                                          
 |,_-'  /   ` |'  `.                 		                       
 |     |    | |    |                                        
 /     `.__/| /    |                                        
                                                            
  _______    .    .                                         
 '   /    `  |   _/_                                        
     |    |  |    |                                         
     |    |  |    |                                         
     /    / /\__  \__/                                      
                                                            
     .                                   _       .          
    /|      ____   ____   ___  , _ , _   \ ___   |   ,    . 
   /  \    (      (     .'   ` |' `|' `. |/   \  |   |    ` 
  /---'\   `--.   `--.  |----' |   |   | |    `  |   |    | 
,'      \ \___.' \___.' `.___, /   '   / `___,' /\__  `---|.
                                                      \___/  */

//Pan Tilt Assembly

//Description: This file assembles the three motors with the arm.
//by Melanie Allen

/*
module TG9() {
	base_x = 12; base_y = 32.5; base_z = 29;
	fix_offset = 5; fix_y = 4; fix_z = 2;
	cyl_height = 5; cyl_radius = 6;	cyl_offset = 6;
	smallcyl_radius = 3; smallcyl_offset = 15;
	axe_height = 3; axe_radius = 2.5;
*/

include<pantilt.scad>;
include<cameraservo.scad>;
include<RaspberryPiCam.scad>;

motorDisp=14;
//motorSlide=;

translate([xHornScrewEntry,0,extrusionMid]) rotate([0,-90,-90]) translate([-6, -26.5, -34])TG9(); //bottomSide motor
//translate([6,extrusionTop,extrusionMid]) rotate([0,-90,0]) translate([-6, xHornScrewEntry, -34])TG9(); //topSide motor
rotate([0,0,90]) translate([yHornScrewEntry,-topSideWidth,extrusionMid]) rotate([0,-90,-90]) translate([-6, -26.5, -34])TG9(); 

pantilt();
RaspberryPiCam();

translate([12,38,extrusionMid+6]) rasPiCam();