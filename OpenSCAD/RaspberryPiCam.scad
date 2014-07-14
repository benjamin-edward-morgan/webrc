/*
   _____                       
 / ___/__ ___ _  ___ _______ _
/ /__/ _ `/  ' \/ -_) __/ _ `/
\___/\_,_/_/_/_/\__/_/ _\_,_/ 
  / __/__  ___  / /__ / /     
 / _// _ \/ _ \/ (_-</_/      
/_/  \___/\___/_/___(_)  */

//Raspberry Pi Camera model

//For use with Pan Tilt Assembly file.

epsilon=.1;
width= 25;
height= 24;
depth=2;
camDispSide= 8.5;
camDispTop=12.5;
camDispBottom=5.5;
camSide=8;
holeD=2;
holeDisp=2;
holeDispHole=21;
holesDisp=9.5;

module cameraBack(){
cube([width,height,depth]);
}

//cameraBack();

module camera(){
translate([camDispSide,camDispBottom,depth])cube([camSide,camSide,depth]);
}
//camera();

module baseCam(){ union(){color("PaleGreen") cameraBack(); color("DarkBlue") camera();}

}

module hole(){
cylinder(h=depth+2*epsilon,r=holeD/2,center=false);
}

module holes(){
translate([holeDisp,holeDisp,-epsilon]) hole();

translate([holeDisp,camDispTop,-epsilon]) hole();

translate([holeDispHole+holeDisp,camDispTop,-epsilon]) hole();

translate([holeDispHole+holeDisp,holeDisp,-epsilon]) hole();
}

//holes();

module rasPiCam(){
difference(){baseCam(); translate([0,holesDisp,0])holes();}
}

//rasPiCam();

