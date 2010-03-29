s.waitForBoot{a = PatternPlayer.new}
a.jatis
x = JatiControl(a.jatis)

x.synthOff


a.jatis.do { |item, i|
	item.midiPlayback_(true);
	item.synthPlayback_(false);
};
128.do { |i|
	m.noteOff(0, i, 120);
};

x = Jati.new(3,4,1)
x.play
s.boot

j.play

j.syllables


(("x" ! 4)[0]=$X).reduce('++')