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
	
	*new { |aCollection|
		^super.new.init(aCollection);
	}

	init { |aCollection|		
		jatis = List[].addAll(aCollection);
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
		jatis.do { |item, i| 
			item.amp_(aAmp);
		};
	}
	
}

JatiControllerView {
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
