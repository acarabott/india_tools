s.waitForBoot {a = PatternPlayer.new};

a.pattern = "xooo xooo"
a.tala.play

a.tala.gati = 3
a.tala.tempo = 90

"xooo oxoo"[0]

"xo asdfhkahdjf ahjsdfhs  oox".reject({|item, i| ['x','o'].includes(item.asSymbol).not })

SCTextField
a.pattern_field.action_({|field|
	a.pattern_field.stringColor = Color.black;
	a.pattern = field.value;
});


a.pattern
a.pattern_field.keyDownAction_({|view, char, mod, uni|
	a.pattern_field.stringColor = Color.red;
});

a.pattern_field.keyUpAction_({|view, char, mod, uni|
	if(a.pattern_field.string.asSymbol == a.pattern.asSymbol) {
		a.pattern_field.stringColor = Color.black;
	};
	
});

a.play
a.stop
a.tala.is_playing

a.pattern_field.keyDownAction = nil

b = Tala.new

a.stop;
a.play;

a.is_playing
a.tala.is_playing

Routine {
	inf.do {|i|
		i.postln;
		1.wait;
	};
}.()