
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
~string_width = {|string, font| GUI.stringBounds(string, font).width};
~one_char_bounds = GUI.stringBounds("a",Font("Monaco",12));

~margin = 5;
~m_point = ~margin@~margin;
~win 	= Window("Tala", Rect(25,400,520,300)).front;
~win_dec = ~win.addFlowLayout.margin_(~m_point).gap_(~m_point);
~ct_field_bounds = ((~one_char_bounds.width*22)+10)@(~one_char_bounds.height*5);
~label_bounds = 120@20;
~field_width = ~ct_field_bounds.x;
~total_width = ~label_bounds.x + ~field_width + ~margin;
~tempo 	= EZNumber(	~win, 
					~label_bounds.x+(~num_width=30)@~label_bounds.y,
					~tempo_string = " Tempo ",
					ControlSpec(1,999,\lin,1,120,"bpm"),
					globalAction:{|ezn| ("Tempo is now: " ++ ezn.value).postln},
					initVal:120,
					labelWidth:~label_bounds.x,
					numberWidth:~num_width,
					gap:~m_point
		).setColors(Color.grey, Color.white);
~win_dec.nextLine;
~gati_pop = EZPopUpMenu( ~win,
						 ~total_width@~label_bounds.y,
						 ~gati_pop_string = " Gati ",
						[
							'3 - Tisra'		->	{|a| "Tisra".postln;},
							'4 - Caturasra'	->	{|a| "Caturasra".postln;},
							'5 - Khanda'	->	{|a| "Khanda".postln;},
							'7 - Misra'		->	{|a| "Misra".postln;},
							'9 - Sankirna'	->	{|a| "Sankirna".postln;},
						],
						globalAction: {|a| "Global Action".postln;},
						initVal:1,
						initAction:false,
						labelWidth:~label_bounds.x,
						gap:~m_point
		).setColors(Color.grey, Color.white);
~win_dec.nextLine;
~tala_pop = EZPopUpMenu( ~win,
						 ~total_width@~label_bounds.y,
						 ~tala_pop_string=" Tala Presets ",
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
						labelWidth: ~label_bounds.x,
						gap:~m_point
		).setColors(Color.grey, Color.white);
/*~win_dec.nextLine;*/
~custom_tala_comp = CompositeView(~win, (~field_width + ~label_bounds.x+12)@~ct_field_bounds.y);


~custom_tala_comp.addFlowLayout.margin_(0@0).gap_(~margin@0).left_(~custom_tala_comp.decorator.bounds.width).top_(0);
~custom_tala_label = StaticText(~custom_tala_comp, ~ct_label_bounds)
						.string_(~ct_label_string)
						.stringColor_(Color.white)
						.background_(Color.grey)
						.align_(\right);
~custom_tala_field = TextView(~custom_tala_comp, ~ct_field_bounds)
						.font_(~custom_tala_field_font)
						.usesTabToFocusNextView_(false)
						.enterInterpretsSelection_(false)
						.hasVerticalScroller_(true)
						.autohidesScrollers_(true);
~win_dec.nextLine;
/*~custom_tala_comp.decorator.left=380*/
~start_stop_routine = Routine {
	inf.do {
		"Started!".postln;
		0.yield;
		"Stopped!".postln;
		0.yield;
	};
};

~start_stop = Button(~win, Rect(~margin, 145, 70, 40))
				.states_([
					["Start Tala", Color.black, Color.green],
					["Stop Tala", Color.white, Color.red]
				])
				.action_({|button|
					~start_stop_routine.();
				});
/*~start_stop_drone = Routine {
	inf.do {
		"Drone Started!".postln;
		0.yield;
		"Drone Stopped".postln;
		0.yield
	}
	
};
FlowLayout
~drone_on_off = Button(~win, Rect(~margin,175,75,60))
				.states_([
					["Start Drone", Color.black, Color.white],
					["Stop Drone", Color.white, Color.black]
				])
				.action_({|button|
					~start_stop_drone.()
				});
*/

)

/*
	TODO Gati
	TODO Multislider for sub-divisions
	TODO Eduppu
*/
p = PathName.new("/Users/arthurc/Documents/programming/computerMusic/india_tools/tala/images");
~images = Array.newClear(p.files.size);
p.filesDo {|aFile, i|
	~images[i] = SCImage.new(aFile.fullPath);
};
p.files.do { |item, i|
	item.postln;
};

~images
PathName.dumpInterface;
x = CompositeView(~win, 100@109)
x.backgroundImage_(~images[8..9])

~adi = [~images[8],~images[0..2], ~images[8..9] ! 2].flat;
{
	inf.do { |i|
		x.backgroundImage_(~adi.wrapAt(i));
		0.5.wait
	};
}.fork(AppClock)

[1,2] ! 2

a = [1,2,3,4,5,6,7,8]
[a[3..4] ! 2].flat