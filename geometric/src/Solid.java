import java.util.Arrays;
import java.util.List;

public class Solid {

    Face [] f;
    int size;

    public Solid(List<Face> faces, int size){
        this.f = new Face[size];
        this.f = faces.toArray(this.f);
        this.size = size;
    }

    public Solid() {

    }

    public Face[] getFaces(){
        return Arrays.copyOf(f, f.length);
    }
}
