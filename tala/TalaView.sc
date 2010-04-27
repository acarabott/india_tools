TalaView {
	//Could refactor some of these, check if they are actually needed throughout the class or just by one widget
	//	In which case they can be method variables.
	
	
	classvar <extent;
	classvar <margin;		
	classvar <sideExtent;
	classvar <labelWidth;
	classvar <lineExtent;
	classvar <volumeExtent;
	                     
	//values
	var <position;
	var <bounds;
	var oneCharBounds;
	var ctfFont;
	var regularFont;
	var labelBgCol;
	var labelStrCol;
	var leftNumLines;

	//View elements
	var <win;
	var <parent;
	var <view;
	var leftSide;
	var leftDec;
	var rightSide;
	var rightDec;
	var <talaImage;
	var <playStopButton;
	var visible;
	
	//Other shit
	var <playStopRout;
	
	var tala;	// Tala instance to control
	
	*initClass {
		sideExtent = 350@310;
		volumeExtent = 40@sideExtent.y;
		extent = (sideExtent.x*2)+volumeExtent.x@sideExtent.y;
        margin = 5@5;
		labelWidth = sideExtent.x/2 - (margin.x*2);		
		lineExtent = sideExtent.x-(margin.x*2)@20;
	}
	
	*new {|tala, parent, position|
		if(parent==nil) {
			^super.new.initWindow(tala);
		} {
			^super.new.initView(tala, parent, position)
		};
	}
	
	initWindow {|aTala|
		this.createWindow;
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
		ctfFont		= Font("Monaco", 12);
		oneCharBounds = GUI.stringBounds("a", ctfFont);
		regularFont	= Font("Cochin",12);
		labelBgCol	= Color.grey;
		labelStrCol		= Color.white;
		leftNumLines	= 0;
		
		tala = aTala;
		view = CompositeView(parent, Rect(position.x, position.y, extent.x, extent.y));
		view.addFlowLayout(0@0,0@0);
		
		this.createLeftSide;
		this.createVolume;
		this.createRightSide;		
		
	}
	
	createWindow {
		var sBounds = Window.screenBounds;
		bounds	= Rect(	sBounds.width/2 - (extent.x), 
							sBounds.height/2 - (extent.y/2), 
							extent.x, 
							extent.y
		);		
		parent = Window("Carnatic Tala Meter", bounds, true).front;
	}
		
	createLeftSide {
		leftSide 		= CompositeView(view, sideExtent);
		leftDec 		= leftSide.addFlowLayout(margin, margin);
		leftDec.nextLine;
		
		this.createTempoBox;
		leftNumLines = leftNumLines + 1;
		leftDec.nextLine;
		this.createGatiPop;
		leftNumLines = leftNumLines + 1;
		leftDec.nextLine;
		this.createTalaPop;
		leftNumLines = leftNumLines + 1;
		leftDec.nextLine;
		// this.createCt;
		// leftDec.nextLine;
		// leftNumLines = leftNumLines + 1;
		this.createPlayStopBut;		
	}
	
	createTempoBox {
		EZNumber(
			leftSide, 
			lineExtent,
			" Tempo ",
			ControlSpec(1,999,\lin,1,120,"bpm"),
			action:{|ezn| (tala.tempo_(ezn.value))},
			initVal:60,
			labelWidth:labelWidth,
			gap:margin
		).setColors(labelBgCol, labelStrCol);		
	}

	//Not yet working
	createGatiPop {
		EZPopUpMenu(
			leftSide,
			lineExtent,
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
			labelWidth:labelWidth,
			gap:margin
		).setColors(labelBgCol, labelStrCol);
	}
	
	createTalaPop {
		EZPopUpMenu( 
			leftSide,
			lineExtent,
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
			labelWidth: labelWidth,
			gap:margin
		).setColors(labelBgCol, labelStrCol);
		
	}
	
	//Not yet functioning
/*	createCt {
		var ctComp;
		var ctFieldBounds = itemExtent.x @ (oneCharBounds.height*5-5);
		
		ctComp = CompositeView( leftSide, itemAndLabelExtent.x@ctFieldBounds.y);
		ctComp.addFlowLayout.margin_(0@0).gap_(margin@0).left_(ctComp.decorator.bounds.width).top_(0);
		
		StaticText(ctComp, itemExtent)
			.string_(" Custom Tala ")
			.stringColor_(labelStrCol)
			.background_(labelBgCol)
			.align_(\right);

		TextView(ctComp, ctFieldBounds)
			.font_(ctfFont)
			.usesTabToFocusNextView_(false)
			.enterInterpretsSelection_(false)
			.hasVerticalScroller_(true)
			.autohidesScrollers_(true);
	}
*/	
	createPlayStopBut {
		var buttonHeight;
		
		playStopRout = Routine {
			inf.do {
				tala.play;
				0.yield;
				tala.stop;
				0.yield;
			};
		};

		buttonHeight = extent.y;
		buttonHeight = buttonHeight - (margin.x*4);
		buttonHeight = buttonHeight - (leftNumLines*(lineExtent.y+leftDec.gap.y));
		// playStopButton = Button(leftSide, 340@ extent.y - (margin.x*2) - (leftNumLines*(lineExtent.y+leftDec.gap.y))- (margin.y*2))
		playStopButton = Button(leftSide, 340@buttonHeight)
			.states_([
				["Start", Color.black, Color.green],
				["Stop", Color.white, Color.red]
			])
			.action_({|button|
				playStopRout.();
			})
			.font_(regularFont.copy.size_(60)
		);
	}
	
	createRightSide {
		rightSide 		= CompositeView(view, sideExtent);
		rightDec 		= rightSide.addFlowLayout(margin, margin);
		
		talaImage = TalaImage.new(rightSide, sideExtent-(margin*2));
	}
	
	createVolume {
		var volView = CompositeView(view, volumeExtent);
		var volViewDec = volView.addFlowLayout(margin, margin);
		var buttonExtent = (volumeExtent.x-(margin.x*2)).asPoint;
		var sliderExtent = buttonExtent.x @ (volumeExtent.y - (margin.y*2) - buttonExtent.y - volViewDec.gap.x);
		
		EZSlider(
			volView, 
			sliderExtent,
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
		
		Button(volView, buttonExtent)
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
			talaImage.prAction(index, xMul, yMul);	
/*			(tala.waitTime*0.4).wait;*/
/*			talaImage.prAction(TalaImage.images.size-1);*/
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
	
	var <labelExtent;
	
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
		var fontSize		= bounds.height*0.14;
		var font			= if(Font.availableFonts.any{|item, i| item.asSymbol=='Cochin'}) {Font.new("Cochin", fontSize) } {Font("Times", fontSize)};
		var sBounds		= {|string| GUI.stringBounds(string, font)};
		
		labelExtent	= sBounds.(strings[strings.collect({|item, i| sBounds.(item).width}).maxIndex]).extent;

		comp 		= CompositeView(aParent, bounds ?? (aParent.bounds.width@aParent.bounds.height)).backgroundImage_(images.last,11);
		
/*		comp 		= CompositeView(aParent, bounds ?? (10@10)).background_(Color.grey);*/

		label 		= StaticText(comp, labelExtent).font_(font).stringColor_(Color.yellow);
								
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
	
	fontSize {
		^label.font.size
	}
	
	fontSize_ {|aSize|
		label.font = label.font.size = aSize
	}
	
	labelColor {
		^label.stringColor
	}
	
	labelColor_ {|aColor|
		label.stringColor = aColor;
	}
	
	bounds {
		^comp.bounds;
	}
	
	labelOrigin {
		^label.bounds.origin;
	}
	
	labelOrigin_ {|aPoint|
		label.bounds = Rect(aPoint.x, aPoint.y,labelExtent.x, labelExtent.y)
	}
	
	//actions
	
	prAction {|index, xMul=0, yMul=0|
		defer {
			comp.backgroundImage_(images[index], 11);
			label.string_(strings[index]);
			this.labelOrigin_((this.bounds.width*xMul)@(this.bounds.height*yMul))			
		}
		
	}		
}