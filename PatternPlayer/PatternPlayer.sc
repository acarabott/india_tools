/*
	TODO Parsing groups of xs to make takadimi takita etc
	TODO Extend pattern box when end is reached
	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?	
	TODO _Underline_ for double speed
*/

PatternPlayer {
	var <pattern;
	var <konakkolSounds;
	var <kanjiraSounds;
	var <customSounds;
	var <>sounds;
	var <>s;
	var <>buffers;
	var <tala;
	var <jatis;
	var <jatisRoutine;
	var <>pGUI;
	var <>amp;
	var <>mute;
	
	*new { 
		^super.new.init;
	}

	init { 
		konakkolSounds = ["sounds/KKTA.wav", "sounds/KKDIM.wav"];
		kanjiraSounds 	= ["sounds/KJDIM.wav", "sounds/KJBELL.wav"];
		customSounds	= List[];
		sounds 			= kanjiraSounds;
		amp				= 1;
		mute			= 1;
		s 				= Server.default;
		tala 			= Tala.new(60, 4, false);
		jatis			= List[];
		pattern			= "Xxxx";
		
		{
			this.loadBuffers;
			this.loadSynthDef;
			s.sync;
		}.fork;
		
		pGUI 		= PatternPlayerGUI.new(this);
		tala.tGUI	= pGUI.tGUI;
		
		this.pattern_("Xxxx");
		
		tala.stopFunc = { this.stop };
		
		
	}
	
	loadSynthDef {
		SynthDef(\simplePlay, {| out = 0, bufnum = 0, amp = 1|
			Out.ar(out, 
				(PlayBuf.ar(1, bufnum, BufRateScale.kr(bufnum), doneAction:2)*amp).dup
			)
		}).load(s)
	}
	
	loadBuffers {
		buffers = Array.newClear(sounds.size);
		sounds.do { |item, i|
			buffers[i] = Buffer.read(s, item);
		};
	}
	
	createJatisRoutine {
		jatisRoutine = Routine {
			jatis.do {|item, i|
				item.play(tala.clock);
				item.duration.wait;
			};
		};
		jatisRoutine = jatisRoutine.loop;
		
		tala.syncRoutines.clear;
		tala.syncRoutines.add(jatisRoutine);
	}
	
	pattern_ {|newPattern|
		tala.tGUI.playStopButton.valueAction_(0);
		pattern = newPattern;
		
		this.createJatis;
	}
	
	createJatis {
		
		jatis.clear;
				
		//Split up the string into the various jatis
		pattern.split($ ).do { |item, i|
			var syllables;
			var split;
			var jati;
			var gati = tala.gati;
			var karve = 1;
			split = item.split($:);
			if(split.size>1) {
				//Get the multiplier (number of __)
				karve = 1/(2 ** split[0].count({|item, i| item==$_}));
				gati = split[0].findRegexp("[0-9]")[0] ?? tala.gati;
				if(gati!=tala.gati) { gati = gati[1].asInteger };
			};
			syllables = split.last;	

			jati = Jati(syllables.size, gati, karve ).syllables_(syllables);
			jatis.add(jati);
		};	
		
		this.createJatisRoutine;
		
	}
	
	play {
		// tala.play;
		jatisRoutine.play(tala.clock, 1);
	}
	
	stop {
		// tala.stop;
		jatisRoutine.stop;
		jatis.do { |item, i|
			item.stop;
		};
		this.createJatisRoutine;
	}
	
	isPlaying {
		^tala.isPlaying;
	}
}
