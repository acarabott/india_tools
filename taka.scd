s.waitForBoot{a = PatternPlayer.new}

x = JatiControl(a.jatis)

x.synthOff


a.jatis.do { |item, i|
	item.midiPlayback_(true);
	item.synthPlayback(false);
};

a.jatis[0].play
a.jatis[0].synthPlayback = false
MIDI
MIDIClient.init(1,1)
m = MIDIOut.newByName("IAC Driver", "Bus 1");
m.noteOn(0, 36, 120);
m.noteOff(0, 36, 120);

MIDIOut
	
	MIDIKeyboard
	
	
//	MIDI Patch that sends vol swell for every note

~norm = 90;
~time = 1;
r = Routine {
	(0..90).do {|i|
		m.control(0, 7, i);
		~wait.wait;
	};
	r.yieldAndReset;
};
r.play
(0..90)
mcontrol ( chan, ctlNum, val )


f = {|notes|
	m.control(0, 7, 0);
	r.play;
	fork {
		0.01.wait;
		notes.do { |jtem, j|
			jtem.postln;
			m.noteOn(0, jtem, 120);			
		};
		1.wait;
		m.noteOff(0, jtem, 120)
	};
	
};

~wait = 0.25/90;

f.([60,63,67]);

m.note

w = Window("Hello", Rect(400,400,400,400)).front;
j = JatiControlGUI(w, 400@400)

c = CompositeView(w, 400@400)
c.background_(Color.red)