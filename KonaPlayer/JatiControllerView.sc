JatiControllerView {
	var <view;				//	CompositeView for all elements
	var <titleLabel;
	var octave;
	var note;
	var <>controller;
	var <>origin;
	var <parent;
	var window;
	var <>midiDeviceArray;
	var <midiPopUp;
	
	*new { |parent, origin|
		if(parent==nil) {
			^super.new.initWindow(origin);
		} {
			^super.new.init(parent, origin)
		};
	}
	
	initWindow {|aOrigin|
		window = Window("Jati Controller", Rect(100,100,245,355)).front;
		this.init(window, aOrigin);
	}
	
	init { |aParent, aOrigin|
		origin = aOrigin ?? (0@0);
		parent = aParent;
		view = CompositeView(parent, Rect(origin.x, origin.y, 245, 355))
			.background_(Color(0.7,0.7,0.7));
		
		midiDeviceArray = List[];
		note = 60;
			
		this.createLabel;
		this.createSrutiControls;
		this.createPlaybackControls;
		this.createVolumeControls;
	}
	
	createLabel {
		titleLabel = StaticText(view, Rect(5,5, view.bounds.width-10, 35))
			.string_("Sruti and Playback Controls")
			.align_(\centre)
			.background_(Color.grey)
			.stringColor_(Color.white)
			.font_(Font(Font.defaultSansFace,16));		
	}
	
	createSrutiControls {
		var octaveDownButton;
		var octaveUpButton;
		var keyboardButtonView;
		var srutiKeyboard;
		var keyboardLabel;
		var keyboardView;

		keyboardView = CompositeView(view, Rect(5,50,193,102));
		keyboardView.background_(Color(0.9,0.9,0.9));
		keyboardLabel = StaticText(keyboardView, Rect(1,1,190,20)).string_("Sruti").align_(\center).background_(Color.grey).stringColor_(Color.white);
		srutiKeyboard = MIDIKeyboard(keyboardView, Rect(2,24,150,75), 1, 60)
			.keyDownAction_({|key| 
				if(note!=nil) {
					srutiKeyboard.removeColor(note);
				};
				note = key;
				controller.sruti_(note);
				srutiKeyboard.setColor(key, Color.grey);	
			});
		srutiKeyboard.setColor(note, Color.grey);
		keyboardButtonView = CompositeView(keyboardView, Rect(158,22,32,75));
		
		octaveUpButton = Button(keyboardButtonView, Rect(6,6,20,20))
			.states_([
				["+", Color.white, Color.grey]
			])
			.action_({|but|
				controller.sruti_(controller.sruti+12);
			});
		octaveDownButton = Button(keyboardButtonView, Rect(6,49,20,20))
			.states_([
				["-", Color.white, Color.grey]
			])
			.action_({|but|
				controller.sruti_(controller.sruti-12);
			});
		
	}
	
	createPlaybackControls {
		var playbackView;
		var playbackLabel;
		var synthOnOffButton;
		var midiOnOffButton;
		var midiPopLabel;

		playbackView = CompositeView(view, Rect(5, 205, 193, 115));
		playbackView.addFlowLayout(1@1);
		playbackView.background_(Color(0.9,0.9,0.9));
		playbackLabel = StaticText(playbackView, 190@20).string_("Playback Controls").align_(\center).background_(Color.grey).stringColor_(Color.white);

		synthOnOffButton = Button(playbackView, 93@40)
			.states_([
				["Synth", Color.black, Color.green],
				["Synth", Color.black, Color.white]
			])
			.action_({|butt|
				var bool;
				if(butt.value==0) {
					bool = true
				} {
					bool = false;
				};
				controller.synthPlayback_(bool);
			});

		midiOnOffButton = Button(playbackView, 93@40)
			.states_([
				["MIDI", Color.black, Color.white],
				["MIDI", Color.black, Color.green]
			])
			.action_({|butt|
				var bool;
				if(butt.value==1) {
					bool = true
				} {
					bool = false;
				};
				controller.midiPlayback_(bool);
			});
			
		midiPopLabel = StaticText(playbackView, 190@20).string_("MIDI Device: ").align_(\center).background_(Color.grey).stringColor_(Color.white);
		midiPopUp = PopUpMenu(playbackView, 190@20);
		this.refreshMidiDevices;
		midiPopUp.action = {|menu|
			var device = midiDeviceArray[menu.value];
			controller.setMidiOut(device[0], device[1]);
			// [menu.value, menu.item].postln /*MIDIOut.newByName("IAC Driver", "Bus 1");*/
		};

	}
	
	refreshMidiDevices {
		midiPopUp.items = midiDeviceArray.collect { |item, i| item.reduce('+')};
		midiPopUp.value_(-1);
		midiPopUp.valueAction_(0);
	}
	
	createVolumeControls {
		var muteButton;
		var volumeView;
		var volumeSlider;
		
		volumeView = CompositeView(view, Rect(200,50,47,300));
		volumeSlider = EZSlider(
			volumeView, 
			Rect(8,0,30,265),
			" Vol", 
			ControlSpec(-inf, 6, 'db', 0.01, -inf, " dB"),
			{|ez| controller.amp_(ez.value.dbamp)},
			initVal:1,
			unitWidth:30, 
			numberWidth:60,
			layout:\vert
		).setColors(Color.grey,Color.white, Color.grey(0.7),Color.grey, Color.white, Color.white,nil,nil, Color.grey(0.7))
			.font_(Font(Font.defaultSansFace,10));
			

		muteButton = Button(volumeView, Rect(8,270,30,30))
			.states_([
				["M", Color.white, Color.(0.3,0.3,0.3)],
				["M", Color.white, Color.blue(0.8)]
			])
			.action_({|button|
				controller.mute_((button.value-1).abs);
			});
		
	}
	
}