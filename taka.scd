s.waitForBoot{a = PatternPlayer.new}
a = Tala.new

a.jatis
a.jatis.last.play
a.jatis[0].play
a.jatisRoutine
a.tala.syncRoutines.add(a.jatisRoutine)
a.tala.stopFunc = { a.stop; a.tala.syncRoutines.clear; a.tala.syncRoutines.add(a.jatisRoutine)}

a.stop

x = List[1,2,3,4]

x.clear
x
x = "(__:xoxoxoxo) (3:xoxoxo) xxxx (5__:xoxoxo) ";
x = "Xooo Xoxo T:Xxx Xxxx k:Xxxxx 3_:Xxxxxx 7:Xxxxxxx _:Xxxxxxxx 9:Xxxxxxxxx";

g=4;
x.split($ ).do { |item, i|
	var syllables;
	var gati = 4;
	var karve;
	var jati;
	var split = item.split($:);
	var mult = 1;
	
	if(split.size>1) {
		//Get the multiplier (number of __)
		mult = 2 ** split[0].count({|item, i| item==$_});
		gati = split[0].findRegexp("[0-9a-zA-Z]")[0] ?? g;
		gati.postln;
		if(gati!=g) { 
			switch (gati)
				{$t}	{}
				{$T}	{}
				{$c}	{}
				{$C}	{}
				{$k}	{}
				{$K}	{}
				{$m}	{}
				{$M}	{}
				{$s}	{}
				{$S}	{}
				;
			
			gati = gati[1].asInteger 
		};
	};
	gati.postln;
	syllables = split.last;	
	// "syllables.class: ".post; (syllables.class).postln;
	// "syllables.size.class: ".post; (syllables.size.class).postln;
	// "gati.class: ".post; (gati.class).postln;
	// "mult.class: ".post; (mult.class).postln;
	// "karve.class: ".post; (1/mult).class.postln;
	karve = 1/mult;
	// jati = Jati(syllables.size, gati, karve ).syllables_(syllables);
	// j.add(jati);
	
};


i=0;

j[i].play; i=i+1;
fork {
	j.do { |item, i|
		item.play;
		item.duration.wait;
	};
	
}
j.clear
Jati(6,5,1)

x = List[];
x.isKindOf(Collection)
"xxxx"
2**"_xxxx".count({ |item, i| item==$_})
2**"__xxxx".count({ |item, i| item==$_})
2** "___xxxx".count({ |item, i| item==$_})

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

