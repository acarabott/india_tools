s.waitForBoot{a = Jati.new(4,4,1)}

a.createRoutine;


a.play;
a.stop;

r = Routine {

	inf.do {|i|
		a.play;
		1.wait;
	};
};

r.play;
r.stop
a.play
a.stop;
r.stop

