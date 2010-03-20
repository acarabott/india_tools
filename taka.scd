s.waitForBoot{a = PatternPlayer.new};
s.waitForBoot{b = Tala.new};
a.tala.gati_mult = 0.5

b.gati_mult = 2
b.gati_func = {|i,j|
	Synth(\beep, [\freq, 440]);
}
a.pattern = "xxxx"

['x','x','x','x'].do { |item, i|
	item.postln;
};

~is_x  = {|item| ['x','X'].includes(item)};
~x = {|item| item.postln;}

['x','X'].includes('X');
~is_x.('x');


t = TempoClock(1);
r = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 440]);
		1.wait;
	};
};
q = Routine {
	inf.do {|i|
		Synth(\beep, [\freq, 880]);	
		0.25.wait;
	};
};

t.clear;
t = TempoClock(1);

x = nil ! 10;

x.size.do { |i|
	x[i] = Routine {
		(i+1).do { |j|
			Synth(\beep, [\freq, 440]);
			(1/(i+1)).wait;
			// (1/j+1).wait;
		};
	};
};

~int = 0;
t.schedAbs(t.beats.ceil, {
	x[~int].play(t); 
	~int = ~int + 1;
	x[~int-1].reset;
	if(~int==x.size) {
		~int = 0;
	};
	1
})

t.tempo = 60/60
