/*
	TODO o SHOULDN'T PLAY!!!ONE1
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
		pattern = new_pattern.reject({|item, i| ['x','o'].includes(item.asSymbol).not });
		
		tala.gati_func = {|i, j|
			var index;
			
			switch (pattern.wrapAt(j%pattern.size).asSymbol)
				{'x'}	{index = 0}
				{'o'}	{index = 1};
				
			if(index != nil) {
				Synth(\simple_play, [\bufnum, buffers[index]]);
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
