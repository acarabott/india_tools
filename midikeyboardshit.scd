(

// (c) 2006, Thor Magnusson - www.ixi-software.net
// GNU licence - google it.
w.close;
w = Window("myboard", Rect(100,100,400,400)).front;
~win = w;
~bounds = Rect(100,100,280,75);
~octaves = 2;
~mouseTracker = UserView(~win, ~bounds).background_(Color.yellow); // thanks ron!
~bounds = ~mouseTracker.bounds;
~pen = Pen;
~startnote = ~argstartnote ? 48;
~trackKey = 0;
~pix = [0, 6, 10, 16, 20, 30, 36, 40, 46, 50, 56, 60].normalize;
// ~pix = [ 0, 0.1, 0.17, 0.27, 0.33, 0.5, 0.6, 0.67, 0.77, 0.83, 0.93, 1 ]; // as above but normalized
// ~keys = Array.newClear(~octaves*12);
~keys = List[];

~octaves.do({arg j;
	// 7.do { |j|
	// 	var whiteKeyWidth = (~bounds.width/~octaves)/7;
	// 	~r = Rect(
	// 			i+(j*whiteKeyWidth),
	// 			0,
	// 			whiteKeyWidth,
	// 			~bounds.height
	// 		);
	// 	~keys[i+j] = MIDIKey.new(~startnote+(i*12)+j, ~r, Color.white);
	// };
	12.do({arg i;
		if((i == 1) || (i == 3) || (i == 6) || (i == 8) || (i == 10), {
			var blackKeyWidth = (~bounds.width/~octaves)/10;
			"i: ".post; (i).postln;
			"j: ".post; (j).postln;
			"~pix[i]: ".post; (~pix[i]).postln;
			"~pix[i]*blackKeyWidth: ".post; (~pix[i]*blackKeyWidth).postln;
			"j*(blackKeyWidth*10): ".post; (j*(blackKeyWidth*10)).postln;
			~r = Rect(
				~pix[i]*blackKeyWidth + (j*(blackKeyWidth*10)),
				0,
				blackKeyWidth,
				~bounds.height/1.7
			);
			// ~r = Rect(	((~pix[i]*((~bounds.width/~octaves) -
			// 			(~bounds.width/~octaves/7))).round(1) + ((~bounds.width/~octaves)*j)).round(1)+0.5,
			// 		0, 
			// 		~bounds.width/~octaves/10, 
			// 		~bounds.height/1.7);

			~keys.add(MIDIKey.new(~startnote+i+(j*12), ~r, Color.black));
		}, {
			var whiteKeyWidth = ~bounds.width/~octaves;
			"~pix[i]*whiteKeyWidth: ".post; (~pix[i]*whiteKeyWidth).postln;
			~r = Rect(
					~pix[i]*whiteKeyWidth + (j*(whiteKeyWidth*7)),
					0,
					whiteKeyWidth,
					~bounds.height
				);
			// ~r = Rect(((~pix[i]*((~bounds.width/~octaves) -
			// 			(~bounds.width/~octaves/7))).round(1) + ((~bounds.width/~octaves)*j)).round(1)+0.5,
			// 		0, 
			// 		~bounds.width/~octaves/7, 
			// 		~bounds.height);
			~keys.add(MIDIKey.new(~startnote+i+(j*12), ~r, Color.white));
		});
	});
});

// ~keys.do{|item, i| if(item.color==Color.white){item.rect.postln;}}
~mouseTracker
	.canFocus_(false)
	// .relativeOrigin_(false)
	// .relativeOrigin_(true)
	.mouseDownAction_({|me, x, y, mod|
		// ~chosenkey = this.findNote(x, y);
		// trackKey = chosenkey;
		// chosenkey.color = Color.grey;
		// downAction.value(chosenkey.note);
		~mouseTracker.refresh;	
	})
	.mouseMoveAction_({|me, x, y, mod|
		// chosenkey = this.findNote(x, y);
		// if(trackKey.note != chosenkey.note, {
			// trackKey.color = trackKey.scalecolor; // was : type
			// trackKey = chosenkey;
			// chosenkey.color = Color.grey;
			// trackAction.value(chosenkey.note);
			~mouseTracker.refresh;
		// });
	})
	.mouseUpAction_({|me, x, y, mod|
		// chosenkey = this.findNote(x, y);
		// trackKey = chosenkey;
		// chosenkey.color = chosenkey.scalecolor; // was:  type
		// upAction.value(chosenkey.note);
		~mouseTracker.refresh;
	});
	
	~mouseTracker.drawFunc_({
		~octaves.do { |j|
			12.do { |i|
				var key = ~keys[i+(j*12)];
				var rect;
				if(key.type==Color.white) {
					~pen.color = Color.black;
					~pen.strokeRect(key.rect);
					// ~pen.strokeRect(Rect(
					// 	key.rect.left+0.5, 
					// 	key.rect.top+0.5,
					// 	// ,
					// 	// 0, 
					// 	key.rect.width+0.5, 
					// 	key.rect.height+0.5
					// ));
					// ~pen.color = key.color; // white or grey
					// ~pen.fillRect(Rect(
					// 	// 0,
					// 	// 0,
					// 	key.rect.left+0.5, 
					// 	key.rect.top+0.5, 
					// 	key.rect.width+0.5, 
					// 	key.rect.height-0.5
					// ));
					
				};
			};
			12.do({arg i;
				var key;
				var rect;
				key = ~keys[i+(j*12)];
				if(key.type == Color.black, {
					~pen.color = key.color;
					~pen.fillRect(key.rect);
					// ~pen.fillRect(Rect(key.rect.left+0.5, key.rect.top+0.5, key.rect.width+0.5, key.rect.height+0.5));
				});
			})
		};
	});
	// .drawFunc_({
	// 		~octaves.do({arg j;
	// 			// first draw the white keys
	// 			12.do({arg i;
	// 				var key;
	// 				key = ~keys[i+(j*12)];
	// 				if(key.type == Color.white, {
	// 					~pen.color = Color.black;
	// 					~pen.strokeRect(Rect(key.rect.left+0.5, key.rect.top+0.5, key.rect.width+0.5, key.rect.height-0.5));
	// 					~pen.color = key.color; // white or grey
	// 					~pen.fillRect(Rect(key.rect.left+0.5, key.rect.top+0.5, key.rect.width+0.5, key.rect.height-0.5));
	// 				});
	// 			});
	// 			// and then draw the black keys on top of the white
	// 			12.do({arg i;
	// 				var key;
	// 				key = ~keys[i+(j*12)];
	// 				if(key.type == Color.black, {
	// 					~pen.color = key.color;
	// 					~pen.fillRect(Rect(key.rect.left+0.5, key.rect.top+0.5, key.rect.width+0.5, key.rect.height+0.5));
	// 				});
	// 			})
	// 		})
	// 	});
)