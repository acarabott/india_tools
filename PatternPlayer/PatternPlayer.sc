PatternPlayer {
	var <pattern;
	var <>routine;
	var <default_sounds;
	var <>sounds;
	var <tempo;
	var <wait_time;
	var <>gati;
	var <>buffers;
	var <>s;
	var <tala;
	var <clock;
	var <no_play;
	
	*new { 
		^super.new.init;
	}

	init { 
		pattern 		= "xxxx";
		default_sounds 	= ["sounds/DIM.wav", "sounds/BELL.wav"];
		sounds 			= default_sounds.copy;
		tempo 			= 60;
		wait_time		= 60/tempo;
		gati			= 4;
		s 				= Server.default;
		tala 			= Tala.new(tempo);
		no_play 		= true;
		
		s.waitForBoot {
			this.load_synth_def;
			this.load_buffers;
			this.set_routine;
			no_play = false;
		};
	}
	
	set_routine {
		var index;
		routine = Routine {
			pattern.do { |item, i|
				if(sounds.size==2) {
					if(i%pattern.size!=0) {
						index = 1;
					} {
						index = i%pattern.size;
					};
				};
				switch (item.asSymbol)
					{'x'}	{ Synth(\simple_play, [\bufnum, buffers[index]]) }					
					{'o'}	{};

				(wait_time/gati).wait;
			};
		};
		
		routine = routine.loop;
	}
	
	load_synth_def {
		SynthDef(\simple_play, {| out = 0, bufnum = 0, amp = 1|
			Out.ar(out, 
				(PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), doneAction:2)*amp).dup
			)
		}).load(s)
	}
	
	load_buffers {
		buffers = Array.newClear(sounds.size);
		sounds.do { |item, i|
			buffers[i] = Buffer.read(s, item);
		};
	}
	
	play {
		{
			while({no_play}, {
				0.1.wait;
			});
			routine.play;
			tala.play;	
		}.fork		
	}
	
	stop {
		{
			routine.stop;
			tala.stop;
			no_play = true;
			(wait_time*1.5).wait;
			routine.reset;
			no_play = false;
		}.fork
	}
	
	restart {
		this.stop;
		this.play;
	}
	
	tempo_ {|new_tempo|
		tempo = new_tempo;
		wait_time = 60/tempo;
		tala.laya = new_tempo;
		this.restart;		
	}
	
	pattern_ {|new_pattern|
		pattern = new_pattern;
		this.restart;
	}
		
}
