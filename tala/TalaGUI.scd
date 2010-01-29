
/*
	TODO MIDIKeyboard for Sruti, with octave buttons
	TODO Image box
*/

TalaGUI {
	var <win;
	
	*new {
		^super.new.init
	}
	
	init {
		win = Window("Tala", Rect(400,400,400,400)).front;;
		
	}
}

{
	5.wait;
	~win.refresh
}.fork(AppClock);

~custom_tala_field.bounds.height_(100)
(
/*~win.close;*/
~string_width = {|string| GUI.stringBounds(string).width};
~margin = 5;
/* 
	FIXME Stop expanding textfield from changing with a long single line
	FIXME Stop textfield from shrinking after adding a new line (enter) 
*/
~resize = {|keycode| 
	if(GUI.stringBounds(~custom_tala_field.string,~custom_tala_field_font).width > (~custom_tala_field.bounds.width - 9) || (keycode == 36)) {
		~custom_tala_field.bounds = ~custom_tala_field.bounds.height_(~custom_tala_field.bounds.height + 20);
	} {
		if(~custom_tala_field.bounds.height!=20) {
			~custom_tala_field.bounds = ~custom_tala_field.bounds.height_(~custom_tala_field.bounds.height - 20);
		};
	};
	~custom_tala_field.focus(false);
	~custom_tala_field.focus(true);		
	~win.refresh
};
~win 	= Window("Tala", Rect(400,400,400,400)).front;
~tempo_string = " Tempo ";
~tempo 	= EZNumber(	~win, 
					Rect(~margin,5,75,20),
					~tempo_string,
					ControlSpec(1,999,\lin,1,120,"bpm"),
					{|ezn| ("Tempo is now: " ++ ezn.value).postln},
					120,
					labelWidth:~string_width.(~tempo_string),
					numberWidth:30,
					gap:~margin@~margin
		).setColors(Color.grey, Color.white);
~tala_pop_string = " Tala Presets ";
~tala_pop = EZPopUpMenu( ~win,
						 Rect(~margin,35,200,20),
						 ~tala_pop_string,
						[
							'Adi' -> {|a| "Adi".postln},
							'Rupaka' -> {|a| "Rupaka".postln},
							'Khanda Chapu' -> {|a| "Khanda Capu".postln},
							'Misra Chapu' -> {|a| "Misra Capu".postln},
							'Sankeerna Chapu' -> {|a| "Sankeerna Capu".postln},
							
						],
						globalAction: {|a| "Global Action".postln},
						initVal: 0,
						initAction: false,
						labelWidth: ~string_width.(~tala_pop_string),
						gap:~margin@~margin
		).setColors(Color.grey, Color.white);
~custom_tala_text_string = " Custom Tala ";
~custom_tala_text = StaticText(~win, Rect(~margin,65, ~string_width.(~custom_tala_text_string),20))
						.string_(~custom_tala_text_string)
						.stringColor_(Color.white)
						.background_(Color.grey);
~custom_tala_field_font = Font("Monaco", 12);
~custom_tala_field = TextView(~win, Rect(~custom_tala_text.bounds.width + (~margin*2), 65, 120, (4*GUI.stringBounds("a", ~custom_tala_field_font).height)+5))
						.font_(~custom_tala_field_font)
						.usesTabToFocusNextView_(false)
						.enterInterpretsSelection_(false)
						.hasVerticalScroller_(true)
						.autohidesScrollers_(true);
~start_stop_routine = Routine {
	inf.do {
		"Started!".postln;
		0.yield;
		"Stopped!".postln;
		0.yield;
	};
};
~start_stop = Button(~win, Rect(~margin,145, 150, 20))
				.states_([
					["Start Tala", Color.black, Color.green],
					["Stop Tala", Color.white, Color.red]
				])
				.action_({|button|
					~start_stop_routine.();
				});
~start_stop_drone = Routine {
	inf.do {
		"Drone Started!".postln;
		0.yield;
		"Drone Stopped".postln;
		0.yield
	}
	
};
~drone_on_off = Button(~win, Rect(~margin,175,75,60))
				.states_([
					["Start Drone", Color.black, Color.white],
					["Stop Drone", Color.white, Color.black]
				])
				.action_({|button|
					~start_stop_drone.()
				});
~drone_pitch_
)

/*
	TODO Gati
	TODO Multislider for sub-divisions
	TODO Eduppu
*/