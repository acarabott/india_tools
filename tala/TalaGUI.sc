
/*
	TODO MIDIKeyboard for Sruti, with octave buttons
	TODO Image box
	TODO FullScreen image
	TODO Make this into a view, which can be added to other windows?
*/

/*
	TODO Gati
	TODO Multislider for sub-divisions
	TODO Eduppu
*/

TalaGUI {
	//Could refactor some of these, check if they are actually needed throughout the class or just by one widget
	//	In which case they can be method variables.
	
	//values
	var win_bounds;
	var margin;
	var m_point;
	var item_bounds;
	var total_bounds;
	var ct_field_bounds;
	var ct_field_font;
	var one_char_bounds;
	var line_height;

	//GUI elements
	var <win;
	var left_side;
	var left_dec;
	var right_side;
	var right_dec;
	
	var tempo_box;
	var gati_pop;
	var tala_pop;
	
	var ct_comp;
	var ct_label;
	var ctf_font;
	var ct_field;
	
	var start_stop_but;
	
	//Other shit
	var regular_font;
	var label_bg_col;
	var label_s_col;
	
	
	*new {
		^super.new.init
	}
	
	init {		
		margin 			= 5;
		m_point 		= margin@margin;
		line_height		= 20;
		one_char_bounds = GUI.stringBounds("a",Font("Monaco",12));
		item_bounds 	= (one_char_bounds.width*22) + 10 @ line_height;
		total_bounds 	= item_bounds.x*2 + margin @ line_height;
		ct_field_bounds = item_bounds.x @ (one_char_bounds.height*5-5);
		ct_field_font	= Font("Monaco", 12);
		regular_font	= Font("Cochin",12);
		label_bg_col	= Color.grey;
		label_s_col		= Color.white;
		
		this.create_window;
		this.create_left_side;
		this.create_right_side;
	}
	
	create_window {
		var s_bounds		= Window.screenBounds;
		var win_w;
		var win_h;
		
		win_bounds	= Rect(	s_bounds.width/2 - ((win_w = (total_bounds.x*2)+(margin*4))/2), 
							s_bounds.height/2 - ((win_h=300)/2), 
							win_w, 
							win_h
		);	
		win = Window("Tala", win_bounds,false).front;
	}
	
	create_left_side {
		left_side 		= CompositeView(win, Rect(0,0,win.bounds.width/2, win.bounds.height));
		left_dec 		= left_side.addFlowLayout.margin_(m_point).gap_(m_point/2);
		left_dec.nextLine;
		
		this.create_tempo_box;
		left_dec.nextLine;
		this.create_gati_pop;
		left_dec.nextLine;
		this.create_tala_pop;
		left_dec.nextLine;
		this.create_ct;
		left_dec.nextLine;
		this.create_start_stop_but;		
	}
	
	create_tempo_box {
		tempo_box  = EZNumber(	left_side, 
								item_bounds.x + margin + (one_char_bounds.width*3+9) @ line_height,
								" Tempo ",
								ControlSpec(1,999,\lin,1,120,"bpm"),
								action:{|ezn| ("Tempo is now: " ++ ezn.value).postln},
								initVal:120,
								labelWidth:item_bounds.x,
								gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}
	
	create_gati_pop {
		gati_pop = EZPopUpMenu( left_side,
								 total_bounds,
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
								labelWidth:item_bounds.x,
								gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}
	
	create_tala_pop {
		tala_pop = EZPopUpMenu( left_side,
								 total_bounds,
								 " Tala Presets ",
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
								labelWidth: item_bounds.x,
								gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}
	
	create_ct {
		ct_comp = CompositeView( left_side, total_bounds.x@ct_field_bounds.y);

		ct_comp.addFlowLayout.margin_(0@0).gap_(margin@0).left_(ct_comp.decorator.bounds.width).top_(0);
		ct_label = StaticText(ct_comp, item_bounds)
								.string_(" Custom Tala ")
								.stringColor_(label_s_col)
								.background_(label_bg_col)
								.align_(\right);
		ct_field = TextView(ct_comp, ct_field_bounds)
								.font_(ctf_font)
								.usesTabToFocusNextView_(false)
								.enterInterpretsSelection_(false)
								.hasVerticalScroller_(true)
								.autohidesScrollers_(true);
	}
	
	create_start_stop_but {
		
		start_stop_but = Button(left_side, total_bounds.x@(win.bounds.height-margin/2))
						.states_([
							["Start Tala", Color.black, Color.green],
							["Stop Tala", Color.white, Color.red]
						])
						.action_({|button|
							Routine {
								inf.do {
									"Started!".postln;
									0.yield;
									"Stopped!".postln;
									0.yield;
								};
							}.();
						})
						.font_(regular_font.copy.size_(60));
	}
	
	create_right_side {
		right_side 	= CompositeView(win, Rect(win.bounds.width/2,0,win.bounds.width/2, win.bounds.height));
		right_dec 	= right_side.addFlowLayout.margin_(m_point).gap_(m_point/2);
/*		a = TalaImage.new(~win, Rect(0,0,x,x));*/
	}
}

