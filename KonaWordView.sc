// How to subclass SCUserView to make custom GUI interfaces. Jost Muxfeldt, 2008.
// For many purposes you can use this as a template, and simply adjust the methods

KonaWordView : SCUserView {

	// (1) Setup instance vars appropriate to your widget. Make sure to define value.
	classvar <words;
	
	var <jatis;
	var <string;
	var <numLines;
	var <positionBounds;
	var <stringBounds;
	var <withLinesBounds;
	var <finalBounds;
	var <font;
	var <marginView;
	var <compView;
	var <textView;
	
	// (2) Set the viewClass to SCUserView
	*viewClass { ^SCUserView } // this ensures that SCUserView's primitive is called 
	
	*initClass {
		//Set up lookup table for syllables
		words = Array.newClear(10);
		words[0] = ['-'];
		words[1] = ['Tha'];
		words[2] = ['Tha', 'Ka'];
		words[3] = ['Tha', 'Ki', 'Ta'];
		words[4] = ['Tha', 'Ka', 'Dhi', 'Mi'];
		words[5] = ['Tha', 'Dhi', 'Ki', 'Na', 'Thom'];
		words[6] = ['Tha', 'Dhi', ',', 'Ki', 'Na', 'Thom'];
		words[7] = ['Tha', ',', 'Dhi', ',', 'Ki', 'Na', 'Thom'];
		words[8] = ['Tha', 'Dhi', ',', 'Ki', ',', 'Na', ',', 'Thom'];
		words[9] = ['Tha', ',', 'Dhi', ',', 'Ki', ',', 'Na', ',', 'Thom'];
	}

	// (3) Set up your view
	init { |aParent, aBounds, aJatis=4|

		font = Font.new("Monaco", 12);
		jatis = words[aJatis];
		string = "";
		jatis.do { |item, i| string = string ++ jatis[i] ++ " " };
/*		numLines = aNumUnderlines;*/
		
		positionBounds = aBounds;
		stringBounds = GUI.stringBounds(string,font);
		withLinesBounds = stringBounds.copy.height_(stringBounds.height*2);
		finalBounds = Rect(positionBounds.left, positionBounds.top, withLinesBounds.width, withLinesBounds.height);
		super.init(aParent, finalBounds);
		
/*		compView = CompositeView(aParent, finalBounds).background_(Color.green);*/
		textView = StaticText(aParent, finalBounds).font_(font).string_(string);

		// set the draw function of the SCUserView
		this.drawFunc={ this.draw}; 
	}
	
	setDefaultString {|aJatis|
		
	}
	
	addLine {|add|
		add = add * 0.2;
		
		SCPen.strokeColor = Color.black;
		Pen.line(0@stringBounds.height*(1.5+add), this.bounds.width@stringBounds.height*(1.5+add));
		Pen.stroke;
	}
	// (4) define a drawing function for SCPen
	draw {
		numLines.do { |i|
			this.addLine(i);			
		};
	}
		
	numLines_ {|new_num|
		numLines = new_num;
		numLines.do { |i|
			this.addLine(i);
		};
	}	
	// (5) define typical widget methods  (only those you need or adjust as needed)
/*	valueAction_{ arg val; // most widgets have this
		this.value=val;
		this.doAction;
	}*/
/*	value_{ |val|  	 // in many widgets, you can change the 
					 // value and refresh the view , but not do the action.
		value=val;
		this.refresh;
	}*/
			// these are like in SCSlider
/*	increment { |zoom=1| ^this.valueAction = this.value + (max(this.step, this.pixelStep) * zoom) }*/
/*	decrement { |zoom=1| ^this.valueAction = this.value - (max(this.step, this.pixelStep) * zoom) }*/

/*	pixelStep {  // like in SCSlider
		var bounds = this.bounds; 
		^(bounds.width-1).reciprocal
	}
*/	
	
	// (6) override mouseActions
/*	mouseDown{ arg x, y, modifiers, buttonNumber, clickCount;
		var newVal;
		// this allows for user defined mouseDownAction
		mouseDownAction.value(this, x, y, modifiers, buttonNumber, clickCount); 

		// set the value and do the action
		([256, 0].includes(modifiers)).if{ // restrict to no modifier
		
			newVal= x.linlin(0,this.bounds.width,0,1); 
			// translates the relative mouse position in pixels to a value between 0 and 1
			
			if (newVal != value) {this.valueAction_(newVal)}; // only do something if the value changed
		};
	}*/
	
/*	mouseMove{ arg x, y, modifiers, buttonNumber, clickCount;
		var newVal;  
		// this allows for user defined mouseMoveAction
		mouseMoveAction.value(this, x, y, modifiers, buttonNumber, clickCount);
		
		// set the value and do the action
		([256, 0].includes(modifiers)).if{ // restrict to no modifier
		
			newVal= x.linlin(0,this.bounds.width,0,1); 
			// translates the  relative mouse position in pixels to a value between 0 and 1
			
			if (newVal != value) {this.valueAction_(newVal)}; // only do something if the value changed
		};
		
	}
*/	
	// (7) define default key actions
	// make sure to return "this", if successful, and nil if not successful
/*	defaultKeyDownAction { arg char, modifiers, unicode,keycode;
		if (unicode == 16rF700, { this.increment; ^this });
		if (unicode == 16rF703, { this.increment; ^this });
		if (unicode == 16rF701, { this.decrement; ^this });
		if (unicode == 16rF702, { this.decrement; ^this });
		
		^nil		// bubble if it's an invalid key
	}
*/	
	// (8) define drag and drop
/*	defaultGetDrag {^value} // what to drag*/
/*	defaultCanReceiveDrag  {^currentDrag.isNumber} // when to receive*/
/*	defaultReceiveDrag { this.valueAction = currentDrag;} // what to do on receiving*/
	

}