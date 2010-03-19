s.waitForBoot{a = PatternPlayer.new};
s.waitForBoot{b = Tala.new};

r = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 440]);
		1.wait;
	};
};

~gati = 4;
g = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 880]);
		1.wait;
	};
};

t = TempoClock(1);
r.play(t);g.play(t);
r.stop;g.stop;
r.reset;g.reset;

q = Routine {
	t.timeToNextBeat.postln;
	t.timeToNextBeat.wait;
	~gati = 3;
};

f = { |gati|
	fork {
		t.timeToNextBeat.wait;
		~gati = gati
	}			
};

f.(3)
t.tempo = 1
t.playNextBar({~gati = 5});
~gati = 4


(
	// get elapsed time, round up to next second
v = Main.elapsedTime.ceil;

// create two clocks, starting at time v.
t = TempoClock(1, 0, v);
u = TempoClock(1, 0, v);

// start two functions at beat zero in each clock.
// t controls u's tempo. They should stay in sync.
t.schedAbs(0, { arg beat, sec; u.tempo = t.tempo * ~gati; Synth(\beep, [\freq, 440]); 1 });
u.schedAbs(0, { arg beat, sec; Synth(\beep, [\freq, 880]); 1 });
)

~gati = 5;
~tempo = 0.1;
~tempo = 100;

((~tempo/60)/t.tempo)

t.tempo
u.tempo

u.tempo = u.tempo * ((~tempo/60)/t.tempo);
t.tempo = t.tempo * ((~tempo/60)/t.tempo);

t.tempo