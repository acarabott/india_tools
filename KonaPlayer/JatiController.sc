/*
	TODO GUI for Jatis
	
	-Set sruti for all
	-Select sample set
	-MIDI On/Off
	-Synth On/Off
*/
JatiController {
	var <>jatis;			//	The collection of jatis to control
	var <midiPlayback;		//	Boolean 
	var <synthPlayback;		//	Boolean
	var <amp;				//	Amplitude 
	var <>mute;
	var <sruti;
	var <octave;
	
	*new { |aCollection|
		^super.new.init(aCollection);
	}

	init { |aCollection|		
		jatis = List[].addAll(aCollection);
		amp = 0.5;
		mute = 1;
	}
	
	add { |aJati|
		jatis.add(aJati)
	}
	
	addAll { |aCollection| 
		jatis.addAll(aCollection);
	}
	
	clear {
		jatis.clear;
	}
	
	midiPlayback_ { |aBoolean| 
		jatis.do { |item, i| 
			item.midiPlayback_(aBoolean);
		}
	}

	synthPlayback_ { |aBoolean| 
		jatis.do { |item, i| 
			item.synthPlayback_(aBoolean);
		}
	}
	
	amp_ { |aAmp|
		amp = aAmp;
		jatis.do { |item, i| 
			item.amp_(aAmp*mute);
		};
	}
}