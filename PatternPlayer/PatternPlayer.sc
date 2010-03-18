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

PatternPlayerGUI {
	
	classvar <p_width;
	classvar <p_height;
	classvar <extent;
	classvar <margin;
	
	var <player;
	var <parent;
	var <position;
	
	var <view;
	
	var <width;
	var <height;
	
	var <window;
	var <pattern_view;
	var <pattern_field;
	var <sound_popup;
	var <amp_slider;
	var <tala_label;
	var <tala_gui;
	
	*initClass {
		p_width = 700;
		p_height = 75;
		extent = (p_width)@(310 + p_height);
		margin = 5.asPoint;
	}
	
	*new {|player, parent, position|
		if(parent==nil) {
			^super.new.initWindow(player);
		} {
			^super.new.initView(player, parent, position)
		};
	}
	
	initWindow {|aPlayer|
		this.create_window;
		position=0@0;
		this.init(aPlayer);
	}
	
	initView {|aPlayer, aParent, aPosition|
		parent = aParent;
		position = aPosition ? (0@0);
		this.init(aPlayer);
	}
	
	init {|aPlayer|
		player = aPlayer;
		this.create_gui;				
	}
	
	create_window {
		var s_bounds = Window.screenBounds;
		parent = Window.new("Pattern Player",
			Rect(	((s_bounds.width/2)-(extent.x/2)).floor,
					((s_bounds.height/2)-(extent.y/2)).floor,
					extent.x,
					extent.y
			),
			false
		).userCanClose_(true)
		.front;
		
	}
	
	create_gui {
		view = SCCompositeView(parent, Rect(position.x, position.y, extent.x, extent.y));
		view.decorator = FlowLayout(view.bounds, 0@0, 0@0);
		pattern_view = CompositeView(view, Rect(0,0, p_width, p_height));
		pattern_view.decorator = FlowLayout(pattern_view.bounds).margin_(margin).gap_(margin/2);
		pattern_field = TextField(pattern_view, Rect(5,5,p_width-20,20))
			.string_(player.pattern)
			.action_({|field| 
				player.pattern_(field.value);
				field.stringColor = Color.black;
			})
			.keyDownAction_({|view, char, mod, uni|
				view.stringColor = Color.red;
			})
			.keyUpAction_({|view, char, mod, uni|
				if(view.value.asSymbol == player.pattern.asSymbol) {
					view.stringColor = Color.black;
				};
			});
		
		
		sound_popup = EZPopUpMenu(
			pattern_view, 
			340@20, 
			" Sound ",
			[
				\Kanjira 	->{|a| player.sounds = player.kanjira_sounds; player.load_buffers},
				\Konakkol 	->{|a| player.sounds = player.konakkol_sounds; player.load_buffers}
				/*, \Custom 	->{|a| sounds = custom_sounds;}*/
			],
			initVal: 0,
			initAction: false,
			labelWidth: 165, //magic number...
			gap: margin
		).setColors(Color.grey, Color.white);
		
		Button(pattern_view, 20@20)
			.states_([
				["M", Color.white, Color.blue(1.5)],
				["M", Color.white, Color.blue(0.8)]
			])
			.action_({|button|
				player.mute = (button.value-1).abs
			});
		
		amp_slider = EZSlider(
			pattern_view, 
			(340-20)@20, //magic number
			" Vol  ", 
			ControlSpec(-inf, 6, 'db', 0.01, -inf, " dB"),
			{|ez| player.amp = ez.value.dbamp},
			initVal:1,
			// unitWidth:30, 
			// numberWidth:60,
			layout:\horz
		).setColors(Color.grey,Color.white, Color.grey(0.7),Color.grey, 
			Color.white, Color.white,nil,nil, Color.grey(0.7))
		.font_(Font("Helvetica",10));
		
		pattern_view.decorator.nextLine;
		
		tala_label = StaticText(pattern_view, p_width@20)
			.string_("Tala Controls")
			.align_(\center)
			.font_(Font("Lucida Grande",13));
		
		tala_gui = TalaGUI.new(player.tala, view, 0@p_height+10);	
	}
}
