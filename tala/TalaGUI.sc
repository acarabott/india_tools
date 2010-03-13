TalaGUI {
	//Could refactor some of these, check if they are actually needed throughout the class or just by one widget
	//	In which case they can be method variables.
	
	
	classvar <extent;
	classvar <margin;		
	classvar <side_extent;
	classvar <label_width;
	classvar <line_extent;
	classvar <volume_extent;
	                     
	//values
	var <position;
	var <bounds;
	var one_char_bounds;
	var ctf_font;
	var regular_font;
	var label_bg_col;
	var label_s_col;
	var left_num_lines;

	//GUI elements
	var <win;
	var <parent;
	var <view;
	var left_side;
	var left_dec;
	var right_side;
	var right_dec;
	var <tala_image;
	var <play_stop_button;
	var visible;
	
	//Other shit
	var <play_stop_rout;
	
	var tala;	// Tala instance to control
	
	*initClass {
		side_extent = 350@310;
		volume_extent = 40@side_extent.y;
		extent = (side_extent.x*2)+volume_extent.x@side_extent.y;
        margin = 5@5;
		label_width = side_extent.x/2 - (margin.x*2);		
		line_extent = side_extent.x-(margin.x*2)@20;
	}
	
	*new {|tala, parent, position|
		if(parent==nil) {
			^super.new.initWindow(tala);
		} {
			^super.new.initView(tala, parent, position)
		};
	}
	
	initWindow {|aTala|
		this.create_window;
		position = 0@0;
		this.init(aTala);
	}
	
	initView {|aTala, aParent, aPosition|
		parent = aParent;
		position = aPosition ? (0@0);
		this.init(aTala);
		bounds = view.bounds;
	}
	
	init {|aTala|
		ctf_font		= Font("Monaco", 12);
		one_char_bounds = GUI.stringBounds("a", ctf_font);
		regular_font	= Font("Cochin",12);
		label_bg_col	= Color.grey;
		label_s_col		= Color.white;
		left_num_lines	= 0;
		
		tala = aTala;
		view = CompositeView(parent, Rect(position.x, position.y, extent.x, extent.y));
		view.addFlowLayout(0@0,0@0);
		
		this.create_left_side;
		this.create_volume;
		this.create_right_side;		
		
	}
	
	create_window {
		var s_bounds = Window.screenBounds;
		bounds	= Rect(	s_bounds.width/2 - (extent.x), 
							s_bounds.height/2 - (extent.y/2), 
							extent.x, 
							extent.y
		);		
		parent = Window("Carnatic Tala Meter", bounds, true).front;
	}
		
	create_left_side {
		left_side 		= CompositeView(view, side_extent);
		left_dec 		= left_side.addFlowLayout(margin, margin);
		left_dec.nextLine;
		
		this.create_tempo_box;
		left_num_lines = left_num_lines + 1;
		left_dec.nextLine;
		this.create_gati_pop;
		left_num_lines = left_num_lines + 1;
		left_dec.nextLine;
		this.create_tala_pop;
		left_num_lines = left_num_lines + 1;
		left_dec.nextLine;
		// this.create_ct;
		// left_dec.nextLine;
		// left_num_lines = left_num_lines + 1;
		this.create_play_stop_but;		
	}
	
	create_tempo_box {
		EZNumber(
			left_side, 
			line_extent,
			" Tempo ",
			ControlSpec(1,999,\lin,1,120,"bpm"),
			action:{|ezn| (tala.tempo_(ezn.value))},
			initVal:60,
			labelWidth:label_width,
			gap:margin
		).setColors(label_bg_col, label_s_col);		
	}

	//Not yet working
	create_gati_pop {
		EZPopUpMenu(
			left_side,
			line_extent,
			 " Gati ",
			[
				'3 - Tisra'		->	{|a| tala.gati_(3)},
				'4 - Caturasra'	->	{|a| tala.gati_(4)},
				'5 - Khanda'	->	{|a| tala.gati_(5)},
				'7 - Misra'		->	{|a| tala.gati_(7)},
				'9 - Sankirna'	->	{|a| tala.gati_(9)},
			],
			initVal:1,
			initAction:false,
			labelWidth:label_width,
			gap:margin
		).setColors(label_bg_col, label_s_col);
	}
	
	create_tala_pop {
		EZPopUpMenu( 
			left_side,
			line_extent,
			 " Tala Presets ",
			[
				'Adi' 				-> {|a| tala.adi},
				'Rupaka' 			-> {|a| tala.rupaka},
				'Khanda Chapu' 		-> {|a| tala.kCapu},
				'Misra Chapu' 		-> {|a| tala.mCapu},
				'Sankeerna Chapu' 	-> {|a| tala.sCapu}/*,
								'Custom Tala' 		-> {|a| "Not yet functioning!".postln}*/
			],
			initVal: 0,
			initAction: false,
			labelWidth: label_width,
			gap:margin
		).setColors(label_bg_col, label_s_col);
		
	}
	
	//Not yet functioning
/*	create_ct {
		var ct_comp;
		var ct_field_bounds = item_extent.x @ (one_char_bounds.height*5-5);
		
		ct_comp = CompositeView( left_side, item_and_label_extent.x@ct_field_bounds.y);
		ct_comp.addFlowLayout.margin_(0@0).gap_(margin@0).left_(ct_comp.decorator.bounds.width).top_(0);
		
		StaticText(ct_comp, item_extent)
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
	create_play_stop_but {
		var button_height;
		
		play_stop_rout = Routine {
			inf.do {
				tala.play;
				0.yield;
				tala.stop;
				0.yield;
			};
		};

		button_height = extent.y;
		button_height = button_height - (margin.x*4);
		button_height = button_height - (left_num_lines*(line_extent.y+left_dec.gap.y));
		// play_stop_button = Button(left_side, 340@ extent.y - (margin.x*2) - (left_num_lines*(line_extent.y+left_dec.gap.y))- (margin.y*2))
		play_stop_button = Button(left_side, 340@button_height)
			.states_([
				["Start Tala", Color.black, Color.green],
				["Stop Tala", Color.white, Color.red]
			])
			.action_({|button|
				play_stop_rout.();
			})
			.font_(regular_font.copy.size_(60)
		);
	}
	
	create_right_side {
		right_side 		= CompositeView(view, side_extent);
		right_dec 		= right_side.addFlowLayout(margin, margin);
		
		tala_image = TalaImage.new(right_side, side_extent-(margin*2));
	}
	
	create_volume {
		var vol_view = CompositeView(view, volume_extent);
		var vol_view_dec = vol_view.addFlowLayout(margin, margin);
		var button_extent = (volume_extent.x-(margin.x*2)).asPoint;
		var slider_extent = button_extent.x @ (volume_extent.y - (margin.y*2) - button_extent.y - vol_view_dec.gap.x);
		
		EZSlider(
			vol_view, 
			slider_extent,
			" Vol", 
			ControlSpec(-inf, 12, 'db', 0.01, -inf, " dB"),
			{|ez| tala.amp = ez.value.dbamp},
			initVal:1,
			unitWidth:30, 
			numberWidth:60,
			layout:\vert
		).setColors(Color.grey,Color.white, Color.grey(0.7),Color.grey, 
			Color.white, Color.white,nil,nil, Color.grey(0.7))
		.font_(Font("Helvetica",10));
		
		Button(vol_view, button_extent)
			.states_([
				["M", Color.white, Color.blue(1.5)],
				["M", Color.white, Color.blue(0.8)]
			])
			.action_({|button|
				tala.mute = (button.value-1).abs
			});
		
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

TalaImage {
	classvar <images;
	classvar <strings;
	
	var <comp;
	var <label;
	var <parent;
	
	var <label_extent;
	
	*initClass {
/*		images = PathName.new(String.scDir+/+"SCClassLibrary/tala/images/").files.collect({|item, i| SCImage.new(item.fullPath) });*/
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
		
/*		comp 		= CompositeView(aParent, bounds ?? (10@10)).background_(Color.grey);*/

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