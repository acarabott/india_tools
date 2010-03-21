s.waitForBoot{a = Jati.new(4,3,1)}


a.syllables = "Xoxo"
a.syllables = "+SrGmPdN++S+NdPmGrS"
a.play

Synth(\beep, [\freq, 60.midicps]);

Synth(\beep, [\freq, 440]);

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


x = List[];
~str = ",srgmpdn's";
~str.do { |item, i|
	if(item==$, || (item==39.asAscii) ) {
		~nonPuncIndex = ~str[1..].detectIndex({ |item, i| [44.asAscii, 39.asAscii].includes(item).not });
	};
};



44.asAscii


3 * 12 




"++srgmpdns"

+++s	3
++n		2
++d		2
++p		2
++m		2
++g		2
++r		2
++s		2
+n		1
+d		1
+p		1
+m		1	
+g		1
+r		1
+s		1
n		0
d		0
p		0
m		0
g		0
r		0
-s		0
-n		-1
-d		-1
-p		-1
-m		-1
-g		-1
-r		-1
--s		-2
--n		-2
--d		-2
--p		-2
--m		-2
--g		-2
--r		-2
---s	-3	


0*12 + 11 + 60;
