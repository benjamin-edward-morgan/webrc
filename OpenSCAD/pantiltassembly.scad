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

include<pantilt.scad>;
include<cameraservo.scad>;

motorDisp=14;
//motorSlide=;

translate([xHornScrewEntry,0,extrusionMid]) rotate([0,-90,-90]) translate([-6, -26.5, -34])TG9(); //bottomSide motor
//translate([6,extrusionTop,extrusionMid]) rotate([0,-90,0]) translate([-6, xHornScrewEntry, -34])TG9(); //topSide motor
rotate([0,0,90]) translate([yHornScrewEntry,-topSideWidth,extrusionMid]) rotate([0,-90,-90]) translate([-6, -26.5, -34])TG9(); 

pantilt();
