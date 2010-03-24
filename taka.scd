s.waitForBoot{a = PatternPlayer.new}



a.jatis.do { |item, i|
	item.midiPlayback_(true);
};

a.jatis[0].play
a.jatis[0].synthPlayback = false
MIDI
MIDIClient.init(1,1)
~mOut = MIDIOut.newByName("IAC Driver", "Bus 1");
~mOut.noteOn(0, 36, 120);
~mOut.noteOff(0, 36, 120);


if(x) {
	true
};

false.if {true} {false};

if(true) {
	true
} {
	false
};

MIDIClient.init