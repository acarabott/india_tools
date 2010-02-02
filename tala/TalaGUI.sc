
/*
	TODO MIDIKeyboard for Sruti, with octave buttons
	TODO FullScreen image
	TODO Make this into a view, which can be added to other windows?
	TODO Custom tala field working
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
	var one_char_bounds;
	var line_height;

	//GUI elements
	var <win;
	var left_side;
	var left_dec;
	var right_dec;
	var <tala_image;
	var <start_stop_button;
	
	//Other shit
	var ctf_font;
	var regular_font;
	var label_bg_col;
	var label_s_col;
	var <start_stop_rout;
	
	var tala;	// Tala instance to control
	
	*new {|aTala|
		^super.new.init(aTala);
	}
	
	init {|aTala|
		margin 			= 5;
		m_point 		= margin@margin;
		line_height		= 20;
		ctf_font		= Font("Monaco",12);
		one_char_bounds = GUI.stringBounds("a", ctf_font);
		item_bounds 	= (one_char_bounds.width*22) + 10 @ line_height;
		total_bounds 	= item_bounds.x*2 + margin @ line_height;
		regular_font	= Font("Cochin",12);
		label_bg_col	= Color.grey;
		label_s_col		= Color.white;
		
		tala = aTala;

		this.create_window;
		this.create_left_side;
		this.create_right_side;
	
	}
	
	create_window {
		var s_bounds	= Window.screenBounds;
		var win_w;
		var win_h;
		
		win_bounds	= Rect(	s_bounds.width/2 - ((win_w = (total_bounds.x*2)+(margin*4))/2), 
							s_bounds.height/2 - ((win_h=300)/2), 
							win_w, 
							win_h
		);	
		win = Window("Tala", win_bounds, false).front;

	}
	
	create_left_side {
		left_side 		= CompositeView(win, Rect(0,0,win.bounds.width/2, win.bounds.height));
		left_dec 		= left_side.addFlowLayout.margin_(m_point).gap_(m_point/2);
		left_dec.nextLine;
		
		this.create_tempo_box;
		left_dec.nextLine;
/*		this.create_gati_pop;
		left_dec.nextLine;
*/		this.create_tala_pop;
		left_dec.nextLine;
/*		this.create_ct;
		left_dec.nextLine;
*/		this.create_start_stop_but;		
	}
	
	create_tempo_box {
		EZNumber(
			left_side, 
			item_bounds.x + margin + (one_char_bounds.width*3+9) @ line_height,
			" Tempo ",
			ControlSpec(1,999,\lin,1,120,"bpm"),
			action:{|ezn| (tala.tempo_(ezn.value)).postln},
			initVal:120,
			labelWidth:item_bounds.x,
			gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}

	//Not yet working
/*	create_gati_pop {
		EZPopUpMenu(
			left_side,
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
*/	
	create_tala_pop {
		EZPopUpMenu( 
			left_side,
			total_bounds,
			 " Tala Presets ",
			[
				'Adi' 				-> {|a| tala.adi},
				'Rupaka' 			-> {|a| tala.rupaka},
				'Khanda Chapu' 		-> {|a| tala.kCapu},
				'Misra Chapu' 		-> {|a| tala.mCapu},
				'Sankeerna Chapu' 	-> {|a| tala.sCapu},
				'Custom Tala' 		-> {|a| "Not yet functioning!".postln}
			],
			initVal: 0,
			initAction: false,
			labelWidth: item_bounds.x,
			gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}
	
	//Not yet functioning
/*	create_ct {
		var ct_comp;
		var ct_field_bounds = item_bounds.x @ (one_char_bounds.height*5-5);
		
		ct_comp = CompositeView( left_side, total_bounds.x@ct_field_bounds.y);
		ct_comp.addFlowLayout.margin_(0@0).gap_(margin@0).left_(ct_comp.decorator.bounds.width).top_(0);
		
		StaticText(ct_comp, item_bounds)
			.string_(" Custom Tala ")
			.stringColor_(label_s_col)
			.background_(label_bg_col)
			.align_(\right);

		TextView(ct_comp, ct_field_bounds)
			.font_(ctf_font)
			.usesTabToFocusNextView_(false)
			.enterInterpretsSelection_(false)
			.hasVerticalScroller_(true)
			.autohidesScrollers_(true);
	}
*/	
	create_start_stop_but {
		start_stop_rout = Routine {
			inf.do {
				tala.play;
				0.yield;
				tala.stop;
				0.yield;
			};
		};
		
		start_stop_button = Button(left_side, total_bounds.x@(win.bounds.height-margin/2))
			.states_([
				["Start Tala", Color.black, Color.green],
				["Stop Tala", Color.white, Color.red]
			])
			.action_({|button|
				start_stop_rout.();
			})
			.font_(regular_font.copy.size_(60));
	}
	
	create_right_side {
		var right_side;
		
		right_side		= CompositeView(win, Rect(win.bounds.width/2,0,win.bounds.width/2, win.bounds.height));
		right_side.addFlowLayout.margin_(m_point).gap_(m_point/2);
		tala_image = TalaImage.new(right_side, right_side.bounds.extent-margin);
	}
	
	//Actions
	
	prAction {|index, xMul, yMul|
		fork {
			tala_image.prAction(index, xMul, yMul);	
/*			(tala.wait_time*0.4).wait;*/
/*			tala_image.prAction(TalaImage.images.size-1);*/
		}
		
	}
	
	clap {
		this.prAction(0, 0.41, 0.45);
	}
	
	wave {
		this.prAction(1, 0.41, 0.45);
	}
	
	lf {|num|
		this.prAction(num, 0.6, 0);
	}
	
	rf {|num|
		this.prAction(num, 0.45, -0.04);
	}
	
	mf {|num|
		this.prAction(4, 0.35, -0.03);
	}
	
	pf {|num|
		this.prAction(5, 0.27, 0.09);
	}
	
	tf {|num|
		this.prAction(6, 0.23, 0.32);
	}
	
}

TalaImage : Object {
	classvar <images;
	classvar <strings;
	
	var <comp;
	var <label;
	var <parent;
	
	var <label_extent;
	
	*initClass {
		images = PathName.new("/Users/arthurc/Documents/programming/computerMusic/india_tools/tala/images").files.collect({|item, i| SCImage.new(item.fullPath) });
		images.add(SCImage.color(images[0].bounds.extent, Color.gray));
		strings = #["Clap", "Wave", "2", "3", "4", "5", "6", "7", "8", "9",""];
	}
	
	*new {|aParent, aBounds|
		^super.new.init(aParent, aBounds);
	}

	init {|aParent, aBounds|
		var parent			= aParent;
		var bounds			= if(aBounds.class==Rect) {aBounds} {Rect(0,0,aBounds.x,aBounds.y)};
		var font_size		= bounds.height*0.14;
		var font			= if(Font.availableFonts.any{|item, i| item.asSymbol=='Cochin'}) {Font.new("Cochin", font_size) } {Font("Times", font_size)};
		var s_bounds		= {|string| GUI.stringBounds(string, font)};
		
		label_extent	= s_bounds.(strings[strings.collect({|item, i| s_bounds.(item).width}).maxIndex]).extent;

		comp 		= CompositeView(aParent, bounds ?? (aParent.bounds.width@aParent.bounds.height)).backgroundImage_(images.last,11);
		label 		= StaticText(comp, label_extent).font_(font).stringColor_(Color.yellow);
								
	}
	
	string {
		^label.string;
	}
		
	font {
		^label.font;
	}
	
	font_ {|aFont|
		label.font = aFont;
	}
	
	font_size {
		^label.font.size
	}
	
	font_size_ {|aSize|
		label.font = label.font.size = aSize
	}
	
	label_color {
		^label.stringColor
	}
	
	label_color_ {|aColor|
		label.stringColor = aColor;
	}
	
	bounds {
		^comp.bounds;
	}
	
	label_origin {
		^label.bounds.origin;
	}
	
	label_origin_ {|aPoint|
		label.bounds = Rect(aPoint.x, aPoint.y,label_extent.x, label_extent.y)
	}
	
	//actions
	
	prAction {|index, xMul=0, yMul=0|
		defer {
			comp.backgroundImage_(images[index], 11);
			label.string_(strings[index]);
			this.label_origin_((this.bounds.width*xMul)@(this.bounds.height*yMul))			
		}
		
	}
		
}