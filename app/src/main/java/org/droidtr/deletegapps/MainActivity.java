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
		LinearLayout main = new LinearLayout(getApplicationContext());
		LinearLayout ll = new LinearLayout(getApplicationContext());
		LinearLayout ll2 = new LinearLayout(getApplicationContext());
		main.setPadding(20,10,20,10);
		Button delete = new Button(getApplicationContext());
		delete.setText("Delete(gapps)");
		delete.setSingleLine();
		delete.setLayoutParams(butparam);
		Button deletems = new Button(getApplicationContext());
		deletems.setText("Delete(msapps)");
		deletems.setSingleLine();
		deletems.setLayoutParams(butparam);
		Button safe = new Button(getApplicationContext());
		safe.setText("Delete(no-gms)");
		safe.setLayoutParams(butparam);
		safe.setSingleLine();
		Button disable = new Button(getApplicationContext());
		disable.setText("Disable(gapps)");
		disable.setLayoutParams(butparam);
		disable.setSingleLine();
		Button disablems = new Button(getApplicationContext());
		disablems.setText("Disable(msapps)");
		disablems.setLayoutParams(butparam);
		disablems.setSingleLine();
		Button dalvik =new Button(getApplicationContext());
		dalvik.setText("Clear(dalvik)");
		dalvik.setLayoutParams(butparam);
		dalvik.setSingleLine();
		main.setOrientation(LinearLayout.HORIZONTAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
		ll.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		ll.setOrientation(LinearLayout.VERTICAL);
		ll2.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		ll2.setOrientation(LinearLayout.VERTICAL);
		ll.addView(delete);
		ll.addView(deletems);
		ll.addView(safe);
		ll2.addView(disable);
		ll2.addView(disablems);
		ll2.addView(dalvik);
		main.addView(ll);
		main.addView(ll2);
		setContentView(main);
		super.onCreate(savedInstanceState);
		try{
			final Process process = Runtime.getRuntime().exec("su");
			final DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes("mount -o rw,remount /system\n");
            dataOutputStream.flush();
            dataOutputStream.writeBytes("mount -o rw,remount /vendor\n");
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
						String code="pm list package -f --user 0 | grep google";
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
						dataOutputStream.writeBytes("pm list package -f --user 0 | grep com.android.vending | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\">> /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("pm list package -f --user 0 | grep com.android.chrome | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\">> /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("sh /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.close();
						Toast.makeText(getApplicationContext(),"Gapps deleted succesfuly.",Toast.LENGTH_LONG).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
					}
				}
			});
		deletems.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					try{
						Process process = Runtime.getRuntime().exec("su");
						DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
						dataOutputStream.writeBytes("rm -rf /data/app/*{m,M}icrosoft*  &\n");
						dataOutputStream.flush();
						dataOutputStream.writeBytes("rm -rf /data/data/*{m,M}icrosoft* &\n");
						dataOutputStream.flush();
						//delete all msapps
						String code="pm list package -f --user 0 | grep microsoft | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\"> /data/list\n";
						dataOutputStream.writeBytes(code);
						dataOutputStream.flush();
						dataOutputStream.writeBytes("sh /data/list\n");
						dataOutputStream.flush();
						dataOutputStream.close();
						Toast.makeText(getApplicationContext(),"Microsoft Apps deleted succesfuly.",Toast.LENGTH_LONG).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
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
						String code="pm list package -f --user 0 | grep google";
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
						Toast.makeText(getApplicationContext(),"Gapps cleared succesfuly.",Toast.LENGTH_LONG).show();
					}catch(Exception e){
						Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
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
							Toast.makeText(getApplicationContext(),"Gapps disabled succesfuly.",Toast.LENGTH_LONG).show();
							
						}catch(Exception e){
							Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
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
							dataOutputStream.writeBytes("pm list package | grep microsoft | sed \"s/.*:/pm disable /\"> /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.writeBytes("sh /data/list\n");
							dataOutputStream.flush();
							dataOutputStream.close();
							Toast.makeText(getApplicationContext(),"Msapps disabled succesfuly.",Toast.LENGTH_LONG).show();
							
						}catch(Exception e){
							Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
						}
					}
				});
			dalvik.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View p1)
					{
						try{
							Process process = Runtime.getRuntime().exec("su");
							DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
                            dataOutputStream.writeBytes("rm -rf /data/dalvik-cache/*\n");
                            dataOutputStream.flush();
                            dataOutputStream.writeBytes("reboot\n");
                            dataOutputStream.flush();

						}catch(Exception e){
							Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
						}
					}
				});
				}
		catch (Exception e)
		{
			Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
		}

    }
}
