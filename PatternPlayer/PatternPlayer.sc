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
	var <tempo;
	var <>gati;
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
		tempo 			= 60;
		gati			= 4;
		s 				= Server.default;
		tala 			= Tala.new(tempo, gati, false);
		jatis			= List[];
		
		this.pattern_("Xxxx");
		
		{
			this.loadBuffers;
			this.loadSynthDef;
			s.sync;
		}.fork;
		
		pGUI 		= PatternPlayerGUI.new(this);
		tala.tGUI	= pGUI.talaGui;
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
	}
	
	pattern_ {|newPattern|
		jatis = List[];
		
		if(newPattern.includesAny([$,, $(, $), $_, $[, $]]).not) {
			pattern = newPattern;
			jatis.add(Jati(pattern.size, gati, 1));
			jatis.last.syllables = pattern;
		};
		this.createJatisRoutine;
	}
	
	play {
		tala.play;
		jatisRoutine.play(tala.clock, 1);
	}
	
	stop {
		tala.stop;
		jatisRoutine.stop;
		this.createJatisRoutine;
	}
	
	isPlaying {
		^tala.isPlaying;
	}
}