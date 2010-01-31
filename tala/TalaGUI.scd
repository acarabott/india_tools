
/*
	TODO MIDIKeyboard for Sruti, with octave buttons
	TODO Image box
	TODO FullScreen image
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
~win.close;
~string_width 			= {|string, font| GUI.stringBounds(string, font).width};
~one_char_bounds 		= GUI.stringBounds("a",Font("Monaco",12));
~margin 				= 5;
~line_height			= 20;
~m_point 				= ~margin@~margin;
~item_bounds			= (~one_char_bounds.width*22) + 10 @ ~line_height;
~total_bounds			= ~item_bounds.x*2 + ~margin @ ~line_height;
~ct_field_bounds		= ~item_bounds.x@(~one_char_bounds.height*5-5);
~custom_tala_field_font	= Font("Monaco", 12);
~regular_font			= if(Font.availableFonts.any{|item, i| item.asSymbol=='Cochin'}) {
								Font("Cochin",12)
							} {
								Font("Times",50)
							};


~win 		= Window("Tala", Rect(25,400,(~total_bounds.x*2)+(~margin*4),300),false).front;
~left_side 	= CompositeView(~win, Rect(0,0,~win.bounds.width/2, ~win.bounds.height));

~left_dec 	= ~left_side.addFlowLayout.margin_(~m_point).gap_(~m_point/2);

~tempo 	= EZNumber(	~left_side, 
					~item_bounds.x + ~margin + (~one_char_bounds.width*3+9) @ ~line_height,
					" Tempo ",
					ControlSpec(1,999,\lin,1,120,"bpm"),
					action:{|ezn| ("Tempo is now: " ++ ezn.value).postln},
					initVal:120,
					labelWidth:~item_bounds.x,
					gap:~m_point
		).setColors(Color.grey, Color.white);
~left_dec.nextLine;

~gati_pop = EZPopUpMenu( ~left_side,
						 ~total_bounds,
						 " Gati ",
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
						labelWidth:~item_bounds.x,
						gap:~m_point
		).setColors(Color.grey, Color.white);
		
~left_dec.nextLine;

~tala_pop = EZPopUpMenu( ~left_side,
						 ~total_bounds,
						 ~tala_pop_string=" Tala Presets ",
						[
							'Adi' 				-> {|a| "Adi".postln},
							'Rupaka' 			-> {|a| "Rupaka".postln},
							'Khanda Chapu' 		-> {|a| "Khanda Capu".postln},
							'Misra Chapu' 		-> {|a| "Misra Capu".postln},
							'Sankeerna Chapu' 	-> {|a| "Sankeerna Capu".postln},
							'Custom Tala' 		-> {|a| "Custom Tala".postln}
						],
						globalAction: {|a| "Global Action".postln},
						initVal: 0,
						initAction: false,
						labelWidth: ~item_bounds.x,
						gap:~m_point
		).setColors(Color.grey, Color.white);
~left_dec.nextLine;
~custom_tala_comp = CompositeView( ~left_side, ~total_bounds.x@~ct_field_bounds.y);
/*~custom_tala_comp.background_(Color.black)*/
~custom_tala_comp.addFlowLayout.margin_(0@0).gap_(~margin@0).left_(~custom_tala_comp.decorator.bounds.width).top_(0);
~custom_tala_label = StaticText(~custom_tala_comp, ~item_bounds)
						.string_(" Custom Tala ")
						.stringColor_(Color.white)
						.background_(Color.grey)
						.align_(\right);
~custom_tala_field = TextView(~custom_tala_comp, ~ct_field_bounds)
						.font_(~custom_tala_field_font)
						.usesTabToFocusNextView_(false)
						.enterInterpretsSelection_(false)
						.hasVerticalScroller_(true)
						.autohidesScrollers_(true);
~left_dec.nextLine;
/*~custom_tala_comp.decorator.left=380*/
~start_stop_routine = Routine {
	inf.do {
		"Started!".postln;
		0.yield;
		"Stopped!".postln;
		0.yield;
	};
};

~start_stop = Button(~left_side, ~total_bounds.x@(~win.bounds.height-~margin/2))
				.states_([
					["Start Tala", Color.black, Color.green],
					["Stop Tala", Color.white, Color.red]
				])
				.action_({|button|
					~start_stop_routine.();
				})
				.font_(~regular_font.copy.size_(60));
	
~right_side 	= CompositeView(~win, Rect(~win.bounds.width/2,0,~win.bounds.width/2, ~win.bounds.height));
~right_dec 		= ~right_side.addFlowLayout.margin_(~m_point).gap_(~m_point/2);
~paths 			= PathName.new("/Users/arthurc/Documents/programming/computerMusic/india_tools/tala/images");
~images 		= Array.newClear(~paths.files.size);
~paths.filesDo {|aFile, i|
	~images[i] = SCImage.new(aFile.fullPath);
};
~image_holder 	= CompositeView(~right_side, ~right_side.bounds.extent-(~margin*2)).backgroundImage_(~images[8],11);
~adi 			= [~images[8],~images[0..2], ~images[8..9] ! 2].flat;
~rupaka 		= ~images[8]!2 ++ ~images[9];
~khanda 		= ~images[8]!3;
~misra			= (~images[9]!2) ++ (~images[8]!2);
~sankirna		= (~images[8]!3) ++ (~images[9]!2);



)
/*
	TODO Gati
	TODO Multislider for sub-divisions
	TODO Eduppu
*/
