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
				//	Set the pattern, unless there is an uneven number of underscores
				var underscores = field.value.occurrencesOf($_);
				var mult;
				if(underscores.even) {
					if(underscores==0) {
						mult = 1;
					} {
						mult = underscores
					};
					player.tala.gati_mult = mult;
					player.pattern_(field.value);				
					field.stringColor = Color.black;
				} {
					field.stringColor = Color.red;					
				};
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