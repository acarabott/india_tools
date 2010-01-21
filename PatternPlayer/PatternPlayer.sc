/*
	TODO create routine, then make duplicates from that, faster?
	TODO Add gati input
	TODO Red text for unset values
	TODO Project pattern onto a graph
	TODO work with reading arrays instead of routines?
*/
PatternPlayer {
	var <pattern;
	var <>routine;
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
	var <no_play;
	
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
		tala 			= Tala.new(tempo);
		no_play 		= true;
		
		{
			this.load_buffers;
			this.load_synth_def;
			s.sync;
			this.create_routine;			
		}.fork;
		this.create_gui;
		no_play = false;
	}
	
	create_routine {
		var index;
		var item_c;
		routine = Routine {
			pattern.do { |item, i|
				item_c = item.toLower.asSymbol;
				if(sounds.size==2) {
					if(i%pattern.size!=0) {
						if(['x','o'].includes(pattern[i-1].toLower.asSymbol).not) {
							index = 0;
						} {
							index = 1;
						};
					} {
						index = 0;
						
					};
				};
				
				if((item_c=='x') || (item_c=='o')) {
					if(item_c=='x') {
						Synth(\simple_play, [\bufnum, buffers[index]]);
					};
					(wait_time/gati).wait;
				};
				

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
		this.create_routine;
		routine.play;
		tala.play;
	}
	
	stop {
		routine.stop;
		tala.stop;
	}
	
	tempo_ {|new_tempo|
		this.play_stop_button.valueAction_(0);
		tempo = new_tempo;
		wait_time = 60/tempo;
		tala.tempo = new_tempo;
	}
	
	set_pattern {|new_pattern|
		pattern = new_pattern;
	}
		
	create_gui {
		var	w = 400;
		var h = 100;
		window = Window.new("Pattern Player", Rect((Window.screenBounds.width/2)-(w/2),(Window.screenBounds.height/2)-(h/2),w,h), false)
			.userCanClose_(true)
			.front;
		pattern_field = TextField(window, Rect(10,10,w-20,20))
			.string_(pattern)
			.action_({|field| 
				this.set_pattern(field.value);
				this.confirm_set(routine_set);
			});
/*		pattern_set = StaticText(window, Rect(70,70,70, 20)).background_(Color.white).align_(\center);*/
		play_stop_button = Button(window, Rect(10,40,50,50))
			.states_([
				["Play", Color.black, Color.green],
				["Stop", Color.white, Color.red]
			])
			.action_({|button|
				if(routine.isPlaying) {
					this.stop;
/*					"stop!".postln;*/
				} {
					this.play;
/*					"play!".postln;*/
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
	
	confirm_set {|field|
		Routine {
			field.string_("Set!")
				.background_(Color.green);
			2.wait;
			field.string_("")
				.background_(Color.white);
		}.play(AppClock);
	}
	
	update_routine_set {|i|
		var dots = "";
		if(i == -1) {
			{routine_set.string = "";}.fork(AppClock)
		} {			
			{
				i.do {
					dots = dots ++ "."
				};
				routine_set.string = " Resetting " ++ dots;						
			}.fork(AppClock)
		};
	}	
}
		