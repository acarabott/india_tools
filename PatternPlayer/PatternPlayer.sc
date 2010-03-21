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
		
		this.pattern_("xxxx");
		
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
	
	pattern_ {|newPattern|
		
	}
	
	play {
		tala.play;
	}
	
	stop {
		tala.stop;
	}
	
	isPlaying {
		^tala.isPlaying;
	}
}