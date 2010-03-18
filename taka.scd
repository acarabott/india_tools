s.waitForBoot{a = PatternPlayer.new};
s.waitForBoot{b = Tala.new};
a.tala.gati_mult = 0.5

a.tala.gati_mult = 1
a.tala.stop
a.tala.tGUI.play_stop_button.valueAction_(0)
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


x = "xooo yes yes xooo _xoox_";
y = x.reject({|item, i| [$x, $X, $o, $ , ($,), $-, $_].includes(item).not})
z = y.removeEvery([$ ]);

z.do { |item, i|
	if(item==$_) {
		i.postln;
	};
};

~pat_sym = x.collectAs({|item, i| item.asSymbol }, Array).reject({|item, i| ['x','X','o',' ',',','-'].includes(item.asSymbol).not });

count
"hello".occurrencesOf($l).class


