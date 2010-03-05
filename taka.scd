s.boot;

t = TempoClock(1);

r = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 440]);
		1.wait;
	};
};

g = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 293]);
		(1/~gati).wait;
	};
};

f = {|i|
	~gati = i;
	"~gati: ".post; (~gati).postln;
	nil;
}

f.(3)

r.stop;g.stop;
r.reset;g.reset;

t.schedAbs(t.beats.ceil, {r.play(t)})
t.schedAbs(t.beats.ceil, {g.play(t)})
t.sched(0, {t.clear; ~gati = 4; t.schedAbs(t.beats.ceil, {r.play(t); g.play(t)})});
t.schedAbs(t.beats.ceil, {t.clear; ~gati = 3; r.play(t); g.play(t); nil})

t.tempo = 0.75
t.beats.ceil

t.schedAbs((t.beats.ceil-0.1), Synth(\beep, [\freq, 329]))
t.tempo = 1

s.waitForBoot {a = Tala.new}
a.set_gati_amp(3,1)
a.tempo