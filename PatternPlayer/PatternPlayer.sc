/*
	TODO Add gati input
	TODO Restrict 
	TODO Red text for unset pattern?
	TODO Project pattern onto a graph
	TODO work with reading arrays instead of routines?
	TODO Change Tala
	TODO Extend pattern box when end is reached
	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?
	
	TODO Abstract out Tala image so that other controls of tala window aren't available. Or make Tala into a view that is part of this, checking tala values before playing.
*/


PatternPlayer {
	var <pattern;
	var <stored_routine;
	var <play_routine;
	var <konakkol_sounds;
	var <kanjira_sounds;
	var <custom_sounds;
	var <>sounds;
	var <tempo;
	var <wait_time;
	var <>gati;
	var <>buffers;
	var <>s;
	var <tala;
	var <clock;
	
	var <window;
	var <pattern_field;
	var <pattern_set;
	var <play_stop_button;
	var <routine_set;
	var <sound_popup;
	var <tempo_field;
	var <tempo_text;
	
	*new { 
		^super.new.init;
	}

	init { 
		pattern 		= "xxxx";
		konakkol_sounds = ["sounds/KKTA.wav", "sounds/KKDIM.wav"];
		kanjira_sounds 	= ["sounds/KJDIM.wav", "sounds/KJBELL.wav"];
		custom_sounds	= List[];
		sounds 			= kanjira_sounds;
		tempo 			= 60;
		wait_time		= 60/tempo;
		gati			= 4;
		s 				= Server.default;
		tala 			= Tala.new(tempo, gati, false);
		
		{
			this.load_buffers;
			this.load_synth_def;
			s.sync;
			this.create_routine;			
		}.fork;
/*		this.create_gui;*/
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
	
}