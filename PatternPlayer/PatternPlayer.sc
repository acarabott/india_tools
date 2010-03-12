/*

	TODO Parsing groups of xs to make takadimi takita etc
	TODO Extend pattern box when end is reached
	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?
	
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
	
	var <window;
	var <pattern_view;
	var <pattern_field;
	var <pattern_set;
	var <play_stop_button;
	var <routine_set;
	var <sound_popup;
	var <tempo_field;
	var <tempo_text;
	var play_stop_rout;
	var tala_gui;
	
	*new { 
		^super.new.init;
	}

	init { 
		konakkol_sounds = ["sounds/KKTA.wav", "sounds/KKDIM.wav"];
		kanjira_sounds 	= ["sounds/KJDIM.wav", "sounds/KJBELL.wav"];
		custom_sounds	= List[];
		sounds 			= kanjira_sounds;
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
		this.create_gui;
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
		
		pattern = new_pattern;
		all_x = List[];
		
		//	Strip all non x, o or space chars from the pattern
		pat_sym = pattern.collectAs({|item, i| item.asSymbol }, Array).reject({|item, i| ['x','o',' ',',','-'].includes(item.asSymbol).not });
		
		//	Find group starters; xs after space	
		pat_sym.do { |item, i|
			if(item=='x' && (pat_sym[i-1]==' ')) {
				pat_sym[i] = 'X'
			};
		};
		//	Remove spaces
		pat_sym = pat_sym.removeEvery([' ']);
		
		//	Store indices of xs
		pat_sym.do { |item, i|
			if(item=='x' || (item=='X')) {
				all_x.add(i);
			};
		};
		
		tala.gati_func = {|i, j|
			var sound;
			var index;
			var cur, prev, next;
			
			//looping index
			index = (j%pat_sym.size).asInteger;

			if(pat_sym[index] == 'x') {		
				if(index == all_x[0]) {		//	If this is the first x
					sound = 0;					//	Make it a Ta!
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
			} {
				//	if it's a group starter, play the first sound
				if(pat_sym[index] == 'X') {
					sound = 0;
				};
			};
			
			if(sound!=nil) {
				Synth(\simple_play, [\bufnum, buffers[sound]]);
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
	
	create_gui {
		var	pattern_w = 400;
		var pattern_h = 100;
		var tala_extent = TalaGUI.extent;
		var total_extent = (pattern_w + tala_extent.x)@(pattern_h + tala_extent.y);
		"tala_extent: ".post; (tala_extent).postln;
		
		window = Window.new(
			"Pattern Player", 
			Rect(
				(Window.screenBounds.width/2)-(total_extent.x/2),
				(Window.screenBounds.height/2)-(total_extent.y/2),
				total_extent.x,
				total_extent.y
			), 
			false
			)
			.userCanClose_(true)
			.front;
		
		pattern_view = CompositeView(window, Rect(0,0, pattern_w, pattern_h));
		pattern_view.decorator = FlowLayout(pattern_view.bounds);
			
		tala_gui 		= TalaGUI.new(tala);	
		
		pattern_field = TextField(pattern_view, Rect(10,10,pattern_w-20,20))
			.string_(pattern)
			.action_({|field| 
				this.pattern_(field.value);
				field.stringColor = Color.black;
			})
			.keyDownAction_({|view, char, mod, uni|
				view.stringColor = Color.red;
			})
			.keyUpAction_({|view, char, mod, uni|
				if(view.value.asSymbol == pattern.asSymbol) {
					view.stringColor = Color.black;
				};
			});
		
		this.create_play_stop_rout;
		
		play_stop_button = Button(pattern_view, Rect(10,40,50,50))
			.states_([
				["Play", Color.black, Color.green],
				["Stop", Color.white, Color.red]
			])
			.action_({|button|
				play_stop_rout.();
			});

		sound_popup = EZPopUpMenu(
			window, 
			Rect(pattern_w-210,40,200,20), 
			"Sound",
			[
				\Kanjira 	->{|a| sounds = kanjira_sounds; this.load_buffers},
				\Konakkol 	->{|a| sounds = konakkol_sounds; this.load_buffers}/*,
								\Custom 	->{|a| sounds = custom_sounds;}*/
			],
			gap:5@5
		);
		
	}
	
	create_play_stop_rout {
		play_stop_rout = Routine {
			inf.do {|i|
				this.play;
				0.yield;
				this.stop;
				0.yield
			};
		};
	}
}