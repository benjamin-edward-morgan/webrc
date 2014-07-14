include<global_parameters.scad>


spring_wire_diameter_in = 0.045;
spring_wire_r = spring_wire_diameter_in*mm_in_1in/2;

spring_diameter_in = (0.45+0.33)/2;
spring_r = spring_diameter_in*mm_in_1in/2;

spring_height_in = 2.5;
spring_height = spring_height_in*mm_in_1in;

spring_coil_pitch_in = 0.17;
spring_coil_pitch = spring_coil_pitch_in*mm_in_1in;

spring_coils = spring_height/spring_coil_pitch;

linear_extrude(height=spring_height,$fn=60,twist=360*spring_coils) {
translate([spring_r,0])
scale([1,2])
circle(r=spring_wire_r, $fn=10);
}



