<<<<<<< HEAD:PatternPlayer/PatternPlayer.sc
/*
=======
 /*
	TODO Add gati input
>>>>>>> master:PatternPlayer/PatternPlayer.sc
	TODO Restrict 
	TODO Project pattern onto a graph
	TODO Change Tala
	TODO Extend pattern box when end is reached
	TODO Draw graph showing pattern against Tala
	TODO Enter key creates new line, click button to set?
	
	TODO Abstract out Tala image so that other controls of tala window aren't available. Or make Tala into a view that is part of this, checking tala values before playing.
*/


PatternPlayer {
	var <>pattern;
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
		gati			= 4;
		s 				= Server.default;
		tala 			= Tala.new(tempo, gati, false);
		
		this.set_func;
		
		{
			this.load_buffers;
			this.load_synth_def;
			s.sync;
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
	
	set_func {
		tala.gati_func = {|i, j|
			var index;
			
			switch (pattern.wrapAt(j%pattern.size).asSymbol)
				{'x'}	{index = 0}
				{'o'}	{index = 1};
				
			Synth(\simple_play, [\bufnum, buffers[index]]);
			index.postln;
		}
	}
	
	
}


/*create_gui {
	var	w = 400;
	var h = 100;
	window = Window.new("Pattern Player", Rect((Window.screenBounds.width/2)-(w/2),(Window.screenBounds.height/2)-(h/2),w,h), false)
		.userCanClose_(true)
		.front;
	pattern_field = PPTextField(window, Rect(10,10,w-20,20))
		.string_(pattern)
		.action_({|field| 
			this.set_pattern(field.value);
			this.confirm_set(routine_set);
		});

	pattern_set = StaticText(window, Rect(70,70,70, 20)).background_(Color.white).align_(\center);
	play_stop_button = Button(window, Rect(10,40,50,50))
		.states_([
			["Play", Color.black, Color.green],
			["Stop", Color.white, Color.red]
		])
		.action_({|button|
			if(play_routine.isPlaying) {
				this.stop;
			} {
				this.play;
			};
		});
	routine_set = StaticText(window, Rect(70,70,80,20)).background_(Color.white);
	sound_popup = EZPopUpMenu(
		window, 
		Rect(w-210,40,200,20), 
		"Sound",
		[
			\Konakkol 	->{|a| sounds = konakkol_sounds;},
			\Kanjira 	->{|a| sounds = kanjira_sounds},
			\Custom 	->{|a| sounds = custom_sounds;}
		],
		gap:5@5
	);
	tempo_text 	= StaticText(window, Rect(70, 40, 45, 20)).string_("Tempo: ");
	tempo_field = NumberBox(window, Rect(120,40,30,20))
			.value_(this.tempo)
			.action_({|field|
				this.tempo_(field.value.asInteger);
			});		
}
*/
