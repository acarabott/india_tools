s.boot;
a = PatternPlayer.new

w = 1;
g = 4;
a = Routine {
	inf.do { |i|
		Synth(\beep, [\freq, 440]);
		w.wait;
	};

};

b = Routine {
	inf.do { |i|
		Synth(\beep, [\freq, 880]);
		(w/g).wait;
	};
};

c = TempoClock(1)
c.tempo=1
c.stop

a.play(c);b.play(c);

a.stop
a.reset

b.play(c)
b.stop
b.reset

[a,b].do { |item, i|
	item.stop;
};

a.play
