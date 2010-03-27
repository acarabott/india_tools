/*
	TODO GUI for Jatis
	
	-Set sruti for all
	-Select sample set
	-MIDI On/Off
	-Synth On/Off
*/
JatiControl {
	
	var <>jatis;		//	The Jati instances to control
	var <sruti;			//	The sruti for the Jatis playback
	var <octave;		//	The octave for the Jatis playback
	
	*new { |collection|
		^super.new.init(collection);
	}

	init { |collection|
		jatis = collection;
		sruti = 60;
		octave = 0;
	}
	
	setSruti { |aSruti|
		sruti = aSruti;
		jatis.do { |item, i|
			item.sruti = aSruti;
		};
	}
	
	shiftOctave { |change|		
		octave = octave + (change*12);
		jatis.do { |item, i|
			item.sruti = sruti + octave;
		};
	}
	
	midiOn {
		jatis.do { |item, i|
			item.midiPlayback_(true);
		};
	}
	
	midiOff {
		jatis.do { |item, i|
			item.midiPlayback_(false);
		};
	}
	
	synthOn {
		jatis.do { |item, i|
			item.synthPlayback_(true);
		};
	}

	synthOff {
		jatis.do { |item, i|
			item.synthPlayback_(false);
		};
	}
	
	mute {
		this.midiOff;
		this.synthOff;
	}
}

JatiControlGUI {
	var <view;				//	CompositeView for all elements
	
	var <>srutiKeyboard;	//	MIDIKeyboard instance (ixiQuarks) for setting the Sruti
	
	*new { |parent, bounds|
		^super.new.init(parent, bounds);
	}

	init { |parent, bounds|
		view = CompositeView(parent, bounds);
		srutiKeyboard = MIDIKeyboard(view, Rect(150,75), 1, 60);
	}
}
