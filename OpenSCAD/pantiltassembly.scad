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
motorHeight=10;
motor2Height=-60;
motor2Disp=-20;
motor2Slide=-25;

rotate([0,-90,0]) translate([motorDisp,35,-40])TG9(); //bottomSide motor
rotate([0,90,90]) translate([motor2Disp,motor2Height,motor2Slide]) TG9(); //topSide motor
pantilt();
