package org.droidtr.deletegapps;

import android.app.*;
import android.os.*;
import java.io.*;

import java.lang.Process;
import android.widget.*;
import android.view.View.*;
import android.view.*;

public class MainActivity extends Activity 
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        
		LinearLayout ll = new LinearLayout(this);
		final TextView tt = new TextView(this);
		Button delete = new Button(this);
		delete.setText("Delete");
		Button disable = new Button(this);
		disable.setText("Disable");
		Button reboot =new Button(this);
		reboot.setText("Reboot");
		ll.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
		ll.addView(delete);
		ll.addView(disable);
		ll.addView(reboot);
		ll.addView(tt);
		setContentView(ll);
		super.onCreate(savedInstanceState);
		try{
			final Process process = Runtime.getRuntime().exec("su");
			final DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
			dataOutputStream.writeBytes("mount -o rw,remount /system\n");
			dataOutputStream.flush();
			dataOutputStream.writeBytes("sed -i \"s/ro.setupwizard.mode=ENABLED/ro.setupwizard.mode=DISABLED/\" /system/build.prop \n");
			dataOutputStream.flush();
			dataOutputStream.writeBytes("rm -rf /data/app/*{g,G}oogle*  &\n");
			dataOutputStream.flush();
			dataOutputStream.writeBytes("rm -rf /data/data/*{g,G}oogle* &\n");
			dataOutputStream.flush();
			dataOutputStream.writeBytes("rm -rf /data/app/*com.android.vending* &\n");
			dataOutputStream.flush();
			dataOutputStream.writeBytes("rm -rf /data/data/*com.android.vending* &\n");
			dataOutputStream.flush();
			delete.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					try{
					dataOutputStream.writeBytes("pm list package -f | grep google | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\"> /data/list\n");
					dataOutputStream.flush();
					dataOutputStream.writeBytes("pm list package -f | grep com.android.vending | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\">> /data/list\n");
					dataOutputStream.flush();
					dataOutputStream.writeBytes("sh /data/list\n");
					dataOutputStream.flush();
					tt.setText("Gapps deleted succesfuly.");
					}catch(Exception e){
						tt.setText("Fail: "+e.toString());
					}
				}
			});
			disable.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						try{
							dataOutputStream.writeBytes("pm list package  | grep google | sed \"s/.*:/pm disable /\"> /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.writeBytes("pm list package  | grep com.android.vending sed \"s/.*:/pm disable /\">> /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.writeBytes("sh /data/list\n");
							dataOutputStream.flush();
							tt.setText("Gapps disabled succesfuly.");
							
						}catch(Exception e){
							tt.setText("Fail: "+e.toString());
						}
					}
				});
			reboot.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						try{
							dataOutputStream.writeBytes("reboot\n");
							dataOutputStream.flush();
							tt.setText("Gapps disabled succesfuly.");

						}catch(Exception e){
							tt.setText("Fail: "+e.toString());
						}
					}
				});
				}
		catch (Exception e)
		{
			tt.setText("Fail: "+e.toString());
		}

    }
}
