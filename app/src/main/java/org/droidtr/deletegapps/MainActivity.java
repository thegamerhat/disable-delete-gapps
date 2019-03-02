package org.droidtr.deletegapps;

import android.app.*;
import android.content.Intent;
import android.net.Uri;
import android.os.*;
import java.io.*;

import java.lang.Process;
import android.widget.*;
import android.view.View.*;
import android.view.*;

public class MainActivity extends Activity 
{

	public void run(String cmd) throws IOException {
		Process process = Runtime.getRuntime().exec("su");
		DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
		dataOutputStream.writeBytes(cmd);
		dataOutputStream.flush();
		dataOutputStream.close();
		
		

	}
	public Button getButton(String label){
		LinearLayout.LayoutParams butparam=new LinearLayout.LayoutParams(-1,-1);
		butparam.weight=1;
		Button b=new Button(getApplicationContext());
		b.setText(label);
		b.setSingleLine();
		b.setLayoutParams(butparam);
		return b;
	}
	public TextView getLabel(String label){
		LinearLayout.LayoutParams butparam=new LinearLayout.LayoutParams(-1,-1);
		butparam.weight=1;
		TextView t=new TextView(getApplicationContext());
		t.setText(label);
		t.setSingleLine();
		t.setLayoutParams(butparam);
		return t;
	}
	public LinearLayout getLinearLayout(){
		LinearLayout ll = new LinearLayout(getApplicationContext());
		ll.setLayoutParams(new LinearLayout.LayoutParams(-1,-2));
		ll.setOrientation(LinearLayout.HORIZONTAL);
		return ll;
	}
	public void deletePackage(String name) throws IOException {
		run("pm list package -f --user 0 | grep "+name+" | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\">> /data/list\n");
	}
	public void disablePackage(String name) throws IOException {
		run("pm disable "+name);
}
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

		LinearLayout main = new LinearLayout(getApplicationContext());
		main.setPadding(20,10,20,10);
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));

		final Button delete = getButton("Delete");
		Button deletems = getButton("Delete");
		Button safe = getButton("Delete without gms");
		final Button disable = getButton("Disable");
		Button disablems = getButton("Disable");
		Button deletefb = getButton("Delete");
		Button disablefb = getButton("Disable");
		Button deleteknx = getButton("Delete");
		Button disableknx = getButton("Disable");
		Button dalvik =getButton("Clear & Reboot");
		Button info = getButton("Telegram Group");

		LinearLayout ll = getLinearLayout();
		ll.addView(delete);
		ll.addView(safe);
		ll.addView(disable);
		main.addView(getLabel("Google Apps (gapps)"));
		main.addView(ll);

		LinearLayout ll2 = getLinearLayout();
		ll2.addView(deletems);
		ll2.addView(disablems);
		main.addView(getLabel("Microsoft Apps (msapps)"));
		main.addView(ll2);

		LinearLayout ll3 = getLinearLayout();
		ll3.addView(deletefb);
		ll3.addView(disablefb);
		main.addView(getLabel("Facebook Apps (fbapps)"));
		main.addView(ll3);

		LinearLayout ll4 = getLinearLayout();
		ll4.addView(deleteknx);
		ll4.addView(disableknx);
		main.addView(getLabel("Samsung Knox"));
		main.addView(ll4);


		LinearLayout lls = getLinearLayout();
		lls.addView(dalvik);
		lls.addView(info);
		main.addView(getLabel("Other"));
		main.addView(lls);

		setContentView(main);
		super.onCreate(savedInstanceState);
		try {
			run("mount -o rw,remount /system\n");
			run("mount -o rw,remount /vendor\n");

		}catch (Exception e)
			{
				Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
			}

			delete.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					try{
						run("sed -i \"s/ro.setupwizard.mode=ENABLED/ro.setupwizard.mode=DISABLED/\" /system/build.prop \n");
						run("rm -rf /data/app/*{g,G}oogle*  &\n");
						run("rm -rf /data/data/*{g,G}oogle* &\n");
						run("rm -rf /data/app/*com.android.vending* &\n");
						run("rm -rf /data/data/*com.android.vending* &\n");
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
						run(code);
						run("pm list package -f --user 0 | grep com.android.chrome | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\">> /data/list\n");
						run("sh /data/list\n");
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
					//delete all msapps
					run("pm list package -f --user 0 | grep microsoft | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\"> /data/list\n");
					run("sh /data/list\n");
					deletePackage("com.skype.raider");
					Toast.makeText(getApplicationContext(),"Microsoft Apps deleted succesfuly.",Toast.LENGTH_LONG).show();
				}catch(Exception e){
					Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		});
		deleteknx.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				try{
					//delete all msapps
					run("pm list package -f --user 0 | grep knox | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\"> /data/list\n");
					run("sh /data/list\n");
                    deletePackage("com.samsung.android.securitylogagent");
                    deletePackage("com.sec.android.providers.security");
                    deletePackage("com.samsung.android.mdm");
                    deletePackage("com.samsung.android.bbc.bbcagent");
                    deletePackage("com.sec.android.app.sysscope");
                    deletePackage("com.samsung.klmsagent");
                    deletePackage("com.sec.android.diagmonagent");
					run("rm -rf /system/container/ContainerAgent2\n");
					run("rm -rf /system/container/KnoxBBCProivder\n");
					run("rm -rf /system/container/KnoxBluetooth\n");
					run("rm -rf /system/container/KnoxKeyguard\n");
					run("rm -rf /system/container/KnoxPackageVerifier\n");
					run("rm -rf /system/container/KnoxShortcuts\n");
					run("rm -rf /system/container/KnoxSwitcher\n");
					run("rm -rf /system/container/resources\n");
					run("rm -rf /system/container/SharedDeviceKeyguard\n");
					run("rm -rf /system/etc/secure_storage/com.sec.knox.store\n");
					run("rm -rf /system/etc/recovery-resource.dat\n");
					run("rm -rf /system/preloadedkiosk/kioskdefault\n");
					run("rm -rf /system/preloadedsso/ssoservice.apk_\n");
					run("rm -rf /system/recovery-from-boot.p\n");
					Toast.makeText(getApplicationContext(),"Samsung Knox deleted succesfuly.",Toast.LENGTH_LONG).show();
				}catch(Exception e){
					Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		});
		deletefb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				try{
					//delete all msapps
					run("pm list package -f --user 0 | grep facebook | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\"> /data/list\n");
					run("sh /data/list\n");
					deletePackage("com.instagram.android");
					deletePackage("com.whatsapp");
					Toast.makeText(getApplicationContext(),"Facebook Apps deleted succesfuly.",Toast.LENGTH_LONG).show();
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
						run("rm -rf /data/app/*{g,G}oogle*  &\n");
						run("rm -rf /data/data/*{g,G}oogle* &\n");
						run("rm -rf /data/app/*com.android.vending* &\n");
						run("rm -rf /data/data/*com.android.vending* &\n");
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
						run(code);
						run("sh /data/list\n");
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
							run("pm list package  | grep -v \"ext.shared\" | grep -v \"packageinstaller\" |  grep -v \"ext.services\" | grep google | sed \"s/.*:/pm disable /\"> /data/list\n");
							run("pm list package  | grep com.android.vending sed \"s/.*:/pm disable /\">> /data/list\n");
							run("pm list package  | grep com.android.chrome sed \"s/.*:/pm disable /\">> /data/list\n");
							run("sh /data/list\n");
							Toast.makeText(getApplicationContext(),"Gapps disabled succesfuly.",Toast.LENGTH_LONG).show();
						}catch(Exception e){
							Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
						}
					}
				});
		disablems.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				try{
					run("pm list package | grep microsoft | sed \"s/.*:/pm disable /\"> /data/list\n");
					run("sh /data/list\n");
					disablePackage("com.skype.raider");
					Toast.makeText(getApplicationContext(),"Microsoft apps disabled succesfuly.",Toast.LENGTH_LONG).show();

				}catch(Exception e){
					Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		});

		disableknx.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				try{
					run("pm list package | grep knox | sed \"s/.*:/pm disable /\"> /data/list\n");
					run("sh /data/list\n");
					disablePackage("com.samsung.android.securitylogagent");
                    disablePackage("com.sec.android.providers.security");
                    disablePackage("com.samsung.android.mdm");
                    disablePackage("com.samsung.android.bbc.bbcagent");
                    disablePackage("com.sec.android.app.sysscope");
                    disablePackage("com.samsung.klmsagent");
                    disablePackage("com.sec.android.diagmonagent");
					Toast.makeText(getApplicationContext(),"Samsung Knox disabled succesfuly.",Toast.LENGTH_LONG).show();

				}catch(Exception e){
					Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
				}
			}
		});

		disablefb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View p1)
			{
				try{
					run("pm list package | grep facebook | sed \"s/.*:/pm disable /\"> /data/list\n");
					run("sh /data/list\n");
					disablePackage("com.instagram.android");
					disablePackage("com.whatsapp");
					Toast.makeText(getApplicationContext(),"Facebook apps disabled succesfuly.",Toast.LENGTH_LONG).show();
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
                            run("rm -rf /data/dalvik-cache/*\n");
                            run("reboot\n");
						}catch(Exception e){
							Toast.makeText(getApplicationContext(),"Fail: "+e.toString(),Toast.LENGTH_LONG).show();
						}
					}
				});
			info.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = "http://t.me/antigapps";
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
				}
			});
				}
    }
