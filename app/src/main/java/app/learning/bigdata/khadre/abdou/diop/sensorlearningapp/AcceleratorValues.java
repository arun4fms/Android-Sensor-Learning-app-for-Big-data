package app.learning.bigdata.khadre.abdou.diop.sensorlearningapp;

/**
 * @author  Abdou khadre DIOP
 * @since 22/02/2017
 * This class represent one set of accelerometer values
 */

public class AcceleratorValues {
    private float x;
    private float y;
    private float z;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public AcceleratorValues(float x, float y, float z) {
        setX(x);
        setY(y);
        setZ(z);
    }
}
