/*
	TODO Improve Drone sound
	TODO Add subdivision playback
*/

Tala {
	
	classvar <adi;			//	Adi Tala Preset
	classvar <rupaka;		//	Rupaka Tala Preset
	classvar <kCapu;		//	Khanda Capu Preset
	classvar <mCapu;		//	Misra Capu Preset
	classvar <sCapu;		//	Sankirna Capu Preset
	
	var <s;					//	Server
	var <>amp;				//	Amplification multiplier
	var <tempo;				//	Tempo
	var <wait_time;			//	Wait time
	var <>kallai;
	var <gati;
	var <eduppu;
	
	var <>parts;			
	var <routine;			//	The playback routine
	var <routine_duration;	//	The duration (in seconds) of the routine
	var <no_play;			//	If true, the routine can't yet be played
	
	var <drone_note;		//	Drone Note
	var <drone_amp;			//	Drone Volume
	var drone_synth;		//	Drone Synth
	var drone_routine;		//	Drone Routine
	
	*initClass {
		adi 		= ["I4", "O", "O"];
		rupaka 		= ["U", "O"];
		kCapu 		= ["U", "K"];
		mCapu		= ["M", "U", "U"];
		sCapu 		= ["U", "U", "U", "K"];
	}
	
	*new {
		^super.new.init;
	}

	init {|aTempo=60, aNote=62|
		amp 		= 1;
		tempo 		= aTempo;
		gati		= 4;
		kallai		= 1;
		eduppu		= 0;
		wait_time	= 60/tempo;
		
		parts		= adi;
		
		routine_duration 	= 0;
		no_play 			= true;
		this.create_routine;
		
		drone_note 	= aNote;
		drone_amp 	= 1;
		
		s = Server.default;
		{
			this.load_synth_defs;		
			s.sync;
			this.create_drone_routine;
			drone_synth = Synth(\drone, [\rootNote, drone_note, \amp, 0]);
		}.fork;
		no_play = false;
	}
	
	load_synth_defs {
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
		
		SynthDef(\drone, {|rootNote=62, amp=1|

		var signal1, signal2, root, fifth, octaveA, octaveB, env;
		root = rootNote.midicps;
		fifth = (rootNote+7).midicps;
		octaveA = (rootNote+12).midicps;
		octaveB = (rootNote-12).midicps;

		env = {EnvGen.kr(Env.new(
						 					Array.rand(16, 0, 0.2),  //Random drones
											Array.rand(15, 1, 5),
											'exponential',
											0,
											1))};
		signal1 = Mix(SinOsc.ar([root, fifth, [octaveA, octaveB].choose], 0, 0.3*[env, env, env]));
		signal2 = Mix(LFSaw.ar([root, fifth, [octaveA, octaveB].choose], 0, 0.4*[env, env, env]));							

		Out.ar(	0,
		 		Pan2.ar(signal1)*amp,
		 		Pan2.ar(signal2, FSinOsc.kr(0.05))*amp
		 		);
		}).load(s)
		
	}
	
	tempo_ {|new_value|
		tempo 		= new_value;
		wait_time 	= 60/tempo;
	}
	
	
	//	Routine methods
	add_routine {|to_add|
		routine = routine ++ to_add
	}
	
	create_routine {
		routine = Routine {};
		parts.do { |item, i|
			switch (item[0].asSymbol)
				{'I'}	{ routine = routine ++ this.laghu(item[1].digit) }
				{'O'}	{ routine = routine ++ this.drutam()}
				{'U'}	{ routine = routine ++ this.anudrutam}
				{'K'}	{ routine = routine ++ this.capu("clap")}
				{'M'}	{ routine = routine ++ this.capu("clap_b")};
			
		};
		
		routine = routine.loop;
	}
	
	play {
		if(routine.isPlaying.not) {
			routine.play;
		};
		
	}
	
	stop {
		routine.stop;
		this.create_routine;
	}
	
	reset {
		{
			no_play = true;
			(wait_time)*1.5.wait;
			routine.reset;	
			no_play = false;
		}.fork
	}
	
	add_rout_time {|time|
		routine_duration = routine_duration + time;
	}
	
	check_stop_tala {|new_tala|
		if(new_tala!=parts) {
			this.stop;
		};
	}
	//	Angas
	
	laghu {|number|
		this.add_rout_time(number*wait_time);
		^Routine {
			this.clap();
			wait_time.wait;
			(number-1).do { |i|
				this.finger(i);
				wait_time.wait;
			};
		};
	}
	
	drutam {
		this.add_rout_time(2*wait_time);
		^Routine {
			this.clap();
			wait_time.wait;
			this.clap_b();
			wait_time.wait;
		};
	}
	
	anudrutam {
		this.add_rout_time(wait_time);
		^Routine {
			this.clap();
			wait_time.wait;
		};
	}
	
	//	Capu	Returns a routine with two claps and a rest, all equal.
	//	@type	A string, either "clap" for normal clap, or "clap_b" for a back of the hand clap
	capu {|type|
		var which_clap = {
			if(type.asSymbol == 'clap') {
				this.clap();
			} {
				this.clap_b();
			};
		};

		this.add_rout_time(wait_time*1.5);		
		^Routine {
			which_clap.();
			(wait_time/2).wait;
			which_clap.();
			wait_time.wait;
		};
	}
	
	//	Gestures
	clap {
		"clap!".postln;
		this.generic_clap(0.7, 0.8, 2000, 2500, 0.9);
		this.generic_clap(0.7, 0.8, 700, 1200, 0.9);
		
	}
			
	clap_b {
		"back clap!".postln;
		this.generic_clap(0.1, 0.2, 400, 600, 0.9);
	}

	finger {|number|
		("Finger - " ++ (number+1)).postln;
		this.generic_clap(0.2, 0.3, 6000, 7000, 0.9);
		
	}
	
	//	Synth methods
	generic_clap {|amp1, amp2, freq1, freq2, rq|
/*		s.bind {*/
			Synth(\clapping, [\amp, rrand(amp1, amp2)*amp, \filterfreq, rrand(freq1, freq2), \rq, rq.rand]) 
/*		}*/
	}
	
	drone_amp_ {|new_amp|
		drone_amp = new_amp;
		drone_synth.set(\amp, drone_amp);
	}
	
	drone_note_ {|new_note|
		drone_note = new_note;
		drone_synth.set(\rootNote, drone_note);
	}
	
	create_drone_routine {
		drone_routine = Routine {
			inf.do { |i|
				drone_synth.set(\amp, drone_amp);				
				0.yield;
				drone_synth.set(\amp, 0);								
				0.yield
			};
		};
	}
	
	drone {
		drone_routine.();
	}
	
	//	Preset loading methods
	adi {
		this.check_stop_tala(adi);
		parts = adi;
		this.create_routine;
	}
	
	rupaka {
		this.check_stop_tala(rupaka);		
		parts = rupaka;
		this.create_routine;		
	}
	
	kCapu {
		this.check_stop_tala(kCapu);
		parts = kCapu;
		this.create_routine;
	}
	
	mCapu {
		this.check_stop_tala(mCapu);
		parts = mCapu;
		this.create_routine;
	}

	sCapu {
		this.check_stop_tala(sCapu);
		parts = sCapu;
		this.create_routine;
	}
	
	
}

TalaImage : Object {
	classvar <images;
	classvar <strings;
	
	var <comp;
	var <label;
	var <parent;
	
	var <label_extent;
	
	*initClass {
		images = PathName.new("/Users/arthurc/Documents/programming/computerMusic/india_tools/tala/images").files.collect({|item, i| SCImage.new(item.fullPath) });
		strings = #["Clap", "Wave", "2", "3", "4", "5", "6", "7", "8", "9"];
	}
	
	*new {|aParent, aBounds|
		^super.new.init(aParent, aBounds);
	}

	init {|aParent, aBounds|
		var parent			= aParent;
		var font_size		= aBounds.height*0.14;
		var font			= if(Font.availableFonts.any{|item, i| item.asSymbol=='Cochin'}) {Font.new("Cochin", font_size) } {Font("Times", font_size)};
		var s_bounds		= {|string| GUI.stringBounds(string, font)};
		
		label_extent	= s_bounds.(strings[strings.collect({|item, i| s_bounds.(item).width}).maxIndex]).extent;

		comp 		= CompositeView(aParent, aBounds ?? (aParent.bounds.width@aParent.bounds.height));
		label 		= StaticText(comp, label_extent).font_(font).stringColor_(Color.yellow);
		
		this.clap;
						
	}
	
	string {
		^label.string;
	}
		
	font {
		^label.font;
	}
	
	font_ {|aFont|
		label.font = aFont;
	}
	
	font_size {
		^label.font.size
	}
	
	font_size_ {|aSize|
		label.font = label.font.size = aSize
	}
	
	label_color {
		^label.stringColor
	}
	
	label_color_ {|aColor|
		label.stringColor = aColor;
	}
	
	bounds {
		^comp.bounds;
	}
	
	label_origin {
		^label.bounds.origin;
	}
	
	label_origin_ {|aPoint|
		label.bounds = Rect(aPoint.x, aPoint.y,label_extent.x, label_extent.y)
	}
	
	//actions
	
	prAction {|index, xMul, yMul|
		comp.backgroundImage_(images[index], 11);
		label.string_(strings[index]);
		this.label_origin_((this.bounds.width*xMul)@(this.bounds.height*yMul))
		
	}
	
	clap {
		this.prAction(0, 0.41, 0.45);
	}
	
	wave {
		this.prAction(1, 0.41, 0.45);
	}
	
	lf {|num|
		this.prAction(num, 0.6, 0);
	}
	
	rf {|num|
		this.prAction(num, 0.45, -0.04);
	}
	
	mf {|num|
		this.prAction(4, 0.35, -0.03);
	}
	
	pf {|num|
		this.prAction(5, 0.27, 0.09);
	}
	
	tf {|num|
		this.prAction(6, 0.23, 0.32);
	}
	
}

