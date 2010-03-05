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
~gati = 4
r.stop;g.stop;
r.reset;g.reset;

t.schedAbs(t.beats.ceil, {r.play(t)})
t.schedAbs(t.beats.ceil, {g.play(t)})
r.play(t, 1);
g.play(t, 1)
t.sched(0, {t.clear; ~gati = 4; t.schedAbs(t.beats.ceil, {r.play(t); g.play(t)})});
t.schedAbs(t.beats.ceil, {t.clear; ~gati = 3; g.play(t,2); nil})
r.stop

t.tempo = 0.75
t.beats.ceil

t.schedAbs((t.beats.ceil-0.1), Synth(\beep, [\freq, 329]))
t.tempo = 1

s.waitForBoot {a = Tala.new}
a.gati_amps.size.do { |i|
	a.set_gati_amp(i,1)
};
a.tempo = 100
a.gati_amps
a.gati = 8
s.quit
a.stop