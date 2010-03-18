s.waitForBoot{a = PatternPlayer.new};
s.waitForBoot{b = Tala.new};

a.pattern = "xxxx"

['x','x','x','x'].do { |item, i|
	item.postln;
};

~is_x  = {|item| ['x','X'].includes(item)};
~x = {|item| item.postln;}

['x','X'].includes('X');
~is_x.('x');