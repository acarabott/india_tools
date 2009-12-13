PatternPlayer {
	var <pattern;
	var <>routine;
	var <default_sounds;
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
	
	*new { 
		^super.new.init;
	}

	init { 
		pattern 		= "xxxx";
		default_sounds 	= ["sounds/DIM.wav", "sounds/BELL.wav"];
		sounds 			= default_sounds.copy;
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
			this.set_routine;			
		}.fork;
		this.create_gui;
		no_play = false;
	}
	
	set_routine {
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
		var i = 0;
		{
			while({no_play}, {
				this.update_routine_set(i%3);
				0.1.wait;
				i = i +1;
			});
			this.update_routine_set(-1);
			routine.play;
			tala.play;	
		}.fork		
	}
	
	stop {
		no_play = true;
		routine.stop;
		tala.stop;
		this.reset;
	}
	
	reset {
		{
			(wait_time*1.5).wait;
			routine.reset;
			no_play = false;
		}.fork;
	}
	
	restart {
		this.stop;
		this.play;
	}
	
	tempo_ {|new_tempo|
		tempo = new_tempo;
		wait_time = 60/tempo;
		tala.laya = new_tempo;
		this.restart;		
	}
	
	set_pattern {|new_pattern|
		pattern = new_pattern;
		if(routine.isPlaying) {
			this.restart
		} {
			this.reset;
		};
	}
	
	create_gui {
		window = Window.new("Pattern Player", Rect(500,500,500,500))
			.userCanClose_(false)
			.front;
		pattern_field = TextField(window, Rect(100,100,150,20))
			.string_(pattern)
			.action_({|field| 
				this.set_pattern(field.value);
				this.confirm_set(pattern_set);
			});
		pattern_set = StaticText(window, Rect(185, 130, 40, 20)).background_(Color.white).align_(\center);
		play_stop_button = Button(window, Rect(50,50,50,50))
			.states_([
				["Play", Color.black, Color.green],
				["Stop", Color.white, Color.red]
			])
			.action_({|button|
				if(routine.isPlaying) {
					this.stop;
				} {
					this.play;
				};
			});
		routine_set = StaticText(window, Rect(150,120,100,20)).background_(Color.white);
		
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
				routine_set.string = "Loading " ++ dots;						
			}.fork(AppClock)
		};
	}
	
	
}
		