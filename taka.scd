s.waitForBoot{b = Tala.new};
s.waitForBoot{a = PatternPlayer.new};
b.gati_func = {}
b.gatis_muted = true

b.gati_mult = 2
b.
b.make_gatis_clap
b.set_gati_amp(3,1)
b.clock.queue
b.clock.clear

b.clock.schedAbs(0, { |beat, sec| b.gati_clock.tempo = b.clock.tempo * b.gati_total; b.gati_total.postln; 1 });
b.gati_clock.schedAbs(0, { |beat, sec| b.gati_func = {"yeah".postln}; 1 });

b.gati_func = {|i| if(i==0) {
	Synth(\beep, [\freq, 440]);
};	i.postln;
}

b.gati_func = {"hi".postln;}
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

<<<<<<< HEAD:taka.scd
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
=======
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
~tempo = 80;

t.tempo = (~tempo/60);
u.tempo = (~tempo/60)*~gati;

t.tempo


a = KonaWord.new(4,4,1);


/*
	Options...
	Multiple clocks (for each gati)
		-Pros
			-No need to fuddle with getting in sync, all are kept in sync
		-Cons
			-To what degree do you need? Gati 
*/

(r = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 440]);
		1.wait;
	};
>>>>>>> sync:taka.scd
};
q = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 880]);
		(1/~gati).wait;
	};
};
t = TempoClock(1);
~gati = 4;
)
r.play(t);q.play(t);
t.schedAbs(t.timeToNextBeat, {Synth(\beep, [\freq, 1320]);},nil)
z = Routine {
	(t.timeToNextBeat).wait;
	~gati = 3;
};
z.play(t)
z.reset;

1/~gati/2


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
s.waitForBoot {a = SyncTest.new}
a.tempo = 60
a.gati = 

4.do { |i|
	i.postln;
};

