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

60/70/4/2

a.tala.tempo = 50
