package org.droidtr.deletegapps;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import java.io.*;

import java.lang.Process;

public class MainActivity extends Activity {
	Process process = null;
	DataOutputStream dataOutputStream = null;
	String cmdlist="";
	TextView label=null;
	java.lang.Process p = null;
	DataOutputStream dos = null;
	
	public void execNoWait(final String command){
		new AsyncTask<String,String,Void>(){
			@Override
			public Void doInBackground(String... params){
				execForStringOutput(command);
				return null;
			}
		}.execute();
	}
	public String execForStringOutput(String command) {
		try{
			String line;
			StringBuilder s = new StringBuilder();
			dos.writeBytes(command+"\n");
			dos.flush();
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null){
				if(line.trim().length()>0){
					s.append((line).trim()+"\n");
				}
			}
			input.close();
			return s.toString();
		}catch(Exception e){
			return "";
		}
	}
	public void init() {
		try {
		java.lang.Process p = Runtime.getRuntime().exec("su");
		dos = new DataOutputStream(p.getOutputStream());
		execForStringOutput("mount -o rw,remount /");
		execForStringOutput("mount -o rw,remount /system");
		execForStringOutput("mount -o rw,remount /oem");
		execForStringOutput("mount -o rw,remount /cust");
		execForStringOutput("mount -o rw,remount /vendor");
		execForStringOutput("mount -o remount,rw /");
		execForStringOutput("mount -o remount,rw /system");
		execForStringOutput("mount -o remount,rw /oem");
		execForStringOutput("mount -o remount,rw /cust");
		execForStringOutput("mount -o remount,rw /vendor");
		if (Build.VERSION.SDK_INT > 21) {
			setTheme(android.R.style.Theme_Material);
		} else if (Build.VERSION.SDK_INT > 14) {
			setTheme(android.R.style.Theme_Holo);
		} else {
			setTheme(android.R.style.Theme);
		}
			 label=getLabel("Warning: Be careful before deleting any system app or service. You must ensure that the package is not used by system to function. Removing a critical system app may result in bootlooping or soft bricking your device.", false);
		} catch (Exception e) {
			Toast.makeText(this, "Error: "+e.toString(), Toast.LENGTH_LONG).show();
			finish();
		}

	}

	public void copyFromInternet(String url, String path) {
		Toast.makeText(this, "Downloading...", Toast.LENGTH_LONG).show();
		new Download(this).execute(url, path);

	}
	public Button getButton(String label){
		return(getButton(label,Color.parseColor("#ec407a")));
	}

	public Button getButton(String label,int c) {
		LinearLayout.LayoutParams butparam = new LinearLayout.LayoutParams(-2, -1);
		butparam.weight = 1;
		Button b = new Button(getApplicationContext());
		b.setText(label);
		b.setTextColor(Color.WHITE);
		GradientDrawable gd = new GradientDrawable();
		gd.setStroke(10, 0);
		gd.setCornerRadius(15);
		gd.setColor(c);
		b.setBackgroundDrawable(gd);
		b.setLayoutParams(butparam);
		b.setSingleLine(true);
		return b;
	}

	public TextView getLabel(String label) {
		LinearLayout.LayoutParams butparam = new LinearLayout.LayoutParams(-1, -1);
		butparam.weight = 1;
		TextView t = new TextView(getApplicationContext());
		t.setText(label);
		t.setTextColor(Color.BLACK);
		t.setTypeface(Typeface.create("", Typeface.BOLD));
		t.setSingleLine();
		t.setPadding(7, 7, 7, 7);
		t.setLayoutParams(butparam);
		return t;
	}

	public TextView getLabel(String label, boolean singleLine) {
		TextView t = getLabel(label);
		t.setSingleLine(singleLine);
		return t;
	}

	public LinearLayout getLinearLayout() {
		LinearLayout ll = new LinearLayout(getApplicationContext());
		ll.setLayoutParams(new LinearLayout.LayoutParams(-1, -2));
		ll.setOrientation(LinearLayout.HORIZONTAL);
		return ll;
	}

	public void deletePackage(String name){
		 String code = "pm list package -f --user 0  | grep " + name + " | sed \"s/apk=.*/apk/\" | sed \"s/.*:/rm -rf /g\" > /data/list";
					execForStringOutput(code);
		 code = "pm list package -f --user 0  | grep " + name + " | sed \"s/.*=//\" | sed \"s/^/pm uninstall -k --user 0 /g\" >> /data/list";
					execForStringOutput(code);
					execForStringOutput("sh /data/list");
	}
	public void disablePackage(String name){
		 String code = "pm list package -e -f --user 0  | grep " + name + " | sed \"s/.*=//\" | sed \"s/^/pm disable /g\" > /data/list";
					execForStringOutput(code);
					execForStringOutput("sh /data/list");
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		init();
		LinearLayout main = new LinearLayout(getApplicationContext());
		main.setPadding(20, 20, 20, 20);
		main.setOrientation(LinearLayout.VERTICAL);
		main.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
		GradientDrawable gd = new GradientDrawable();
		gd.setStroke(2, Color.BLACK);
		gd.setCornerRadius(15);
		gd.setColor(Color.parseColor("#90a4ae"));


		final Button delete = getButton("Delete",Color.parseColor("#EC4055"));
		Button deletems = getButton("Delete");
		Button safe = getButton("Delete without GMS");
		final Button disable = getButton("Disable",Color.parseColor("#4055EC"));
		Button disablems = getButton("Disable",Color.parseColor("#4055EC"));
		Button deletefb = getButton("Delete");
		Button disablefb = getButton("Disable",Color.parseColor("#4055EC"));
		Button deletesam = getButton("Delete");
		Button disablesam = getButton("Disable",Color.parseColor("#4055EC"));
		Button deletever = getButton("Delete");
		Button disablever = getButton("Disable",Color.parseColor("#4055EC"));
		Button deleteknx = getButton("Delete");
		Button disableknx = getButton("Disable",Color.parseColor("#4055EC"));
		Button deleteama = getButton("Delete");
		Button disableama = getButton("Disable",Color.parseColor("#4055EC"));
		Button deletemis = getButton("Delete");
		final Button disablemis = getButton("Disable",Color.parseColor("#4055EC"));
		Button deletetur = getButton("Delete");
		Button disabletur = getButton("Disable",Color.parseColor("#4055EC"));
		Button deleteeve = getButton("Delete");
		Button disableeve = getButton("Disable",Color.parseColor("#4055EC"));
		Button dalvik = getButton("Clear & Reboot",Color.BLACK);
		Button adblock = getButton("Block Ads",Color.BLACK);
		Button enable = getButton("Enable All",Color.BLACK);
		Button info = getButton("Telegram Channel",Color.BLACK);

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

		LinearLayout ll8 = getLinearLayout();
		ll8.addView(deletetur);
		ll8.addView(disabletur);
		main.addView(getLabel("Mobile operator Bloatwares"));
		main.addView(ll8);

		LinearLayout ll9 = getLinearLayout();
		ll9.addView(deleteeve);
		ll9.addView(disableeve);
		main.addView(getLabel("Evenwell Bloatwares"));
		main.addView(ll9);

		LinearLayout llm = getLinearLayout();
		llm.addView(deletemis);
		llm.addView(disablemis);
		main.addView(getLabel("Miscellaneous Bloatware"));
		main.addView(llm);

		LinearLayout lls = getLinearLayout();
		LinearLayout lls2 = getLinearLayout();
		lls.addView(dalvik);
		lls2.addView(adblock);
		lls2.addView(enable);
		lls2.addView(info);
		main.addView(getLabel("Other"));
		main.addView(lls);
		main.addView(lls2);

		LinearLayout mainLayout = new LinearLayout(getApplicationContext());
		mainLayout.setLayoutParams(main.getLayoutParams());
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		ScrollView mainsw = new ScrollView(getApplicationContext());
		mainLayout.addView(mainsw);
		mainsw.addView(main);
		main.addView(label);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mainLayout.setBackgroundDrawable(gd);
		setContentView(mainLayout);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		super.onCreate(savedInstanceState);
		

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					execForStringOutput("sed -i \"s/ro.setupwizard.mode=ENABLED/ro.setupwizard.mode=DISABLED/\" /system/build.prop ");
					execForStringOutput("rm -rf /data/app/*{g,G}oogle* ");
					execForStringOutput("rm -rf /data/data/*{g,G}oogle*");
					execForStringOutput("rm -rf /data/app/*com.android.vending*");
					execForStringOutput("rm -rf /data/data/*com.android.vending*");
					//delete all gapps
					String code = "pm list package -f --user 0  | grep google";
					String[] list = {
							"com.google.android.ext.services",
							"com.google.android.packageinstaller",
							"com.google.android.ext.shared",
					};
					for (int i = 0; i < list.length; i++) {
						code = code + " | grep -v \"" + list[i] + "\"";
					}
					code = code + " | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\" | sed \"s/$/ \\&/\" > /data/list";
					execForStringOutput(code);
					execNoWait("sh /data/list");
					deletePackage("com.android.chrome");
					deletePackage("com.android.vending");
				   Toast.makeText(getApplicationContext(), "Gapps deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		deletems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					//delete all msapps
					deletePackage("microsoft");
					deletePackage("com.skype.raider");
				   Toast.makeText(getApplicationContext(), "MSapps deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		deleteknx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					//delete knox packages
					deletePackage("knox");
					deletePackage("com.samsung.android.securitylogagent");
					deletePackage("com.sec.android.providers.security");
					deletePackage("com.samsung.android.mdm");
					deletePackage("com.samsung.android.bbc.bbcagent");
					deletePackage("com.sec.android.app.sysscope");
					deletePackage("com.samsung.klmsagent");
					deletePackage("com.sec.android.diagmonagent");
					execForStringOutput("rm -rf /system/container/ContainerAgent2");
					execForStringOutput("rm -rf /system/container/KnoxBBCProivder");
					execForStringOutput("rm -rf /system/container/KnoxBluetooth");
					execForStringOutput("rm -rf /system/container/KnoxKeyguard");
					execForStringOutput("rm -rf /system/container/KnoxPackageVerifier");
					execForStringOutput("rm -rf /system/container/KnoxShortcuts");
					execForStringOutput("rm -rf /system/container/KnoxSwitcher");
					execForStringOutput("rm -rf /system/container/resources");
					execForStringOutput("rm -rf /system/container/SharedDeviceKeyguard");
					execForStringOutput("rm -rf /system/etc/secure_storage/com.sec.knox.store");
					execForStringOutput("rm -rf /system/etc/recovery-resource.dat");
					execForStringOutput("rm -rf /system/preloadedkiosk/kioskdefault");
					execForStringOutput("rm -rf /system/preloadedsso/ssoservice.apk_");
					execForStringOutput("rm -rf /system/recovery-from-boot.p");
				   Toast.makeText(getApplicationContext(), "Knox deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		deletefb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					//delete all fbapps
					deletePackage("facebook");
					deletePackage("com.instagram.android");
					deletePackage("com.whatsapp");
				   Toast.makeText(getApplicationContext(), "FBapps deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		safe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					execForStringOutput("rm -rf /data/app/*{g,G}oogle*  ");
					execForStringOutput("rm -rf /data/data/*{g,G}oogle* ");
					execForStringOutput("rm -rf /data/app/*com.android.vending* ");
					execForStringOutput("rm -rf /data/data/*com.android.vending* ");
					//delete without gmscore
					String code = "pm list package -f --user 0  | grep google";
					String[] list = {
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
					for (int i = 0; i < list.length; i++) {
						code = code + " | grep -v \"" + list[i] + "\"";
					}
					code = code + " | sed \"s/=.*//\" | sed \"s/.*:/rm -rf /\" | sed \"s/$/ \\&/\" > /data/list";
					execForStringOutput(code);
					execForStringOutput("sh /data/list");
				   Toast.makeText(getApplicationContext(), "Gapps without gmscore deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		disable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					execForStringOutput("pm list package  | grep -v \"ext.shared\" | grep -v \"packageinstaller\" |  grep -v \"ext.services\" | grep google | sed \"s/.*:/pm disable /\"> /data/list");
					execForStringOutput("sh /data/list");
					disablePackage("com.android.vending");
					disablePackage("com.android.chrome");
				   Toast.makeText(getApplicationContext(), "Gapps disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		disablems.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
				   deletePackage("microsoft");
				   disablePackage("com.skype.raider");
				   Toast.makeText(getApplicationContext(), "MSapps disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});

		disableknx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					disablePackage("knox");
					disablePackage("com.samsung.android.securitylogagent");
					disablePackage("com.sec.android.providers.security");
					disablePackage("com.samsung.android.mdm");
					disablePackage("com.samsung.android.bbc.bbcagent");
					disablePackage("com.sec.android.app.sysscope");
					disablePackage("com.samsung.klmsagent");
					disablePackage("com.sec.android.diagmonagent");
				   Toast.makeText(getApplicationContext(), "Knox disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});

		disablefb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					disablePackage("facebook");
					disablePackage("com.instagram.android");
					disablePackage("com.whatsapp");
				   Toast.makeText(getApplicationContext(), "FBapps disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});


		deletesam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
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
				   Toast.makeText(getApplicationContext(), "Samsung Bloatwares deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});

		disablesam.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
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
				   Toast.makeText(getApplicationContext(), "Samsung Bloatwares disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
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
					deletePackage("com.verizon.permissions.qos");
					deletePackage("com.verizon.vzwavs");
				   Toast.makeText(getApplicationContext(), "Verizon Bloatvares deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
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
					disablePackage("com.verizon.permissions.qos");
					disablePackage("com.verizon.vzwavs");
				   Toast.makeText(getApplicationContext(), "Verizon Bloatwares disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});

		deleteama.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					deletePackage("com.amazon");
				   Toast.makeText(getApplicationContext(), "Amazon Bloatwares deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		disableama.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					disablePackage("com.amazon");
				   Toast.makeText(getApplicationContext(), "Amazon Bloatwares disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		deletetur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					deletePackage("turkcell");
					deletePackage("com.ttech.android.onlineislem");
					deletePackage("vodafone");
					deletePackage("com.tmob.AveaOIM");
					deletePackage("turktelekom");
					deletePackage("avea");
				   Toast.makeText(getApplicationContext(), "Operator Bloatwares deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		deleteeve.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					deletePackage("com.evenwell");
				   Toast.makeText(getApplicationContext(), "Evenwell Bloatwares deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		disabletur.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					disablePackage("turkcell");
					disablePackage("com.ttech.android.onlineislem");
					disablePackage("vodafone");
					disablePackage("com.tmob.AveaOIM");
					disablePackage("turktelekom");
					disablePackage("avea");
				   Toast.makeText(getApplicationContext(), "Operator Bloatwares disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		disableeve.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					disablePackage("com.evenwell");
				   Toast.makeText(getApplicationContext(), "Evenwell Bloatwares disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		deletemis.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					deletePackage("flipboard.boxer.app");
					deletePackage("flipboard.app");
					deletePackage("com.hancom.office.editor.hidden");
					deletePackage("com.audible.application");
					deletePackage("com.blurb.checkout");
					deletePackage("com.cequint.ecid");
					deletePackage("com.imdb.mobile");
					deletePackage("com.gotv.nflgamecenter.us.lite");
					deletePackage("com.infraware.polarisoffice5");
					deletePackage("com.nuance.swype.input");
					deletePackage("com.android.stk");
					deletePackage("com.android.cellbroadcastreceiver");
					deletePackage("com.mobitv.client.tmobiletvhd");
					deletePackage("us.com.dt.iq.appsource.tmobile");
					deletePackage("com.generalmobile.assistant");
					deletePackage("com.android.email");
					deletePackage("org.lineageos.jelly");
					execForStringOutput("rm -rf /system/vendor/pittpatt");
					execForStringOutput("rm -rf /system/customize/");
					execForStringOutput("rm -rf /system/media/video/");
					execForStringOutput("rm -rf /system/lib/libLaputaEngine.so");
					execForStringOutput("rm -rf /system/lib/libLaputaLbJni.so");
					execForStringOutput("rm -rf /system/lib/libLaputaLbProviderJni.so");
					execForStringOutput("rm -rf /system/lib/libLaputaLogJni.so");
					execForStringOutput("rm -rf /system/lib/libnotes_jni.so");
					execForStringOutput("rm -rf /system/lib/libnotesprovider_jni.so");
					execForStringOutput("rm -rf /system/lib/libpolarisoffice_Clipboard.so");
				   Toast.makeText(getApplicationContext(), "Other Bloatwares deleted." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		disablemis.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					disablePackage("flipboard.boxer.app");
					disablePackage("flipboard.app");
					disablePackage("com.hancom.office.editor.hidden");
					disablePackage("com.audible.application");
					disablePackage("com.blurb.checkout");
					disablePackage("com.cequint.ecid");
					disablePackage("com.imdb.mobile");
					disablePackage("com.gotv.nflgamecenter.us.lite");
					disablePackage("com.infraware.polarisoffice5");
					disablePackage("com.nuance.swype.input");
					disablePackage("com.android.stk");
					disablePackage("com.android.cellbroadcastreceiver");
					disablePackage("com.mobitv.client.tmobiletvhd");
					disablePackage("us.com.dt.iq.appsource.tmobile");
					disablePackage("com.generalmobile.assistant");
					disablePackage("com.android.email");
					disablePackage("org.lineageos.jelly");
				   Toast.makeText(getApplicationContext(), "Other Bloatwares disabled." , Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});

		dalvik.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View p1) {
				try {
					execForStringOutput("rm -rf /data/data/*/cache/");
					execForStringOutput("rm -rf /sdcard/DCIM/.thumbnails/");
					execForStringOutput("rm -rf /sdcard/Android/data/*/cache/");
					execForStringOutput("rm -rf /storage/*/Android/data/*/cache/");
					execForStringOutput("rm -rf /storage/*/DCIM/.thumbnails/");
					execForStringOutput("rm -rf /cache/*");
					execForStringOutput("rm -rf /data/dalvik-cache/*");
					execForStringOutput("reboot");
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "Fail: " + e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
		enable.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String code = "pm list package -d -f --user 0 | sed \"s/.*=//\" | sed \"s/^/pm enable /\" | sed \"s/$/ \\&/\" > /data/list";
					execForStringOutput(code);
					execForStringOutput("sh /data/list");
				   Toast.makeText(getApplicationContext(), "All packages enabled." , Toast.LENGTH_SHORT).show();
			}
		});
		adblock.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				copyFromInternet("https://gitlab.com/sulincix/Guvenli_Internet/raw/master/hosts", "/system/etc/hosts");
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

