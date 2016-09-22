package mip.nz.stopwatch;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView tvTime = null;
    private TextView tvNanoSec = null;
    private Button btnStart = null;
    private Button btnPause = null;
    private Button btnStop = null;
    private LinearLayout ll = null;

    private int min  = 00;
    private int hour = 00;
    private int sec  = 00;
    private int nanosec  = 00;
    private int countPause = 0;
    private boolean isStart = false;
    private boolean isPause = false;
    private boolean isStop = false;
    private Handler handler = new Handler();
    private StartWatchThread swt = null;
    private DecimalFormat df = new DecimalFormat("00");
    private Typeface font = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get time VIEW
        tvTime = (TextView)findViewById(R.id.tvTime);
        // Get nanosec VIEW
        tvNanoSec = (TextView)findViewById(R.id.tvNanoSec);


        //Set custom font
        font = Typeface.createFromAsset(getAssets(), "fonts/FFF_Tusj.ttf");
        tvTime.setTypeface(font);
        tvTime.setTextSize(55);

        // GET LAYOUT
        ll = (LinearLayout)findViewById(R.id.dataLayout);




        //Get buttons
        btnStart = (Button)findViewById(R.id.btnStart);
        btnPause = (Button)findViewById(R.id.btnPause);
        btnStop = (Button)findViewById(R.id.btnStop);


    }


    public void doStart(View view){

        btnStart.setEnabled(false);

        if(!isStart) isStart = true;

        if(!btnPause.isEnabled()) btnPause.setEnabled(true);

        swt = new StartWatchThread();
        swt.start();

    }


    //DO PAUSE
    public void doPause(View view){



        isStart = false;
        btnPause.setEnabled(false);
        btnStart.setEnabled(true);
        countPause++;
        TextView tv = new TextView(this);
        tv.setText(countPause+".  "+df.format(hour)+":"+df.format(min)+":"+df.format(sec)+"."+nanosec);
        tv.setTextSize(35);
        tv.setTextColor(Color.parseColor("#ffff99"));
        tv.setTypeface(font);
        tv.setPadding(40,5,5,5);

        ll.addView(tv);

        //tv.setLayoutParams(ll, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    }


    //DO STOP
    public void doStop(View view){
        btnPause.setEnabled(false);
        btnStart.setEnabled(true);

            isStart = false;
            hour = 0;
            min = 0;
            sec = 0;

        tvTime.setText( df.format(hour)+":"+df.format(min)+":"+df.format(sec) );
        ll.removeAllViews();
    }



    class StartWatchThread extends Thread{




        public void run(){

            while(isStart){

                nanosec++;
                if(nanosec == 999){
                    nanosec = 0;
                    sec++;
                }

                if(sec == 59){
                    sec = 0;
                    min++;
                }
                if(min == 59){

                    min = 0;
                    hour ++;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        final int  n = nanosec;
                        // SHOW TIME
                        tvTime.setText( df.format(hour)+":"+df.format(min)+":"+df.format(sec) );
                        tvNanoSec.setText(String.valueOf(n));

                    }
                });




                // Sleep the main thread to get 1 sec
                try{
                    Thread.sleep(1);
                }catch(Exception e){
                    Log.d("DEBUG : Error : ",e.getMessage());
                }

            }// END WHILE


        }//END run()


    }

}
