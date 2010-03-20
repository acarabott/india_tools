/*
	TODO x or X
	TODO Parsing groups of xs to make takadimi takita etc
	TODO Extend pattern box when end is reached
	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?	
	TODO _Underline_ for double speed
*/

PatternPlayer {
	var <pattern;
	var <konakkol_sounds;
	var <kanjira_sounds;
	var <custom_sounds;
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
		konakkol_sounds = ["sounds/KKTA.wav", "sounds/KKDIM.wav"];
		kanjira_sounds 	= ["sounds/KJDIM.wav", "sounds/KJBELL.wav"];
		custom_sounds	= List[];
		sounds 			= kanjira_sounds;
		amp				= 1;
		mute			= 1;
		tempo 			= 60;
		gati			= 4;
		s 				= Server.default;
		tala 			= Tala.new(tempo, gati, false);
		
		this.pattern_("xxxx");
		
		{
			this.load_buffers;
			this.load_synth_def;
			s.sync;
		}.fork;
		
		pGUI 		= PatternPlayerGUI.new(this);
		tala.tGUI	= pGUI.tala_gui;
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
	
	pattern_ {|new_pattern|
		var pat_sym;
		var all_x;
		var is_x  = {|sym| ['x','X'].includes(sym)};
		
		pattern = new_pattern;
		all_x = List[];
		
		//	Strip all non x, o or space chars from the pattern
		pat_sym = pattern.collectAs({|item, i| item.asSymbol }, Array).reject({|item, i| ['x','X','o',' ',',','-'].includes(item.asSymbol).not });
		
		//	If there are spaces, make the next x a group starter (if there is a next x)	
		pat_sym.do { |item, i|
			if(item==' ' && pat_sym[i..].includes('x')) {
				pat_sym[i+pat_sym[i..].indexOf('x')] = 'X'
			};
		};

		//	Remove spaces
		pat_sym = pat_sym.removeEvery([' ']);

		//	Store indices of xs
		pat_sym.do { |item, i|
			if(is_x.(item)) {
				all_x.add(i);
			};
		};
		pat_sym.postln;
		tala.gati_func = {|i, j|
			var sound;
			var index;
			var cur, prev, next;
			
			//looping index
			index = (j%pat_sym.size).asInteger;

			if(is_x.(pat_sym[index])) {		
				if(index == all_x[0]) {		//	If this is the first x
					sound = 0;					//	Make it a Ta!
				} {
					//	if it's a group starter, play the first sound
					if(pat_sym[index] == 'X') {
						sound = 0;
					} {
						//store index of current, previous and next 'x's
						cur = all_x.indexOf(index);
						prev = all_x[cur-1];
						next = all_x[cur+1];

						//If the x is not the first or last in the pattern
						if(next != nil && (prev != nil)) {
							//If the next x is closer to the current than the previous make it a group starter
							if((next - index) < (index - prev)) {
								sound = 0;
							} {
								//Else it's a group secondary note
								sound = 1;
							};
						} {
							//Else the note is the last note so should be a secondary note
							sound = 1;
						};						
					};
				};
			};
						
			if(sound!=nil) {
				fork {
					if(sounds==kanjira_sounds) {
						0.01.wait
					};
					Synth(\simple_play, [\bufnum, buffers[sound], \amp, amp*mute]);
				}
			};			
		}
	
	}
	
	play {
		tala.play;
	}
	
	stop {
		tala.stop;
	}
	
	is_playing {
		^tala.is_playing;
	}
}