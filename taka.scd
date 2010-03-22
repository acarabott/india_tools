s.waitForBoot{a = PatternPlayer.new}

a.jatis[0].play
a.jatisRoutine.stop
a.play
a.stop
x = List[];
10.do { |i|
	var jgati = [3,4,5,7,9].choose;
	var karve = [1,2].choose;
	x.add(Jati(jgati, jgati, karve))
};

x.last.syllables = "Xxxx"
x.last.play
r = Routine {
	x.do {|item, i|
		x[i].play(a.tala.clock, 1);
		x[i].duration.wait;
	};
};
r.play;a.tala.play;
a.tala.stop
x.do { |item, i|
	item.duration.postln;
};
x.do { |arg|
	
};
(1..10)
[]
a.jatis[0].play(a.tala.clock)
a.syllables = "Xxxx XoXoo"


a.play
a.stop
a.syllables = "0(SRGmPDN) (SRGmPDN) ++S +NDPmGR +S 0NDPmGRS"
a.play

x = "0(SRGmPDN)-(SRGmPDN)++S+NDPmGR+S0NDPm   GRS"

x = x.asList.removeAll([ $( ])
x.asList.asArray.removeEvery( [ $(, $), $  ] ).reduce('++')
removeEvery

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



"   xoxo   (txoxoxo) (3xoxoxo)  _xoxo_              " 

x = "xxxx"
x[0] = $X
x