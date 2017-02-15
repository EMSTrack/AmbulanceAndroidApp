package hansyuan.cruzrojamobile;

import java.util.Stack;

/**
 * Created by sinan on 2/8/2017.
 */

public class stackLP {

    //temp boolean
    boolean serverSent = true;

    Stack<LocationPoint> s;
    String name;

    stackLP() {
        s = new Stack<LocationPoint>();
    }

    stackLP(String inputName) {
        s = new Stack<LocationPoint>();
        name = inputName;
    }

    public void insert(LocationPoint input) {
        s.push(input);
    }

    //mostRecent method
    public LocationPoint mostRecent() {
       return s.peek();
    }

    public boolean popIfSent() {
        if (serverSent) {
            s.pop();
            return true; //return true if pop happened
        }
        return false;
    }


    //not needed for now, not finished either
    //find method
    /*public boolean find(LocationPoint toFind) {
        Iterator<LocationPoint> it = s.iterator();
        while(it.hasNext()) {
     }
    }*/

    //insert



}
