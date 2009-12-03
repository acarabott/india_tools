Tala {
	
	classvar <adi;			//	Adi Tala Preset
	classvar <rupaka;		//	Rupaka Tala Preset
	classvar <kCapu;		//	Khanda Capu Preset
	classvar <mCapu;		//	Misra Capu Preset
	
	var <s;					//	Server
	
	var <laya;				//	Tempo
	var <wait;				//	Wait time
	var <>kallai;
	var <gati;
	var <eduppu;
	
	var <parts;			
	var <routine;			//	The playback routine
	var <routine_duration;	//	The duration (in seconds) of the routine
	
	*initClass {
		adi 	= ["I4", "O", "O"];
		rupaka 	= ["U", "O"];
		kCapu 	= ["U", "K"];
		mCapu	= ["M", "U", "U"];
	}
	
	*new {
		^super.new.init;
	}

	init {
		s = Server.default;
		s.waitForBoot;
		
		laya 	= 60;
		gati	= 4;
		kallai	= 1;
		eduppu	= 0;
		wait	= 60/laya;
		
		parts	= adi;
		
		routine_duration = 0;
		this.create_routine;
		this.load_synth_defs;
		
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
			signal = CombC.ar(signal, 0.5, 0.03, 0.031)+CombC.ar(signal, 0.5, 0.03016, 0.06);
			//signal = FreeVerb.ar(signal, 0.23, 0.15, 0.2);
			signal = Limiter.ar(signal, 0.7, 0.01);
			Out.ar(0, Pan2.ar(signal*amp, 0));
			DetectSilence.ar(signal, doneAction:2);
		}).load(s)
	}
	
	laya_ {|new_value|
		laya = new_value;
		wait = 60/laya;
	}
	
	
	//	Routine methods
	add_routine {|to_add|
		routine = routine ++ to_add
	}
	
	create_routine {
		routine = Routine {};
		parts.do { |item, i|
			switch (item[0].asSymbol)
				{'I'}	{ this.add_routine(this.laghu(item[1].digit)) }
				{'O'}	{ this.add_routine(this.drutam())}
				{'U'}	{ this.add_routine(this.anudrutam)}
				{'K'}	{ this.add_routine(this.capu("clap"))}
				{'M'}	{ this.add_routine(this.capu("clap_b"))};
			
		};
		
		routine = routine.loop;
	}
	
	play {
		routine.reset;
		routine.play;
	}
	
	stop {
		routine.stop;
	}
	
	add_rout_time {|time|
		routine_duration = routine_duration + time;
	}
	
	//	Angas
	
	laghu {|number|
		this.add_rout_time(number*wait);
		^Routine {
			this.clap();
			wait.wait;
			(number-1).do { |i|
				this.finger(i);
				wait.wait;
			};
		};
	}
	
	drutam {
		this.add_rout_time(2*wait);
		^Routine {
			this.clap();
			wait.wait;
			this.clap_b();
			wait.wait;
		};
	}
	
	anudrutam {
		this.add_rout_time(wait);
		^Routine {
			this.clap();
			wait.wait;
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
		
		type.postln;
		type.asSymbol.postln;
		this.add_rout_time(wait*1.5);
		
		^Routine {
			which_clap.();
			(wait/2).wait;
			which_clap.();
			wait.wait;
		};
	}
	
	//	Gestures
	clap {
		"clap!".postln;
		this.generic_clap(0.4, 0.5, 2000, 2500, 0.9);
		
	}
			
	clap_b {
		"back clap!".postln;
		this.generic_clap(0.3, 0.35, 400, 600, 0.9);
	}

	finger {|number|
		("Finger - " ++ (number+1)).postln;
		this.generic_clap(0.01, 0.05, 6000, 7000, 0.9);
		
	}
		
	//	Synth methods
	generic_clap {|amp1, amp2, freq1, freq2, rq|
		s.bind {
			Synth(\clapping, [\amp, rrand(amp1, amp2), \filterfreq, rrand(freq1, freq2), \rq, rq.rand]) 
		}
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
