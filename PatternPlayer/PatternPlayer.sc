/*
	TODO Project pattern onto a graph
	TODO Change Tala
	TODO Extend pattern box when end is reached
	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?
	
	TODO Abstract out Tala image so that other controls of tala window aren't available. Or make Tala into a view that is part of this, checking tala values before playing.
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
	var <pattern_field;
	var <pattern_set;
	var <play_stop_button;
	var <routine_set;
	var <sound_popup;
	var <tempo_field;
	var <tempo_text;
	var play_stop_rout;
	
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
		tala 			= Tala.new(tempo, gati, true);
		
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
		var group_starters;
		
		pattern = new_pattern;
		group_starters = List[];
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
					1.postln;
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
							2.postln;
						} {
							//Else it's a group secondary note
							sound = 1;
							3.postln;
						};
					} {
						//Else the note is the last note so should be a secondary note
						sound = 1;
						4.postln;
					};
				};
			} {
				if(pat_sym[index] == 'X') {
					sound = 0;
					5.postln;
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
		var h = 100;
		var	w = 400;
		window = Window.new("Pattern Player", Rect((Window.screenBounds.width/2)-(w/2),(Window.screenBounds.height/2)-(h/2),w,h), false)
			.userCanClose_(true)
			.front;

		pattern_field = TextField(window, Rect(10,10,w-20,20))
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
		play_stop_button = Button(window, Rect(10,40,50,50))
			.states_([
				["Play", Color.black, Color.green],
				["Stop", Color.white, Color.red]
			])
			.action_({|button|
				play_stop_rout.();
			});

		sound_popup = EZPopUpMenu(
			window, 
			Rect(w-210,40,200,20), 
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


/*create_gui {

	tempo_text 	= StaticText(window, Rect(70, 40, 45, 20)).string_("Tempo: ");
	tempo_field = NumberBox(window, Rect(120,40,30,20))
			.value_(this.tempo)
			.action_({|field|
				this.tempo_(field.value.asInteger);
			});		
}
*/
