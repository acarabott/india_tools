/*
	TODO Improve Drone functionality
*/

Tala {
	
	classvar <adi;			//	Adi Tala Preset
	classvar <rupaka;		//	Rupaka Tala Preset
	classvar <kCapu;		//	Khanda Capu Preset
	classvar <mCapu;		//	Misra Capu Preset
	classvar <sankeerna;	//	Sankeerna Capu Preset
	
	var <s;					//	Server
	var <>amp;				//	Amplification multiplier
	var <laya;				//	Tempo
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
		sankeerna 	= ["U", "U", "U", "K"]
	}
	
	*new {
		^super.new.init;
	}

	init {
		amp 		= 1;
		laya 		= 60;
		gati		= 4;
		kallai		= 1;
		eduppu		= 0;
		wait_time	= 60/laya;
		
		parts		= adi;
		
		routine_duration 	= 0;
		no_play 			= true;
		this.create_routine;
		
		drone_note 	= 64;
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
	
	laya_ {|new_value|
		laya 		= new_value;
		wait_time 	= 60/laya;
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
		{
			while({no_play}, {
				0.1.wait;
			});
			routine.play;
		}.fork
	}
	
	stop {
		routine.stop;
		this.reset;
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
		parts = adi;
		this.create_routine;
	}
	
	rupaka {
		parts = rupaka;
		this.create_routine;		
	}
	
	khanda {
		parts = kCapu;
		this.create_routine;
	}
	
	misra {
		parts = mCapu;
		this.create_routine;
	}
	
}
