TalaGUI {
	//Could refactor some of these, check if they are actually needed throughout the class or just by one widget
	//	In which case they can be method variables.
	
	classvar <extent;
	classvar <margin;
	classvar <m_point;
	classvar <item_extent;
	classvar <item_label_extent;
	classvar <line_height;
	
	//values
	var <position;
	var <bounds;
	var one_char_bounds;
	var ctf_font;
	var regular_font;
	var label_bg_col;
	var label_s_col;

	//GUI elements
	var <win;
	var <parent;
	var <view;
	var left_side;
	var left_dec;
	var right_dec;
	var <tala_image;
	var <play_stop_button;
	var visible;
	
	//Other shit
	var <play_stop_rout;
	
	var tala;	// Tala instance to control
	
	*initClass {
		margin 				= 5;
		m_point 			= margin@margin;
		line_height			= 20;
		item_extent 		= 164 @ line_height;
		item_label_extent 	= item_extent.x*2 + margin @ line_height;
		extent 				= ( (item_label_extent.x*2) + ((margin*4)/2) )@300
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

		tala = aTala;
		view = CompositeView(parent, Rect(position.x, position.y, extent.x, extent.y));
		
		this.create_left_side;
		this.create_right_side;		
		
	}
	
	create_window {
		var s_bounds = Window.screenBounds;
		bounds	= Rect(	s_bounds.width/2 - (extent.x), 
							s_bounds.height/2 - (extent.y/2), 
							extent.x, 
							extent.y
		);		
		parent = Window("Carnatic Tala Meter", bounds, false).front;
	}
		
	create_left_side {
		left_side 		= CompositeView(view, Rect(0,0,extent.x/2, extent.y));
		left_dec 		= left_side.addFlowLayout.margin_(m_point).gap_(m_point/2);
		left_dec.nextLine;
		
		this.create_tempo_box;
		left_dec.nextLine;
		this.create_gati_pop;
		left_dec.nextLine;
		this.create_tala_pop;
		left_dec.nextLine;
/*		this.create_ct;
		left_dec.nextLine;
*/		this.create_play_stop_but;		
	}
	
	create_tempo_box {
		EZNumber(
			left_side, 
			item_extent.x + margin + (one_char_bounds.width*3+9) @ line_height,
			" Tempo ",
			ControlSpec(1,999,\lin,1,120,"bpm"),
			action:{|ezn| (tala.tempo_(ezn.value))},
			initVal:60,
			labelWidth:item_extent.x,
			gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}

	//Not yet working
	create_gati_pop {
		EZPopUpMenu(
			left_side,
			item_label_extent,
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
			labelWidth:item_extent.x,
			gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}
	
	create_tala_pop {
		EZPopUpMenu( 
			left_side,
			item_label_extent,
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
			labelWidth: item_extent.x,
			gap:m_point
		).setColors(label_bg_col, label_s_col);
		
	}
	
	//Not yet functioning
/*	create_ct {
		var ct_comp;
		var ct_field_bounds = item_extent.x @ (one_char_bounds.height*5-5);
		
		ct_comp = CompositeView( left_side, item_label_extent.x@ct_field_bounds.y);
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
		play_stop_rout = Routine {
			inf.do {
				tala.play;
				0.yield;
				tala.stop;
				0.yield;
			};
		};
		
		play_stop_button = Button(left_side, item_label_extent.x@(extent.y-margin/2))
			.states_([
				["Start Tala", Color.black, Color.green],
				["Stop Tala", Color.white, Color.red]
			])
			.action_({|button|
				play_stop_rout.();
			})
			.font_(regular_font.copy.size_(60));
	}
	
	create_right_side {
		var right_side;
		
		right_side		= CompositeView(view, Rect(extent.x/2,0,extent.x/2,extent.y));
		right_side.addFlowLayout.margin_(m_point).gap_(m_point/2);
		tala_image = TalaImage.new(right_side, right_side.bounds.extent-(margin*2));
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


Tester {
	var window;
	var view;
	var sub_view;
	var button;
	
	*new{
		^super.new.init
	}
	
	init {
		window = Window("Tester", Rect(0,0,400,400)).front;
		view = CompositeView(window, Rect(0,0,400,400)).background_(Color.red);
		sub_view = CompositeView(view, Rect(0,0,100,100)).background_(Color.green);
		button = Button(sub_view, Rect(0,0,50,50))
			.states_([
				["Start Tala", Color.black, Color.green],
				["Stop Tala", Color.white, Color.red]
			])
	}
}