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
		if(Build.VERSION.SDK_INT>21){
			setTheme(android.R.style.Theme_Material_Dialog);
		}else if(Build.VERSION.SDK_INT>14){
			setTheme(android.R.style.Theme_Holo_Dialog);
		}else{
			setTheme(android.R.style.Theme_Dialog);
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        LinearLayout.LayoutParams butparam=new LinearLayout.LayoutParams(-1,-1);
        butparam.weight=1;
		LinearLayout main = new LinearLayout(this);
		LinearLayout ll = new LinearLayout(this);
		LinearLayout ll2 = new LinearLayout(this);
		final TextView tt = new TextView(this);
		tt.setText("Disable: disable gapps\nRemove:delete gapps with gmscore\nClear:delete gapps without gmscore\nReboot: reboot system");
		main.setPadding(20,10,20,10);
		Button delete = new Button(this);
		delete.setText("Delete");
		delete.setSingleLine();
		delete.setLayoutParams(butparam);
		Button safe = new Button(this);
		safe.setText("Clear");
		safe.setLayoutParams(butparam);
		safe.setSingleLine();
		Button disable = new Button(this);
		disable.setText("Disable");
		disable.setLayoutParams(butparam);
		disable.setSingleLine();
		Button reboot =new Button(this);
		reboot.setText("Reboot");
		reboot.setLayoutParams(butparam);
		reboot.setSingleLine();
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
		ll.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		ll2.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		ll.addView(delete);
		ll.addView(safe);
		ll2.addView(disable);
		ll2.addView(reboot);
		main.addView(ll);
		main.addView(ll2);
		main.addView(tt);
		setContentView(main);
		super.onCreate(savedInstanceState);
		try{
			final Process process = Runtime.getRuntime().exec("su");
			final DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
			dataOutputStream.writeBytes("mount -o rw,remount /system\n");	
			dataOutputStream.flush();
			dataOutputStream.close();
			delete.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					try{
						Process process = Runtime.getRuntime().exec("su");
						DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
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
						//delete all gapps
						String code="pm list package -f | grep google";
						String[] list ={
								"com.google.android.ext.services",
								"com.google.android.packageinstaller",
								"com.google.android.ext.shared",
						};
						for(int i=0;i<list.length;i++){
							code=code+" | grep -v \""+list[i]+"\"";
						}
						code=code+" | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\"> /data/list\n";
						dataOutputStream.writeBytes(code);
						dataOutputStream.flush();
						dataOutputStream.writeBytes("pm list package -f | grep com.android.vending | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\">> /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("pm list package -f | grep com.android.chrome | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\">> /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("sh /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.close();
						tt.setText("Gapps deleted succesfuly.");
					}catch(Exception e){
						tt.setText("Fail: "+e.toString());
					}
				}
			});
			safe.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					try{
						Process process = Runtime.getRuntime().exec("su");
						DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
						dataOutputStream.writeBytes("rm -rf /data/app/*{g,G}oogle*  &\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("rm -rf /data/data/*{g,G}oogle* &\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("rm -rf /data/app/*com.android.vending* &\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("rm -rf /data/data/*com.android.vending* &\n");
						dataOutputStream.flush();
						//delete without gmscore
						String code="pm list package -f | grep google";
						String[] list ={
								"com.google.android.gms",
								"com.google.android.ext.services",
								"com.google.android.apps.nexuslauncher",
								"com.google.android.setupwizard",
								"com.google.android.packageinstaller",
								"com.google.android.ext.shared",
								"com.google.android.onetimeinitializer",
								"com.google.android.configupdater",
								"com.google.android.webview",
								"com.google.android.syncadapters.contacts",
								"com.google.android.gsf",
								"com.google.android.partnersetup",
								"com.google.android.feedback",
								"com.google.android.printservice.recommendation",
								"com.google.android.syncadapters.calendar",
								"com.google.android.gsf.login",
								"com.google.android.backuptransport",

						};
						for(int i=0;i<list.length;i++){
							code=code+" | grep -v \""+list[i]+"\"";
						}
						code=code+" | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\"> /data/list\n";
						dataOutputStream.writeBytes(code);
						dataOutputStream.flush();
						dataOutputStream.writeBytes("sh /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.close();
						tt.setText("Gapps cleared succesfuly.");
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
							Process process = Runtime.getRuntime().exec("su");
							DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
							dataOutputStream.writeBytes("pm list package  | grep -v \"ext.shared\" | grep -v \"packageinstaller\" |  grep -v \"ext.services\" | grep google | sed \"s/.*:/pm disable /\"> /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.writeBytes("pm list package  | grep com.android.vending sed \"s/.*:/pm disable /\">> /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.writeBytes("pm list package  | grep com.android.chrome sed \"s/.*:/pm disable /\">> /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.writeBytes("sh /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.close();
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
							Process process = Runtime.getRuntime().exec("su");
							DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
							dataOutputStream.writeBytes("reboot\n");
							dataOutputStream.flush();

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
