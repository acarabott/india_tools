/*
	FIXME GUI IS FUCKEDDSS

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
	var <>pGUI;
		
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
}

PatternPlayerGUI {
	
	classvar <p_width;
	classvar <p_height;
	classvar <extent;
	
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
	var <tala_gui;
	
	*initClass {
		p_width = TalaGUI.extent.x;
		p_height = 50;
		extent = (p_width)@(TalaGUI.extent.y + p_height);
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
			Rect(	(s_bounds.width/2)-(extent.x/2),
					(s_bounds.height/2)-(extent.y/2),
					extent.x,
					extent.y
			),
			false
		).userCanClose_(true)
		.front;
		
	}
	
	create_gui {
		view = CompositeView(parent, Rect(position.x, position.y, extent.x, extent.y));
		view.decorator = FlowLayout(view.bounds);
		pattern_view = CompositeView(view, Rect(0,0, p_width, p_height));
		pattern_view.decorator = FlowLayout(pattern_view.bounds);
		pattern_field = TextField(pattern_view, Rect(10,10,p_width-20,20))
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
			TalaGUI.item_label_extent, 
			" Sound ",
			[
				\Kanjira 	->{|a| player.sounds = player.kanjira_sounds; player.load_buffers},
				\Konakkol 	->{|a| player.sounds = player.konakkol_sounds; player.load_buffers}/*,
								\Custom 	->{|a| sounds = custom_sounds;}*/
			],
			initVal: 0,
			initAction: false,
			labelWidth: TalaGUI.item_extent.x,
			gap: TalaGUI.m_point
		).setColors(Color.grey, Color.white);
		
		tala_gui = TalaGUI.new(player.tala, parent, 0@p_height+TalaGUI.margin);	
		
	}
}
