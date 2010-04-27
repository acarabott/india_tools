w = Window("jati", Rect(40,40,400,400)).front;
~view = CompositeView(w, 400@400);
~view.addFlowLayout;
~controlsView = CompositeView(~view, 250@304);
// ~controlsView.addFlowLayout;
// ~controlsView.decorator.margin_(0@0).gap_(0@0);
~controlsView.background_(Color(0.7,0.7,0.7));
~keyboardView = CompositeView(~controlsView, Rect(4,4,193,102));
// ~keyboardView.addFlowLayout(10@0,0@10);
~keyboardView.background_(Color(0.9,0.9,0.9));
// ~keyboardView.decorator.margin_(0@0);
~keyboardLabel = StaticText(~keyboardView, Rect(1,1,190,20)).string_("Sruti").align_(\center).background_(Color.grey).stringColor_(Color.white);
// ~keyboardView.decorator.nextLine;
// ~keyboardLabel.background_(Color.green);
~srutiKeyboard = MIDIKeyboard(~keyboardView, Rect(10,31,150,75), 1, 60);
~octave = 0;
~note = 60;
~srutiFunc = {(~note + (~octave*12)).postln};

~srutiKeyboard.keyDownAction_({|note| 
	if(~note!=nil) {
		~srutiKeyboard.removeColor(~note);
	};
	~note = note;
	~srutiFunc.();
	~srutiKeyboard.setColor(note, Color.grey);	
	// ~jatiSetSrutiFunction
});
~keyboardButtonView = CompositeView(~keyboardView, Rect(158,22,32,75));
// ~keyboardButtonView.background_(Color.red);
~octaveUpButton = Button(~keyboardButtonView, Rect(6,6,20,20))
	.states_([
		["+", Color.white, Color.grey]
	])
	.action_({|but|
		~octave = ~octave + 1;
		~srutiFunc.();
	});
~octaveDownButton = Button(~keyboardButtonView, Rect(6,49,20,20))
	.states_([
		["-", Color.white, Color.grey]
	])
	.action_({|but|
		~octave = ~octave - 1;
		~srutiFunc.();
	});

MIDIClient.init;
~midiDeviceArray = List[];
MIDIClient.destinations.do { |item, i|	
	~midiDeviceArray.add(item.device + item.name);
};

// ~controlsView.decorator.nextLine;
~playbackView = CompositeView(~controlsView, Rect(4, 150, 193, 115));
~playbackView.addFlowLayout(1@1);
~playbackView.background_(Color(0.9,0.9,0.9));
~playbackLabel = StaticText(~playbackView, 190@20).string_("Playback Controls").align_(\center).background_(Color.grey).stringColor_(Color.white);

~synthOnOffButton = Button(~playbackView, 93@40)
	.states_([
		["Turn synth off", Color.white, Color.black],
		["Turn synth on", Color.black, Color.white]
	])
	.action_({|butt|
		butt.value.postln;	
	});

~midiOnOffButton = Button(~playbackView, 93@40)
	.states_([
		["Turn MIDI on", Color.black, Color.white],
		["Turn MIDI off", Color.white, Color.black]
	])
	.action_({|butt|
		butt.value.postln;	
	});
~midiPopLabel = StaticText(~playbackView, 190@20).string_("MIDI Device: ").align_(\center).background_(Color.grey).stringColor_(Color.white);
~midiPopUp = PopUpMenu(~playbackView, 190@20);
// ~midiPopUp.background_(Color.white);
~midiPopUp.items = ~midiDeviceArray.asArray;
~midiPopUp.action = {|menu| [menu.value, menu.item].postln /*MIDIOut.newByName("IAC Driver", "Bus 1");*/};

~volumeView = CompositeView(~controlsView, Rect(200,4,47,300));
// ~volumeView.background_(Color.red);
~volumeSlider = EZSlider(
	~volumeView, 
	Rect(8,0,30,265),
	" Vol", 
	ControlSpec(-inf, 12, 'db', 0.01, -inf, " dB"),
	{|ez| ez.value.postln;},
	initVal:1,
	unitWidth:30, 
	numberWidth:60,
	layout:\vert
).setColors(Color.grey,Color.white, Color.grey(0.7),Color.grey, 
	Color.white, Color.white,nil,nil, Color.grey(0.7))
.font_(Font("Helvetica",10));


Button(~volumeView, Rect(8,270,30,30))
	.states_([
		["M", Color.white, Color.blue(1.5)],
		["M", Color.white, Color.blue(0.8)]
	])
	.action_({|button|
		"mute!".postln;
	});

w = Window("hi", Rect(0,0,800,800)).front
x = KonaPlayerView.new(nil,w, 0@0)
x = KonaPlayerView.new
a = TalaView.new(nil,w)
b = KonaPlayerView.new(w)