PatternPlayer {
	var <pattern;
	var <>routine;
	var <default_sounds;
	var <>sounds;
	var <tempo;
	var <time;
	var <>gati;
	var <>buffers;
	var <>s;
	var <tala;
	var <tala_routine;
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
		time			= 60/tempo;
		gati			= 4;
		s 				= Server.default;
		tala 			= Tala.new(tempo);
		tala_routine	= tala.create_routine();
		clock 			= TempoClock.new(time);
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

				(time/gati).wait;
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
		routine.play(clock);
		tala_routine.play(clock);	
	}
	
	stop {
		{
			routine.stop;
			tala_routine.stop;
			no_play = true;
			(time*1.5).wait;
			routine.reset;
			tala_routine.reset;			
			no_play = false;
		}.fork
	}
	
	tempo_ {|new_tempo|
		tempo = new_tempo;
		time = 60/tempo;
		tala.laya = new_tempo;
		this.stop;
		{
			while({no_play}, {
				0.1.wait;
			});
			this.play;
		}.fork
		
	}
	
	pattern_ {|new_pattern|
		pattern = new_pattern;
		{
			routine.stop;
			(time*1.1).wait;
			routine.reset;
			routine.play(clock, 1)		
		}.fork;
	}
		
}
