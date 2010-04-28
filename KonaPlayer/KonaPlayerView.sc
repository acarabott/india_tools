KonaPlayerView {
	
	classvar <pWidth;
	classvar <pHeight;
	classvar <extent;
	classvar <margin;
	
	var <player;
	var <parent;
	var <position;
	
	var <view;
	
	var <width;
	var <height;
	
	var <window;
	var <patternView;
	var <patternField;
	var <ampSlider;
	var <tView;
	
	*initClass {
		pWidth = 1000;
		pHeight = 400;
		extent = (pWidth)@(800);
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
		this.createWindow;
		position=0@0;
		this.init(aPlayer);
	}
	
	initView {|aPlayer, aParent, aPosition|
		parent = aParent;
		position = aPosition ? (0@0);
		this.init(aPlayer);
	}
	
	init {|aPlayer|
		player = aPlayer ?? KonaPlayer.new(false);
		this.createGUI;				
	}
	
	createWindow {
		var sBounds = Window.screenBounds;
		parent = Window.new("Pattern Player",
			Rect(	((sBounds.width/2)-(extent.x/2)).floor,
					((sBounds.height/2)-(extent.y/2)).floor,
					extent.x,
					extent.y
			),
			false
		).userCanClose_(true)
		.front;
		
	}
	
	createGUI {
		view = SCCompositeView(parent, Rect(position.x, position.y, extent.x, extent.y));
		view.decorator = FlowLayout(view.bounds, 0@0, 0@0);
		
		patternView = CompositeView(view, Rect(0,0, pWidth, pHeight)).background_(Color.grey);
		patternView.decorator = FlowLayout(patternView.bounds).margin_(margin).gap_(margin/2);
		
		patternField = TextField(patternView, Rect(5,5,pWidth-20,20))
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
				
		Button(patternView, 20@20)
			.states_([
				["M", Color.white, Color.blue(1.5)],
				["M", Color.white, Color.blue(0.8)]
			])
			.action_({|button|
				player.mute = (button.value-1).abs
			});
		
		ampSlider = EZSlider(
			patternView, 
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
		
		patternView.decorator.nextLine;
			
		tView = TalaView.new(player.tala, view, 0@pHeight+10);	
	}
}



// w = Window("KonaPlayer", Rect(40,40,1000,800)).front;
// documentView = CompositeView(w, Rect(0,0,1000, 400)).background_(Color.grey);
// field = TextView(documentView, Rect(5,5,890,390));
// documentButtonView = CompositeView(documentView, Rect(900,5,95,390));
// 
// saveButton = Button(documentButtonView, Rect(10,100,75,30))
// 	.states_([
// 		["Save", Color.black, Color.white],
// 	])
// 	.action_({|butt|
// 		butt.value.postln;	
// 	});
// saveAsButton = Button(documentButtonView, Rect(10,175,75,30))
// 	.states_([
// 		["Save As...", Color.black, Color.white],
// 	])
// 	.action_({|butt|
// 		butt.value.postln;	
// 	});
// openButton = Button(documentButtonView, Rect(10,25,75,30))
// 	.states_([
// 		["Open", Color.black, Color.white],
// 	])
// 	.action_({|butt|
// 		butt.value.postln;	
// 	});
// 
// x = TalaView.new(nil,w, 5@410);
// 
