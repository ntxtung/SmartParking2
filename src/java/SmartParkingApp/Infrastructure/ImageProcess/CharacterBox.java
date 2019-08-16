package SmartParkingApp.Infrastructure.ImageProcess;

import org.opencv.core.Mat;

class CharacterBox implements Comparable<CharacterBox>{

    private Mat img;
    private int x,y;

    public CharacterBox(Mat img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
    }

    public Mat getMat() {
        return this.img;
    }

    @Override
    public int compareTo(CharacterBox o) {
        int minDistance = img.height()/2;
        int deltaY = this.y - o.y;
        if (deltaY <= -minDistance)
            return -1;
        else if (deltaY >= minDistance)
            return 1;
        else {
            if (this.x < o.x)
                return -1;
            else
                return 1;
        }
    }

}