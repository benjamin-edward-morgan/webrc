
$(document).ready(function() {

function HUD() {

            //parameters
            this.gridN = 20;
            this.gridSize = 4000;
            this.camFieldOfView = 45;

            //screen and camera, canvas and renderer
			this.scene = new THREE.Scene();
			this.camera = new THREE.PerspectiveCamera(this.camFieldOfView, window.innerWidth/window.innerHeight, 0.1, 10000);
            this.overlayCanvas = document.getElementById("overlayCanvas");
			this.renderer = new THREE.WebGLRenderer({canvas: this.overlayCanvas, alpha:true});

            //materials
			this.line_material = new THREE.LineBasicMaterial({
                color: 0xffffff,
                linewidth: 3
            });

            //objects
            this.grid = new THREE.Object3D();

            //lights (unnecessary?)
			this.light = new THREE.PointLight( 0xffffff, 1, 100 );


            //render loop
            this.lastRender = new Date().getTime();

			this.render = function () {

                var newLastRender = new Date().getTime();
                var elapsed = newLastRender - this.lastRender;
                this.lastRender=newLastRender;

				//knot.rotation.x += 0.001*elapsed;
				//knot.rotation.y += 0.001*elapsed;

				this.renderer.render(this.scene, this.camera);
			};

			//init

			            this.camera.position.z = this.cameraZ;
                        this.camera.rotation.y = Math.PI/2;
                        this.camera.rotation.z = Math.PI/2;
                        this.renderer.setSize(window.innerWidth, window.innerHeight);

                        for(i=0;i<=this.gridN;i++) {
                            var line_geometry_1 = new THREE.Geometry();
                            line_geometry_1.vertices.push(
                                new THREE.Vector3( -this.gridSize/2, -this.gridSize/2+i*this.gridSize/this.gridN, 0 ),
                                new THREE.Vector3( this.gridSize/2, -this.gridSize/2+i*this.gridSize/this.gridN, 0 )
                            );
                            var line_1 = new THREE.Line( line_geometry_1, this.line_material );
                            this.grid.add(line_1);

                            var line_geometry_2 = new THREE.Geometry();
                            line_geometry_2.vertices.push(
                                new THREE.Vector3(-this.gridSize/2+i*this.gridSize/this.gridN, -this.gridSize/2,  0 ),
                                new THREE.Vector3(-this.gridSize/2+i*this.gridSize/this.gridN, this.gridSize/2,  0 )
                            );
                            var line_2 = new THREE.Line( line_geometry_2, this.line_material );
                            this.grid.add(line_2);

                        }
                        this.scene.add( this.grid );

                        this.light.position.set( 10, 10, 10 );
                        this.scene.add( this.light );


                        this.render();

                        requestAnimationFrame(this.render);

};

    var hud = new HUD();
});

