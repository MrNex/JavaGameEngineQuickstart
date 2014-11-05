package mathematics;

/**
 * Vector class for all Vector arithmetic.
 * @author Nex
 *
 */
public class Vec {

	//Attributes
	private int numComponents;
	private double[] components;

	/**
	 * <p>Constructs a default vector with 2 components
	 * Both indices default to 0.</p>
	 */
	public Vec() {
		//Set attributes
		numComponents = 2;
		//Initialize array
		components = new double[numComponents];
	}

	/**
	 * <p>Constructs a vector with a specified number of components.<br>
	 * All components will default to 0</p>
	 * @param nComps The number of components this vector should have
	 */
	public Vec(int nComps) {
		//Set attributes
		numComponents = nComps;
		//Initialize array
		components = new double[numComponents];

		//Set all components to 0
		for(int i = 0; i < numComponents; i++){
			components[i] = 0;
		}
	}

	/**
	 * <p>Constructs a vector as a copy of another vector</p>
	 * @param v The vector you wish to copy
	 */
	public Vec(Vec v){
		numComponents = v.numComponents;

		components = new double[numComponents];

		for(int i = 0; i < numComponents; i++){
			components[i] = v.components[i];
		}
	}

	/**
	 * <p>Constructs a vector from a set of components. Vector will have the number of components given.</p>	 * 
	 * @param comp Components in Vector
	 */
	public Vec(double... comp){
		numComponents = comp.length;
		components = new double[numComponents];

		for(int i = 0; i < numComponents; i++){
			components[i] = comp[i];
		}
	}

	//Accessors
	/**
	 * Gets the component of this vector at a specified index
	 * @param index Index to retrieve
	 * @return A double containing the component at desired index.
	 */
	public double getComponent(int index){
		return components[index];
	}

	/**
	 * <p>Sets a specified component of this vector</p>
	 * @param index Index of component to set
	 * @param val Value to set to
	 */
	public void setComponent(int index, double val){
		components[index] = val;
	}
	
	/**
	 * Increments a specified component of this vector
	 * @param index Index of component to increment
	 * @param val Value to increment component by
	 */
	public void incrementComponent(int index, double val){
		components[index] += val;
	}


	//Methods
	/**
	 * Copies the contents of a vector into this vector
	 * @param v The vector to copy
	 */
	public void copy(Vec v){
		numComponents = v.numComponents;
		for(int i = 0; i < numComponents; i++)
			components[i] = v.components[i];
	}
	
	/**
	 * Gets the magnitude of this vector
	 * @return Returns the magnitude of this vector
	 */
	public double getMag(){
		double sumSq = 0;
		for(int i = 0; i < numComponents; i++){
			sumSq += Math.pow(components[i], 2);
		}
		return Math.sqrt(sumSq);
	}

	/**
	 * Set the magnitude of this vector while maintaining direction.
	 * @param newMag Desired magnitude
	 */
	public void setMag(double newMag){
		normalize();

		//Multiply each component by the new mag
		for(int i = 0; i < numComponents; i++){
			components[i] *= newMag;
		}
	}

	/**
	 * Create a new vector with set magnitude in the direction of an existing vector
	 * @param v Vector with desired direction
	 * @param newMag Desired magnitude
	 * @return Returns a new vector in the direction of specified vector with a magnitude as specified.
	 */
	public static Vec setMag(Vec v, double newMag){
		Vec returnVector = normalize(v);
		returnVector.setMag(newMag);

		return returnVector;
	}
	
	/**
	 * Limits the magnitude of this vector to a certain cap
	 * @param cap The number to limit the magnitude of this vector to
	 */
	public void limit(double cap){
		if(getMag() > cap) setMag(cap);
	}

	/**
	 * Normalizes this vector.
	 * Maintains direction while giving this vector a magnitude of 1.
	 * Converts this vector to a unit vector.
	 * I'm not sure how else I can say this.
	 */
	public void normalize(){
		double mag = getMag();

		for(int i = 0; i < numComponents; i++){
			components[i] /= mag;
		}
	}

	/**
	 * <p>Creates a unit {@link Vec} in the direction of an  vector</p> 
	 * @param v Vector to normalize
	 * @return A vector with magnitude 1 in the direction of parameter (v)
	 */
	public static Vec normalize(Vec v){
		Vec returnVector = new Vec(v);
		returnVector.normalize();
		return returnVector;
	}
	
	/**
	 * Gets the angle in the XY plane from the positive X axis that this vector is pointing in.
	 * @return The angle (in radians) from the positive X axis that this vector points
	 */
	public double getAngle(){
		return Math.atan2(components[1], components[0]);
	}

	/**
	 * <p>Increments this Vector by another Vector</p>
	 * @param v Vector to increment this vector by
	 */
	public void add(Vec v){
		if(sizeCheck(v)){
			for(int i = 0; i < numComponents; i++){
				components[i] += v.components[i];
			}
		}
	}

	/**
	 * <p>Returns the sum of two vectors</p>
	 * @param v1 Initial vector
	 * @param v2 Vector to add to initial vector
	 * @return Vector containing the sum of the two parameters(v1 + v2)
	 * @return If v1 and v2 are of different size,
	 * 	returns empty Vector 'sum' of size v1
	 */
	public static Vec add(Vec v1, Vec v2){
		Vec sum = new Vec(v1.numComponents);
		
		if(sizeCheck(v1,v2)){
			for(int i = 0; i < v1.numComponents; i++){
				sum.components[i] = v1.components[i] + v2.components[i];
			}
		}
		return sum;
	}

	/**
	 * <p>Decrements this vector by vector v</p>
	 * @param v The vector to take away from this vector.
	 */
	public void subtract(Vec v){
		if(sizeCheck(v)){
			for(int i = 0; i < numComponents; i++){
				components[i] -= v.components[i];
			}
		}
	}

	/**
	 * Subtracts Vector v2 from Vector v1
	 * @param v1 The initial vector
	 * @param v2 The vector to take away from v1
	 * @return A vector containing the difference of the two parameters (V1 - V2).
	 * @return If v1 and v2 are of different size,
	 * 	returns empty Vector 'difference' of size v1
	 */
	public static Vec subtract(Vec v1, Vec v2){
		Vec difference = new Vec(v1.numComponents);

		if(sizeCheck(v1,v2)){
			for(int i = 0; i < v1.numComponents; i++){
				difference.components[i] = v1.components[i] - v2.components[i];
			}
		}

		return difference;
	}
	
	/**
	 * Scales this vector by the scaleFactor
	 * @param scaleFactor Scalefactor the scale the vector by
	 */
	public void scalarMultiply(double scaleFactor){
		for(int i = 0; i < numComponents; i++){
			components[i] *= scaleFactor;
		}
	}
	
	/**
	 * Scales a given vector by a specified scale value
	 * @param v Vector to scale
	 * @param scaleFactor amount to scale by
	 * @return A NEW vector which represents the components of v scaled by the scaleFactor
	 */
	public static Vec scalarMultiply(Vec v, double scaleFactor){
		Vec returnVec = new Vec(v.numComponents);
		returnVec.copy(v);
		for(int i = 0; i < v.numComponents; i++){
			returnVec.components[i] *= scaleFactor;
		}
		
		return returnVec;
	}

	/**
	 * <p>Calculates the dot product of this vector with another vector</p>
	 * @param v Vector to dot with
	 * @return A double containing the scalar(dot) product of the vectors
	 * @return if v and 'this' are of different sizes, returns 0.0
	 */
	public double dot(Vec v){
		double prod = 0.0;
		
		if(sizeCheck(v)){
			for(int i = 0; i < numComponents; i++){
				prod += components[i] * v.components[i];
			}
		}

		return prod;
	}

	/**
	 * <p>Calculates the dot product of two vectors</p>
	 * @param v1 The first vector being dotted
	 * @param v2 The second vector being dotted
	 * @return A double containing the scalar(dot) product of the vectors.
	 * @return if v1 and v2 are of different sizes, returns 0.0
	 */
	public static double dot(Vec v1, Vec v2){
		double prod = 0.0;

		if(sizeCheck(v1,v2)){
			for(int i = 0; i < v1.numComponents; i++){
				prod += v1.components[i] * v2.components[i];
			}
		}

		return prod;
	}
	
	/**
	 * Geometrically rotates a vector by a specified degree angle in XY plane
	 * Converts degrees to radians and calls normal rotate function
	 * @param deg angle in degrees by which to rotate.
	 */
	public void rotateByDeg(double deg){
		rotate((deg*Math.PI)/ 180.0);
	}
	
	/**
	 * Geometrically rotates a vector by a specified radian angle in the XY plane
	 * @param rad angle in radians to rotate vector
	 */
	public void rotate(double rad){
		//Store components
		double c1 = getComponent(0);
		double c2 = getComponent(1);
		setComponent(0, (Math.cos(rad) * c1 - Math.sin(rad) * c2));
		setComponent(1, (Math.sin(rad) * c1 + Math.cos(rad) * c2));
	}
	
	/**
	 * Rotates a specified vector by a specified radian angle
	 * @param v Vector to rotate
	 * @param rad Angle in radians to rotate it by
	 * @return A new vector representing a rotated v
	 */
	public static Vec rotate(Vec v, double rad){
		//Create a new vector identical to v
		Vec returnVector = new Vec(v);
		
		//GEt the components
		double c1 = returnVector.getComponent(0);
		double c2 = returnVector.getComponent(1);
		
		
		
		returnVector.setComponent(0, (Math.cos(rad) * c1 - Math.sin(rad) * c2));
		returnVector.setComponent(1, (Math.sin(rad) * c1 + Math.cos(rad) * c2));
		
		return returnVector;
	}
	
	/**
	 * Rotates a vector by a specified degree angle
	 * Converts degrees to radians and calls normal Vec.Rotate function
	 * @param v Vector to rotate
	 * @param deg Degree angle to rotate by
	 * @return A new vector representing a rotated v
	 */
	public static Vec rotateByDeg(Vec v, double deg){
		return Vec.rotate(v, (deg*Math.PI)/180.0);
	}
	
	/**
	 * @param v is the vector that 'this' is being operated by
	 * @return returns true if v has same numComponents as 'this'
	 */
	private boolean sizeCheck(Vec v){
		if(v.numComponents != this.numComponents){
			return false;
		}
		return true;
	}
	
	/**
	 * @param v1 vector to compare
	 * @param v2 is the vector that v1 is being operated by
	 * @return returns true if v1 has same numComponents as v2
	 */
	private static boolean sizeCheck(Vec v1, Vec v2){
		if(v1.numComponents != v2.numComponents){
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Creates a string representation of this vector
	 * @return A string containing the components of his vector.
	 */
	public String toString(){
		String returnString = "[";
		for(int i = 0; i < numComponents; i++){
			returnString += components[i];
			if(i < numComponents - 1){
				returnString += ", ";
			}
		}
		returnString += "]";
		
		return returnString;
	}

}
