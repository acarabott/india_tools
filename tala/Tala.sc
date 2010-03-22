/*
	Standalone

	TODO Update
*/

/*
	1.1

	TODO Calculatable Tempo field
	TODO Try passing in time and greying out
	TODO Volume slider
	TODO Change tempo box to calculateable one?
	TODO If window is closeable, cleanup on closing
	TODO FullScreen image
	TODO Multislider for sub-divisions
	TODO Kallai
	TODO Spacebar plays or stops regardless of scope
*/

/*
	1.2

	TODO Make this into a view, which can be added to other windows?
	TODO Custom tala field working
	TODO Images for tisras etc
	TODO Eduppu
	TODO Use Task to allow Play/Pause? Can be achieved with Condition class
	TODO Store previous boot time as variable (file?) then give a loading bar...
	
*/
/*
	2.0
	TODO Video
	TODO Tap Tempo
	TODO More Talas, ask KSS
*/

Tala {
	
	classvar <adi;			//	Adi Tala Preset
	classvar <rupaka;		//	Rupaka Tala Preset
	classvar <kCapu;		//	Khanda Capu Preset
	classvar <mCapu;		//	Misra Capu Preset
	classvar <sCapu;		//	Sankirna Capu Preset
	
	var <s;					//	Server
	var <>amp;				//	Amplification multiplier
	var <>mute;				//	Mute multiplier
	
	var <>parts;			
	var <routine;			//	The playback routine
	var <talaRoutine;
	var <talaRoutineDuration;	//	The duration (in seconds) of the routine
	var <clock;				//	Clock for playback
	
	var <gati;				//	Gati (Sub-division)
	var <gatiMult;			//	Gati multiplier, e.g. to change from 3 per beat to 6 etc
	var <gatiTotal;		//	Total sub-divisions (gati * gatiMult)
	var <gatiRoutine;		//	Gati playback routine;
	var <gatiAmps;			//	Amplitudes for the sub-divisions
	var <>gatiFunc;		//	Function to be called on each sub-division playback
		
	var <>tGUI;					//	GUI :)
	
	*initClass {
		adi 		= ["I4", "O", "O"];
		rupaka 		= ["U", "O"];
		kCapu 		= ["U", "K"];
		mCapu		= ["M", "U", "U"];
		sCapu 		= ["U", "U", "U", "K"];
	}
	
	*new {|tempo=60, gati=4, gui=true|
		^super.new.init(tempo, gati, gui);
	}

	init {|aTempo, aGati, aGUI|
		amp 		= 1;
		mute		= 1;
		
		clock		= TempoClock(aTempo/60);

		gati		= aGati;
		gatiMult	= 1;
		gatiTotal	= gati*gatiMult;
		gatiAmps 	= 0 ! gatiTotal;
		gatiFunc	= {|i| i};
			
		parts		= adi;
		
		talaRoutineDuration 	= 0;		

		this.createTalaRoutine;
		this.createGatiRoutine;
			
		s = Server.default;
		this.loadSynthDefs;		
								
		if(aGUI) {
			tGUI = TalaGUI.new(this);
		};
		
	}
	
	loadSynthDefs {
		//	Clapping SynthDef by Thor Magnusson
		SynthDef(\clapping, {arg t_trig=1, amp=0.5, filterfreq=100, rq=0.1;
			var env, signal, attack,  noise, hpf1, hpf2;
			noise = WhiteNoise.ar(1)+SinOsc.ar([filterfreq/2,filterfreq/2+4 ], pi*0.5, XLine.kr(1,0.01,4));
			hpf1 = RLPF.ar(noise, filterfreq, rq);
			hpf2 = RHPF.ar(noise, filterfreq/2, rq/4);
			env = EnvGen.kr(Env.perc(0.003, 0.00035));
			signal = (hpf1+hpf2) * env;
/*			signal = CombC.ar(signal, 0.5, 0.03, 0.031)+CombC.ar(signal, 0.5, 0.03016, 0.06);*/
			//signal = FreeVerb.ar(signal, 0.23, 0.15, 0.2);
/*			signal = Limiter.ar(signal, 0.7, 0.01);*/
			Out.ar(0, Pan2.ar(signal*amp, 0));
			DetectSilence.ar(signal, doneAction:2);
		}).load(s);
				
	}
	
	tempo {
		^clock.tempo*60
	}
	
	tempo_ {|newTempo|
		clock.tempo	= newTempo/60;
	}
	
	gati_ {|newGati, newMult|
		gati = newGati;
		gatiMult = newMult ?? gatiMult;
		this.prGatiUpdate;
	}
	
	gatiMult_ {|newMult|
		gatiMult = newMult;
		this.prGatiUpdate;
	}
	
	//Internal method
	prGatiUpdate {
		clock.clear;
		gatiTotal = gati * gatiMult;
		gatiAmps = gatiAmps.extend(gatiTotal, 1);
		this.createGatiRoutine;
		if(this.isPlaying) {
			talaRoutine.play(clock, 1); 
			gatiRoutine.play(clock, 1);
		};
		
	}
	setGatiAmp {|index, value|
		if(index<gatiAmps.size) {
			gatiAmps[index] = value;
		} {
			"Invalid Index!".postln;
		};
	}
	
	//	Routine methods
	addTalaRoutine {|toAdd|
		talaRoutine = talaRoutine ++ toAdd
	}
	
	createTalaRoutine {
		var toAdd;
		talaRoutine = Routine {};
		
		parts.do { |item, i|
			switch (item[0].asSymbol)
				{'I'}	{ toAdd = this.laghu(item[1].digit) }
				{'O'}	{ toAdd = this.drutam()}
				{'U'}	{ toAdd = this.anudrutam}
				{'K'}	{ toAdd = this.capu("clap")}
				{'M'}	{ toAdd = this.capu("clapB")};
			
			talaRoutine = talaRoutine ++ toAdd;
		};
		
		talaRoutine = talaRoutine.loop;
	}
	
	createGatiRoutine {
		var gatiAmp;
		var index;
		
		gatiRoutine = Routine {
			inf.do { |i|
				index = i%gatiTotal;
				gatiAmp = gatiAmps[index];
				this.genericClap(0.01*gatiAmp, 0.01*gatiAmp, 4000, 4000, 1);
				this.gatiFunc.(index, i);
				(1/(gatiTotal)).wait;	
			};
		};
	}
	
	play {
		if(talaRoutine.isPlaying.not) {
			talaRoutine.play(clock, 1);
			gatiRoutine.play(clock, 1);				
		};	
	}
	
	stop {
		talaRoutine.stop;
		gatiRoutine.stop;
		this.createTalaRoutine;
		this.createGatiRoutine;
	}
	
	addRoutTime {|time|
		talaRoutineDuration = talaRoutineDuration + time;
	}
	
	checkStopTala {|newTala|
		if(newTala!=parts) {
			this.stop;
			tGUI.playStopButton.valueAction_(0);
		};
	}
	
	isPlaying {
		if(talaRoutine.isPlaying) {
			^true
		}{
			^false
		};
	}
	//	Angas
	
	laghu {|number|
		this.addRoutTime(number);
		^Routine {
			this.clap();
			1.wait;
			(number-1).do { |i|
				this.finger(i+2);
				1.wait;
			};
		};
	}
	
	drutam {
		this.addRoutTime(2);
		^Routine {
			this.clap();
			1.wait;
			this.clapB();
			1.wait
		};
	}
	
	anudrutam {
		this.addRoutTime(1);
		^Routine {
			this.clap();
			1.wait;
		};
	}
	
	//	Capu	Returns a routine with two claps and a rest, all equal.
	//	@type	A string, either "clap" for normal clap, or "clapB" for a back of the hand clap
	capu {|type|
		var whichClap = {
			if(type.asSymbol == 'clap') {
				this.clap();
			} {
				this.clapB();
			};
		};

		this.addRoutTime(1.5);
		^Routine {
			whichClap.();
			0.5.wait;
			whichClap.();
			1.wait;
		};
	}
	
	//	Gestures
	clap {
		tGUI !? {tGUI.clap};
		this.genericClap(0.8, 0.9, 2000, 2500, 0.9);
		this.genericClap(0.7, 0.8, 700, 1200, 0.9);
		
	}
			
	clapB {
		tGUI !? {tGUI.wave};
		this.genericClap(0.1, 0.2, 400, 600, 0.9);
	}

	finger {|number|
		var a =	case
				{[2,7].includes(number)}	{tGUI !? {tGUI.lf(number)}}
				{[3,8].includes(number)}	{tGUI !? {tGUI.rf(number)}}
				{[4,9].includes(number)}	{tGUI !? {tGUI.mf(number)}}
				{number==5}					{tGUI !? {tGUI.pf(number)}}
				{number==6}					{tGUI !? {tGUI.tf(number)}};
		
		this.genericClap(0.2, 0.3, 6000, 7000, 0.9);
		
	}
	
	//	Synth methods
	genericClap {|amp1, amp2, freq1, freq2, rq|
			Synth(\clapping, [\amp, rrand(amp1, amp2)*(amp*mute), \filterfreq, rrand(freq1, freq2), \rq, rq.rand]) 
	}
	
	//	Preset loading methods
	adi {
		this.checkStopTala(adi);
		parts = adi;
		this.createTalaRoutine;
	}
	
	rupaka {
		this.checkStopTala(rupaka);		
		parts = rupaka;
		this.createTalaRoutine;		
	}
	
	kCapu {
		this.checkStopTala(kCapu);
		parts = kCapu;
		this.createTalaRoutine;
	}
	
	mCapu {
		this.checkStopTala(mCapu);
		parts = mCapu;
		this.createTalaRoutine;
	}

	sCapu {
		this.checkStopTala(sCapu);
		parts = sCapu;
		this.createTalaRoutine;
	}	
}