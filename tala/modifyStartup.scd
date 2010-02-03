//		Remember to change to .sc when used in package

+ OSXPlatform {

	startupFiles {
//		var filename = "startup.rtf";
//		^[this.systemAppSupportDir +/+ filename, this.userAppSupportDir +/+ filename];
			// look for startup files inside the app Contents directory
		var filename = "startup.*";
		^(String.scDir +/+ filename).pathMatch;
	}
	
	startup {
		Document.implementationClass.startup;
		// make a server window for the internal if you like
/*		Server.internal.makeWindow;*/
//		Server.local.makeWindow;
		// uncomment if you use a startup file
//		this.loadStartupFiles;		
		// uncomment if you have multiple help files
//		Help.addToMenu;			
	}
}

+ Main { 
	startup {
		var loading_win, loading_text, loading_flag, loading_rout, loading_image;
		
		platform = this.platformClass.new;
		platform.initPlatform;
	
		super.startup;
		
		GUI.fromID( this.platform.defaultGUIScheme );
		GeneralHID.fromID( this.platform.defaultHIDScheme );
		
		// Set Server.default and the 's' interpreter variable to the internal server.
		// You should use the internal server for standalone applications --
		// otherwise, if your application has a problem, the user will
		// be stuck with a process, possibly making sound, that he won't know 
		// how to kill.
		Server.default = Server.internal; 
		interpreter.s = Server.default;

			// some folder paths that should point inside the app's Contents folder
		SynthDef.synthDefDir = String.scDir +/+ "synthdefs/";
		Archive.archiveDir = String.scDir;

		this.platform.startup;

		// from here on, you should customize what should happen...

/*		StartUp.run;*/
		// One can boot the server, then use loadPath to evaluate a file
		// OR - put things into a class... like the SCSA_Demo

/*		"Welcome to Standalone Demo made with SuperCollider, type cmd-d for help.".postln;*/
		
/*		Server.default.boot;*/

		// close post window if user should not have it
		Document.listener.close		

		loading_win 	= Window("Loading", Rect(617, 450, 686, 300), border:false).front;

		loading_text 	= StaticText(loading_win, Rect(100,100,loading_win.bounds.extent.x,loading_win.bounds.extent.y))
							.string_("Loading Carnatic Tala Meter")
							.font_(Font("Geneva", 35))
							.align_(\left);

		loading_image  = CompositeView(loading_win, Rect(0, 20, loading_win.bounds.width,200)).backgroundImage_(SCImage(String.scDir +/+ "SCClassLibrary/tala/images/mridangam.png"),11);

		loading_flag	= true;
		loading_rout 	= Routine {
			var i = 0;
			while({loading_flag}, {
				loading_text.string_("Loading Carnatic Tala Meter" ++ "..."[0..i%3]);
				i = i + 1;
				0.25.wait;
			});
		};			i
		loading_rout.play(AppClock);		

		Server.default.waitForBoot({
			Tala.new;
			loading_win.close;
			loading_rout.stop;			
		});



	}
	
}
