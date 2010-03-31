s.waitForBoot{a = KonaPlayer.new}
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

s.waitForBoot {x = Jati.new(3,4,1).syllables_("xoo")}

x.syllables
x.stop
x.play


j.play
j.play

j.syllables


(("x" ! 4)[0]=$X).reduce('++')