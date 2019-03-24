package org.droidtr.deletegapps;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.*;
import java.io.*;

import java.lang.Process;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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
	public void copyFromInternet(String url,String path){
		Toast.makeText(getApplicationContext(),"Downloading...",Toast.LENGTH_LONG).show();
		new Download(getApplicationContext()).execute(url,path);

	}
	public Button getButton(String label){
		LinearLayout.LayoutParams butparam=new LinearLayout.LayoutParams(-2,-1);
		butparam.weight=1;
		Button b=new Button(getApplicationContext());
		b.setText(label);
		b.setTextColor(Color.WHITE);
		GradientDrawable gd = new GradientDrawable();
		gd.setStroke(10,0);
		gd.setCornerRadius(15);
		gd.setColor(Color.DKGRAY);
		b.setBackgroundDrawable(gd);
		b.setLayoutParams(butparam);
		b.setSingleLine(true);
		return b;
	}
	public TextView getLabel(String label){
		LinearLayout.LayoutParams butparam=new LinearLayout.LayoutParams(-1,-1);
		butparam.weight=1;
		TextView t=new TextView(getApplicationContext());
		t.setText(label);
		t.setTextColor(Color.BLACK);
		t.setTypeface(Typeface.create("",Typeface.BOLD));
		t.setSingleLine();
		t.setPadding(7,7,7,7);
		t.setLayoutParams(butparam);
		return t;
	}
	public TextView getLabel(String label,boolean singleLine){
		TextView t= getLabel(label);
		t.setSingleLine(singleLine);
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
		LinearLayout main = new LinearLayout(getApplicationContext());
		main.setPadding(20,20,20,20);
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(-1,-1));
		GradientDrawable gd = new GradientDrawable();
		gd.setStroke(2,Color.BLACK);
		gd.setCornerRadius(15);
		gd.setColor(Color.LTGRAY);

		final Button delete = getButton("Delete");
		Button deletems = getButton("Delete");
		Button safe = getButton("Delete without GMS");
		final Button disable = getButton("Disable");
		Button disablems = getButton("Disable");
		Button deletefb = getButton("Delete");
		Button disablefb = getButton("Disable");
		Button deletesam = getButton("Delete");
		Button disablesam = getButton("Disable");
		Button deletever = getButton("Delete");
		Button disablever = getButton("Disable");
		Button deleteknx = getButton("Delete");
		Button disableknx = getButton("Disable");
		Button deleteama = getButton("Delete");
		Button disableama = getButton("Disable");
		Button dalvik =getButton("Clear & Reboot");
		Button info = getButton("Telegram Group");
		Button adblock = getButton("Block Ads");

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

		LinearLayout ll5 = getLinearLayout();
		ll5.addView(deletesam);
		ll5.addView(disablesam);
		main.addView(getLabel("Samsung Bloatwares"));
		main.addView(ll5);

		LinearLayout ll6 = getLinearLayout();
		ll6.addView(deletever);
		ll6.addView(disablever);
		main.addView(getLabel("Verizon Bloatwares"));
		main.addView(ll6);

		LinearLayout ll7 = getLinearLayout();
		ll7.addView(deleteama);
		ll7.addView(disableama);
		main.addView(getLabel("Amazon Bloatwares"));
		main.addView(ll7);

		LinearLayout lls = getLinearLayout();
		lls.addView(dalvik);
		lls.addView(info);
		lls.addView(adblock);
		main.addView(getLabel("Other"));
		main.addView(lls);

		LinearLayout mainLayout=new LinearLayout(getApplicationContext());
		mainLayout.setLayoutParams(main.getLayoutParams());
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		ScrollView mainsw= new ScrollView(getApplicationContext());
		mainLayout.addView(mainsw);
		mainsw.addView(main);

		main.addView(getLabel("Warning: Be careful before deleting any system app or service. You must ensure that the package is not used by system to function. Removing a critical system app may result in bootlooping or soft bricking your device.",false));

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mainLayout.setBackgroundDrawable(gd);
		setContentView(mainLayout);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
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
						deletePackage("com.android.chrome");
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
							disablePackage("com.android.vending");
							disablePackage("com.android.chrome");
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


		deletesam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					deletePackage("com.samsung.svoice.sync");
					deletePackage("com.samsung.android.app.watchmanager");
					deletePackage("com.samsung.android.drivelink.stub");
					deletePackage("com.samsung.android.svoice");
					deletePackage("com.samsung.android.widgetapp.yahooedge.finance");
					deletePackage("com.samsung.android.widgetapp.yahooedge.sport");
					deletePackage("com.samsung.android.spdfnote");
					deletePackage("com.sec.android.widgetapp.samsungapps");
					deletePackage("com.samsung.android.email.provider");
					deletePackage("com.samsung.android.app.ledcoverdream");
					deletePackage("com.sec.android.cover.ledcover");
					deletePackage("com.sec.android.app.desktoplauncher");
					deletePackage("com.sec.android.app.withtv");
					deletePackage("com.samsung.android.app.memo");
					deletePackage("com.sec.spp.push");
					deletePackage("com.sec.android.app.shealth");
					deletePackage("com.samsung.android.spay");
					deletePackage("com.samsung.android.voicewakeup");
					deletePackage("com.samsung.voiceserviceplatform");
					deletePackage("com.sec.android.sidesync30");
					deletePackage("com.samsung.android.hmt.vrsvc");
					deletePackage("com.samsung.android.app.vrsetupwizardstub");
					deletePackage("com.samsung.android.hmt.vrshell");
					deletePackage("com.android.exchange");
					deletePackage("com.samsung.groupcast");
					deletePackage("com.sec.android.service.health");
					deletePackage("com.sec.kidsplat.installer");
					deletePackage("com.sec.android.widgetapp.diotek.smemo");
					deletePackage("com.sec.android.provider.snote");
					deletePackage("com.sec.android.app.translator");
					deletePackage("com.vlingo.midas");
					deletePackage("com.sec.readershub");
					deletePackage("com.sec.android.app.gamehub");
					deletePackage("com.sec.everglades.update");
					deletePackage("com.sec.everglades");
					deletePackage("tv.peel.samsung.app");
					deletePackage("com.sec.yosemite.phone");
					deletePackage("com.samsung.android.app.episodes");
					deletePackage("com.samsung.android.app.storyalbumwidget");
					deletePackage("com.samsung.android.tripwidget");
					deletePackage("com.samsung.android.service.travel");
					deletePackage("com.tripadvisor.tripadvisor");
					deletePackage("com.sec.android.app.ocr");
					deletePackage("com.samsung.android.game.gamehome");
					deletePackage("com.enhance.gameservice");
					deletePackage("com.samsung.android.game.gametools");

				}catch (Exception e){}
			}
		});

        disablesam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					disablePackage("com.samsung.svoice.sync");
					disablePackage("com.samsung.android.app.watchmanager");
					disablePackage("com.samsung.android.drivelink.stub");
					disablePackage("com.samsung.android.svoice");
					disablePackage("com.samsung.android.widgetapp.yahooedge.finance");
					disablePackage("com.samsung.android.widgetapp.yahooedge.sport");
					disablePackage("com.samsung.android.spdfnote");
					disablePackage("com.sec.android.widgetapp.samsungapps");
					disablePackage("com.samsung.android.email.provider");
					disablePackage("com.samsung.android.app.ledcoverdream");
					disablePackage("com.sec.android.cover.ledcover");
					disablePackage("com.sec.android.app.desktoplauncher");
					disablePackage("com.sec.android.app.withtv");
					disablePackage("com.samsung.android.app.memo");
					disablePackage("com.sec.spp.push");
					disablePackage("com.sec.android.app.shealth");
					disablePackage("com.samsung.android.spay");
					disablePackage("com.samsung.android.voicewakeup");
					disablePackage("com.samsung.voiceserviceplatform");
					disablePackage("com.sec.android.sidesync30");
					disablePackage("com.samsung.android.hmt.vrsvc");
					disablePackage("com.samsung.android.app.vrsetupwizardstub");
					disablePackage("com.samsung.android.hmt.vrshell");
					disablePackage("com.android.exchange");
					disablePackage("com.samsung.groupcast");
					disablePackage("com.sec.android.service.health");
					disablePackage("com.sec.kidsplat.installer");
					disablePackage("com.sec.android.widgetapp.diotek.smemo");
					disablePackage("com.sec.android.provider.snote");
					disablePackage("com.sec.android.app.translator");
					disablePackage("com.vlingo.midas");
					disablePackage("com.sec.readershub");
					disablePackage("com.sec.android.app.gamehub");
					disablePackage("com.sec.everglades.update");
					disablePackage("com.sec.everglades");
					disablePackage("tv.peel.samsung.app");
					disablePackage("com.sec.yosemite.phone");
					disablePackage("com.samsung.android.app.episodes");
					disablePackage("com.samsung.android.app.storyalbumwidget");
					disablePackage("com.samsung.android.tripwidget");
					disablePackage("com.samsung.android.service.travel");
					disablePackage("com.tripadvisor.tripadvisor");
					disablePackage("com.sec.android.app.ocr");
					disablePackage("com.samsung.android.game.gamehome");
					disablePackage("com.enhance.gameservice");
					disablePackage("com.samsung.android.game.gametools");


				}catch (Exception e){}
			}
		});
        deletever.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					deletePackage("com.vcast.mediamanager");
					deletePackage("com.samsung.vmmhux");
					deletePackage("com.vzw.hss.myverizon");
					deletePackage("com.asurion.android.verizon.vms");
					deletePackage("com.motricity.verizon.ssodownloadable");
					deletePackage("com.vzw.hs.android.modlite");
					deletePackage("com.samsung.vvm");
					deletePackage("com.vznavigator");

				}catch (Exception e){}
			}
		});
        disablever.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					disablePackage("com.vcast.mediamanager");
					disablePackage("com.samsung.vmmhux");
					disablePackage("com.vzw.hss.myverizon");
					disablePackage("com.asurion.android.verizon.vms");
					disablePackage("com.motricity.verizon.ssodownloadable");
					disablePackage("com.vzw.hs.android.modlite");
					disablePackage("com.samsung.vvm");
					disablePackage("com.vznavigator");
				}catch (Exception e){}
			}
		});

		deleteama.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					deletePackage("com.amazon*");
				} catch (Exception e) {}
			}
		});
		disableama.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					disablePackage("com.amazon*");
				} catch (Exception e) {}
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
			adblock.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					copyFromInternet("https://gitlab.com/parduscix/Guvenli_Internet/raw/master/hosts","/system/etc/hosts");
					}
			});
    }

}