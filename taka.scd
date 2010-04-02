s.waitForBoot{a = KonaPlayer.new}
a.jatis

a.jatis.do { |item, i|
	item.midiPlayback = true;
	item.synthPlayback = false;
};

m = MIDIOut.newByName("")
127.do { | i|
	m.noteOff(i,i,i)
};
m.noteOff(0,50,125)
"hello ".split($ ).reject({ |item, i| item.size == 0})