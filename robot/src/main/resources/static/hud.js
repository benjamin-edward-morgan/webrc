$(document).ready(
		function() {
		
            var pi = 3.1415926535;
            var gridN = 20;
            var gridSize = 4000;


			var scene = new THREE.Scene();
			var camera = new THREE.PerspectiveCamera(45, window.innerWidth/window.innerHeight, 0.1, 10000);
		    camera.position.z = 180;
            camera.rotation.y = pi/2;
            camera.rotation.z = pi/2;


            var overlayCanvas = document.getElementById("overlayCanvas");
			var renderer = new THREE.WebGLRenderer({canvas: overlayCanvas, alpha:true});
			renderer.setSize(window.innerWidth, window.innerHeight);


			//var knot_geometry = new THREE.TorusKnotGeometry( 1, .3, 100, 16 );
			//var knot_material = new THREE.MeshPhongMaterial({specular: 0xaaaaaa, color: 0x1010cc});
			//var knot = new THREE.Mesh(knot_geometry, knot_material);
			//scene.add(knot);

			var line_material = new THREE.LineBasicMaterial({
                color: 0xffffff,
                linewidth: 3
            });


            var grid = new THREE.Object3D();
            for(i=0;i<=gridN;i++) {
                var line_geometry_1 = new THREE.Geometry();
                line_geometry_1.vertices.push(
                    new THREE.Vector3( -gridSize/2, -gridSize/2+i*gridSize/gridN, 0 ),
                    new THREE.Vector3( gridSize/2, -gridSize/2+i*gridSize/gridN, 0 )
                );
                var line_1 = new THREE.Line( line_geometry_1, line_material );
                grid.add(line_1);

                var line_geometry_2 = new THREE.Geometry();
                line_geometry_2.vertices.push(
                    new THREE.Vector3(-gridSize/2+i*gridSize/gridN, -gridSize/2,  0 ),
                    new THREE.Vector3(-gridSize/2+i*gridSize/gridN, gridSize/2,  0 )
                );
                var line_2 = new THREE.Line( line_geometry_2, line_material );
                grid.add(line_2);

            }
            scene.add( grid );



			var light = new THREE.PointLight( 0xffffff, 1, 100 );
			light.position.set( 10, 10, 10 );
			scene.add( light );


            var lastRender = new Date().getTime();
			var render = function () {
				requestAnimationFrame(render);

                var newLastRender = new Date().getTime();
                var elapsed = newLastRender-lastRender;
                lastRender=newLastRender;

				//knot.rotation.x += 0.001*elapsed;
				//knot.rotation.y += 0.001*elapsed;

				renderer.render(scene, camera);
			};

			render();

});