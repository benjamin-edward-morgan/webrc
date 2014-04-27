/*      :::::::::     :::     ::::    :::      ::::::::::: ::::::::::: :::    :::::::::::    
     :+:    :+:  :+: :+:   :+:+:   :+:          :+:         :+:     :+:        :+:         
    +:+    +:+ +:+   +:+  :+:+:+  +:+          +:+         +:+     +:+        +:+          
   +#++:++#+ +#++:++#++: +#+ +:+ +#+          +#+         +#+     +#+        +#+           
  +#+       +#+     +#+ +#+  +#+#+#          +#+         +#+     +#+        +#+            
 #+#       #+#     #+# #+#   #+#+#          #+#         #+#     #+#        #+#             
###       ###     ### ###    ####          ###     ########### ########## ###              
        :::   :::    ::::::::  :::::::::: ::::::::   ::::::::                              
      :+:+: :+:+:  :+:    :+: :+:       :+:    :+: :+:    :+:                              
    +:+ +:+:+ +:+ +:+    +:+ +:+       +:+    +:+ +:+                                      
   +#+  +:+  +#+ +#+    +:+ :#::+::#  +#+    +:+ +#++:++#++                                
  +#+       +#+ +#+    +#+ +#+       +#+    +#+        +#+                                 
 #+#       #+# #+#    #+# #+#       #+#    #+# #+#    #+#                                  
###       ###  ########  ###        ########   ########                                    */             

/*~*~*~*~*~*~*~*~*~*************Pan Tilt Mechanism**************~*~*~*~*~*~*~*~*~*~*~*~*/


// This pan tilt mechanism functions to maneuver the camera for the RaspberrySpy car. //


/*Description: The pan tilt mechanism consists of a tiltArm, two horns, two servo motors. The tiltArm connects to the two horns of two servo motors. The horns are permanentally connected to the motor via a screw and two screw pins. 

Assuming front view, one servo connects to the underSide of the piece and the other servo connects to the vertical topSide. Controlled by touch screen operation, the tiltArm maneuvers the camera to look up and town with vertical swipes of the screen and side to side with horizontal swipe input. 

With a top view orientation the part appears as an L shape, a flat rectangle along the x axis and a flat rectangle joined with a half circle on the y axis. Two hexagons are extruded into the piece for use for inserting the screws into the horns. Hexagons rather than circles were chosen for this purpose to prevent 45 degree rule printing complications.*/

epsilon=.1; //This small value prevents problematic rendering.

/*Creating the Arm*/

/*Set the value of the extrusionHeight and the topSide and underSide rectangles that make up the tiltArm. */
extrusionHeight=20;
extrusionMid= extrusionHeight/2; //This value used to get the hornScrewEntries in the middle.
extrusionTop= extrusionMid*0.5;
extrusionBottom=extrusionHeight-extrusionTop;
topSideHeight=64;
underSideWidth=topSideHeight;                                                                                                  
topSideWidth=4;
underSideHeight=topSideWidth;

/*Set the position of the hornScrewEntrys.*/
xHornScrewEntry=37;
yHornScrewEntry=57;

module tiltArm(){
//Join the topSide and underSide
	union(){

	/*Construct topSide*/
	cube(size=[topSideHeight,topSideWidth,extrusionHeight], center=false); //Construct topSide rectangle
	translate(topSideHeight, 0, extrusionMid)
	translate([0,topSideHeight,extrusionMid])
	rotate([0,90,0])
	cylinder(h=topSideWidth,r=extrusionMid, center=false, $fn=100); //Construct curved surface
	/*Construct underSide*/
	cube(size=[underSideHeight,underSideWidth,extrusionHeight], center=false);//Construct underSide rectangle
	}
}
//tiltArm(); 

//end of Creating the Arm

/*Constructing the hornScrewEntrys*/
module hornScrewEntry(){
	//topSide hornScrew
	translate([xHornScrewEntry,topSideWidth+epsilon,extrusionMid])
	rotate([90,0,0])
	cylinder(h=topSideWidth+2*epsilon, r=3, center=false, $fn=6);

	//underSide hornScrew
	translate([-epsilon,yHornScrewEntry,extrusionMid])
	rotate([0,90,0])
	cylinder(h=topSideWidth+2*epsilon, r=3, center=false, $fn=6);
}

/*Subtracting the hornScrewEntrys*/
module insertHornScrews(){
difference(){
	tiltArm(); hornScrewEntry();
	}
}

/*Constructing the screwPinEntrys*/
module screwPinEntry(){
	//undersideSide pinScrews
	translate([xHornScrewEntry,topSideWidth+epsilon,extrusionTop])
	rotate([90,0,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //top

	translate([xHornScrewEntry,topSideWidth+epsilon,extrusionBottom])
	rotate([90,0,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //bottom

	translate([xHornScrewEntry+6,topSideWidth+epsilon,extrusionMid])
	rotate([90,0,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //left

	translate([xHornScrewEntry-6,topSideWidth+epsilon,extrusionMid])
	rotate([90,0,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //right

	//topSide pinScrews
	translate([-epsilon,yHornScrewEntry,extrusionTop])
	rotate([0,90,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //top

	translate([-epsilon,yHornScrewEntry,extrusionBottom])
	rotate([0,90,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //bottom

	translate([-epsilon,yHornScrewEntry-6,extrusionMid])
	rotate([0,90,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //left

	translate([-epsilon,yHornScrewEntry+6,extrusionMid])
	rotate([0,90,0])
	cylinder(h=topSideWidth+2*epsilon, r=1, center=false, $fn=6); //right
}

/*Subtracting the screwPinEntrys*/
difference(){
	insertHornScrews(); screwPinEntry();	
}


