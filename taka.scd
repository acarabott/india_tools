s.waitForBoot{a = Jati.new(4,4,1)}
s.waitForBoot{a = PatternPlayer.new}
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
