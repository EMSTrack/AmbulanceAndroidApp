package hansyuan.cruzrojamobile;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class LPBackupExplorer extends AppCompatActivity {
    private final String tableContents = "00_TableOfContents.txt";
    private ArrayList<Button> listButtons = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpbackup_explorer);
        readTableOfContents();

    }





/**
 * Displays, in order of most recent, the list of posts.
 * Each link will be the date/time, and is a link to
 * open the file and display its contents.
 */





    /**
     * Automatically initiate a toast based on the string parameter.
     * @param toToast
     */
    protected void toasting(String toToast){
        Context context = getApplicationContext();
        CharSequence text = toToast;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    /**
     *
     * @return
     */
    protected String readTableOfContents() {
        String ret = "";
        ArrayList<File> fileList = new ArrayList<File>();

        try {
            /*

            // Open the table of contents.
            //inFile = openFileInput(tableContents);
            FileReader fr = new FileReader(
                    new File( this.getFilesDir(), tableContents) );
            BufferedReader br = new BufferedReader(fr);

            // Read as many as possible (probably want to change to BR)
            while (true) {
                String currLine = br.readLine();

                if(currLine != null)
                {
                    ret += currLine + "\n";
                    fileList.add(currLine);
                }

                else
                {
                    break;
                }
            }*/

            File path = new File("/sdcard/", "LPs");

            File[] s = path.listFiles();
            for (File f: s) {
                fileList.add(f);
            }



        } catch (Exception e) { e.printStackTrace(); }

        //toasting(ret);
        forEachFileCreateButton(fileList);
        return ret;

    }

    /**
     *
     */
    protected void forEachFileCreateButton(ArrayList<File> all)
    {
        /** Iterate through each file in the table of contents
         * and pass file name to next method. */
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        /** Read as many as possible (probably want to change to BR) */
        for ( File curr: all)
        {
            final String currFile = curr.getName();
            Button button = new Button(this);
            button.setText(currFile);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayFile(currFile);
                }
            });
            linearLayout.addView(button);
            listButtons.add(button);
        }
    }


    /**
     *
     *
     */
    protected void displayFile(String filename)
    {
        String content = "";
        try {

            // Open the table of contents.
            //inFile = openFileInput(tableContents);
            FileReader fr = new FileReader(
                    new File( this.getFilesDir(), filename) );
            BufferedReader br = new BufferedReader(fr);

            // Read as many as possible (probably want to change to BR)
            while (true) {
                String currLine = br.readLine();

                if(currLine != null)
                {
                    content += currLine + "\n";
                }

                else
                {
                    break;
                }
            }


        } catch (Exception e) { e.printStackTrace(); }

        //toasting(content);

    }


}
