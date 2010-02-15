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
	
	var <>gati;				//	Gati (Sub-division)
	var <>gati_mult;		//	Gati multiplier, e.g. to change from 3 per beat to 6 etc

	var <>kallai;			//
	var <eduppu;
	
	var <>parts;			
	var <tala_routine;			//	Tala playback routine
	var <tala_routine_duration;	//	The duration (in seconds) of the routine
	var <no_play;				//	If true, the routine can't yet be played
	
	var <gati_routine;			//	Gati playback routine;
		
	var <tGUI;					//	GUI :)
	
	*initClass {
		adi 		= ["I4", "O", "O"];
		rupaka 		= ["U", "O"];
		kCapu 		= ["U", "K"];
		mCapu		= ["M", "U", "U"];
		sCapu 		= ["U", "U", "U", "K"];
	}
	
	*new {|aTempo=60, aGati=4, aGUI=true|
		^super.new.init(aTempo, aGati, aGUI);
	}

	init {|aTempo, aGati, aGUI|
		amp 		= 1;
		tempo 		= aTempo;
		gati		= aGati;
		gati_mult	= 1;
		kallai		= 1;
		eduppu		= 0;
		wait_time	= 60/tempo;
		
		parts		= adi;
		
		tala_routine_duration 	= 0;
		no_play 				= true;
		
		this.create_tala_routine;
		this.create_gati_routine;
			
		s = Server.default;
		this.load_synth_defs;		
		
		no_play = false;
		
		if(aGUI) {
			tGUI = TalaGUI.new(this);
		};
		
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
				
	}
	
	tempo_ {|new_value|
		tempo 		= new_value;
		wait_time 	= 60/tempo;
	}
	
	
	//	Routine methods
	add_tala_routine {|to_add|
		tala_routine = tala_routine ++ to_add
	}
	
	create_tala_routine {
		tala_routine = Routine {};
		parts.do { |item, i|
			switch (item[0].asSymbol)
				{'I'}	{ tala_routine = tala_routine ++ this.laghu(item[1].digit) }
				{'O'}	{ tala_routine = tala_routine ++ this.drutam()}
				{'U'}	{ tala_routine = tala_routine ++ this.anudrutam}
				{'K'}	{ tala_routine = tala_routine ++ this.capu("clap")}
				{'M'}	{ tala_routine = tala_routine ++ this.capu("clap_b")};
			
		};
		
		tala_routine = tala_routine.loop;
	}
	
	create_gati_routine {
		gati_routine = Routine {
			inf.do { |item, i|
				this.generic_clap(0.01, 0.01, 4000, 4000, 1);
				(wait_time/(gati*gati_mult)).wait;	
			};
		};
	}
	
	play {
		if(tala_routine.isPlaying.not) {
			tala_routine.play;
			gati_routine.play;
		};
		
	}
	
	stop {
		tala_routine.stop;
		gati_routine.stop;
		this.create_tala_routine;
		this.create_gati_routine;
	}
	
	add_rout_time {|time|
		tala_routine_duration = tala_routine_duration + time;
	}
	
	check_stop_tala {|new_tala|
		if(new_tala!=parts) {
			this.stop;
			tGUI.start_stop_button.valueAction_(0);
		};
	}
	//	Angas
	
	laghu {|number|
		this.add_rout_time(number*wait_time);
		^Routine {
			this.clap();
			wait_time.wait;
			(number-1).do { |i|
				this.finger(i+2);
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
		tGUI !? {tGUI.clap};
		this.generic_clap(0.8, 0.9, 2000, 2500, 0.9);
		this.generic_clap(0.7, 0.8, 700, 1200, 0.9);
		
	}
			
	clap_b {
		tGUI !? {tGUI.wave};
		this.generic_clap(0.1, 0.2, 400, 600, 0.9);
	}

	finger {|number|
		var a =	case
				{[2,7].includes(number)}	{tGUI !? {tGUI.lf(number)}}
				{[3,8].includes(number)}	{tGUI !? {tGUI.rf(number)}}
				{[4,9].includes(number)}	{tGUI !? {tGUI.mf(number)}}
				{number==5}					{tGUI !? {tGUI.pf(number)}}
				{number==6}					{tGUI !? {tGUI.tf(number)}};
		
		this.generic_clap(0.2, 0.3, 6000, 7000, 0.9);
		
	}
	
	//	Synth methods
	generic_clap {|amp1, amp2, freq1, freq2, rq|
			Synth(\clapping, [\amp, rrand(amp1, amp2)*amp, \filterfreq, rrand(freq1, freq2), \rq, rq.rand]) 
	}
	
	//	Preset loading methods
	adi {
		this.check_stop_tala(adi);
		parts = adi;
		this.create_tala_routine;
	}
	
	rupaka {
		this.check_stop_tala(rupaka);		
		parts = rupaka;
		this.create_tala_routine;		
	}
	
	kCapu {
		this.check_stop_tala(kCapu);
		parts = kCapu;
		this.create_tala_routine;
	}
	
	mCapu {
		this.check_stop_tala(mCapu);
		parts = mCapu;
		this.create_tala_routine;
	}

	sCapu {
		this.check_stop_tala(sCapu);
		parts = sCapu;
		this.create_tala_routine;
	}	
}