JavaGameEngineQuickstart
========================

An Abstraction of the game engine used in MrNex/GDD2Game2 and MrNex/Noble-Experiment. This provides a quick and easy way to jump into game development in Java using nothing but standard libraries. Compiled through eclipse.

Using
------------------------
This engine is compiled in Eclipse. Importing this into your eclipse workspace as an existing project will allow you to develop a game atop the existing framework.

Engine components
------------------------
The engine itself consists of five main components:
+ Camera Manager: Handles coordinate system construction and affine transformation handling. Allows the viewport to follow an object around the game-world.
+ Collision Mnager: Handles interaction between game objects, resolves collisions of movable game objects.
+ ContentManager: Handles the loading and storing of all data needed in your game. Currently has loaders implemented for images aswell as loading levels from an image (only tested with .png)
+ InputManager: Handles retrieving and storing data sensative to user-input. Detects and handles both mouse and keyboard input.
+ ScreenManager: Handles drawing objects from the engine's current state to the screen.

Objects
------------------------
Everything in the game stems from a base class GameObject. Gameobject's can have states and triggers attached to them, altering their behavior. States and triggers will be explained later on. Gameobject's also have a forward and right vector, allowing for rotations by alteration of the forward vector. A gameobject can represent itself via a BufferedImage courtesy of the ImageLoader, or from a RectangularShape courtesy of Java's 2D Graphics renderer. When gameobject's update they call whatever state is attached to them to update- followed by updating their shape to match whatever data may have been altered by the state. Gameobject's also draw themselves putting their buffered image on the screen. A shape will only be drawn if an object's buffered image is null. After drawing the object will call it's state's draw method. And drawing that takes place inside of an object's state is drawn at a coordinate system which has been translated and rotated to the gameObject's orientation and position. For example, (0, 0) would be the center of your object, and (20, 0) would be a point 20 units in front of wherever your object is facing (What MrNex really means is: (20, 0) would be a point 20 units along your forward vector from the center of your gameobject). 

State
------------------------
The engine runs on finite state machines. There are two state systems simultaneously running in the game engine. There is the Engine's state system and individual object state systems.

**The engine state system** is very simple. It keeps track of one current engine state which holds all objects currently in the game. It will constantly update these objects, calling the object's internal state's update method, remove objects which must be removed, and add objects which are queued to be added to the world. This will repeat until the engine is no longer running. You may swap state's out on the fly.

**The object state system** runs very similarly. Objects keep track of a current state- performing the updating and drawing routine of it's currently attached state. The one difference is object's have states which have special enter and exit methods being called upon attachment and removal. This is because objects are expected to be swapping states much more often and chaotically, so having each state perform it's own setup and cleanup is helpful.

Triggers
----------------------
Triggers served as my quick and easy way to deal with interactions between gameObjects. GameObjects all have an arraylist of triggers which is initialized upon setting the triggerable attribute of a gameObject to true. Triggers contain an action method that is called when a gameobject collides with any other gameObject. The action method of a trigger recieves the gameObject with which it collided and a buffer containing data about the collision. Triggers, like states, also have reference to the object they are attached to.
