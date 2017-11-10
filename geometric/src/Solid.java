public class Solid {

    Face [] faces;
    int size;

    public void add_face(Face face){
        faces[size] = face;
    }

    public int getSize() {
        return size;
    }

    public Face[] getFaces() {
        return faces;
    }

    public void setFaces(Face[] faces) {
        this.faces = faces;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
