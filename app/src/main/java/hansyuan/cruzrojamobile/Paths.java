package hansyuan.cruzrojamobile;


import java.util.Stack;

/**
 * Created by sinan on 2/8/2017.
 */

public class Paths {

    Stack<LocationPoint> s = new Stack<LocationPoint>();

    //create a queue of location point
        //name of path
        //


    //find method

    //insert
    public void insert(LocationPoint input) {
        s.push(input);
    }

    //mostRecent method

    public LocationPoint mostRecent() {
        return LocationPoint r = s.pop();
    }


}
