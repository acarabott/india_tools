SCTextView
SCTextField
x = KonaPlayer.new
x.jatis[0].synthPlayback
w = Window("h",Rect(0,0,600,600)).front;
t = TextField(w, Rect(20,20,400,400))
	.string_("Xxxx")
	
// patternField = TextField(patternView, Rect(5,5,890,390))
	.string_(player.pattern)
	.action_({|field|
		player.pattern_(field.value);				
		field.stringColor = Color.black;
	})
	.keyDownAction_({|view, char, mod, uni|
		view.stringColor = Color.red;
	})
	.keyUpAction_({|view, char, mod, uni|
		if(view.value.asSymbol == player.pattern.asSymbol) {
			view.stringColor = Color.black;
		};
	});

x = "one two three four";

f = File.new("/Users/arthurc/Desktop/word.txt", "w");
f.write("what");
f.close

f = File.openDialog

f = File.open("/Users/arthurc/Desktop/word.txt", "r");
f.readAllString.postln;

f = File.new()

File.saveDialog("hello")

GUI.dialog.savePanel


Dialog.savePanel({|path|
		var f = File.new(path,"w");
		f.write(x);
		f.close;		
	} , {
		"cancelled".postln;
	})
	
Dialog.getPaths({|paths|
	f = File.open(paths[0],"r");
	f.readAllString.postln;
	f.close;
	} , {
		"cancelled".postln;
	},
	false
)